package yavalath;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MainMenuTest {
    static MainMenu menu;
    @BeforeAll
    static void setUp() {
        menu = new MainMenu();
    }

    @Test
    void getPlayerType() {
        assertEquals(Player.Type.NONE, menu.getPlayer1Type());
    }

    @Test
    void getPlayerColor() {
        assertEquals(Color.WHITE, menu.getPlayer1Color());
    }
}