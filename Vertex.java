package col106.assignment6;

public class Vertex {
    public int i;
    public int j;
    public int key;
    public double distance;
    int direction;
    int prev;
    public Vertex(int i, int j, int key){
        this.i = i;
        this.j = j;
        this.key = key;
        this.distance=Double.MAX_VALUE;
        this.direction=1;
        this.prev=1;
    }
}