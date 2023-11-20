package yavalath;

import javax.swing.*;
import java.util.Map;

public class Game extends JFrame {
    private final Player p1;
    private final Player p2;
    private final Player p3;
    private static Player activePlayer = null;

    public Game(Map<Integer,Player> players) {
        p1 = players.get(1);
        p2 = players.get(2);
        p3 = players.get(3);
        activePlayer = p1;
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
}
