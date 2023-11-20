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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class Game extends JFrame implements Serializable {
    private static final Logger logger = Logger.getLogger("Game");
    private static Player p1 = null;
    private static Player p2 = null;
    private static Player p3 = null;
    private static boolean p3inGame = false;
    private static Player activePlayer = null;
    private static final JLabel currentPlayerLabel = new JLabel();
    private static ColorCube currentPlayerColor;

    public static void initializeGame(Map<Integer,Player> players, int startingPlayer) {
        p1 = players.get(1);
        p2 = players.get(2);
        p3 = players.get(3);
        p3inGame = p3.getType() != Player.Type.NONE;
        activePlayer = players.get(startingPlayer);
        currentPlayerColor = new ColorCube(new Dimension(30,30), p1.getColor());
        currentPlayerLabel.setFont(new Font("Sans Serif", Font.ITALIC, 30));
    }
    public Game() {
        super("Yavalath");
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem saveMenuItem = new JMenuItem("Save");
        fileMenu.add(saveMenuItem);
        menuBar.add(fileMenu);
        this.add(menuBar,BorderLayout.SOUTH);

        saveMenuItem.addActionListener(ae -> {
            try(FileOutputStream fileStream = new FileOutputStream("save.ser");ObjectOutputStream stream = new ObjectOutputStream(fileStream)) {
                stream.writeObject(p1);
                stream.writeObject(p2);
                stream.writeObject(p3);
                if(activePlayer == p1) stream.writeInt(1);
                else if(activePlayer == p2) stream.writeInt(2);
                else if(activePlayer == p3) stream.writeInt(3);
                stream.writeObject(this);
                logger.info("Saved to file succesfully!");
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
        HexagonalMap hexagonalMap = new HexagonalMap(8);
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
        initializeGame(dummyPlayers,1);
        new Game();
    }
}
