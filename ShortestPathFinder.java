package col106.assignment6;

import java.util.ArrayList;
import java.util.PriorityQueue;

public class ShortestPathFinder implements ShortestPathInterface {
    /**
     * Computes shortest-path from the source vertex s to destination vertex t 
     * in graph G.
     * DO NOT MODIFY THE ARGUMENTS TO THIS CONSTRUCTOR
     *
     * @param  G the graph
     * @param  s the source vertex
     * @param  t the destination vertex 
     * @param left the cost of taking a left turn
     * @param right the cost of taking a right turn
     * @param forward the cost of going forward
     * @throws IllegalArgumentException unless 0 <= s < V
     * @throws IllegalArgumentException unless 0 <= t < V
     * where V is the number of vertices in the graph G.
     */
    static Digraph G;
    static int[] s;
    static int[] t;
    static int left;
    static int right;
    static int forward;
    ArrayList<Edge> pathroute;
    public int[] prev;
    ArrayList<Integer> nodedistance;
    private static int[] pointdirec;
    static boolean[] done; 
    static int direc=0;
    ArrayList<int[]> pseudodual;
    int width=0;
    static int backward;
    public ShortestPathFinder (final Digraph G, final int[] s, final int[] t, 
    final int left, final int right, final int forward) {
        // YOUR CODE GOES HERE
        this.G=G;
        this.s=s;
        this.t=t;
        this.left=left;
        this.right=right;
        this.forward=forward;
        this.pathroute=new ArrayList<>(); 
        this.prev=new int[G.V()];
        this.done= new boolean[G.V()];
        this.pointdirec=new int[G.V()];
        for(int i=0;i<G.V();i++){
            pointdirec[i]=1;
        }
        pointdirec[s[0]*G.W()+s[1]]=0;
        this.nodedistance=new ArrayList<>();
        for(int i=0;i<G.V();i++){
            nodedistance.add(Integer.MAX_VALUE);
        }
        
        this.pseudodual=dualGraph();
        this.backward=0;
        
    }

    // Return number of nodes in dual graph
    public int numDualNodes() {
        // YOUR CODE GOES HERE
        Iterable<Edge> er=G.edges();
        int y=0;
        for(Edge e:er){
            if(e.to()!=s[0]*G.W()+s[1]){
                y++;
            }
        }
        return y+1;
    }

    // Return number of edges in dual graph
    public int numDualEdges() {
        // YOUR CODE GOES HERE
        return this.width;
    }

    // Return hooks in dual graph
    // A hook (0,0) - (1,0) - (1,2) with weight 8 should be represented as
    // the integer array {0, 0, 1, 0, 1, 2, 8}
    public ArrayList<int[]> dualGraph() {
        // YOUR CODE GOES HERE        
        //Iterable<Edge> list=G.edges();
        //ArrayList<Edge>[] check=G.adjacency();
        ArrayList<int[]> result=new ArrayList<int[]>();;
        //int i=s[0]*G.W()+s[1];
        int[] prevhelp=new int[G.adjacency().length];
        PriorityQueue<Vertex> mainqueue = new PriorityQueue<>(new Vertexcompare());
        Vertex firstel=new Vertex(s[0],s[1],s[0]*G.W()+s[1]);
        firstel.distance=0;
        firstel.direction=0;
        firstel.prev=-1;
        mainqueue.add(firstel);
		nodedistance.set(s[0]*G.W()+s[1], 0);		
        done[0] = true;
        int re=0;		
        while (!mainqueue.isEmpty())
		{
            Vertex node = mainqueue.poll();		
            int u = node.key;          
            prevhelp[u]=node.prev;			
			for (Edge edge: G.adjacency()[u])
			{
                //o=edge;
                int v = edge.to();
                pointdirec[u]=node.direction;                
                int[] arrayout=new int[7];
                //prevhelp[v]=node.key;
                if(u==s[0]*G.W()+s[1]){
                    arrayout[0]=-1;
                    arrayout[1]=-1;
                    arrayout[2]=s[0];
                    arrayout[3]=s[1];
                    arrayout[4]=edge.to()/G.W();
                    arrayout[5]=edge.to()%G.W();
                    arrayout[6]=(int)edge.weight()+turnpenalty(s[0]*G.W()+s[1], edge.to());
                    
                }else{
                    arrayout[0]=prevhelp[u]/G.W();
                    arrayout[1]=prevhelp[u]%G.W();
                    arrayout[2]=edge.from()/G.W();
                    arrayout[3]=edge.from()%G.W();
                    arrayout[4]=edge.to()/G.W();
                    arrayout[5]=edge.to()%G.W();
                    arrayout[6]=(int)edge.weight()+turnpenalty(edge.from(),edge.to());    
                }
                Boolean rfty=true;
                for(int[] h:result){
                    int check=0;
                    for(int l=0;l<h.length;l++){
                        
                    if(arrayout[l]==h[l]){
                        check++;
                    }}
                    if(check==h.length){
                        rfty=false;
                    }
                }if(rfty){
                result.add(re,arrayout);
                re++;                            
                width++;}
                
					nodedistance.set(v, /*(int)node.distance + weight+red*/0);
                    //prev[v] = node.prev;
                    Vertex r=new Vertex((v/G.W()),v%G.W(),v);
                    r.distance=nodedistance.get(v);
                    r.direction=direc;
                    r.prev=u;
                    mainqueue.add(r);                               
            }            
            done[u] = true;
        //     i++;
        // if(i>=G.adjacency().length){
        //     i=G.adjacency().length-i;
        // }
        } 
        for(int j=0;j<G.V();j++){
            pointdirec[j]=1;
        }
        pointdirec[s[0]*G.W()+s[1]]=0;
        for(int k=0;k<G.V();k++){
            nodedistance.set(k,Integer.MAX_VALUE);
        }
        for(int l=0;l<done.length;l++){
            done[l]=false;
        }
        prev=new int[G.V()];
        return result;
    }

    // Return true if there is a path from s to t.
    public boolean hasValidPath() {
        // YOUR CODE GOES HERE
        if(s[0]*G.W()+s[1]>G.V()||t[0]*G.W()+t[1]>G.V()){
            return false;
        }
        return true;
    }

    // Return the length of the shortest path from s to t.
    public int ShortestPathValue() {
        // YOUR CODE GOES HERE
        if(s[0]==t[0]&&s[1]==t[1]){
            return 0;
        }
        ArrayList<int[]> truth=pseudodual;
        //nodedistance.set(s[0]*G.W()+s[1], 0);
        PriorityQueue<Vertex> mainqueue = new PriorityQueue<>(new Vertexcompare());
        Vertex firstel=new Vertex(s[0],s[1],s[0]*G.W()+s[1]);
        firstel.distance=0;
        //firstel.direction=0;
        firstel.prev=-4;
        mainqueue.add(firstel);
		nodedistance.set(s[0]*G.W()+s[1], 0);		
		done[0] = true;		
		prev[s[0]*G.W()+s[1]] = -1;
        while (!mainqueue.isEmpty())
		{
            if(done[t[0]*G.W()+t[1]]==true){
                break;
            }
            Vertex node = mainqueue.poll();		
            int u = node.key;
            //System.out.println(u);
            //System.out.println(node.distance);
            prev[u]=node.prev;
            for(int[] hook:truth){
                int ut=hook[2]*G.W()+hook[3];
                if(ut==u&&(hook[0]*G.W()+hook[1]==node.prev)){
                    int v=hook[4]*G.W()+hook[5];
                    if(!done[v]&&(node.distance + hook[6]<nodedistance.get(v))){
                        nodedistance.set(v, (int)node.distance + hook[6]);                        
                        //prev[ut]=hook[0]*G.W()+hook[1];
                        prev[v]=u;
                        Vertex r=new Vertex((v/G.W()),v%G.W(),v);
                        r.distance=nodedistance.get(v);
                        //r.distance=nodedistance.get(u) + weight+red;
                        //System.out.println(direc);
                        //r.direction=direc;
                        r.prev=ut;
                        mainqueue.add(r);
                    }
                }
            }
            done[u]=true;
        }
        int h=nodedistance.get(t[0]*G.W()+t[1]);
        return h;
    }

    // Return the shortest path computed from s to t as an ArrayList of nodes, 
    // where each node is represented by its location on the grid.
    public ArrayList<int[]> getShortestPath() {
        // YOUR CODE GOES HERE
        ArrayList<int[]> result=new ArrayList<int[]>();
        if(s[0]==t[0]&&s[1]==t[1]){
            int[] arrayout=new int[2];                              
            arrayout[0]=s[0];
            arrayout[1]=s[1];
            result.add(arrayout);
            return result;
        }
        getRoute(prev,t[0]*G.W()+t[1],pathroute);
        int i=0;
        for(Edge e:pathroute){
            
            if(e.from()==s[0]*G.W()+s[1]){ 
                int[] arrayout=new int[2];                              
                arrayout[0]=s[0];
                arrayout[1]=s[1];
                result.add(i,arrayout);
                i++;
            }
                int[] arrayout=new int[2];
                arrayout[0]=e.to()/G.W();
                arrayout[1]=e.to()%G.W();
                result.add(i,arrayout);
                i++;            
        }
        return result;
    }
    static int turnpenalty(int u,int v){
        int u1=u/G.W();
        int u2=u%G.W();
        int v1=v/G.W();
        int v2=v%G.W();
        if(pointdirec[u]==0){
            if(u1==v1&&v2>u2){
                direc=1;
                return forward;
            }else if(u1==v1&&v2<u2){
                direc=2;
                return forward;
            }else if(u1<v1&&v2==u2){
                direc=3;
                return forward;
            }else if(u1>v1&&v2==u2){
                direc=4;
                return forward;
            }
        }
        else if(pointdirec[u]==1){
            if(u1==v1&&v2>u2){
                direc=1;
                return forward;
            }else if(u1<v1&&v2==u2){
                direc=3;
                return right;
            }else if(u1>v1&&v2==u2){
                direc=4;
                return left;
            }
        }else if(pointdirec[u]==2){
            if(u1==v1&&v2<u2){
                direc=2;
                return forward;
            }else if(u1<v1&&v2==u2){
                direc=3;
                return left;
            }else if(u1>v1&&v2==u2){
                direc=4;
                return right;
            }
        }else if(pointdirec[u]==3){
            if(u1==v1&&v2>u2){
                direc=1;
                return left;
            }else if(u1==v1&&v2<u2){
                direc=2;
                return right;
            }else if(u1<v1&&v2==u2){
                direc=3;
                return forward;
            }
        }else if(pointdirec[u]==4){
            if(u1==v1&&v2>u2){
                direc=1;
                return right;
            }else if(u1==v1&&v2<u2){
                direc=2;
                return left;
            }else if(u1>v1&&v2==u2){
                direc=4;
                return forward;
            }
        }
        return 0;
    }  

    private static void getRoute(int[] prev, int i, ArrayList<Edge> pathroute){
        if (i > s[0]*G.W()+s[1]) {
            getRoute(prev, prev[i], pathroute);
            Edge i1=new Edge(prev[i], i, 0);
            pathroute.add(i1);
        }
    }
    
}
