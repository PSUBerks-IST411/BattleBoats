
package battleboats.ships;

import battleboats.Assets;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

/**
 *
 * @author Robert Zwolinski
 */
public class Ship {
    
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int HORIZONTAL_REVERSE = 2;
    public static final int VERTICAL_REVERSE = 3;
    
    public static final int CARRIER = 0;
    public static final int BATTLESHIP = 1;
    public static final int DESTROYER = 2;
    public static final int SUBMARINE = 3;
    public static final int PATROLBOAT = 4;
    
    
    
    private BufferedImage boatImage[] = new BufferedImage[4];
    private final int intSize;
    private int intOrient = Ship.HORIZONTAL;
    
    private boolean boolPlaced = false;
    
    private Rectangle shipSpot = new Rectangle();
    
    private int unitWidth = 50, unitHeight = 50;
    
    
    public Ship(int intSize, BufferedImage[] boatImage) {
        
        this.intSize = intSize;
        this.boatImage = boatImage;
        
        setDimensions();
        
    }
    
    private void setDimensions(){
        
        if (intOrient == Ship.VERTICAL || intOrient == Ship.VERTICAL_REVERSE) {
            
            shipSpot.setSize(unitWidth, unitHeight * intSize);
            
        } else {
            
            shipSpot.setSize(unitWidth * intSize, unitHeight);
            
        }
        
    }
    
    public boolean isPlaced(){
        return boolPlaced;
    }
    
    public void setPlaced (boolean b){
        boolPlaced = b;
        if (b) {
            
            // If true, does some checks to make sure it is valid
            Assets.clipPlaced.setFramePosition(0);
            Assets.clipPlaced.start(); // play sound
            
        }
    }
    
    public void setOrientation(int i){
        intOrient = i;
        setDimensions();
    }
    
    public int getOrientation(){
        return intOrient;
    }
    
    public BufferedImage getImage(){
        return boatImage[intOrient];
    }

    public Rectangle getShipSpot() {
        return shipSpot;
    }
    
    
    public static int valueOf(String inputString){
        switch (inputString.toLowerCase()){
            case "carrier":
                return CARRIER;
            case "battleship":
                return BATTLESHIP;
            case "destroyer":
                return DESTROYER;
            case "submarine":
                return SUBMARINE;
            case "patrolboat":
                return PATROLBOAT;
            default:
                return -1;
        }
    }
    
}
