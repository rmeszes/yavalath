package yavalath;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    static final boolean WINDOW_SHOWN = false;
    static Game game;
    @BeforeAll
    static void setUp() {
        Map<Integer, Player> dummyPlayers = HashMap.newHashMap(3);
        dummyPlayers.put(1,new Player("Játékos 1", Color.RED, Player.Type.HUMAN));
        dummyPlayers.put(2,new Player("Játékos 2",Color.MAGENTA, Player.Type.BOT));
        dummyPlayers.put(3,new Player("Játékos 3",Color.BLUE, Player.Type.HUMAN));
        game = new Game(dummyPlayers,4);
        game.setVisible(WINDOW_SHOWN);
    }

    @Test
    void getP1() {
        assertDoesNotThrow(() -> game.getP1());
    }

    @Test
    void getActivePlayers() {
        assertEquals(3,game.getActivePlayers());
        game.takeOutPlayer();
        assertEquals(2, game.getActivePlayers());
    }

    @Test
    void takeOutPlayer() {
        int initial = game.getActivePlayers();
        Player active = game.getActivePlayer();
        game.takeOutPlayer();
        assertEquals(initial-1,game.getActivePlayers());
        assertNotEquals(active, game.getActivePlayer());
        assertNotNull(game.getActivePlayer());
    }

    @Test
    void nextPlayer() {
        Player initial = game.getActivePlayer();
        game.nextPlayer();
        assertNotEquals(initial,game.getActivePlayer());

        assertEquals(game.getActivePlayer().getType() == Player.Type.BOT,game.isBotCurrentlyActive());
        game.nextPlayer();
        assertEquals(game.getActivePlayer().getType() == Player.Type.BOT,game.isBotCurrentlyActive());
    }
}