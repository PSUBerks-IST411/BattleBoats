
package battleboats.ships;

import battleboats.Assets;

/**
 *
 * @author Robert Zwolinski
 */
public class Destroyer extends Ship {

    private static final int SIZE = 3;
    
    public Destroyer(){
        
        super(SIZE, Assets.imgDestroyer);
        
    }
    
}
