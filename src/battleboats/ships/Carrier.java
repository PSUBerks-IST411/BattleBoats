
package battleboats.ships;

import battleboats.Assets;

/**
 *
 * @author Robert Zwolinski
 */
public class Carrier extends Ship {
    
    private static final int SIZE = 5;
    
    public Carrier(){
        
        super(SIZE, Assets.imgCarrier);
        
    }
    
}
