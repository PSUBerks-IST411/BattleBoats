/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleboats;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 *
 * @author Robert Zwolinski
 */
public class GameBoard extends JPanel {
    
    private boolean isPainted = false;
    private int mouseX = -1, mouseY = -1;
    
    private Color colorSquare;
    
    private GameDisplay newGD;
    private Thread t1;

    
    private Carrier shipCarrier;
    
    
    BufferedImage test;
    
    public GameBoard(GameDisplay newGD) {
        
        this.newGD = newGD;
        
        setPreferredSize(new Dimension(500, 500));
        setMaximumSize(new Dimension(500, 500));
        setMinimumSize(new Dimension(500, 500));
        
        this.setBackground(Color.black);
        setVisible(true);
        
        this.addMouseListener(new ClickListen());
        this.addMouseMotionListener(new ClickListen());
        this.addMouseWheelListener(new ClickListen());
        
        
        t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                
                int fps = 60;
                double timePerTick = 1000000000 / fps;
                double delta = 0;
                long now;
                long lastTime = System.nanoTime();
        
                long timer = 0;
                int ticks = 0;
                
                int c = 0; // For water animation
                boolean forwardWater = true;
                
                while (true) {
                    
                    
                    now = System.nanoTime();
                    delta += (now - lastTime) / timePerTick;
                    timer += now - lastTime;
                    lastTime = now;

                    if (delta >= 1) {
                        
                        test = Assets.imgWater.getSubimage(c, 0, 1000, 1000);
                        refresh();
                        
                        if (c >= 1816) {
                            forwardWater = false;
                        }
                        if (c <= 0) {
                            forwardWater = true;
                        }
                        
                        c = (forwardWater) ? c + 1 : c - 1;
                        
                        ticks++;
                        delta--;
                    }

                    if (timer >= 1000000000){
                        System.out.println("Ticks and Frames: " + ticks);
                        ticks = 0;
                        timer = 0;
                    }
                    
                    
                    

                }
            }
            
        });
        
        t1.start();
    }
    
    
    @Override
    protected void paintComponent(Graphics g) {
        
        super.paintComponent(g);
        
        
        // Paint Water with transparent with background ----------------
        g.drawImage(test, 0, 0, 500, 500, null);
        
        g.setColor(new Color(255, 255, 255, 70));
        g.fillRect(0, 0, 500, 500);
        // -------------------------------------------------------------
        
        
        // Paint the Grid ------------------------------------------
        g.setColor(Color.BLACK);
        
        for (int i = -1; i < 500; i += 50) {
            
            g.fillRect(i, 0, 2, 500);
            g.fillRect(0, i, 500, 2);
            
        }
        // ---------------------------------------------------------
        
        
        // Paint random carrier (for testing)
        g.drawImage(Assets.imgCarrier[Ship.HORIZONTAL], 50, 50, 250, 50, null);
        
        
        if (shipCarrier != null && !shipCarrier.isPlaced() && isPainted) {
            g.drawImage(shipCarrier.getImage(), mouseX, mouseY, shipCarrier.getWidth(), shipCarrier.getHeight(), null);
        }
        
        if (shipCarrier != null && shipCarrier.isPlaced()) {
            g.drawImage(shipCarrier.getImage(), shipCarrier.getLocX(), shipCarrier.getLocY(), shipCarrier.getWidth(), shipCarrier.getHeight(), null);
        }
        
        
        
        // Paint square under mouse pointer (also for testing)
        if (isPainted) {
            
            g.setColor(colorSquare);
            g.fillRect(mouseX, mouseY, 50, 50);
            
        }
        
    }
    
    private synchronized void refresh() {
        
        repaint();
        
    }
    
    private void newSquareColor(){
        
        colorSquare = new Color( (int) (Math.random()*255), (int) (Math.random()*255), (int) (Math.random()*255));
        
    }
    
    
    public void createNewShip(String strShip){
        
        switch (strShip){
            case "Carrier":
                shipCarrier = new Carrier(Ship.HORIZONTAL);
            default:
                System.out.println("No Ship Selected");
        }
        
    }
    
    
    public class ClickListen extends MouseAdapter implements MouseListener {
        
        @Override
        public void mouseMoved(MouseEvent e){
            
            int oldMouseX = mouseX, oldMouseY = mouseY;
            
            mouseX = e.getX();
            mouseY = e.getY();
            
            mouseX = mouseX - (mouseX % 50);
            mouseY = mouseY - (mouseY % 50);
            
            
            /*if (shipCarrier != null && !shipCarrier.isPlaced()) {
                
                shipCarrier.setLocX(mouseX);
                shipCarrier.setLocY(mouseY);
                
            }*/
            
            
            
            
            if (!(oldMouseX == mouseX && oldMouseY == mouseY)) {
                isPainted = true;
                newSquareColor();
                
                newGD.updateLabels(mouseX / 50, mouseY / 50, oldMouseX / 50, oldMouseY / 50);
                
                repaint();
            }
            
        }
        
        
        @Override
        public void mouseExited(MouseEvent e) {
            
            mouseX = -1; 
            mouseY = -1;
            isPainted = false;
            
            newGD.clearLabelColor();
            
            repaint();
            
        }
        
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            
            //System.out.println("Wheel Moved");
            
            if (shipCarrier != null && !shipCarrier.isPlaced()) {
                if (e.getWheelRotation() > 0) {
                    shipCarrier.setOrientation((shipCarrier.getOrientation() + 1) % 4);
                } else if (e.getWheelRotation() < 0) {
                    int tempOrient = (shipCarrier.getOrientation() == 0) ? 4 : shipCarrier.getOrientation();
                    shipCarrier.setOrientation((tempOrient - 1) % 4);
                }
            }
            
        }
        
        
        @Override
        public void mousePressed(MouseEvent e){
            
            if (shipCarrier != null && !shipCarrier.isPlaced()) {
                shipCarrier.setPlaced(true);
                shipCarrier.setLocX(mouseX);
                shipCarrier.setLocY(mouseY);
            }
            
        }
        
    }
    
    
}
