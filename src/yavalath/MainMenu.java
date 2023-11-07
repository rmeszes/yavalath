package yavalath;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class MainMenu extends JFrame {
    private static final PlayerTypeSelector player1TypeSelector = new PlayerTypeSelector();
    private static final PlayerTypeSelector player2TypeSelector = new PlayerTypeSelector();
    private static final PlayerTypeSelector player3TypeSelector = new PlayerTypeSelector();
    private static final ColorPicker player1ColorPicker = new ColorPicker();
    private static final ColorPicker player2ColorPicker = new ColorPicker();
    private static final ColorPicker player3ColorPicker = new ColorPicker();
    private static final JButton startGameButton = new JButton("Játék indítása");

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

        GridBagConstraints labelGBC = new GridBagConstraints();
        labelGBC.insets = new Insets(5,5,5,0);
        labelGBC.fill = GridBagConstraints.VERTICAL;
        labelGBC.anchor = GridBagConstraints.FIRST_LINE_END;
        labelGBC.weightx = 0.5;
        labelGBC.gridy = 1;
        newGamePanel.add(new JLabel("Játékos 1:"),labelGBC);
        labelGBC.gridy = 2;
        newGamePanel.add(new JLabel("Játékos 2:"),labelGBC);
        labelGBC.gridy = 3;
        newGamePanel.add(new JLabel("Játékos 3:"),labelGBC);

        GridBagConstraints playerTypeSelectorGBC = new GridBagConstraints();
        playerTypeSelectorGBC.insets = new Insets(5,5,5,5);
        playerTypeSelectorGBC.gridx = 1;
        playerTypeSelectorGBC.fill = GridBagConstraints.HORIZONTAL;
        playerTypeSelectorGBC.gridwidth = 2;
        playerTypeSelectorGBC.weightx = 0.5;
        playerTypeSelectorGBC.gridy = 1;
        newGamePanel.add(player1TypeSelector,playerTypeSelectorGBC);
        playerTypeSelectorGBC.gridy = 2;
        newGamePanel.add(player2TypeSelector,playerTypeSelectorGBC);
        playerTypeSelectorGBC.gridy = 3;
        newGamePanel.add(player3TypeSelector,playerTypeSelectorGBC);


        GridBagConstraints colorPickerGBC = new GridBagConstraints();
        colorPickerGBC.insets = new Insets(5,5,5,5);
        colorPickerGBC.fill = GridBagConstraints.VERTICAL;
        colorPickerGBC.gridx = 3;
        colorPickerGBC.anchor = GridBagConstraints.PAGE_END;
        colorPickerGBC.weightx = 0.5;

        colorPickerGBC.gridy = 1;
        newGamePanel.add(player1ColorPicker,colorPickerGBC);
        colorPickerGBC.gridy = 2;
        newGamePanel.add(player1ColorPicker,colorPickerGBC);
        colorPickerGBC.gridy = 3;
        newGamePanel.add(player1ColorPicker,colorPickerGBC);


        startGameButton.setActionCommand("start game");
        startGameButton.setEnabled(false);
        startGameButton.addActionListener(new ButtonListener());
        middleGBC.gridy = 4;
        newGamePanel.add(startGameButton,middleGBC);

        this.add(BorderLayout.NORTH, newGamePanel);
        this.add(BorderLayout.CENTER, new JSeparator());
    }
    public MainMenu() {
        super("Yavalath - Főmenü");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(350,400);
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
                startGameButton.setEnabled(e.getActionCommand().equals("player type changed") && !getPlayer1Type().equals(Player.Type.NONE) && !getPlayer2Type().equals(Player.Type.NONE));
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
                }
            }
        }
    }

    public static class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getActionCommand().equals("start game")) {
                Map<Integer,Player> players = HashMap.newHashMap(3);
                players.put(1,new Player("1. Játékos",getPlayer1Color(),getPlayer1Type()));
                players.put(2,new Player("2. Játékos",getPlayer2Color(),getPlayer2Type()));
                players.put(3,new Player("3. Játékos",getPlayer3Color(),getPlayer3Type()));
                Game.createGame(players);
                SwingUtilities.getWindowAncestor((Component) e.getSource()).dispose();
            }
        }
    }

    public static void main(String[] args) {
        MainMenu m = new MainMenu();
        m.setLocationRelativeTo(null);
        m.setVisible(true);
    }
}