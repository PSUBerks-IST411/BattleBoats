
package battleboats.ships;

import battleboats.Assets;

/**
 *
 * @author Robert Zwolinski
 */
public class Submarine extends Ship {
    
    private static final int SIZE = 3;
    
    public Submarine(){
        
        super(SIZE, Assets.imgSubmarine);
        
    }
    
}
