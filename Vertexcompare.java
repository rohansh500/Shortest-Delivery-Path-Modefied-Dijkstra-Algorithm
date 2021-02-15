package col106.assignment6;
import java.util.Comparator;


public class Vertexcompare implements Comparator<Vertex> {
    public int compare(Vertex u,Vertex v){
        return (int)(u.distance-v.distance);
    }    
}