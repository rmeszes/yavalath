package yavalath;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Game extends JFrame implements Serializable {
    private transient Logger logger = Logger.getLogger("Game");
    private Player p1;
    private Player p2;
    private Player p3;
    private boolean p3inGame;
    private Player activePlayer;
    private final JLabel currentPlayerLabel = new JLabel();
    private ColorCube currentPlayerColor;
    private HexagonalMap map;

    public void reInitialize() {
        logger = Logger.getLogger("Game");
        map.reInitialize();
    }

    public void initializeGame(Map<Integer,Player> players) {
        p1 = players.get(1);
        p2 = players.get(2);
        p3 = players.get(3);
        p3inGame = p3.getType() != Player.Type.NONE;
        activePlayer = p1;
        currentPlayerColor = new ColorCube(new Dimension(30, 30), p1.getColor());
        currentPlayerLabel.setFont(new Font("Sans Serif", Font.ITALIC, 30));


        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Fájl");
        JMenuItem saveMenuItem = new JMenuItem("Mentés és kilépés");
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);
        this.add(menuBar,BorderLayout.SOUTH);

        saveMenuItem.addActionListener(ae -> {
            try(FileOutputStream fileStream = new FileOutputStream("save.ser");ObjectOutputStream stream = new ObjectOutputStream(fileStream)) {
                stream.writeObject(this);
                logger.info("Saved to file succesfully!");
                System.exit(0);
            } catch (IOException e) {
                logger.warning(e.getMessage());
            }
        });

        JPanel currentPlayerPanel = new JPanel();
        currentPlayerPanel.setBorder(new EmptyBorder(60,10,10,60));
        JLabel l1 = new JLabel("Következő lépés:");
        l1.setFont(new Font("Sans Serif", Font.PLAIN, 30));
        currentPlayerPanel.add(l1);
        currentPlayerLabel.setText(p1.getName());
        currentPlayerPanel.add(currentPlayerLabel);
        currentPlayerPanel.add(currentPlayerColor);



        this.add(currentPlayerPanel,BorderLayout.NORTH);
        map = new HexagonalMap(8);
        this.add(map);
        this.pack();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.setVisible(true);
    }
    public Game() {
        super("Yavalath");
    }

    public Player getActivePlayer() {
        return activePlayer;
    }
    public void nextPlayer() {
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
    //Csak könnyebb tesztelésért
    public static void main(String[] args) {
        Map<Integer, Player> dummyPlayers = HashMap.newHashMap(3);
        dummyPlayers.put(1,new Player("Játékos 1",Color.RED, Player.Type.HUMAN));
        dummyPlayers.put(2,new Player("Játékos 2",Color.MAGENTA, Player.Type.BOT));
        dummyPlayers.put(3,new Player("Játékos 3",Color.WHITE, Player.Type.NONE));
        Game g = new Game();
        g.initializeGame(dummyPlayers);
    }
}
