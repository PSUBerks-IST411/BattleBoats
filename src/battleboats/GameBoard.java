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
    
    public static final short DEFENSE_BOARD = 0;
    public static final short ATTACK_BOARD = 1;
    
    private short[][] shotsTaken = new short[10][10];
        public static final short NONE = 0;
        public static final short MISS = 1;
        public static final short HIT = 2;
    
    private boolean isPainted = false;
    private int mouseX = -1, mouseY = -1;
    
    
    int waterPos = 0; // For water animation
    boolean forwardWater = true;
    
    
    private Color colorSquare;
    
    private GameDisplay newGD;
    private final short shortBoard; // To identify which labels to update, 0 for def, 1 for attack
    
    private Ship[] arrShip = new Ship[5]; // 5 ships per board
    private Missile missile = new Missile(); // Missile
    
    private int intSelectedShip = -1;
    
    BufferedImage imgWaterCrop;
    
    public GameBoard(GameDisplay newGD, short shortBoard) {
        
        this.newGD = newGD;
        this.shortBoard = shortBoard;
        
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
        paintHits(g);
        
        
        if (isPainted && shortBoard == ATTACK_BOARD && newGD.isMyTurn() && !missile.isPlaced()) {
            g.drawImage(Assets.imgMissile, mouseX, mouseY, null);
        }
        
        if (shortBoard == ATTACK_BOARD && newGD.isMyTurn() && missile.isPlaced()) {
            g.drawImage(Assets.imgMissile, missile.getPosition().x, missile.getPosition().y, null);
        }
        
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
    
    private void paintHits(Graphics g){
        
        for (int i = 0; i < 10; i++) {
            
            for (int j = 0; j < 10; j++) {
                
                if (shotsTaken[i][j] == MISS) {
                    g.drawImage(Assets.imgX, i * 50, j * 50, null);
                } else if (shotsTaken[i][j] == HIT) {
                    g.drawImage(Assets.imgExplosion, i * 50, j * 50, null);
                }
            }
        }
        
    }
    
    protected Point getMissileLoc(){
        return new Point(missile.getPosition().x, missile.getPosition().y);
    }
    
    protected Missile getMissile(){
        return missile;
    }
    
    protected boolean checkHit(Point location){
        
        int x = ((location.x - (location.x % 50)) / 50);
        int y = ((location.y - (location.y % 50)) / 50);
        
        for (Ship arrShip1 : arrShip) {
            if (arrShip1.isHit(location)) {
                // Put in shotsTaken
                shotsTaken[x][y] = HIT;
                if (arrShip1.isSank()) {
                    newGD.shipSank();
                }
                return true;
            }
        }
        
        // Put in a miss in shotsTaken
        shotsTaken[x][y] = MISS;
        return false;
    }
    
    protected void setShotsTaken(Point location, short result){
        
        int x = (location.x - (location.x % 50)) / 50;
        int y = (location.y - (location.y % 50)) / 50;
        
        shotsTaken[x][y] = result;
        
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
                
                newGD.updateLabels(mouseX / 50, mouseY / 50, oldMouseX / 50, oldMouseY / 50, shortBoard);
                
                
            }
            
        }
        
        
        @Override
        public void mouseExited(MouseEvent e) {
            
            mouseX = -1; 
            mouseY = -1;
            isPainted = false;
            
            newGD.clearLabelColor(shortBoard);
            
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
            
            if (newGD.isMyTurn() && shortBoard == ATTACK_BOARD) {
                missile.setPosition(mouseX, mouseY);
                missile.setPlaced(true);
                newGD.setFireEnabled(true);
            }
            
            if (!newGD.isSetUp()) { return; } // Skip placement code
            
            
            // For Ship placement -------------------------------------
            
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
    
    private void labelEnable(int ship, boolean enabled){
        
        switch (ship) {
                case Ship.CARRIER:
                    newGD.getLblCarrier().setEnabled(enabled);
                    break;
                case Ship.DESTROYER:
                    newGD.getLblDestroyer().setEnabled(enabled);
                    break;
                case Ship.PATROLBOAT:
                    newGD.getLblBoat().setEnabled(enabled);
                    break;
                case Ship.SUBMARINE:
                    newGD.getLblSub().setEnabled(enabled);
                    break;
                case Ship.BATTLESHIP:
                    newGD.getLblBattleship().setEnabled(enabled);
                    break;
                default:
                    break;
            }
        
    }
    
    private void checkReady(){
        
        boolean isReady = true;
        
        for (Ship arrShip1 : arrShip) {
            if (!arrShip1.isPlaced()) {
                isReady = false;
                break;
            }
        }
        
        if (isReady) {
            newGD.updateReadyButton(isReady);
        }
        
    }
    
    private void placeShip(){
        
        if (checkPlacement()) {
            
            arrShip[intSelectedShip].setPlaced(true);
            labelEnable(intSelectedShip, false);
            
            intSelectedShip = -1; // Deselect ship after placement
            
            checkReady(); // Check if all ships have been placed
            
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
        
        return rectBoard.contains(arrShip[intSelectedShip].getShipSpot());
        
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
            labelEnable(selectedShip, true);
            
            newGD.updateReadyButton(false);
            
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
