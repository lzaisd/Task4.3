package main;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.Random;
import java.util.function.Consumer;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.DefaultXYDataset;

import org.jfree.data.xy.XYDataset;
import util.ArrayUtils;
import util.JTableUtils;
import util.SwingUtils;
import sort.*;


public class SortDemoFrame extends JFrame {
    private JButton buttonSample1;
    private JButton buttonRandom1;
    private JButton buttonRandom2;
    private JTable tableArr;
    private JButton buttonPerformanceTest;
    private JPanel panelMain;
    private JPanel panelPerformance;
    private JButton buttonRadixSort;

    private ChartPanel chartPanel = null;

    public SortDemoFrame() {
        this.setTitle("Поразрядная сортировка");
        this.setContentPane(panelMain);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();

        JTableUtils.initJTableForArray(tableArr, 60, false, true, false, true);
        tableArr.setRowHeight(30);

        buttonSample1.addActionListener(actionEvent -> {
            int[] arr = {3, 8, 2, 5, 6, 1, 9, 7, 0, 4};
            JTableUtils.writeArrayToJTable(tableArr, arr);
        });
        buttonRandom1.addActionListener(actionEvent -> {
            int[] arr = ArrayUtils.createRandomIntArray(10, 100);
            JTableUtils.writeArrayToJTable(tableArr, arr);
        });
        buttonRandom2.addActionListener(actionEvent -> {
            int[] arr = ArrayUtils.createRandomIntArray(500, 10000);
            JTableUtils.writeArrayToJTable(tableArr, arr);
        });

        buttonRadixSort.addActionListener(actionEvent -> {
            try {
                int[] arr = JTableUtils.readIntArrayFromJTable(tableArr);
                sort.RadixSort.sort(arr, 10);
                if (!checkSorted(arr)) {
                    SwingUtils.showInfoMessageBox("Массив неправильно отсортирован!");
                }
                JTableUtils.writeArrayToJTable(tableArr, arr);
            } catch (Exception ex) {
                SwingUtils.showErrorMessageBox(ex);
            }
        });

        buttonPerformanceTest.addActionListener(actionEvent -> {
            String[] sortNames = {
                    "Встроенная (Arrays.sort)",
                    "Поразрядная (RadixSort)"
            };
            @SuppressWarnings("unchecked")
            Consumer<Integer[]>[] sorts = new Consumer[]{
                    (Consumer<Integer[]>) Arrays::sort,
                    (Consumer<Integer[]>) (arr) -> RadixSort.sort(arr, 256)
            };
            int[] sizes = {
                    100000, 200000, 300000, 400000, 500000,
                    600000, 700000, 800000, 900000, 1000000
            };
            performanceTestDemo(sortNames, sorts, sizes);
        });

        buttonSample1.doClick();
    }

    public static boolean checkSorted(int[] arr) {
        for (int i = 1; i < arr.length; i++) {
            if (arr[i] < arr[i - 1]) {
                return false;
            }
        }
        return true;
    }

    private static double[][] performanceTest(Consumer<Integer[]>[] sorts, int[] sizes) {
        Random rnd = new Random();
        double[][] result = new double[sorts.length][sizes.length];

        for (int i = 0; i < sizes.length; i++) {
            Integer[] arr1 = new Integer[sizes[i]];
            for (int j = 0; j < arr1.length; j++) {
                arr1[j] = rnd.nextInt((int) 1E6);
            }

            Integer[] arr2 = new Integer[sizes[i]];
            for (int j = 0; j < sorts.length; j++) {
                long moment1, moment2;
                System.arraycopy(arr1, 0, arr2, 0, arr1.length);
                System.gc();
                moment1 = System.nanoTime();
                sorts[j].accept(arr2);
                moment2 = System.nanoTime();
                result[j][i] = (moment2 - moment1) / 1E6;
            }
        }

        return result;
    }

    private void customizeChartDefault(JFreeChart chart) {
        XYPlot plot = chart.getXYPlot();
        XYDataset ds = plot.getDataset();

        for (int i = 0; i < ds.getSeriesCount(); i++) {
            chart.getXYPlot().getRenderer().setSeriesStroke(i, new BasicStroke(2));
        }

        Font font = buttonPerformanceTest.getFont();
        chart.getLegend().setItemFont(font);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.BLACK);
        plot.setRangeGridlinePaint(Color.BLACK);
        plot.getRangeAxis().setTickLabelFont(font);
        plot.getRangeAxis().setLabelFont(font);
        plot.getDomainAxis().setTickLabelFont(font);
        plot.getDomainAxis().setLabelFont(font);
    }

    private void performanceTestDemo(String[] sortNames, Consumer<Integer[]>[] sorts, int[] sizes) {
        double[][] result = performanceTest(sorts, sizes);

        DefaultXYDataset ds = new DefaultXYDataset();
        double[][] data = new double[2][result.length];
        data[0] = Arrays.stream(sizes).asDoubleStream().toArray();
        for (int i = 0; i < sorts.length; i++) {
            data = data.clone();
            data[1] = result[i];
            ds.addSeries(sortNames[i], data);
        }

        JFreeChart chart = ChartFactory.createXYLineChart(
                "Быстродействие сортировок",
                "Размерность массива, элементов",
                "Время выполнения, мс",
                ds
        );
        customizeChartDefault(chart);


        if (chartPanel == null) {
            chartPanel = new ChartPanel(chart);
            panelPerformance.add(chartPanel, BorderLayout.CENTER);
            panelPerformance.updateUI();
        } else {
            chartPanel.setChart(chart);
        }
    }

    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        panelMain = new JPanel();
        panelMain.setLayout(new GridLayoutManager(4, 2, new Insets(10, 10, 10, 10), 10, 10));
        final JPanel panel1 = new JPanel();
        panel1.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.add(panel1, new GridConstraints(0, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonSample1 = new JButton();
        buttonSample1.setText("Пример 1");
        panel1.add(buttonSample1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final Spacer spacer1 = new Spacer();
        panel1.add(spacer1, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        buttonRandom1 = new JButton();
        buttonRandom1.setText("Random, размер 10");
        panel1.add(buttonRandom1, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonRandom2 = new JButton();
        buttonRandom2.setText("Random, размер 500");
        panel1.add(buttonRandom2, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel2 = new JPanel();
        panel2.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.add(panel2, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonRadixSort = new JButton();
        buttonRadixSort.setText("Поразрядная сортировка");
        panel2.add(buttonRadixSort, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JPanel panel3 = new JPanel();
        panel3.setLayout(new GridLayoutManager(6, 1, new Insets(0, 0, 0, 0), -1, -1));
        panelMain.add(panel3, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        final Spacer spacer2 = new Spacer();
        panel3.add(spacer2, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_VERTICAL, 1, GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        buttonPerformanceTest = new JButton();
        buttonPerformanceTest.setText("Тест производительности");
        panel3.add(buttonPerformanceTest, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_EAST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        panelPerformance = new JPanel();
        panelPerformance.setLayout(new BorderLayout(0, 0));
        panelMain.add(panelPerformance, new GridConstraints(3, 0, 1, 2, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(-1, 300), null, 0, false));
        final Spacer spacer3 = new Spacer();
        panelPerformance.add(spacer3, BorderLayout.WEST);
        final JScrollPane scrollPane1 = new JScrollPane();
        panelMain.add(scrollPane1, new GridConstraints(1, 0, 1, 2, GridConstraints.ANCHOR_NORTH, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, new Dimension(-1, 120), new Dimension(-1, 120), new Dimension(-1, 120), 0, false));
        tableArr = new JTable();
        scrollPane1.setViewportView(tableArr);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return panelMain;
    }

}
