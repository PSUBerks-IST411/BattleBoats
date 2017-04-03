/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleboats;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Robert Zwolinski
 */
public class GameDisplay extends JPanel {
    
    private GameBoard defBoard = new GameBoard(this);
    
    //private ShipSelectListener shipSelectListener= new ShipSelectListener();
    
    private JLabel[] lblRow = new JLabel[10];
    private JLabel[] lblCol = new JLabel[10];
    
    private JLabel lblCarrier, lblBattlship, lblDestroyer, lblSub, lblBoat;
    private String strSelected = "";
    
    /**
     * Creates new form newGameBoard
     */
    public GameDisplay() {
        
        initComponents();
        
    }
    
    private void initComponents(){
        
        this.setLayout(null);
        
        setPreferredSize(new Dimension(1200, 700));
        setMaximumSize(new Dimension(1200, 700));
        setMinimumSize(new Dimension(1200, 700));
        
        add(defBoard);
        defBoard.setBounds(50, 50, 500, 500);
        
        
        
        for (int i = 0; i < 10; i++) {
            
            lblRow[i] = new JLabel(String.valueOf((char) (i + 65)));
            lblRow[i].setBounds(0, (i * 50 + 50), 50, 50);
            lblRow[i].setHorizontalAlignment(JLabel.CENTER);
            lblRow[i].setFont(new Font(Font.DIALOG, Font.BOLD, 20));
            add(lblRow[i]);
            
            lblCol[i] = new JLabel(String.valueOf(i + 1));
            lblCol[i].setBounds((i * 50 + 50), 0, 50, 50);
            lblCol[i].setHorizontalAlignment(JLabel.CENTER);
            lblCol[i].setFont(new Font(Font.DIALOG, Font.BOLD, 20));
            add(lblCol[i]);
        }
        
        //Ship myCarrier = new Carrier();
        //myCarrier.setOrientation(Ship.HORIZONTAL);
        
        lblCarrier = new JLabel("Carrier", new ImageIcon(Assets.imgCarrier[Ship.HORIZONTAL]), JLabel.CENTER);
        
        add(lblCarrier);
        lblCarrier.setBounds(600, 100, 260, 80);
        lblCarrier.setHorizontalTextPosition(JLabel.CENTER);
        lblCarrier.setVerticalTextPosition(JLabel.BOTTOM);
        lblCarrier.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        
        lblCarrier.addMouseListener(new ShipSelectListener()); // Mouse Clicks
        lblCarrier.addMouseMotionListener(new ShipSelectListener()); // Mouse Movements
        lblCarrier.setName("Carrier");
        
    }
    
    public void updateLabels(int intCol, int intRow, int intOldCol, int intOldRow){
        
        clearLabelColor();
        
        lblRow[intRow].setForeground(Color.RED);
        lblCol[intCol].setForeground(Color.RED);
        
    }
    
    public void clearLabelColor(){
        
        for (int i = 0; i < 10; i++) {
            
            lblRow[i].setForeground(Color.BLACK);
            lblCol[i].setForeground(Color.BLACK);
            
        }
        
    }
    
    private class ShipSelectListener extends MouseAdapter {
        
        @Override
        public void mouseEntered(MouseEvent e){
            
            ((JLabel) e.getSource()).setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.RED, Color.BLACK));
            
        }
        
        @Override
        public void mouseExited(MouseEvent e){
            
            if (!((JLabel) e.getSource()).getName().equals(strSelected)) {
                ((JLabel) e.getSource()).setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            }
            
            
        }
        
        @Override
        public void mousePressed(MouseEvent e){
            
            if (!strSelected.equals(((JLabel) e.getSource()).getName())) {
                strSelected = ((JLabel) e.getSource()).getName();
                defBoard.createNewShip(strSelected);
            }
            
            
            
        }
        
    }
    
}
