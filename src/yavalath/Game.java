package yavalath;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Game extends JFrame implements Serializable {
    private static final Logger logger = Logger.getLogger("Game");
    private JMenuItem saveMenuItem;
    private Player p1;
    private Player p2;
    private Player p3;
    private Player activePlayer;
    private final JLabel currentPlayerLabel = new JLabel();
    private ColorCube currentPlayerColor;
    private HexagonalMap map;
    private int activePlayers;

    public Player getP1() {
        return p1;
    }
    public Player getP2() {
        return p2;
    }
    public Player getP3() {
        return p3;
    }

    public int getActivePlayers() {
        return activePlayers;
    }
    public void takeOutPlayer() {
        activePlayers--;
    }

    public void reInitialize() {
        saveMenuItem.addActionListener(new SaveMenuItemListener(this));
        map.reInitialize();
    }

    public void initializeGame(Map<Integer,Player> players,int mapSize) {
        p1 = players.get(1);
        p2 = players.get(2);
        p3 = players.get(3);
        boolean p3inGame = p3.getType() != Player.Type.NONE;
        if(p3inGame) {
            activePlayers = 3;
        } else {
            activePlayers = 2;
        }
        activePlayer = p1;
        currentPlayerColor = new ColorCube(new Dimension(30, 30), p1.getColor());
        currentPlayerLabel.setFont(new Font("Sans Serif", Font.ITALIC, 30));


        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Fájl");
        saveMenuItem = new JMenuItem("Mentés és kilépés");
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);
        this.setJMenuBar(menuBar);

        saveMenuItem.addActionListener(new SaveMenuItemListener(this));

        JPanel currentPlayerPanel = new JPanel();
        currentPlayerPanel.setBorder(new EmptyBorder(60,10,10,60));
        JLabel l1 = new JLabel("Következő lépés:");
        l1.setFont(new Font("Sans Serif", Font.PLAIN, 30));
        currentPlayerPanel.add(l1);
        currentPlayerLabel.setText(p1.getName());
        currentPlayerPanel.add(currentPlayerLabel);
        currentPlayerPanel.add(currentPlayerColor);



        this.add(currentPlayerPanel,BorderLayout.NORTH);
        map = new HexagonalMap(mapSize,this);
        this.add(map);
        this.pack();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.setVisible(true);
    }
    public Game() {
        super("Yavalath");
        setResizable(false);
    }

    public Player getActivePlayer() {
        return activePlayer;
    }
    public void nextPlayer() {
        ArrayList<Player> playersInGame = new ArrayList<>(3);
        if(p1.isInGame()) playersInGame.add(p1);
        if(p2.isInGame()) playersInGame.add(p2);
        if(p3.isInGame()) playersInGame.add(p3);

        int active = playersInGame.indexOf(activePlayer);
        if(active != playersInGame.size()-1) {
            activePlayer = playersInGame.get(active +1);
        } else {
            activePlayer = playersInGame.get(0);
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
    public static void main(String[] args) { // csak tesztelésre
        Map<Integer, Player> dummyPlayers = HashMap.newHashMap(3);
        dummyPlayers.put(1,new Player("Játékos 1",Color.RED, Player.Type.HUMAN));
        dummyPlayers.put(2,new Player("Játékos 2",Color.MAGENTA, Player.Type.BOT));
        dummyPlayers.put(3,new Player("Játékos 3",Color.WHITE, Player.Type.NONE));
        Game g = new Game();
        g.initializeGame(dummyPlayers,20);
    }

    private record SaveMenuItemListener(Game parent) implements ActionListener {
        @Override
            public void actionPerformed(ActionEvent ae) {
                try (FileOutputStream fileStream = new FileOutputStream("save.ser"); ObjectOutputStream stream = new ObjectOutputStream(fileStream)) {
                    stream.writeObject(parent);
                    logger.info("Saved to file succesfully!");
                    System.exit(0);
                } catch (IOException e) {
                    logger.warning(e.getMessage());
                }
            }
        }
}
