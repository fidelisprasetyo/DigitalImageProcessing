import javax.swing.*;
import java.awt.*;

public class GaussianMatrixVisualization extends JFrame {

    private double[][] gaussianMatrix;

    public GaussianMatrixVisualization(double[][] gaussianMatrix) {
        this.gaussianMatrix = gaussianMatrix;
        initUI();
    }

    private void initUI() {
        setTitle("2D Gaussian Matrix Visualization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 800);

        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawMatrix(g);
            }
        };

        add(panel);

        setLocationRelativeTo(null);
    }

    private void drawMatrix(Graphics g) {
        int rows = gaussianMatrix.length;
        int cols = gaussianMatrix[0].length;

        int cellSize = Math.min(getWidth() / cols, getHeight() / rows);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                double value = gaussianMatrix[i][j];
                int scaledValue = (int) (value * 255 * 10); // Adjust the scaling factor (e.g., *10)
                int clampedValue = Math.min(255, scaledValue); // Ensure the value is in [0, 255]
                Color color = new Color(clampedValue, clampedValue, clampedValue);
                g.setColor(color);
                g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
            }
        }
    }

    public static void main(String[] args) {
        double[][] gaussianMatrix = KuwaharaFilter.createGaussianKernel(1,2);

        SwingUtilities.invokeLater(() -> {
            GaussianMatrixVisualization example = new GaussianMatrixVisualization(gaussianMatrix);
            example.setVisible(true);
        });
    }
}
