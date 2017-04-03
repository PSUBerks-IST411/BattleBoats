/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleboats;

import java.awt.image.BufferedImage;

/**
 *
 * @author Robert Zwolinski
 */
public class Carrier extends Ship {
    
    private BufferedImage boatImage;
    private int intSize;
    private int intOrient;
    
    private boolean boolPlaced = false;
    
    private int shipWidth, shipHeight;
    private int shipLocX, shipLocY;
    
    private int unitWidth = 50, unitHeight = 50;
    
    
    public Carrier(int intOrient) {
        
        super();
        
        intSize = 5;
        
        setOrientation(intOrient);
        
        
    }
    
    private void loadImage() {
        
        boatImage = Assets.imgCarrier[intOrient];
        
    }
    
    private void setDimensions(){
        
        if (intOrient == Ship.VERTICAL || intOrient == Ship.VERTICAL_REVERSE) {
            
            shipWidth = unitWidth;
            shipHeight = unitHeight * intSize;
            
        } else {
            
            shipWidth = unitWidth * intSize;
            shipHeight = unitHeight;
            
        }
        
    }
    
    private void rotateImage(){
        
        loadImage();
        setDimensions();
    }
    
    public boolean isPlaced(){
        return boolPlaced;
    }
    
    public void setPlaced (boolean b){
        boolPlaced = b;
        if (b) {
            Assets.clipPlaced.start();
        }
    }
    
    public void setOrientation(int i){
        intOrient = i;
        rotateImage();
    }
    
    public int getOrientation(){
        return intOrient;
    }
    
    public BufferedImage getImage(){
        return boatImage;
    }
    
    public int getWidth(){
        return shipWidth;
    }
    
    public int getHeight(){
        return shipHeight;
    }
    
    public int getLocX(){
        return shipLocX;
    }
    
    public int getLocY(){
        return shipLocY;
    }
    
    public void setLocX(int x){
        shipLocX = x;
    }
    
    public void setLocY(int y){
        shipLocY = y;
    }
    
}
