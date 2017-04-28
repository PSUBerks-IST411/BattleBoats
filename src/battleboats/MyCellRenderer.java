
package battleboats;

import battleboats.internet.Player;
import java.awt.Component;
import java.awt.Font;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

/**
 *
 * @author Robert Zwolinski
 */
public class MyCellRenderer extends DefaultListCellRenderer {
    
    private final Player myPlayer;
    
    public MyCellRenderer(Player myPlayer){
        this.myPlayer = myPlayer;
    }
    
    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        
        Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

        if (myPlayer == value) {
            c.setFont(c.getFont().deriveFont(Font.BOLD));
            
        } else {
            c.setFont(c.getFont().deriveFont(Font.PLAIN));
        }
        return c;
    }
    
}
