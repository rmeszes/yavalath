package yavalath;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class HexagonalMap extends JPanel {
    private static final Logger logger = Logger.getLogger("HexagonalMap");
    private static final int HEX_RADIUS = 50;
    private static final int HEX_HEIGHT = (int) (Math.sqrt(3) * HEX_RADIUS);
    private static final int HEX_WIDTH = 2 * HEX_RADIUS;
    private static final int GRID_X_OFFSET = 50;
    private static final int GRID_Y_OFFSET = 50;
    private final List<Hexagon> hexagons;
    public HexagonalMap(int size) {
        hexagons = new ArrayList<>();

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int x = GRID_X_OFFSET + col * HEX_WIDTH + (row % 2) * HEX_RADIUS;
                int y = GRID_Y_OFFSET + row * HEX_HEIGHT;
                Dimension d = new Dimension(x+HEX_WIDTH+GRID_X_OFFSET/4,y+HEX_HEIGHT+GRID_Y_OFFSET/4);
                setPreferredSize(d);
                Hexagon hexagon = new Hexagon(x, y, HEX_RADIUS,30);
                hexagons.add(hexagon);
            }
        }
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });
    }

    private void handleMouseClick(int mouseX, int mouseY) {
        for (Hexagon hexagon : hexagons) {
            if (hexagon.contains(mouseX, mouseY)) {
                // Hexagon clicked
                logger.finest(() ->"Hexagon clicked at (" + mouseX + ", " + mouseY + ")");
                if(hexagon.getTakenBy() == null) {
                    hexagon.setTakenBy(Game.getActivePlayer());
                    this.repaint();
                    Game.nextPlayer();
                }
                break; // Stop searching once a hexagon is found
            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (Hexagon hexagon : hexagons) {
            hexagon.draw(g2d);
        }
    }
}
