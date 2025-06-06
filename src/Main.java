import model.KDTree;
import utils.MetricsTracker;

import java.util.List;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
        MetricsTracker metrics = new MetricsTracker();
        KDTree tree = new KDTree(metrics);

        // Medir inserción
        metrics.resetDiskAccesses();
        metrics.startTimer();
        //KDTree tree = new KDTree();
        tree.insert(new int[]{30, 40});
        tree.insert(new int[]{5, 25});
        tree.insert(new int[]{70, 70});
        tree.insert(new int[]{10, 12});
        tree.insert(new int[]{50, 30});
        tree.insert(new int[]{35, 45});
        metrics.endTimer();

        System.out.println("Árbol KD (inorder):");
        tree.printTree();


        // ---------------------------- CONSULTAS ----------------------------
        // Consulta PUNTUAL
        int[] pointToSearch = {10, 12};
        System.out.println("¿Contiene " + pointToSearch[0] + "," + pointToSearch[1] + "? " +
                tree.search(pointToSearch));

        // Consulta por RANGO
        System.out.println("\nConsulta por rango (10 ≤ x ≤ 50, 10 ≤ y ≤ 50):");
        List<int[]> puntosEnRango = tree.rangeSearch(10, 50, 10, 50);
        for (int[] p : puntosEnRango) {
            System.out.println("(" + p[0] + ", " + p[1] + ")");
        }

        // Consulta VECINO MAS CERCANO
        int[] consulta = {33, 38};
        int[] vecino = tree.nearestNeighbor(consulta);
        System.out.println("\nVecino más cercano a (" + consulta[0] + ", " + consulta[1] + "): (" +
                vecino[0] + ", " + vecino[1] + ")");


        // ---------------------------- METRICAS ----------------------------
        System.out.println("\n-- MÉTRICAS DE INSERCIÓN --");
        System.out.println("Tiempo (ns): " + metrics.getElapsedTime());
        System.out.println("Memoria usada (bytes): " + metrics.getUsedMemory());
        System.out.println("Accesos simulados a disco: " + metrics.getDiskAccesses());

        // Medir búsqueda puntual
        int[] target = {10, 12};
        metrics.resetDiskAccesses();
        metrics.startTimer();
        boolean encontrado = tree.search(target);
        metrics.endTimer();

        System.out.println("\n-- MÉTRICAS DE BÚSQUEDA --");
        System.out.println("Encontrado: " + encontrado);
        System.out.println("Tiempo (ns): " + metrics.getElapsedTime());
        System.out.println("Accesos simulados a disco: " + metrics.getDiskAccesses());


    }
}