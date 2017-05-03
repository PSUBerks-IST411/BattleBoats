
package battleboats.ships;

import battleboats.Assets;

/**
 *
 * @author Robert Zwolinski
 */
public class PatrolBoat extends Ship {
    
    private static final int SIZE = 2;
    
    public PatrolBoat(){
        
        super(SIZE, Assets.imgPatrolBoat);
        
    }
    
}
