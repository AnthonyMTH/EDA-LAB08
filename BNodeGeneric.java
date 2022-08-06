import java.util.Vector;

public class BNodeGeneric<E> {

    Vector<E> keys;
    Vector<BNodeGeneric<E>> children;
    int MinDeg; // Minimum degree of B-tree node
    int num; // Number of keys of node
    boolean isLeaf; // True when leaf node

    public BNodeGeneric(int deg,boolean isLeaf){
        this.MinDeg = deg;
        this.isLeaf = isLeaf;
        this.keys = new Vector<E>(2*this.MinDeg-1); // Node has 2*MinDeg-1 keys at most
        this.children = new Vector <BNodeGeneric<E>>(2*this.MinDeg);
        this.num = 0;
    }

    
}
