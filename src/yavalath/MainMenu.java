package yavalath;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainMenu extends JFrame {
    private final PlayerTypeSelector player1TypeSelector = new PlayerTypeSelector();
    private final PlayerTypeSelector player2TypeSelector = new PlayerTypeSelector();
    private final PlayerTypeSelector player3TypeSelector = new PlayerTypeSelector();
    private final ColorPicker player1ColorPicker = new ColorPicker();
    private final ColorPicker player2ColorPicker = new ColorPicker();
    private final ColorPicker player3ColorPicker = new ColorPicker();
    private void initializeComponents() {
        JPanel newGamePanel = new JPanel();
        BorderLayout paddedBorderLayout = new BorderLayout();
        paddedBorderLayout.setHgap(5);
        paddedBorderLayout.setVgap(5);
        newGamePanel.setLayout(paddedBorderLayout);
        newGamePanel.setBorder(new EmptyBorder(10,10,10,10));

        JPanel labels = new JPanel();
        JPanel playerSelectors = new JPanel();
        JPanel colorPickers = new JPanel();
        colorPickers.setPreferredSize(new Dimension(50,50));

        newGamePanel.add(BorderLayout.WEST,labels);
        newGamePanel.add(BorderLayout.CENTER,playerSelectors);
        newGamePanel.add(BorderLayout.EAST,colorPickers);
        labels.setLayout(new BoxLayout(labels,BoxLayout.Y_AXIS));
        playerSelectors.setLayout(new BoxLayout(playerSelectors,BoxLayout.Y_AXIS));
        colorPickers.setLayout(new BoxLayout(colorPickers,BoxLayout.Y_AXIS));

        labels.add(new Label("Játékos 1:"));
        labels.add(Box.createVerticalGlue());
        labels.add(new Label("Játékos 2:"));
        labels.add(Box.createVerticalGlue());
        labels.add(new Label("Játékos 3:"));

        playerSelectors.add(player1TypeSelector);
        labels.add(Box.createVerticalGlue());
        playerSelectors.add(player2TypeSelector);
        labels.add(Box.createVerticalGlue());
        playerSelectors.add(player3TypeSelector);

        colorPickers.add(player1ColorPicker);
        labels.add(Box.createVerticalGlue());
        colorPickers.add(player2ColorPicker);
        labels.add(Box.createVerticalGlue());
        colorPickers.add(player3ColorPicker);

        this.add(BorderLayout.NORTH, newGamePanel);
    }
    public MainMenu() {
        super("Yavalath - Főmenü");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400,500);
        initializeComponents();
    }
    public static class PlayerTypeSelector extends JComboBox<Player.Type> {
        public PlayerTypeSelector() {
            addItem(Player.Type.NONE);
            addItem(Player.Type.HUMAN);
            addItem(Player.Type.BOT);
        }
    }

    public static class ColorPicker extends JComboBox<Color> {
        public ColorPicker() {
            addItem(Color.BLACK);
            addItem(Color.RED);
            addItem(Color.GREEN);
            addItem(Color.PINK);
            addItem(Color.BLUE);
            setRenderer(new ColorPickerListCellRenderer());
            setEditable(false);
        }
        public static class ColorPickerListCellRenderer extends JPanel implements ListCellRenderer<Color> {
            public ColorPickerListCellRenderer() {
                setOpaque(true);

            }
            boolean b=false;
            @Override
            public void setBackground(Color color) {
                if(!b) {
                    return;
                }
                super.setBackground(color);
            }
            @Override
            public Component getListCellRendererComponent(JList<? extends Color> list, Color value, int index, boolean isSelected, boolean cellHasFocus) {
                b=true;
                super.setBackground(value);
                b=false;
                return this;
            }
        }
    }

    public static void main(String[] args) {
        MainMenu m = new MainMenu();
        m.setLocationRelativeTo(null);
        m.setVisible(true);
    }
}