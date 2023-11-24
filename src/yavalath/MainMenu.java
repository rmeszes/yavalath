package yavalath;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

public class MainMenu extends JFrame {
    private static final Logger logger = Logger.getLogger("MainMenu");
    private static final PlayerTypeSelector player1TypeSelector = new PlayerTypeSelector();
    private static final PlayerTypeSelector player2TypeSelector = new PlayerTypeSelector();
    private static final PlayerTypeSelector player3TypeSelector = new PlayerTypeSelector();
    private static final ColorPicker player1ColorPicker = new ColorPicker();
    private static final ColorPicker player2ColorPicker = new ColorPicker();
    private static final ColorPicker player3ColorPicker = new ColorPicker();
    private static final JButton startGameButton = new JButton("Játék indítása");

    private static final JTextField player1NameField = new JTextField("Játékos 1");
    private static final JTextField player2NameField = new JTextField("Játékos 2");
    private static final JTextField player3NameField = new JTextField("Játékos 3");

    static {
        TextFieldListener l = new TextFieldListener();
        player1NameField.setEnabled(false);
        player2NameField.setEnabled(false);
        player3NameField.setEnabled(false);
        player1NameField.getDocument().addDocumentListener(l);
        player2NameField.getDocument().addDocumentListener(l);
        player3NameField.getDocument().addDocumentListener(l);
    }

    public static Player.Type getPlayer1Type() {
        return (Player.Type) player1TypeSelector.getSelectedItem();
    }
    public static Player.Type getPlayer2Type() {
        return (Player.Type) player2TypeSelector.getSelectedItem();
    }
    public static Player.Type getPlayer3Type() {
        return (Player.Type) player3TypeSelector.getSelectedItem();
    }

    public static Color getPlayer1Color() {
        return player1ColorPicker.currentColor;
    }
    public static Color getPlayer2Color() {
        return player2ColorPicker.currentColor;
    }
    public static Color getPlayer3Color() {
        return player3ColorPicker.currentColor;
    }

    private void initializeComponents() {
        JPanel newGamePanel = new JPanel();
        newGamePanel.setLayout(new GridBagLayout());
        newGamePanel.setBorder(new EmptyBorder(10,10,10,10));

        GridBagConstraints middleGBC = new GridBagConstraints();
        middleGBC.fill = GridBagConstraints.VERTICAL;
        middleGBC.gridwidth = 4;
        middleGBC.insets = new Insets(10,5,10,5);
        newGamePanel.add(new JLabel("Kérlek válaszd ki a játékosot típusát és színét:"),middleGBC);


        final String TIPUS = "Típus:";
        final String NEV = "Név:";

        GridBagConstraints labelGBC = new GridBagConstraints();
        labelGBC.insets = new Insets(5,5,5,0);
        labelGBC.fill = GridBagConstraints.VERTICAL;
        labelGBC.anchor = GridBagConstraints.FIRST_LINE_END;
        labelGBC.weightx = 0.5;
        middleGBC.gridy = 1;
        newGamePanel.add(new JLabel("Játékos 1:"),middleGBC);
        labelGBC.gridy = 2;
        newGamePanel.add(new JLabel(NEV),labelGBC);
        labelGBC.gridy = 3;
        newGamePanel.add(new JLabel(TIPUS),labelGBC);
        middleGBC.gridy = 4;
        newGamePanel.add(new JLabel("Játékos 2:"),middleGBC);
        labelGBC.gridy = 5;
        newGamePanel.add(new JLabel(NEV),labelGBC);
        labelGBC.gridy = 6;
        newGamePanel.add(new JLabel(TIPUS),labelGBC);
        middleGBC.gridy = 7;
        newGamePanel.add(new JLabel("Játékos 3:"),middleGBC);
        labelGBC.gridy = 8;
        newGamePanel.add(new JLabel(NEV),labelGBC);
        labelGBC.gridy = 9;
        newGamePanel.add(new JLabel(TIPUS),labelGBC);

        GridBagConstraints nameFieldGBC = new GridBagConstraints();
        nameFieldGBC.insets = new Insets(5,5,5,5);
        nameFieldGBC.gridx = 1;
        nameFieldGBC.gridwidth = 3;
        nameFieldGBC.weightx = 0.5;
        nameFieldGBC.fill = GridBagConstraints.HORIZONTAL;
        nameFieldGBC.gridy = 2;
        newGamePanel.add(player1NameField,nameFieldGBC);
        nameFieldGBC.gridy = 5;
        newGamePanel.add(player2NameField,nameFieldGBC);
        nameFieldGBC.gridy = 8;
        newGamePanel.add(player3NameField,nameFieldGBC);

        GridBagConstraints playerTypeSelectorGBC = new GridBagConstraints();
        playerTypeSelectorGBC.insets = new Insets(5,5,5,5);
        playerTypeSelectorGBC.gridx = 1;
        playerTypeSelectorGBC.fill = GridBagConstraints.HORIZONTAL;
        playerTypeSelectorGBC.gridwidth = 2;
        playerTypeSelectorGBC.weightx = 0.5;
        playerTypeSelectorGBC.gridy = 3;
        newGamePanel.add(player1TypeSelector,playerTypeSelectorGBC);
        playerTypeSelectorGBC.gridy = 6;
        newGamePanel.add(player2TypeSelector,playerTypeSelectorGBC);
        playerTypeSelectorGBC.gridy = 9;
        newGamePanel.add(player3TypeSelector,playerTypeSelectorGBC);


        GridBagConstraints colorPickerGBC = new GridBagConstraints();
        colorPickerGBC.insets = new Insets(5,5,5,5);
        colorPickerGBC.fill = GridBagConstraints.VERTICAL;
        colorPickerGBC.gridx = 3;
        colorPickerGBC.anchor = GridBagConstraints.PAGE_END;
        colorPickerGBC.weightx = 0.5;

        colorPickerGBC.gridy = 3;
        newGamePanel.add(player1ColorPicker,colorPickerGBC);
        colorPickerGBC.gridy = 6;
        newGamePanel.add(player2ColorPicker,colorPickerGBC);
        colorPickerGBC.gridy = 9;
        newGamePanel.add(player3ColorPicker,colorPickerGBC);


        startGameButton.setActionCommand("start game");
        startGameButton.setEnabled(false);
        startGameButton.addActionListener(new ButtonListener());
        middleGBC.gridy = 10;
        newGamePanel.add(startGameButton,middleGBC);

        this.add(BorderLayout.NORTH, newGamePanel);
        this.add(BorderLayout.CENTER, new JSeparator());

        JPanel loadGamePanel = new JPanel();
        loadGamePanel.setLayout(new GridBagLayout());
        GridBagConstraints loadbgc = new GridBagConstraints();
        loadbgc.weightx = 0.5;
        loadGamePanel.add(new JLabel("Vagy tölts be egy játékot"),loadbgc);
        JButton loadGameButton = new JButton("Fájl kiválasztása..");
        loadGameButton.setActionCommand("choose file");
        loadGameButton.addActionListener(new ButtonListener());
        loadbgc.gridy = 1;
        loadGamePanel.add(loadGameButton,loadbgc);

        this.add(loadGamePanel,BorderLayout.SOUTH);
    }
    public MainMenu() {
        super("Yavalath - Főmenü");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(350,525);
        setResizable(false);
        initializeComponents();
    }
    public static class PlayerTypeSelector extends JComboBox<Player.Type> {
        public PlayerTypeSelector() {
            for(Player.Type type : Player.getAllTypes()) {
                addItem(type);
            }
            setActionCommand("player type changed");
            addActionListener(new OnChangeListener());
        }
        private static class OnChangeListener implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGameButton.setEnabled(e.getActionCommand().equals("player type changed") && canGameStart());
                PlayerTypeSelector source = (PlayerTypeSelector) e.getSource();
                if(source == player1TypeSelector) {
                    player1NameField.setEnabled(getPlayer1Type() != Player.Type.NONE);
                } else if (source == player2TypeSelector) {
                    player2NameField.setEnabled(getPlayer2Type() != Player.Type.NONE);
                }else if (source == player3TypeSelector) {
                    player3NameField.setEnabled(getPlayer3Type() != Player.Type.NONE);
                }
            }
        }
    }

    public static class ColorPicker extends JButton {
        private Color currentColor;
        public ColorPicker() {
            currentColor = Color.WHITE;
            setActionCommand("color picker");
            addActionListener(new ColorPickerActionListener());
            setBackground(currentColor);
        }
        private class ColorPickerActionListener implements ActionListener {

            @Override
            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().equals("color picker")) {
                    ColorPicker cp = (ColorPicker) e.getSource();
                    Color selected = JColorChooser.showDialog(cp.getParent(), "Válassz színt", cp.currentColor);
                    if (selected != null) {
                        cp.currentColor = selected;
                        cp.setBackground(currentColor);
                    }
                    startGameButton.setEnabled(canGameStart());
                }
            }
        }
    }

    public static class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            if(actionEvent.getActionCommand().equals("start game") && canGameStart()) {
                Map<Integer,Player> players = HashMap.newHashMap(3);
                players.put(1,new Player(player1NameField.getText(),getPlayer1Color(),getPlayer1Type()));
                players.put(2,new Player(player2NameField.getText(),getPlayer2Color(),getPlayer2Type()));
                players.put(3,new Player(player3NameField.getText(),getPlayer3Color(),getPlayer3Type()));
                Game g = new Game();
                g.initializeGame(players,1);
                SwingUtilities.getWindowAncestor((Component) actionEvent.getSource()).dispose();
            } else if (actionEvent.getActionCommand().equals("choose file")) {
                JFileChooser fc = new JFileChooser(System.getProperty("user.dir"));
                fc.addChoosableFileFilter(new FileNameExtensionFilter("Save file","ser"));
                fc.setAcceptAllFileFilterUsed(false);
                int value = fc.showOpenDialog(SwingUtilities.getWindowAncestor((Component) actionEvent.getSource()));

                if(value == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    logger.info(() -> "File selected: " + file.getName());
                    try(FileInputStream fileStream = new FileInputStream(file); ObjectInputStream objStream = new ObjectInputStream(fileStream)) {
                        Game g = (Game) objStream.readObject();
                        g.reInitialize();
                        g.setVisible(true);
                        SwingUtilities.getWindowAncestor((Component) actionEvent.getSource()).dispose();
                    } catch(IOException | ClassNotFoundException exception) {
                        logger.warning(exception.getMessage());
                    }
                }
            }
        }
    }

    public static class TextFieldListener implements DocumentListener {
        private void onUpdate() {
            startGameButton.setEnabled(canGameStart());
        }

        @Override
        public void insertUpdate(DocumentEvent e) {
            onUpdate();
        }

        @Override
        public void removeUpdate(DocumentEvent e) {
            onUpdate();
        }

        @Override
        public void changedUpdate(DocumentEvent e) {
            onUpdate();
        }
    }
    private static boolean validateThirdPlayer() {
        if(player3NameField.getText().isEmpty()) {
            return false;
        }

        if(player1ColorPicker.getBackground().equals(player3ColorPicker.getBackground()) || player2ColorPicker.getBackground().equals(player3ColorPicker.getBackground())) {
            return false;
        }
        if(player3ColorPicker.getBackground().equals(Color.WHITE)) return false;

        //egyező nevek
        return !player1NameField.getText().equals(player3NameField.getText()) && !player2NameField.getText().equals(player3NameField.getText());
    }

    private static boolean canGameStart() {
        if(player1NameField.getText().equals(player2NameField.getText())) { //egyező nevek
            return false;
        }
        if(player1ColorPicker.getBackground().equals(player2ColorPicker.getBackground())) { //egyező színek
            return false;
        }
        if(player1ColorPicker.getBackground().equals(Color.WHITE) || player2ColorPicker.getBackground().equals(Color.WHITE)) {
            return false;
        }


        if(getPlayer1Type().equals(Player.Type.NONE) || getPlayer2Type().equals(Player.Type.NONE)) {
            return false;
        }


        if(player1NameField.getText().isEmpty() || player2NameField.getText().isEmpty()) {
            return false;
        } else {
            if(!getPlayer3Type().equals(Player.Type.NONE)) { //játszik a 3. játékos is
                return validateThirdPlayer();
            }
        }
        return true;
    }

    public static void main(String[] args) {
        MainMenu m = new MainMenu();
        m.setLocationRelativeTo(null);
        m.setVisible(true);
    }
}