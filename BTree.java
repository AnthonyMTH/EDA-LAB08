public class BTree<E extends Comparable<E>> {

    BNodeGeneric<E> root;
    int MinDeg;

    // Constructor
    public BTree(int deg){
        this.root = null;
        this.MinDeg = deg;
    }

    public void add(E value) {
        if (root == null){
            root = new BNodeGeneric<E>(MinDeg,true);
            root.keys.set(0, value);
            root.num = 1;
        }
        else {
            // Cuando la raiz esté llena, el árbol crecerá
            if (root.num == 2*MinDeg-1){
                BNodeGeneric<E> s = new BNodeGeneric<E>(MinDeg,false);
                // El antiguo nodo raíz se convierte en hijo del nuevo nodo raíz
                s.children.set(0, root);
                // Separar el antiguo nodo raíz y dar una clave al nuevo nodo
                s.splitChild(0,root);
                // El nuevo nodo raíz tiene 2 nodos hijos. Mueve el antiguo hacia allí
                int i = 0;
                if (s.keys.elementAt(0).compareTo(value) < 0)
                    i++;
                s.children.elementAt(i).insertNotFull(value);

                root = s;
            }
            else
                root.insertNotFull(value);
        }
    }

    public void remove(E value) {
        if (root == null){
            System.out.println("El árbol está vacío");
            return;
        }

        root.remove(value);

        if (root.num == 0){ // If the root node has 0 keys
            // If it has a child, its first child is taken as the new root,
            // Otherwise, set the root node to null
            if (root.isLeaf)
                root = null;
            else
                root = root.children.elementAt(0);
        }
    }

    public boolean search(E value) {
        return root == null ? null : root.search(value);
    }

    public void recorrido(){
        if (root != null){
            root.recorrido();
        }
    }
}