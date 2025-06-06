package view;

import model.KDTree;
import utils.MetricsTracker;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class MainUI {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MetricsTracker metrics = new MetricsTracker();
            KDTree tree = new KDTree(metrics);

            // Insertar puntos aleatorios
            Random rand = new Random();
            for (int i = 0; i < 10; i++) {
                int x = rand.nextInt(100);
                int y = rand.nextInt(100);
                tree.insert(new int[]{x, y});
            }

            JFrame frame = new JFrame("Visualización KD-Tree");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            KDTreePanel panel = new KDTreePanel(tree);
            frame.add(panel, BorderLayout.CENTER);

            frame.pack();
            frame.setVisible(true);
        });
    }
}
