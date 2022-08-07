import java.util.Vector;

public class BNodeGeneric<E extends Comparable<E>>{

    Vector<E> keys;
    Vector<BNodeGeneric<E>> children;
    int MinDeg; // Minimum degree of B-tree node
    int num; // Number of keys of node
    boolean isLeaf; // True when leaf node

    public BNodeGeneric(int deg,boolean isLeaf){
        this.MinDeg = deg;
        this.isLeaf = isLeaf;
        this.keys = new Vector<E>(); // 2*MinDeg-1 como máximo
        for (int i = 0; i < 2*MinDeg-1; i++) 
        	keys.add(null);
        this.children = new Vector <BNodeGeneric<E>>();
        for(int i = 0; i < 2*MinDeg; i++) 
        	children.add(null);
        this.num = 0;
    }

    public void insertNotFull(E key){

        int i = num - 1; 

        if (isLeaf){ // Si es hoja
            // Bucle para buscar donde se debe insertar
            while (i >= 0 && keys.elementAt(i).compareTo(key) > 0){
                keys.set(i+1, keys.elementAt(i)); 
                i--;
            }
            keys.set(i+1, key);
            num++;
        }
        else{
            // Para encontrar la ubicación del hijo que se debe insertar
            while (i >= 0 && keys.elementAt(i).compareTo(key) > 0)
                i--;
            if (children.elementAt(i+1).num == 2*MinDeg - 1){ // Si el hijo está lleno
                splitChild(i+1,children.elementAt(i+1));
                // Proceso de división del nodo
                if (keys.elementAt(i+1).compareTo(key) < 0)
                    i++;
            }
            children.elementAt(i+1).insertNotFull(key);
        }
    }

    public void splitChild(int i, BNodeGeneric<E> y){

        // First, create a node to hold the keys of MinDeg-1 of y
        BNodeGeneric<E> z = new BNodeGeneric<E>(y.MinDeg, y.isLeaf);
        z.num = MinDeg - 1;

        // Pass the properties of y to z
        for (int j = 0; j < MinDeg-1; j++)
            z.keys.set(j, y.keys.elementAt(j+MinDeg));
        if (!y.isLeaf){
            for (int j = 0; j < MinDeg; j++)
                z.children.set(j, y.children.elementAt(j+MinDeg));
        }
        y.num = MinDeg-1;

        // Insert a new child into the child
        for (int j = num; j >= i+1; j--)
            children.set(j+1, children.elementAt(j));
        children.set(i+1, z);

        // Move a key in y to this node
        for (int j = num-1; j >= i; j--)
            keys.set(j+1, keys.elementAt(j));
        keys.set(i, y.keys.elementAt(MinDeg-1));

        num++;
    }

    
    public void recorrido(){
        int i;
        for (i = 0; i < num; i++){
            if (!isLeaf)
                children.elementAt(i).recorrido();
                System.out.print(keys.elementAt(i)+ " ");
        }

        if (!isLeaf){
            children.elementAt(i).recorrido();
        }
    }
}
