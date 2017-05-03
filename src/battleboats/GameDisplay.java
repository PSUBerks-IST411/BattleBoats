
package battleboats;

import battleboats.internet.Player;
import battleboats.messages.GameMessage;
import battleboats.messages.GameMessage.GameAction;
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
    
    private GameBoard defBoard = new GameBoard(this, GameBoard.DEFENSE_BOARD);
    private GameBoard attBoard = new GameBoard(this, GameBoard.ATTACK_BOARD);
    
    private GameChat gameChat; 
    
    private GameLoopThread gameLoopThread = new GameLoopThread();
    
    private JLabel[][] lblRow = new JLabel[2][10];
    private JLabel[][] lblCol = new JLabel[2][10];
    
    private JLabel lblCarrier, lblBattleship, lblDestroyer, lblSub, lblBoat;
    private String strSelected = "";
    
    
    private final int SETUP_TIME = 60;
    private final int TURN_TIME = 30;
    private volatile int intTime = SETUP_TIME;
    protected Timer turnTimer = new Timer(1000, new TurnListener());
    
    private JButton btnReady = new JButton("Ready!");
    private JButton btnFire = new JButton("Fire!");
    private JLabel lblTime = new JLabel(String.valueOf(intTime));
    private JLabel lblTurn = new JLabel("Waiting for Opponent...");
    private JLabel lblPlace = new JLabel("Place your Ships!");
    
    private volatile boolean setUp = true;
    private volatile boolean opponentSettingUp = true;
    private volatile boolean myTurn = false;
    
    private int myShips = 5;
    
    private MainLobby mainLobby;
    private Player me;
    private Player opponent;
    
    private boolean running = true;
    
    /**
     * Creates new form newGameBoard
     */
    public GameDisplay(Player me, Player opponent, MainLobby mainLobby) {
        
        gameChat = new GameChat(me.getUserName(), opponent, this);
        
        this.me = me;
        this.opponent = opponent;
        this.mainLobby = mainLobby;
        
        initComponents();
        
        gameLoopThread.start();
        
    }
    
    // Empty constructor for UI testing
    public GameDisplay(){
        
        gameChat = new GameChat("Whatever", opponent, this);
        
        initComponents();
        
        gameLoopThread.start();
    }
    
    
    protected void msgControl(GameMessage gameMsg){
        
        switch (gameMsg.getAction()) {
            case Forfeit:
                turnTimer.stop();
                
                Assets.clipVictory.setFramePosition(0);
                Assets.clipVictory.start();
                
                JOptionPane.showMessageDialog(null, opponent.getUserName() + " has forfeit the game!", 
                        "You Won!", JOptionPane.INFORMATION_MESSAGE);
                mainLobby.setInGame(false);
                ((Window) getRootPane().getParent()).dispose();
                break;
                
            case Ready:
                opponentSettingUp = false;
                break;
                
            case First:
                myTurn(true);
                break;
                
            case Second:
                myTurn(false);
                break;
                
            case SkipTurn:
                // Opponent did not take their turn in time
                myTurn(true);
                break;
                
            case Shot:
                // Check to see if the shot hit any ships
                // Display it on the gameboard
                if (defBoard.checkHit(gameMsg.getLocation())) {
                    
                    // Send back the result
                    sendData(new GameMessage(GameAction.Result_Hit, gameMsg.getLocation(), opponent));
                    
                    // If last ship was sunk, Game Over
                    if (myShips <= 0) {
                        sendData(new GameMessage(GameAction.AllShipsSunk, opponent));

                        // Game Loss Code
                        turnTimer.stop();
                        gameLost();
                        
                        break;

                    }
                    
                } else {
                    
                    sendData(new GameMessage(GameAction.Result_Miss, gameMsg.getLocation(), opponent));
                    
                }
                
                
                
                myTurn(true);
                break;
                
            case Result_Hit:
                // Process the result of last turn taken
                // Display it on the gameboard
                attBoard.setShotsTaken(gameMsg.getLocation(), GameBoard.HIT);
                
                Assets.clipExplosion.setFramePosition(0);
                Assets.clipExplosion.start();
                break;
                
            case Result_Miss:
                // Process the result of last turn taken
                // Display it on the gameboard
                attBoard.setShotsTaken(gameMsg.getLocation(), GameBoard.MISS);
                
                Assets.clipWrong.setFramePosition(0);
                Assets.clipWrong.start();
                break;
                
            case ShipSank:
                // Notify the user that a ship has sank
                // In the chat window in BOLD font
                gameChat.receiveChat("<b>You have sank a ship!</b>");
                break;
                
            case AllShipsSunk:
                // Notify the user of their victory
                turnTimer.stop();
                
                Assets.clipVictory.setFramePosition(0);
                Assets.clipVictory.start();
                
                JOptionPane.showMessageDialog(null, "You have sunk all the ships! Congratulations!", 
                        "Victory!", JOptionPane.INFORMATION_MESSAGE);
                //mainLobby.getS().getPlayer().addWin();
                mainLobby.setInGame(false);
                ((Window) getRootPane().getParent()).dispose();
                
                break;
                
            case Chat:
                gameChat.receiveChat(gameMsg);
                break;
                
            default:
                break;
        }
        
    }
    
    private void myTurn(boolean isMyTurn){
        
        myTurn = isMyTurn;
        lblTurn.setVisible(!isMyTurn);
        btnFire.setEnabled(false);
        btnFire.setVisible(isMyTurn);
        
        resetTimer();
        
        if (isMyTurn) {
            Assets.clipTurn.setFramePosition(0);
            Assets.clipTurn.start();
        }
        
    }
    
    private void resetTimer(){
        
        intTime = TURN_TIME;
        lblTime.setText(String.valueOf(intTime));
        
    }
    
    protected synchronized void sendData(GameMessage toSend){
        
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
                add(lblRow[j][i]);

                lblCol[j][i] = new JLabel(String.valueOf(i + 1));
                lblCol[j][i].setBounds((i * 50 + 50 + (j * 900)), 0, 50, 50);
                lblCol[j][i].setHorizontalAlignment(JLabel.CENTER);
                lblCol[j][i].setFont(new Font(Font.DIALOG, Font.BOLD, 20));
                add(lblCol[j][i]);
            }
        }
        
        setLabelVisible(GameBoard.ATTACK_BOARD, false);
        
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
        lblPlace.setBounds(950, 200, 500, 50);
        lblPlace.setHorizontalAlignment(JLabel.CENTER);
        lblPlace.setFont(new Font(Font.DIALOG, Font.BOLD, 48));
        
        
        add(lblTime);
        lblTime.setBounds(950, 270, 500, 50);
        lblTime.setHorizontalAlignment(JLabel.CENTER);
        lblTime.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
        lblTime.setForeground(Color.RED);
        
        add(btnReady);
        btnReady.setBounds(1100, 340, 200, 40);
        btnReady.addActionListener(new ReadyListener());
        btnReady.setEnabled(false);
        btnReady.setFocusable(false);
        
        add(lblTurn);
        lblTurn.setBounds(960, 575, 500, 50);
        lblTurn.setHorizontalAlignment(JLabel.CENTER);
        lblTurn.setFont(new Font(Font.DIALOG, Font.BOLD, 36));
        lblTurn.setVisible(false);
        
        add(btnFire);
        btnFire.setBounds(1100, 575, 200, 40);
        btnFire.addActionListener(new FireListener());
        btnFire.setVisible(false);
        btnFire.setFocusable(false);
        
        turnTimer.start();
    }
    
    
    protected void setFireEnabled(boolean enable){
        btnFire.setEnabled(enable);
    }
    
    private void playerAFK(){
        
        sendData(new GameMessage(GameAction.AFK, opponent));
        mainLobby.setInGame(false);
        JOptionPane.showMessageDialog(null, "Game has been terminated as both opponents are not present.", 
                            "Terminated", JOptionPane.INFORMATION_MESSAGE);
        // Close window
        ((Window) getRootPane().getParent()).dispose();
        
    }
    
    protected void playerForfeit(){
        
        sendData(new GameMessage(GameAction.Forfeit, opponent));
        
        
    }
    
    private void playerReady(){
        
        lblTime.setBounds(600, 550, 260, 50);
        btnReady.setVisible(false);
        lblPlace.setVisible(false);
        attBoard.setVisible(true);
        lblTurn.setVisible(true);
        setLabelVisible(GameBoard.ATTACK_BOARD, true);

        setUp = false;

        Assets.clipStart.setFramePosition(0);
        Assets.clipStart.start();
        
        
        sendData(new GameMessage(GameAction.Ready, opponent));
        
        if (!opponentSettingUp) {
            sendData(new GameMessage(GameAction.RequestTurn, opponent));
        }
        
    }
    
    private void takeTurn(Point location){
        
        myTurn(false);
        sendData(new GameMessage(GameAction.Shot, location, opponent));
        
        
    }
    
    private void skipTurn(){
        
        myTurn(false);
        sendData(new GameMessage(GameAction.SkipTurn, opponent));
        
    }
    
    private void gameLost(){
        
        Assets.clipDefeat.setFramePosition(0);
        Assets.clipDefeat.start();
        
        //mainLobby.getS().getPlayer().addLoss();
        mainLobby.setInGame(false);
        JOptionPane.showMessageDialog(null, "You have been defeated!", "Defeat", JOptionPane.INFORMATION_MESSAGE);
        
        ((Window) getRootPane().getParent()).dispose();
        
    }
    
    protected void shipSank(){
        
        myShips--;
        
        // Notify the other client they sank a ship
        sendData(new GameMessage(GameAction.ShipSank, opponent));
        
        
        
    }
    
    
    private void setLabelVisible(int board, boolean isVisible){
        
        for (JLabel item : lblRow[board]) {
            item.setVisible(isVisible);
        }
        
        for (JLabel item : lblCol[board]) {
            item.setVisible(isVisible);
        }
        
    }
    
    protected void updateLabels(int intCol, int intRow, int intOldCol, int intOldRow, int intBoard){
        
        clearLabelColor(intBoard);
        
        lblRow[intBoard][intRow].setForeground(Color.RED);
        lblCol[intBoard][intCol].setForeground(Color.RED);
        
    }
    
    protected void clearLabelColor(int intBoard){
        
        for (int i = 0; i < 10; i++) {
            
            lblRow[intBoard][i].setForeground(Color.BLACK);
            lblCol[intBoard][i].setForeground(Color.BLACK);
            
        }
        
    }
    
    protected void updateReadyButton(boolean isReady){
        
        btnReady.setEnabled(isReady);
        
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
            
            if (!setUp) { return; } // Skip placement code
            
            
            // For ship placement ------------------------------
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
            
            intTime--;
            lblTime.setText(String.valueOf(intTime));
                
            if (setUp || opponentSettingUp) {    
                if (intTime <= 0) {
                    // Player ran out of time and forfeit the game
                    // Timer will keep running in case only one of the players is AFK
                    turnTimer.stop();
                    playerAFK();
                }
            }
            
            if (myTurn && !setUp) {
                if (intTime <= 0) {
                    skipTurn();
                }
            }
            
        }
        
    }
    
    private class FireListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            takeTurn(attBoard.getMissileLoc());
            attBoard.getMissile().setPlaced(false);
        }
        
    }
    
    private class ReadyListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            
            playerReady();
            
        }
        
    }
    
    
    private class GameLoopThread extends Thread implements Runnable {
        
        @Override
            public void run() {
                
                // Timing variables
                long now;
                long elapsedTime;
                long ms;
                
                while (running) {
                    
                    now = System.currentTimeMillis();
                    
                    defBoard.refresh();
                    attBoard.refresh();
                        
                    elapsedTime = System.currentTimeMillis() - now;
                    
                    try {
                        ms = 16 - elapsedTime >= 0 ? 16 - elapsedTime : 0;
                        Thread.sleep(ms); // 60 FPS
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
    
    public boolean isSetUp(){
        return setUp;
    }
    
    public boolean isMyTurn(){
        return myTurn;
    }
    
    protected GameChat getGameChat(){
        return gameChat;
    }
}
