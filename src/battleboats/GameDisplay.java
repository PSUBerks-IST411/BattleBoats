
package battleboats;

import battleboats.internet.Player;
import battleboats.messages.GameMessage;
import battleboats.ships.Ship;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.swing.*;
import javax.swing.border.BevelBorder;

/**
 *
 * @author Robert Zwolinski
 */
public class GameDisplay extends JPanel {
    
    private GameBoard defBoard = new GameBoard(this, 0);
    private GameBoard attBoard = new GameBoard(this, 1); //Temporarily set to 0
    
    private GameChat gameChat= new GameChat(); 
    
    private GameLoopThread gameLoopThread = new GameLoopThread();
    
    private JLabel[][] lblRow = new JLabel[2][10];
    private JLabel[][] lblCol = new JLabel[2][10];
    
    private JLabel lblCarrier, lblBattleship, lblDestroyer, lblSub, lblBoat;
    private String strSelected = "";
    
    
    private final int SETUP_TIME = 60;
    private final int TURN_TIME = 30;
    private int intTime = SETUP_TIME;
    private Timer turnTimer = new Timer(1000, new TurnListener());
    
    private JButton btnReady = new JButton("Ready!");
    private JButton btnFire = new JButton("Fire!");
    private JLabel lblTime = new JLabel(String.valueOf(intTime));
    private JLabel lblTurn = new JLabel("Waiting for Opponent");
    private JLabel lblPlace = new JLabel("Place your Ships!");
    
    private volatile boolean setUp = true;
    private volatile boolean myTurn = true;
    
    
    private MainLobby mainLobby;
    private Player me;
    private Player opponent;
    
    private boolean running = true;
    
    /**
     * Creates new form newGameBoard
     */
    public GameDisplay(Player me, Player opponent, MainLobby mainLobby) {
        
        initComponents();
        
        this.me = me;
        this.opponent = opponent;
        this.mainLobby = mainLobby;
        
        gameLoopThread.start();
        
    }
    
    // Empty constructor for UI testing
    public GameDisplay(){
        
        initComponents();
        
        gameLoopThread.start();
    }
    
    
    protected void msgControl(GameMessage newMsg){
        
        
        
    }
    
    private void sendData(GameMessage toSend){
        
        try {
            
            mainLobby.sendData(toSend);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
    
    private void initComponents(){
        
        this.setLayout(null);
        
        setPreferredSize(new Dimension(1500, 900));
        setMaximumSize(new Dimension(1500, 900));
        setMinimumSize(new Dimension(1500, 900));
        
        
        add(defBoard);
        defBoard.setBounds(50, 50, 500, 500);
        
        add(attBoard);
        attBoard.setBounds(950, 50, 500, 500);
        attBoard.setVisible(false);
        
        add(gameChat);
        
        
        for (int i = 0; i < 10; i++) {
            
            for (int j = 0; j < 2; j++) {
                lblRow[j][i] = new JLabel(String.valueOf((char) (i + 65)));
                lblRow[j][i].setBounds(j * 900, (i * 50 + 50), 50, 50);
                lblRow[j][i].setHorizontalAlignment(JLabel.CENTER);
                lblRow[j][i].setFont(new Font(Font.DIALOG, Font.BOLD, 20));
                lblRow[j][i].setVisible(false);
                add(lblRow[j][i]);

                lblCol[j][i] = new JLabel(String.valueOf(i + 1));
                lblCol[j][i].setBounds((i * 50 + 50 + (j * 900)), 0, 50, 50);
                lblCol[j][i].setHorizontalAlignment(JLabel.CENTER);
                lblCol[j][i].setFont(new Font(Font.DIALOG, Font.BOLD, 20));
                lblCol[j][i].setVisible(false);
                add(lblCol[j][i]);
            }
        }
        
        lblCarrier = new JLabel("Carrier", new ImageIcon(Assets.imgCarrier[Ship.HORIZONTAL]), JLabel.CENTER);
        
        add(lblCarrier);
        lblCarrier.setBounds(600, 50, 260, 80);
        lblCarrier.setHorizontalTextPosition(JLabel.CENTER);
        lblCarrier.setVerticalTextPosition(JLabel.BOTTOM);
        lblCarrier.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        
        lblCarrier.addMouseListener(new ShipSelectListener()); // Mouse Clicks
        lblCarrier.addMouseMotionListener(new ShipSelectListener()); // Mouse Movements
        lblCarrier.setName("Carrier");
        
        
        lblBattleship = new JLabel("Battleship", new ImageIcon(Assets.imgBattleship[Ship.HORIZONTAL]), JLabel.CENTER);
        
        add(lblBattleship);
        lblBattleship.setBounds(600, 150, 260, 80);
        lblBattleship.setHorizontalTextPosition(JLabel.CENTER);
        lblBattleship.setVerticalTextPosition(JLabel.BOTTOM);
        lblBattleship.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        
        lblBattleship.addMouseListener(new ShipSelectListener()); // Mouse Clicks
        lblBattleship.addMouseMotionListener(new ShipSelectListener()); // Mouse Movements
        lblBattleship.setName("Battleship");
        
        
        lblDestroyer = new JLabel("Destroyer", new ImageIcon(Assets.imgDestroyer[Ship.HORIZONTAL]), JLabel.CENTER);
        
        add(lblDestroyer);
        lblDestroyer.setBounds(600, 250, 260, 80);
        lblDestroyer.setHorizontalTextPosition(JLabel.CENTER);
        lblDestroyer.setVerticalTextPosition(JLabel.BOTTOM);
        lblDestroyer.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        
        lblDestroyer.addMouseListener(new ShipSelectListener()); // Mouse Clicks
        lblDestroyer.addMouseMotionListener(new ShipSelectListener()); // Mouse Movements
        lblDestroyer.setName("Destroyer");
        
        
        lblSub = new JLabel("Submarine", new ImageIcon(Assets.imgSubmarine[Ship.HORIZONTAL]), JLabel.CENTER);
        
        add(lblSub);
        lblSub.setBounds(600, 350, 260, 80);
        lblSub.setHorizontalTextPosition(JLabel.CENTER);
        lblSub.setVerticalTextPosition(JLabel.BOTTOM);
        lblSub.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        
        lblSub.addMouseListener(new ShipSelectListener()); // Mouse Clicks
        lblSub.addMouseMotionListener(new ShipSelectListener()); // Mouse Movements
        lblSub.setName("Submarine");
        
        
        lblBoat = new JLabel("Patrol Boat", new ImageIcon(Assets.imgPatrolBoat[Ship.HORIZONTAL]), JLabel.CENTER);
        
        add(lblBoat);
        lblBoat.setBounds(600, 450, 260, 80);
        lblBoat.setHorizontalTextPosition(JLabel.CENTER);
        lblBoat.setVerticalTextPosition(JLabel.BOTTOM);
        lblBoat.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
        
        lblBoat.addMouseListener(new ShipSelectListener()); // Mouse Clicks
        lblBoat.addMouseMotionListener(new ShipSelectListener()); // Mouse Movements
        lblBoat.setName("Patrolboat");
        
        
        add(lblPlace);
        lblPlace.setBounds(950, 200, 500, 100);
        lblPlace.setHorizontalAlignment(JLabel.CENTER);
        lblPlace.setFont(new Font(Font.DIALOG, Font.BOLD, 48));
        
        // TODO: Place timer and ready button under lblPlace
        add(lblTime);
        lblTime.setBounds(600, 550, 260, 50);
        lblTime.setHorizontalAlignment(JLabel.CENTER);
        lblTime.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
        lblTime.setForeground(Color.RED);
        
        add(btnReady);
        btnReady.setBounds(200, 570, 200, 40);
        
        turnTimer.start();
    }
    
    public void updateLabels(int intCol, int intRow, int intOldCol, int intOldRow, int intBoard){
        
        clearLabelColor(intBoard);
        
        lblRow[intBoard][intRow].setForeground(Color.RED);
        lblCol[intBoard][intCol].setForeground(Color.RED);
        
    }
    
    public void clearLabelColor(int intBoard){
        
        for (int i = 0; i < 10; i++) {
            
            lblRow[intBoard][i].setForeground(Color.BLACK);
            lblCol[intBoard][i].setForeground(Color.BLACK);
            
        }
        
    }
    
    private class ShipSelectListener extends MouseAdapter {
        
        @Override
        public void mouseEntered(MouseEvent e){
            
            if (!((JLabel)e.getSource()).isEnabled()) { return; }
            ((JLabel) e.getSource()).setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.RED, Color.BLACK));
            
        }
        
        @Override
        public void mouseExited(MouseEvent e){
            
            if (!((JLabel)e.getSource()).isEnabled()) { return; }
            
            ((JLabel) e.getSource()).setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            
        }
        
        @Override
        public void mousePressed(MouseEvent e){
            
            strSelected = ((JLabel) e.getSource()).getName();

            defBoard.setSelectedShip(Ship.valueOf(strSelected));
            
            Assets.clipPickup.setFramePosition(0);
            Assets.clipPickup.start();
            
        }
        
    }
    
    
    private class TurnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            
            // This timer will keep track of turns and setup time
            if (setUp) {
                intTime--;
                lblTime.setText(String.valueOf(intTime));
                
                if (intTime <= 0) {
                    // Player ran out of time and forfeit the game
                    turnTimer.stop();
                }
            }
            
            
        }
        
    }
    
    
    private class GameLoopThread extends Thread implements Runnable {
        
        @Override
            public void run() {
                
                // Timing variables
                long now;
                long elapsedTime;
                
                
                while (running) {
                    
                    now = System.currentTimeMillis();
                    
                    defBoard.refresh();
                    attBoard.refresh();
                        
                    elapsedTime = System.currentTimeMillis() - now;
                    
                    try {
                        Thread.sleep(16 - elapsedTime); // 60 FPS
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                    
                }
            }
        
    }
    
    public JLabel getLblCarrier(){
        return lblCarrier;
    }
    
    public JLabel getLblDestroyer(){
        return lblDestroyer;
    }
    
    public JLabel getLblSub(){
        return lblSub;
    }
    
    public JLabel getLblBoat(){
        return lblBoat;
    }
    
    public JLabel getLblBattleship(){
        return lblBattleship;
    }
    
}
