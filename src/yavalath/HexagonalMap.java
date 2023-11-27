package yavalath;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class HexagonalMap extends JPanel implements Serializable {
    private static final Logger logger = Logger.getLogger("HexagonalMap");
    private static final int HEX_RADIUS = 50;
    private static final int HEX_HEIGHT = (int) (Math.sqrt(3) * HEX_RADIUS);
    private static final int HEX_WIDTH = 2 * HEX_RADIUS;
    private static final int GRID_X_OFFSET = 50;
    private static final int GRID_Y_OFFSET = 50;
    private final List<Hexagon> hexagons;

    public void reInitialize() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });

    }
    public HexagonalMap(int size) {
        hexagons = new ArrayList<>();

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int x = GRID_X_OFFSET + col * HEX_WIDTH + (row % 2) * HEX_RADIUS;
                int y = GRID_Y_OFFSET + row * HEX_HEIGHT;
                Dimension d = new Dimension(x+HEX_WIDTH+GRID_X_OFFSET/4,y+HEX_HEIGHT+GRID_Y_OFFSET/4);
                setPreferredSize(d);
                Hexagon hexagon = new Hexagon(x, y, HEX_RADIUS,30,col,row);
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
                logger.info(() ->"Hexagon clicked at (" + hexagon.getQ() + ", " + hexagon.getR() + ")");
                if(hexagon.getTakenBy() == null) {
                    hexagon.setTakenBy(((Game)SwingUtilities.getWindowAncestor(this)).getActivePlayer());
                    this.repaint();
                    ((Game)SwingUtilities.getWindowAncestor(this)).nextPlayer();
                }
                break;
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

    private static boolean neighborsOfOdd(int qDiff, int rDiff) {
        return qDiff == -1 && rDiff == 0 ||
                qDiff == -1 && rDiff == -1 ||
                qDiff == 0 && rDiff == -1 ||
                qDiff == 1 && rDiff == 0 ||
                qDiff == 0 && rDiff == 1 ||
                qDiff == -1 && rDiff == 1;
    }

    private static boolean neigbhoursOfEven(int qDiff,int rDiff){
        return qDiff == -1 && rDiff == 0 ||
                qDiff == 0 && rDiff == -1 ||
                qDiff == 1 && rDiff == -1 ||
                qDiff == 1 && rDiff == 0 ||
                qDiff == 0 && rDiff == 1 ||
                qDiff == 1 && rDiff == 1;
    }

    private static boolean areNeighbors(Hexagon hex1, Hexagon hex2) {
        int q1 = hex1.getQ();
        int r1 = hex1.getR();
        int q2 = hex2.getQ();
        int r2 = hex2.getR();

        int qDiff = q1 - q2;
        int rDiff = r1 - r2;

        if(r1 % 2 == 1) {
            return neighborsOfOdd(qDiff,rDiff);
        } else {
            return neigbhoursOfEven(qDiff,rDiff);
        }
    }

}
