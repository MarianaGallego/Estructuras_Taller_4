package view;

import model.*;
import utils.MetricsTracker;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Random;

public class MainUI extends JFrame {
    private JComboBox<String> estructuraSelector;
    private JTextArea metricasArea;
    private JPanel canvasPanel;
    private KDTreePanel kdPanel;
    private QuadTreePanel quadPanel;
    private GridFilePanel gridPanel;

    private KDTree kdTree;
    private QuadTree quadTree;
    private GridFile gridFile;
    private MetricsTracker metrics;

    public MainUI() {
        super("Explorador Espacial");
        setSize(800, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        metrics = new MetricsTracker();
        kdTree = new KDTree(metrics);
        quadTree = new QuadTree(metrics);
        gridFile = new GridFile(10, metrics);

        kdPanel = new KDTreePanel(kdTree);
        quadPanel = new QuadTreePanel(quadTree);
        gridPanel = new GridFilePanel(gridFile);

        canvasPanel = new JPanel(new CardLayout());
        canvasPanel.add(kdPanel, "KD");
        canvasPanel.add(quadPanel, "QUAD");
        canvasPanel.add(gridPanel, "GRID");

        estructuraSelector = new JComboBox<>(new String[]{"KD-Tree", "QuadTree", "Grid File"});
        estructuraSelector.addActionListener(e -> switchPanel());

        JButton insertarBtn = new JButton("Insertar puntos aleatorios");
        insertarBtn.addActionListener(e -> insertarPuntos());

        JButton consultaRangoBtn = new JButton("Consulta por rango");
        consultaRangoBtn.addActionListener(e -> consultaRango());

        JButton vecinoBtn = new JButton("Vecino más cercano");
        vecinoBtn.addActionListener(e -> vecinoMasCercano());

        JPanel topPanel = new JPanel();
        topPanel.add(new JLabel("Estructura:"));
        topPanel.add(estructuraSelector);
        topPanel.add(insertarBtn);
        topPanel.add(consultaRangoBtn);
        topPanel.add(vecinoBtn);

        metricasArea = new JTextArea(6, 60);
        metricasArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(metricasArea);

        add(topPanel, BorderLayout.NORTH);
        add(canvasPanel, BorderLayout.CENTER);
        add(scroll, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void switchPanel() {
        CardLayout cl = (CardLayout) canvasPanel.getLayout();
        String selection = (String) estructuraSelector.getSelectedItem();
        if (selection.equals("KD-Tree")) cl.show(canvasPanel, "KD");
        else if (selection.equals("QuadTree")) cl.show(canvasPanel, "QUAD");
        else cl.show(canvasPanel, "GRID");
    }

    private void insertarPuntos() {
        Random rand = new Random();
        metrics.resetDiskAccesses();
        metrics.startTimer();
        for (int i = 0; i < 20; i++) {
            int x = rand.nextInt(100);
            int y = rand.nextInt(100);
            if (estructuraSelector.getSelectedItem().equals("KD-Tree")) {
                kdTree.insert(new int[]{x, y});
            } else if (estructuraSelector.getSelectedItem().equals("QuadTree")) {
                quadTree.insert(new int[]{x, y});
            } else {
                gridFile.insert(new int[]{x, y});
            }
        }
        metrics.endTimer();
        repaint();
        mostrarMetricas("Inserción");
    }

    private void consultaRango() {
        int xMin = 20, xMax = 70, yMin = 20, yMax = 70;
        List<int[]> encontrados;

        metrics.resetDiskAccesses();
        metrics.startTimer();
        if (estructuraSelector.getSelectedItem().equals("KD-Tree")) {
            encontrados = kdTree.rangeSearch(xMin, xMax, yMin, yMax);
        } else if (estructuraSelector.getSelectedItem().equals("QuadTree")) {
            encontrados = quadTree.rangeQuery(xMin, xMax, yMin, yMax);
        } else {
            encontrados = gridFile.rangeQuery(xMin, xMax, yMin, yMax);
        }
        metrics.endTimer();
        repaint();
        mostrarMetricas("Consulta por rango", encontrados.size());
    }

    private void vecinoMasCercano() {
        int[] target = {50, 50};
        int[] vecino;

        metrics.resetDiskAccesses();
        metrics.startTimer();
        /*if (estructuraSelector.getSelectedItem().equals("KD-Tree")) {
            vecino = kdTree.nearestNeighbor(target);
        } else if (estructuraSelector.getSelectedItem().equals("QuadTree")) {
            vecino = quadTree.nearestNeighbor(target);
        } else {
            vecino = gridFile.nearestNeighbor(target);
        }*/
        if (estructuraSelector.getSelectedItem().equals("KD-Tree")) {
            vecino = kdTree.nearestNeighbor(target);
        } else {
            vecino = gridFile.nearestNeighbor(target);
        }
        metrics.endTimer();
        repaint();
        mostrarMetricas("Vecino más cercano", 1, vecino);
    }

    private void mostrarMetricas(String operacion) {
        mostrarMetricas(operacion, -1);
    }

    private void mostrarMetricas(String operacion, int resultados) {
        mostrarMetricas(operacion, resultados, null);
    }

    private void mostrarMetricas(String operacion, int resultados, int[] punto) {
        StringBuilder sb = new StringBuilder();
        sb.append("--- ").append(operacion).append(" ---\n");
        sb.append("Tiempo (ns): ").append(metrics.getElapsedTime()).append("\n");
        sb.append("Memoria usada (bytes): ").append(metrics.getUsedMemory()).append("\n");
        sb.append("Accesos simulados a disco: ").append(metrics.getDiskAccesses()).append("\n");
        if (resultados >= 0) sb.append("Resultados encontrados: ").append(resultados).append("\n");
        if (punto != null) sb.append("Vecino: (").append(punto[0]).append(", ").append(punto[1]).append(")\n");

        metricasArea.setText(sb.toString());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MainUI::new);
    }
}

