public class BTree<E extends Comparable<E>> {

    BNodeGeneric<E> root;
    int MinDeg;

    // Constructor
    public BTree(int deg){
        this.root = null;
        this.MinDeg = deg;
    }

    public boolean add(E value) {
        if (root == null){
            root = new BNodeGeneric<E>(MinDeg,true);
            root.keys.set(0, value);
            root.num = 1;
        }
        else {
            // When the root node is full, the tree will grow high
            if (root.num == 2*MinDeg-1){
                BNodeGeneric<E> s = new BNodeGeneric<E>(MinDeg,false);
                // The old root node becomes a child of the new root node
                s.children.set(0, root);
                // Separate the old root node and give a key to the new node
                s.splitChild(0,root);
                // The new root node has 2 child nodes. Move the old one over there
                int i = 0;
                if (s.keys.get(0).compareTo(value) < 0)
                    i++;
                s.children.get(i).insertNotFull(value);

                root = s;
            }
            else
                root.insertNotFull(value);
        }
    }

    public E remove(E value) {
        //TODO implement here!
        return null;
    }

    public void clear() {
        //TODO implement here!
    }

    public boolean search(E value) {
        //TODO implement here!
        return false;
    }

    public int size() {
        //TODO implement here!
        return 0;
    }
}