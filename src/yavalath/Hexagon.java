package yavalath;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Path2D;
import java.io.Serializable;

public class Hexagon extends JComponent implements Serializable {
    private Player takenBy;
    private final int centerX;
    private final int centerY;
    private final int size;
    private final int rotationAngle;
    private int q; // axial coordinate
    private int r; // axial coordinate

    public Player getTakenBy() {
        return takenBy;
    }
    public void setTakenBy(Player p) {
        takenBy = p;
    }

    public Hexagon(int centerX, int centerY, int size, int rotationAngle, int q, int r) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.size = size;
        takenBy = null;
        this.rotationAngle = rotationAngle;
        this.q = q;
        this.r = r;
    }

    public void draw(Graphics2D g2d) {
        // Calculate the six vertices of the hexagon
        int[] xPoints = new int[6];
        int[] yPoints = new int[6];
        for (int i = 0; i < 6; i++) {
            double angle = 2.0 * Math.PI / 6 * i + Math.toRadians(rotationAngle);
            xPoints[i] = (int) (centerX + size * Math.cos(angle));
            yPoints[i] = (int) (centerY + size * Math.sin(angle));
        }

        // Create a Path2D object and add the hexagon shape
        Path2D hexagon = new Path2D.Double();
        hexagon.moveTo(xPoints[0], yPoints[0]);
        for (int i = 1; i < 6; i++) {
            hexagon.lineTo(xPoints[i], yPoints[i]);
        }
        hexagon.closePath();

        // Draw the hexagon
        if(takenBy == null) {
            g2d.setColor(Color.WHITE);
        } else {
            g2d.setColor(takenBy.getColor());
        }
        g2d.fill(hexagon);
        g2d.setColor(Color.BLACK); // Set your desired border color
        g2d.draw(hexagon);
    }

    @Override
    public boolean contains(int x, int y) {
        return new java.awt.Polygon(getXPoints(), getYPoints(), 6).contains(x, y);
    }
    private int[] getXPoints() {
        int[] xPoints = new int[6];
        for (int i = 0; i < 6; i++) {
            double angle = 2.0 * Math.PI / 6 * i + Math.toRadians(rotationAngle);
            xPoints[i] = (int) (centerX + size * Math.cos(angle));
        }
        return xPoints;
    }

    private int[] getYPoints() {
        int[] yPoints = new int[6];
        for (int i = 0; i < 6; i++) {
            double angle = 2.0 * Math.PI / 6 * i + Math.toRadians(rotationAngle);
            yPoints[i] = (int) (centerY + size * Math.sin(angle));
        }
        return yPoints;
    }

    public int getQ() {
        return q;
    }

    public int getR() {
        return r;
    }
}
