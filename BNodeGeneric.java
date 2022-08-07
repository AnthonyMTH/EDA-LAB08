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

    public void remove(E key){

        int idx = findKey(key);
        if (idx < num && keys.elementAt(idx).compareTo(key) == 0){
            if (isLeaf) 
                removeFromLeaf(idx);
            else // Si está en un nodo no hoja
                removeFromNonLeaf(idx);
        }
        else{
            if (isLeaf){ // Si a estas alturas se verifica que es hoja, entonces, no existe en el árbol
                System.out.println("La clave" + key + " no existe en el árbol");
                return;
            }

            // De lo contrario, la clave a eliminar existe en el subárbol con el nodo como raíz
            // Esta bandera indica si la clave existe en el subárbol cuya raíz es el último hijo del nodo
            // Cuando idx es igual a num, se compara todo el nodo y la bandera es true
            boolean flag = idx == num; 
            
            if (children.elementAt(idx).num < MinDeg) // Si el hijo aún tiene espacio, se procede a llenarlo
                fill(idx);

            // Si el último nodo hijo se ha fusionado, debe haberse fusionado con el nodo hijo anterior, por lo que recurrimos al nodo hijo (idx-1).
            // En caso contrario, recurrimos al nodo hijo (idx), que ahora tiene al menos las claves del grado mínimo
            if (flag && idx > num)
                children.elementAt(idx-1).remove(key);
            else
                children.elementAt(idx).remove(key);
        }
    }
    
    public int findKey(E key){
        int idx = 0;
        // Sale del bucle en caso se haya recorrido todo o se haya encontrado la clave o uno mayor
        while (idx < num && keys.elementAt(idx).compareTo(key) < 0)
            ++idx;
        return idx;
    }

    public void removeFromLeaf(int idx){
        for (int i = idx+1;i < num; ++i)
            keys.set(i-1,keys.elementAt(i));
        num--;
    }

    public void removeFromNonLeaf(int idx){
        E key = keys.elementAt(idx);
        //Si tiene por lo menos el mínimo de claves
        if (children.elementAt(idx).num >= MinDeg){
            E pred = getPred(idx); //Se obtiene el predecesor
            keys.set(idx, pred); //Se reemplaza por el predecesor
            children.elementAt(idx).remove(pred); //Se elimina
        }
        //Si el anterior tenía menos claves, comprueba idx+1
        else if (children.elementAt(idx+1).num >= MinDeg){
            E succ = getSucc(idx); //Se obtiene el sucesor
            keys.set(idx, succ); //Se reemplaza por el sucesor
            
            children.elementAt(idx+1).remove(succ); //Se elimina
        }
        //Si ambos son menores, se combinan
        else{
            merge(idx);
            children.elementAt(idx).remove(key);
        }
    }

    public E getPred(int idx){ 
        BNodeGeneric<E> cur = children.elementAt(idx);
        while (!cur.isLeaf) //Recorrer hasta llegar al nodo hoja
        	cur=cur.children.elementAt(cur.num);
        
        return cur.keys.elementAt(cur.num-1);
    }

    public E getSucc(int idx){
        BNodeGeneric<E> cur = children.elementAt(idx+1);
        while (!cur.isLeaf) //Recorrer hasta llegar al nodo hoja
            cur = cur.children.elementAt(0);
        return cur.keys.elementAt(0);
    }

    public void fill(int idx){ 

    	// Si el nodo secundario anterior tiene mas claves que  MinDeg-1, toma prestado de ahí
        if (idx != 0 && children.elementAt(idx-1).num >= MinDeg)
            borrowFromPrev(idx);
        // Si el que le sigue tiene mas claves que el minimo, se toman de ahí
        else if (idx != num && children.elementAt(idx+1).num >= MinDeg)
            borrowFromNext(idx);
        else{
        	// Fusionar children[idx] y sus hermanos
            // Si children[idx] es el último nodo hijo
            // Entonces se fusiona con el nodo hijo anterior o posterior
            if (idx != num)
                merge(idx);
            else
                merge(idx-1);
        }
    }

    public void merge(int idx){ 

        BNodeGeneric<E> child = children.elementAt(idx);
        BNodeGeneric<E> sibling = children.elementAt(idx+1);

        // Inserta la última clave del nodo actual en la posición MinDeg-1 del nodo hijo
        child.keys.set(MinDeg-1, keys.elementAt(idx));

        // keys: children[idx+1] copiado a children[idx]
        for (int i = 0 ; i < sibling.num; ++i)
        	child.keys.set(i+MinDeg,sibling.keys.elementAt(i));

        // children: children[idx+1] copiado a children[idx]
        if (!child.isLeaf){
            for (int i = 0; i <= sibling.num; ++i)
            	child.children.set(i+MinDeg, sibling.children.elementAt(i));
        }

        // Mover las llaves hacia adelante, no la brecha causada por mover las llaves[idx] a los niños[idx]
        for (int i = idx+1; i < num; ++i)
        	keys.set(i-1, keys.elementAt(i));
     
        // Mover el nodo hijo que corresponde hacia adelante
        for (int i = idx+2; i <= num; ++i)
        	children.set(i-1, children.elementAt(i));

        child.num += sibling.num + 1;
        num--;
    }

    public void borrowFromPrev(int idx){
        BNodeGeneric<E> child = children.elementAt(idx);
        BNodeGeneric<E> sibling = children.elementAt(idx-1);

        // La última clave de children[idx-1] se desborda en el nodo padre
        // La clave[idx-1] desbordada del nodo padre se inserta como primera clave en children[idx]
        // Por lo tanto, sibling disminuye en uno y children aumenta en uno
        for (int i = child.num-1; i >= 0; --i) // children[idx] avanza
            child.keys.set(i+1, child.keys.elementAt(i));

        if (!child.isLeaf){ // Mover los hijos[idx] hacia adelante cuando no son nodos hoja
            for (int i = child.num; i >= 0; --i)
                child.children.set(i+1,child.children.elementAt(i));
        }

        // Establece la primera clave del nodo hijo a las claves del nodo actual [idx-1]
        child.keys.set(0, keys.elementAt(idx-1));
        if (!child.isLeaf) // Toma el último hijo de sibling como primer hijo de children[idx]
            child.children.set(0,sibling.children.elementAt(sibling.num));

        // Mover la última clave del hermano hasta la última clave del nodo actual
        keys.set(idx-1, sibling.keys.elementAt(sibling.num-1));
        child.num += 1;
        sibling.num -= 1;
    }

    public void borrowFromNext(int idx){
        BNodeGeneric<E> child = children.elementAt(idx);
        BNodeGeneric<E> sibling = children.elementAt(idx+1);
        child.keys.set(child.num, keys.elementAt(idx));

        if (!child.isLeaf)
            child.children.set(child.num+1, sibling.children.elementAt(0));

        keys.set(idx, sibling.keys.elementAt(0));

        for (int i = 1; i < sibling.num; ++i)
            sibling.keys.set(i-1, sibling.keys.elementAt(i));

        if (!sibling.isLeaf){
            for (int i= 1; i <= sibling.num;++i)
                sibling.children.set(i-1, sibling.children.elementAt(i));
        }
        child.num += 1;
        sibling.num -= 1;
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
