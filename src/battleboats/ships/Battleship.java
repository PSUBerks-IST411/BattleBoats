
package battleboats.ships;

import battleboats.Assets;

/**
 *
 * @author Robert Zwolinski
 */
public class Battleship extends Ship {

    private static final int SIZE = 4;
    
    public Battleship(){
        
        super(SIZE, Assets.imgBattleship);
        
    }
    
}
