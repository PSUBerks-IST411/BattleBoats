// TODO: May need to extend this class into an Attacker/Defender GameBoard

package battleboats;

import battleboats.ships.*;
import static battleboats.ships.Ship.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author Robert Zwolinski
 */
public class GameBoard extends JPanel {
    
    private boolean running = true;
    
    private boolean isPainted = false;
    private int mouseX = -1, mouseY = -1;
    
    
    int waterPos = 0; // For water animation
    boolean forwardWater = true;
    
    
    private Color colorSquare;
    
    private GameDisplay newGD;
    private int intBoard; // To identify which labels to update, 0 for def, 1 for attack
    
    private Ship[] arrShip = new Ship[5]; // 5 ships per board
    
    private int intSelectedShip = -1;
    
    BufferedImage imgWaterCrop;
    
    public GameBoard(GameDisplay newGD, int intBoard) {
        
        this.newGD = newGD;
        this.intBoard = intBoard;
        
        createShips();
        
        setPreferredSize(new Dimension(500, 500));
        setMaximumSize(new Dimension(500, 500));
        setMinimumSize(new Dimension(500, 500));
        
        setVisible(true);
        
        this.addMouseListener(new ClickListen());
        this.addMouseMotionListener(new ClickListen());
        this.addMouseWheelListener(new ClickListen());
        
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        
        
        // Paint Water with transparent with background ----------------
        paintWater(g);
        
        g.setColor(new Color(255, 255, 255, 70));
        g.fillRect(0, 0, 500, 500);
        
        
        // Paint the Grid ------------------------------------------
        g.setColor(Color.BLACK);
        
        for (int i = -1; i < 500; i += 50) {
            
            g.fillRect(i, 0, 2, 500);
            g.fillRect(0, i, 500, 2);
            
        }
        
        paintShips(g);
        
        
        // Paint square under mouse pointer (also for testing)
        /*if (isPainted) {
            
            g.setColor(colorSquare);
            g.fillRect(mouseX, mouseY, 50, 50);
            
        }*/
        
    }
    
    private void paintWater(Graphics g){
        
        imgWaterCrop = Assets.imgWater.getSubimage(waterPos, 0, 1000, 1000);
        g.drawImage(imgWaterCrop, 0, 0, 500, 500, null);
        
        if (waterPos >= 1816) {
            forwardWater = false;
        }
        if (waterPos <= 0) {
            forwardWater = true;
        }

        waterPos = (forwardWater) ? waterPos + 1 : waterPos - 1;
        
    }
    
    private void paintShips(Graphics g){
        
            for (int i = 0; i < arrShip.length; i++) {
            
            if ((i == intSelectedShip && isPainted) || arrShip[i].isPlaced()) {
                g.drawImage(arrShip[i].getImage(), arrShip[i].getShipSpot().x, arrShip[i].getShipSpot().y, null);
            }
            
        }
        
    }
    
    
    protected synchronized void refresh() {
        
        repaint();
        
    }
    
    
    private void createShips(){
        
        arrShip[CARRIER] = new Carrier();
        arrShip[BATTLESHIP] = new Battleship();
        arrShip[DESTROYER] = new Destroyer();
        arrShip[SUBMARINE] = new Submarine();
        arrShip[PATROLBOAT] = new PatrolBoat();
        
    }
    
    
    private void newSquareColor(){
        
        colorSquare = new Color( (int) (Math.random()*255), (int) (Math.random()*255), (int) (Math.random()*255));
        
    }
    
    
    public class ClickListen extends MouseAdapter implements MouseListener {
        
        @Override
        public void mouseMoved(MouseEvent e){
            
            int oldMouseX = mouseX, oldMouseY = mouseY;
            
            mouseX = e.getX();
            mouseY = e.getY();
            
            mouseX = mouseX - (mouseX % 50);
            mouseY = mouseY - (mouseY % 50);
            
            
            if (!(oldMouseX == mouseX && oldMouseY == mouseY)) {
                isPainted = true;
                //newSquareColor();
                
                if (intSelectedShip > -1) { 
                    arrShip[intSelectedShip].getShipSpot().setLocation(mouseX, mouseY);
                } else {
                    if (checkOccupiedSpot(mouseX, mouseY)) {
                        setCursor(new Cursor(Cursor.HAND_CURSOR));
                    } else {
                        setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    }
                }
                
                newGD.updateLabels(mouseX / 50, mouseY / 50, oldMouseX / 50, oldMouseY / 50, intBoard);
                
            }
            
        }
        
        
        @Override
        public void mouseExited(MouseEvent e) {
            
            mouseX = -1; 
            mouseY = -1;
            isPainted = false;
            
            newGD.clearLabelColor(intBoard);
            
        }
        
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            
            if (intSelectedShip > -1) {
                
                Assets.clipSwap.setFramePosition(0);
                Assets.clipSwap.start();
                
                if (e.getWheelRotation() > 0) {
                    arrShip[intSelectedShip].setOrientation((arrShip[intSelectedShip].getOrientation() + 1) % 4);
                } else if (e.getWheelRotation() < 0) {
                    int tempOrient = (arrShip[intSelectedShip].getOrientation() == 0) ? 4 : arrShip[intSelectedShip].getOrientation();
                    arrShip[intSelectedShip].setOrientation((tempOrient - 1) % 4);
                }
            }
            
        }
        
        
        @Override
        public void mousePressed(MouseEvent e){
            
            if (e.getButton() == MouseEvent.BUTTON1) {
                if (intSelectedShip > -1) {

                    placeShip(); // check to see if placement is valid and place

                } else {
                    checkPlaced(); // check to see if user is clicking a placed ship
                }
            }
            
            if (e.getButton() == MouseEvent.BUTTON3 && intSelectedShip > -1) {
                
                intSelectedShip = -1;
                Assets.clipDeselect.setFramePosition(0);
                Assets.clipDeselect.start();
                
            }
            
        }
        
    }
    
    private void placeShip(){
        
        if (checkPlacement()) {
            
            arrShip[intSelectedShip].setPlaced(true);
                
            intSelectedShip = -1; // Deselect ship after placement
            
        } else {
            Assets.clipWrong.setFramePosition(0);
            Assets.clipWrong.start();
        }
        
    }
    
    private boolean checkOccupiedSpot(int x, int y){
        
        for (int i = 0; i < arrShip.length; i++) {
            if(arrShip[i].getShipSpot().contains(x, y) && arrShip[i].isPlaced()){
                return true;
            }
        }
        
        return false;
    }
    
    private boolean checkPlacement(){
        
        for (int i = 0; i < arrShip.length; i++) {
            
            if (i == intSelectedShip || !arrShip[i].isPlaced()) {
                continue;
            }
            
            if (arrShip[i].getShipSpot().intersects(arrShip[intSelectedShip].getShipSpot())) {
                return false;
            }
        }
        
        Rectangle rectBoard = new Rectangle(this.getWidth(), this.getHeight());
        
        if (!rectBoard.contains(arrShip[intSelectedShip].getShipSpot())) {
            return false;
        }
        
        return true;
        
    }
    
    private void checkPlaced(){
        
        int i;
        boolean isShip = false;
        
        for (i = 0; i < arrShip.length; i++) {
            
            if (arrShip[i].getShipSpot().contains(mouseX, mouseY) && arrShip[i].isPlaced()) {
                isShip = true;
                break;
            }
        }
        
        if (isShip) {
            setSelectedShip(i);
        }
        
    }
    
    public void setSelectedShip(int selectedShip){
        
        if (selectedShip == intSelectedShip) {
            intSelectedShip = -1;
            Assets.clipDeselect.setFramePosition(0);
            Assets.clipDeselect.start();
            return;
        }
        
        if (selectedShip > -1 && arrShip[selectedShip].isPlaced()) {
            arrShip[selectedShip].setPlaced(false);
            Assets.clipPickup.setFramePosition(0);
            Assets.clipPickup.start();
        }
        
        intSelectedShip = selectedShip;
    }
    
    public int getSelectedShip(){
        return intSelectedShip;
    }
    
    public Ship getAShip(int i){
        
        return arrShip[i];
        
    }
    
}
