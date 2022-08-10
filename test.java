public class test {
    public static void main(String[] args) {
        BTree<Integer> a = new BTree<Integer>(3);

        a.add(24);
        a.add(18);
        a.add(4);
        a.add(33);
        a.add(12);
        a.add(41);
        a.add(9);
        a.add(63);
        a.add(3);
        a.add(17);
        a.add(20);

        System.out.println("\nÁrbol:");
        a.recorrido();
        System.out.println("\n");

        System.out.println("Eliminando elemento '4' y '63'...");
        a.remove(4);
        a.remove(63);
        System.out.println("Árbol después de haber eliminado:");
        a.recorrido();
        System.out.println("\n");

        System.out.println("Eliminando elemento '100' y '12'...");
        a.remove(100);
        a.remove(12);
        System.out.println("Árbol después de haber eliminado:");
        a.recorrido();
        System.out.println("\n");

        System.out.println("Eliminando elemento '4' y '17'...");
        a.remove(4);
        a.remove(17);
        System.out.println("Árbol después de haber eliminado:");
        a.recorrido();
        System.out.println("\n");
    }
}
