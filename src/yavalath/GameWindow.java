package yavalath;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class GameWindow extends JFrame {
    public GameWindow(Map<Integer,Player> players){
        super("Yavalath - Játék");
        PlayerModel playerModel = new PlayerModel(players);
        setSize(300,300);
        JTable table = new JTable(playerModel);
        table.setFillsViewportHeight(true);
        this.add(BorderLayout.CENTER,new JScrollPane(table));
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}