import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.Random;

public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Random Bar Chart");
            frame.setSize(1000, 1000);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
            BarChartPanel chartPanel = new BarChartPanel();
            frame.add(chartPanel, BorderLayout.CENTER);
            
            JPanel buttonPanel = new JPanel();
            JButton redrawButton = new JButton("Redraw");
            redrawButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    chartPanel.redrawBars();
                    chartPanel.repaint();
                }
            });
            buttonPanel.add(redrawButton);
            frame.add(buttonPanel, BorderLayout.SOUTH);
            
            frame.setVisible(true);
        });
    }
}

class BarChartPanel extends JPanel {
    private static final int GRID_SIZE = 11;
    private static final int NUM_BARS = 15;
    private static final int MARGIN = 20;
    private int[] barHeights;
    private Color[] barColors;
    private Random random;
    
    public BarChartPanel() {
        random = new Random();
        setBackground(Color.LIGHT_GRAY);
        barHeights = new int[NUM_BARS];
        barColors = new Color[NUM_BARS];
        redrawBars();
    }
    
    public void redrawBars() {
        // Generate random heights and colors for bars
        for (int i = 0; i < NUM_BARS; i++) {
            // Use fixed values for initial drawing
            if (getHeight() <= 0) {
                barHeights[i] = random.nextInt(700) + 50; // Random height between 50-750
            } else {
                // this way acouts for chaning the size of the window
                double gridHeight = getHeight() - 2 * MARGIN; 
                barHeights[i] = random.nextInt((int)(gridHeight * 0.8)) + (int)(gridHeight * 0.05); // Random height between 5% and 85% of grid height
            }
            
            // random rgb values
            barColors[i] = new Color(
                random.nextInt(256), 
                random.nextInt(256), 
                random.nextInt(256)
            );
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        
        int width = getWidth();
        int height = getHeight();
        
        // Calculate grid area accounts for margins
        double gridWidth = width - 2 * MARGIN;
        double gridHeight = height - 2 * MARGIN; 

        // Draw grid background
        g2d.setColor(Color.LIGHT_GRAY);
        g2d.fillRect(MARGIN, MARGIN, (int)gridWidth, (int)gridHeight);
        
        // change to grid lines
        g2d.setColor(Color.GRAY);
        g2d.setStroke(new BasicStroke(1.0f));
        
        // Draw horizontal grid lines
        double cellHeight = gridHeight / GRID_SIZE;
        for (int i = 0; i <= GRID_SIZE; i++) {
            double y = MARGIN + i * cellHeight;
            g2d.draw(new Line2D.Double(MARGIN, y, MARGIN + gridWidth, y));
        }
        
        // Draw vertical grid lines
        double cellWidth = gridWidth / GRID_SIZE;
        for (int i = 0; i <= GRID_SIZE; i++) {
            double x = MARGIN + i * cellWidth;
            g2d.draw(new Line2D.Double(x, MARGIN, x, MARGIN + gridHeight));
        }
        
        // Calculate bar spacing to distribute evenly
        double barSpacing = gridWidth / (NUM_BARS + 1);
        double bottomY = MARGIN + gridHeight; // Bottom of the grid
        
        for (int i = 0; i < NUM_BARS; i++) {
            // Calculate bar position - evenly spaced
            double x = MARGIN + barSpacing * (i + 1);
            
            // Set color and stroke for the bar
            g2d.setColor(barColors[i]);
            g2d.setStroke(new BasicStroke(10.0f));
            
            // Draw the bar as a vertical line starting from the bottom of the grid
            Line2D.Double line = new Line2D.Double(
                x, 
                bottomY - 5, // shift up 5 to account for stroke size
                x, 
                bottomY - barHeights[i]
            );
            g2d.draw(line);
        }
    }
}
