package yavalath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class HexagonalMapTest {
    static final boolean WINDOW_SHOWN = false;
    HexagonalMap map;
    Game game;
    @BeforeEach
    void setUp() {
        Map<Integer, Player> dummyPlayers = HashMap.newHashMap(3);
        dummyPlayers.put(1,new Player("Játékos 1", Color.RED, Player.Type.HUMAN));
        dummyPlayers.put(2,new Player("Játékos 2",Color.MAGENTA, Player.Type.BOT));
        dummyPlayers.put(3,new Player("Játékos 3",Color.BLUE, Player.Type.HUMAN));
        game = new Game(dummyPlayers,4);
        map = game.map;
        game.setVisible(WINDOW_SHOWN);
    }
    @Test
    void isGameOverWhenMapFull() {
        assertFalse(map.isGameOver());
        int i = 0;
        for(Hexagon hexagon : map.hexagons) {
            switch(i) {
                case 0 -> {map.tryToTakeField(hexagon,game.getP1());i++;}
                case 1 -> {map.tryToTakeField(hexagon,game.getP2());i++;}
                default -> {map.tryToTakeField(hexagon,game.getP3());i = 0;}
            }
        }
        game.nextPlayer();
        assertTrue(map.isGameOver());
    }

    @Test
    void isGameOverWhen1PlayerLeft() {
        assertFalse(map.isGameOver());
        Iterator<Hexagon> hexagonIterator = map.hexagons.iterator();
        for(int i = 0; i < 7; i++) {
            if(i < 3) {
                map.tryToTakeField(hexagonIterator.next(),game.getP1());
            } else if (i > 3){
                map.tryToTakeField(hexagonIterator.next(),game.getP3());
            } else {
                hexagonIterator.next();
            }
        }
        assertTrue(map.isGameOver());
    }

    @Test
    void botStep() {
        int freeFields = map.freeFields;
        map.botStep(game.getP2());
        assertTrue(freeFields > map.freeFields);
    }

    @Test
    void tryToTakeFieldWhenEmpty() {
        int freeFields = map.freeFields;
        map.tryToTakeField(map.hexagons.get(0),game.getP1());
        assertEquals(game.getP1(), map.hexagons.get(0).getTakenBy());
        assertEquals(freeFields-1, map.freeFields);
    }
    @Test
    void tryToTakeFieldWhenTaken() {
        int freeFields = map.freeFields;
        map.tryToTakeField(map.hexagons.get(0),game.getP1());
        map.tryToTakeField(map.hexagons.get(0),game.getP3());
        assertEquals(game.getP1(), map.hexagons.get(0).getTakenBy());
        assertEquals(freeFields-1, map.freeFields);
    }
}