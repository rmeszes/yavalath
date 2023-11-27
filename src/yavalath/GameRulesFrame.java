package yavalath;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.logging.Logger;

public class GameRulesFrame extends JFrame {
    private static final Logger logger = Logger.getLogger("GameRulesFrame");
    public GameRulesFrame() {
        super("Yavalath - játékszabályok");

        JPanel panel = new JPanel();
        JTextPane textPane = new JTextPane();
        textPane.setEditable(false);
        textPane.setCaretColor(new Color(0,0,0,0));
        panel.add(textPane);
        readFile(textPane);
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void readFile(JTextPane pane) {
        Document doc = pane.getDocument();
        try(FileReader fr = new FileReader("resources/gamerules.txt"); BufferedReader reader = new BufferedReader(fr) ) {
            String line;
            while((line = reader.readLine()) != null) {
                doc.insertString(doc.getLength(),line + '\n',pane.getCharacterAttributes());
            }
            pane.setDocument(doc);
        } catch (IOException | BadLocationException e) {
            logger.severe(e.getMessage());
            pane.setText("Nem sikerült a szabályokat betölteni!\n" + e.getMessage());
            pane.setForeground(Color.RED);
        }
    }
}
