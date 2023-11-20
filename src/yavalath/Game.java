package yavalath;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Map;

public class Game extends JFrame {
    private static Player p1 = null;
    private static Player p2 = null;
    private static Player p3 = null;
    private static boolean p3inGame = false;
    private static Player activePlayer = null;
    private static final JLabel currentPlayerLabel = new JLabel();
    private static ColorCube currentPlayerColor;

    public static void initializeGame(Map<Integer,Player> players) {
        p1 = players.get(1);
        p2 = players.get(2);
        p3 = players.get(3);
        p3inGame = p3.getType() != Player.Type.NONE;
        activePlayer = p1;
        currentPlayerColor = new ColorCube(new Dimension(30,30), p1.getColor());
        currentPlayerLabel.setFont(new Font("Sans Serif", Font.ITALIC, 30));
    }
    public Game() {
        super("Yavalath");
        JPanel currentPlayerPanel = new JPanel();
        currentPlayerPanel.setBorder(new EmptyBorder(60,10,10,60));
        JLabel l1 = new JLabel("Következő lépés:");
        l1.setFont(new Font("Sans Serif", Font.PLAIN, 30));
        currentPlayerPanel.add(l1);
        currentPlayerLabel.setText(p1.getName());
        currentPlayerPanel.add(currentPlayerLabel);
        currentPlayerPanel.add(currentPlayerColor);
        this.add(currentPlayerPanel,BorderLayout.NORTH);
        HexagonalMap hexagonalMap = new HexagonalMap(8);
        this.add(hexagonalMap);
        this.pack();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public static Player getActivePlayer() {
        return activePlayer;
    }
    public static void nextPlayer() {
        if(activePlayer == p1) {
            activePlayer = p2;
        } else if(activePlayer == p2 && p3inGame) {
            activePlayer = p3;
        } else if(activePlayer == p2) {
            activePlayer = p1;
        } else if(activePlayer == p3) {
            activePlayer = p1;
        }
        currentPlayerLabel.setText(activePlayer.getName());
        currentPlayerColor.setColor(activePlayer.getColor());
    }

    private static class ColorCube extends JButton {
        public ColorCube(Dimension d, Color c) {
            setPreferredSize(d);
            setBackground(c);
        }
        public void setColor(Color c) {
            setBackground(c);
        }
    }
}
