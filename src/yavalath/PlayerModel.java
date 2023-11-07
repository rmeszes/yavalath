package yavalath;

import javax.swing.table.AbstractTableModel;
import java.util.Map;

public class PlayerModel extends AbstractTableModel {
    private final Map<Integer,Player> players;
    public PlayerModel(Map<Integer,Player> players) {
        this.players = players;
    }

    @Override
    public int getRowCount() {
        return players.size();
    }

    @Override
    public int getColumnCount() {
        return 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if(columnIndex == 0) {
            return players.get(rowIndex+1).getType();
        } else {
            return players.get(rowIndex+1).getColor();
        }
    }
}
