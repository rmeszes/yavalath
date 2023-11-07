package yavalath;


import javax.swing.*;
import java.util.Map;

public class Game {
    private final Map<Integer,Player> players;

    public Player getPlayer(int i) {
        return players.get(i);
    }

    private Game(Map<Integer,Player> players) {
        this.players = players;
    }

    public static void createGame(Map<Integer,Player> players) {
        Game g = new Game(players);
    }
}
