package yavalath;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.logging.Logger;

public class HexagonalMap extends JPanel implements Serializable {
    private static final Logger logger = Logger.getLogger("HexagonalMap");
    private static final int GRID_X_OFFSET = 50;
    private static final int GRID_Y_OFFSET = 50;
    private final ArrayList<Hexagon> hexagons;
    private final Game game;

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
        for (Hexagon hexagon : hexagons) {
            if (hexagon.contains(mouseX, mouseY)) {
                // Hexagon clicked
                if(hexagon.getTakenBy() == null) {
                    hexagon.setTakenBy(game.getActivePlayer());
                    this.repaint();
                    switch(gameStateCheck()) {
                        case NORMAL -> game.nextPlayer();
                        case PLAYERWON -> playerWon();
                        case GAMEOVER -> gameOver();
                    }
                }
                logger.info(() ->"Hexagon clicked at (" + hexagon.getQ() + ", " + hexagon.getR() + ")\n" +
                        "count: " + hexagon.getSameColorInRow());
                break;
            }
        }
    }

    private void gameOver() {
        if(game.getP1().isInGame()) showWinDialog(game.getP1());
        if(game.getP2().isInGame()) showWinDialog(game.getP2());
        if(game.getP3().isInGame()) showWinDialog(game.getP3());
    }

    private void playerWon() {
        showWinDialog(game.getActivePlayer());
    }

    private void showWinDialog(Player winner) {
        game.setEnabled(false);
        JFrame winDialog = new JFrame();
        String msg = winner.getName() + " nyert!";
        JLabel label = new JLabel(msg);
        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setVerticalTextPosition(SwingConstants.CENTER);
        winDialog.add(label, BorderLayout.CENTER);
        winDialog.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        winDialog.setSize(200,100);
        winDialog.setLocationRelativeTo(null);
        winDialog.setVisible(true);
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

    private static boolean neighboursOfEven(int qDiff, int rDiff){
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
        return nextState(lastPlayer,maxCount);
    }
    private boolean isALowerNeighbor(Hexagon upper, Hexagon lower) {
        if(areNeighbors(upper, lower) && (upper.getR() <= lower.getR() && upper.getQ() <= lower.getQ())){
            return true;
        } else return upper.getR() % 2 == 1 && upper.getQ() - lower.getQ() == -1 && upper.getR() - lower.getR() == 1;
    }
    private GameState nextState(Player lastPlayer, int maxCount) {
        if(maxCount >= 4) {
            return GameState.PLAYERWON;
        } else if(maxCount == 3) {
            lastPlayer.setInGame(false);
            game.takeOutPlayer();
            if(game.getActivePlayers() == 1) {
                return GameState.GAMEOVER;
            } else {
                return GameState.NORMAL;
            }
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
        GAMEOVER
    }
}
