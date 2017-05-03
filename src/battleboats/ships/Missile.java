
// Missile which gets shot at ships
// simply used to determine the position

package battleboats.ships;

import java.awt.Rectangle;

/**
 *
 * @author Robert Zwolinski
 */
public class Missile {
    
    private Rectangle position;
    private boolean isPlaced = false;
    
    public Missile(){
        position = new Rectangle(-100, -100, 50, 50);
    }
    
    public void setPosition(int x, int y){
        position.x = x;
        position.y = y;
    }
    
    public Rectangle getPosition(){
        return position;
    }
    
    public void setPlaced(boolean isPlaced){
        this.isPlaced = isPlaced;
    }
    
    public boolean isPlaced(){
        return isPlaced;
    }
}
