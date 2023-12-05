package yavalath;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import java.awt.*;
import java.io.*;
import java.util.logging.Logger;

/**
 * Ez az ablak a szabályok megjelenítésére van
 */
public class GameRulesFrame extends JFrame {
    private static final Logger logger = Logger.getLogger("GameRulesFrame");
    protected final JTextPane pane;
    public GameRulesFrame() {
        super("Yavalath - játékszabályok");

        JPanel panel = new JPanel();
        pane = new JTextPane();
        pane.setEditable(false);
        pane.setCaretColor(new Color(0,0,0,0));
        panel.add(pane);
        readFile();
        this.add(panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    public void readFile() {
        Document doc = pane.getDocument();
        InputStream inputStream = GameRulesFrame.class.getResourceAsStream("/resources/gamerules.txt");
        if(inputStream == null) { //nem a jar-ból futunk
            try(BufferedReader reader = new BufferedReader(new FileReader("resources/gamerules.txt"))) {
                String line;
                while((line = reader.readLine()) != null) {
                    doc.insertString(doc.getLength(),line + '\n',pane.getCharacterAttributes());
                }
                pane.setDocument(doc);
            } catch (IOException | BadLocationException e) {
                readFileError(e);
            }
        } else {
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while((line = reader.readLine()) != null) {
                    doc.insertString(doc.getLength(),line + '\n',pane.getCharacterAttributes());
                }
                pane.setDocument(doc);
            } catch (IOException | BadLocationException e) {
                readFileError(e);
            }
        }


    }

    protected void readFileError(Exception e) {
        logger.severe(e.getMessage());
        pane.setText("Nem sikerült a szabályokat betölteni!\n" + e.getMessage());
        pane.setForeground(Color.RED);
    }
}
