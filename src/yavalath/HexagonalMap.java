package yavalath;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Logger;


/**
 * Hexagonal map osztály
 * Ez az osztály tartalmazza a Hexagon-ok gyűjteményét és
 * a kezelésükkel kapcsolatos függvényeket.
 */
public class HexagonalMap extends JPanel implements Serializable {
    private static final Logger logger = Logger.getLogger("HexagonalMap");
    private static final Random rnd = new Random();
    private static final int GRID_X_OFFSET = 50;
    private static final int GRID_Y_OFFSET = 50;
    protected final ArrayList<Hexagon> hexagons;
    private final Game game;


    /** A még szabad mezők száma.*/
    protected int freeFields;
    private boolean isGameOver;

    public boolean isGameOver() {
        return isGameOver;
    }


    /**
     * Deszerializáció után szükséges műveletek.
     */
    public void reInitialize() {
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });

    }
    public HexagonalMap(int size, Game game) {
        this.game = game;
        int hexRadius = 200 / size;
        int hexHeight = (int) (Math.sqrt(3) * hexRadius);
        int hexWidth = 2 * hexRadius;
        hexagons = new ArrayList<>(size*size);
        freeFields = size*size;
        isGameOver = false;
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                int x = GRID_X_OFFSET + col * hexWidth + (row % 2) * hexRadius;
                int y = GRID_Y_OFFSET + row * hexHeight;
                Dimension d = new Dimension(x+ hexWidth +GRID_X_OFFSET/4,y+ hexHeight +GRID_Y_OFFSET/4);
                setPreferredSize(d);
                Hexagon hexagon = new Hexagon(x, y, hexRadius,30,col,row);
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
        if(!game.isBotCurrentlyActive()) {
            for (Hexagon hexagon : hexagons) {
                if (hexagon.contains(mouseX, mouseY)) {
                    // Hexagon clicked
                    tryToTakeField(hexagon,game.getActivePlayer());
                    logger.info(() -> "Hexagon clicked at (" + hexagon.getQ() + ", " + hexagon.getR() + ")\n" +
                            "count: " + hexagon.getSameColorInRow());
                    break;
                }
            }
        }
    }

    private void noMoreFreeFields() {
        showDialog("Nincs több szabad mező");
    }
    public void botStep(Player bot) {
        boolean hasStepped = false;
        int size = hexagons.size();
        while(!hasStepped) {
            Hexagon toTry = hexagons.get(rnd.nextInt(0,size));
            hasStepped = tryToTakeField(toTry,bot);
        }

    }


    /**
     * Megpróbál elfoglalni egy mezőt egy játékos számára
     * @return Az elfoglalás sikeressége.
     */
    protected boolean tryToTakeField(Hexagon h, Player p) {
        if(h.getTakenBy() == null) {
            h.setTakenBy(p);
            freeFields--;
            this.repaint();
            switch (gameStateCheck()) {
                case NORMAL -> game.nextPlayer();
                case PLAYERWON -> playerWon();
                case GAMEOVER -> gameOver();
                case PLAYERTAKEOUT -> {/*no next player should be called here*/}
                case NO_MORE_FREE_FIELDS -> noMoreFreeFields();
            }
            return true;
        }
        return false;
    }

    private void gameOver() {
        String won = " nyert!";
        if(game.getP1().isInGame()) showDialog(game.getP1().getName() + won);
        if(game.getP2().isInGame()) showDialog(game.getP2().getName() + won);
        if(game.getP3().isInGame()) showDialog(game.getP3().getName() + won);
    }

    private void playerWon() {
        showDialog(game.getActivePlayer().getName() + " nyert!");
    }


    /**
     * Megjelenít egy ablakot, ami tájékoztatja a játékost a játék eredményéről.
     * @param msg A megjelenítendő üzenet.
     */
    private void showDialog(String msg) {
        isGameOver = true;
        game.setEnabled(false);
        JFrame dialog = new JFrame();
        JLabel label = new JLabel(msg);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setVerticalTextPosition(SwingConstants.CENTER);
        dialog.add(label, BorderLayout.CENTER);
        dialog.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        dialog.setSize(200,100);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }


    /**
     * A hexagonok kirajzolására
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        for (Hexagon hexagon : hexagons) {
            hexagon.draw(g2d);
        }
    }

    protected static boolean neighborsOfOdd(int qDiff, int rDiff) {
        return qDiff == -1 && rDiff == 0 ||
                qDiff == -1 && rDiff == -1 ||
                qDiff == 0 && rDiff == -1 ||
                qDiff == 1 && rDiff == 0 ||
                qDiff == 0 && rDiff == 1 ||
                qDiff == -1 && rDiff == 1;
    }

    protected static boolean neighboursOfEven(int qDiff, int rDiff){
        return qDiff == -1 && rDiff == 0 ||
                qDiff == 0 && rDiff == -1 ||
                qDiff == 1 && rDiff == -1 ||
                qDiff == 1 && rDiff == 0 ||
                qDiff == 0 && rDiff == 1 ||
                qDiff == 1 && rDiff == 1;
    }

    protected static boolean areNeighbors(Hexagon hex1, Hexagon hex2) {
        int q1 = hex1.getQ();
        int r1 = hex1.getR();
        int q2 = hex2.getQ();
        int r2 = hex2.getR();

        int qDiff = q1 - q2;
        int rDiff = r1 - r2;

        if(r1 % 2 == 1) {
            return neighborsOfOdd(qDiff,rDiff);
        } else {
            return neighboursOfEven(qDiff,rDiff);
        }
    }




    private GameState gameStateCheck() {
        Player lastPlayer = game.getActivePlayer();
        int maxCount = 0;
        for(Hexagon h : hexagons) {
            h.setSameColorInRow(1);
        }
        for(Hexagon base : hexagons) {
            if(base.getTakenBy() == lastPlayer) {
                maxCount = checkNeighbors(base,maxCount);
            }
        }
        return nextState(maxCount);
    }
    private boolean isALowerNeighbor(Hexagon upper, Hexagon lower) {
        if(areNeighbors(upper, lower) && (upper.getR() <= lower.getR() && upper.getQ() <= lower.getQ())){
            return true;
        } else return upper.getR() % 2 == 1 && upper.getQ() - lower.getQ() == -1 && upper.getR() - lower.getR() == 1;
    }
    private GameState nextState(int maxCount) {
        if(maxCount >= 4) {
            return GameState.PLAYERWON;
        } else if(maxCount == 3) {
            game.takeOutPlayer();
            if(game.getActivePlayers() == 1) {
                return GameState.GAMEOVER;
            } else {
                return GameState.PLAYERTAKEOUT;
            }
        } else if (freeFields == 0){
            return GameState.NO_MORE_FREE_FIELDS;
        } else {
            return GameState.NORMAL;
        }
    }

    private int checkNeighbors(Hexagon base, int maxCount) {
        int found = 0;
        for (Hexagon rest : hexagons) {
            if (isALowerNeighbor(base, rest) && base.getTakenBy() != null && (base.getTakenBy() == rest.getTakenBy())) {
                found++;
                int biggestNumber;
                if(base.getSameColorInRow() > rest.getSameColorInRow()) {
                    biggestNumber = base.getSameColorInRow() + 1;
                } else {
                    biggestNumber = rest.getSameColorInRow() + 1;
                }
                rest.setSameColorInRow(biggestNumber);
                base.setSameColorInRow(biggestNumber);
                int count = rest.getSameColorInRow();
                if (count > maxCount) maxCount = count;
            }
            if (found >= 6) break; //tuti minden szomszéd meglett
        }
        return maxCount;
    }
    private enum GameState {
        PLAYERWON,
        NORMAL,
        GAMEOVER,
        PLAYERTAKEOUT,
        NO_MORE_FREE_FIELDS
    }
}
