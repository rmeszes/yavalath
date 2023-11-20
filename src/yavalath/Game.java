package yavalath;

import javax.swing.*;
import java.util.Map;

public class Game extends JFrame {
    private static Player p1 = null;
    private static Player p2 = null;
    private static Player p3 = null;
    private static boolean p3inGame = false;
    private static Player activePlayer = null;

    public static void initializeGame(Map<Integer,Player> players) {
        p1 = players.get(1);
        p2 = players.get(2);
        p3 = players.get(3);
        p3inGame = p3.getType() != Player.Type.NONE;
        activePlayer = p1;

    }
    public Game() {
        HexagonalMap hexagonalMap = new HexagonalMap();
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
    }
}
