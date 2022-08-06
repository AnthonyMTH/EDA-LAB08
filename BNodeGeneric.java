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

    public void insertNotFull(E key){

        int i = num -1; // Initialize i as the rightmost index

        if (isLeaf){ // When it is a leaf node
            // Find the location where the new key should be inserted
            while (i >= 0 && keys.get(i).compareTo(key) > 0){
                keys.set(i+1, keys.get(i)); // keys backward shift
                i--;
            }
            keys.set(i+1, key);
            num++;
        }
        else{
            // Find the child node location that should be inserted
            while (i >= 0 && keys.get(i).compareTo(key) > 0)
                i--;
            if (children.get(i+1).num == 2*MinDeg - 1){ // When the child node is full
                splitChild(i+1,children.get(i+1));
                // After splitting, the key in the middle of the child node moves up, and the child node splits into two
                if (keys.get(i+1).compareTo(key) < 0)
                    i++;
            }
            children.get(i+1).insertNotFull(key);
        }
    }
    
}
