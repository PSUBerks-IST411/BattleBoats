/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleboats;

import battleboats.internet.Player;
import battleboats.internet.Player.Status;
import battleboats.internet.SocketHandler;
import battleboats.messages.*;
import battleboats.messages.ChallengeMessage.CAction;
import battleboats.messages.SystemMessage.MsgType;
import java.awt.Font;
import java.awt.event.*;
import java.io.IOException;
import java.util.Collections;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.swing.*;

/**
 *
 * @author Robert Zwolinski
 */
public class MainLobby extends javax.swing.JFrame {

    private MainLobby mainLobby;
    
    private SocketHandler s;
    
    private volatile DefaultListModel listModel;
    
    private ListenThread listenThread;
    
    private JPopupMenu popupMenu = new JPopupMenu();
    private JMenuItem itemChallenge, itemProfile;
    
    private StringBuffer chatText = new StringBuffer();
    
    private CopyOnWriteArrayList<Player> arrPlayers = new CopyOnWriteArrayList<>();
    private volatile boolean running = true;
    private volatile boolean challengePending = false;
    private volatile boolean inGame = false;
    
    
    /**
     * Creates new form MainLobby
     */
    public MainLobby(SocketHandler s) {
        
        this.s = s;
        mainLobby = this;
        
        initComponents();
        buildPopupMenu();
        setLocationRelativeTo(null);
        txtSend.requestFocus();
        txtSend.addKeyListener(new EnterListener());
        
        showChallengeComponents(false);
        
        txtChat.setContentType("text/html");
        
        listModel = new DefaultListModel();
        listPlayers.setModel(listModel);
        listPlayers.setCellRenderer(new MyCellRenderer(s.getPlayer()));
        
        
        listenThread = new ListenThread();
        listenThread.start();
        
    }

    
    
    
    
    public class ListenThread extends Thread {
        
        @Override
        public void run(){
            
            while (running){
                
                try {
                    Object newMsg = s.readObject();
                    
                    if (newMsg instanceof PlayerListMessage) {
                        PlayerListMessage plMsg = (PlayerListMessage) newMsg;
                        switch (plMsg.getPLAction()) {
                            case EntireList:
                                arrPlayers = plMsg.getPlayers();
                                refreshPlayerList(true);
                                break;
                            case Remove:
                                // Shouldn't need to re-sort
                                arrPlayers.remove(plMsg.getPlayer());
                                
                                SwingUtilities.invokeLater(new Runnable(){
                                    @Override
                                    public void run() {
                                        listModel.removeElement(plMsg.getPlayer());
                                        
                                        lblCount.setText(String.valueOf(arrPlayers.size()));
                                    }

                                });
                                
                                break;
                            case Add:
                                arrPlayers.add(plMsg.getPlayer());
                                refreshPlayerList(true);
                                break;
                            default:
                                break;
                        }
                    } else if (newMsg instanceof SystemMessage) {
                        SystemMessage sysMsg = (SystemMessage) newMsg;
                        switch (sysMsg.getMsgType()) {
                            case Event:
                                chatText.append(sysMsg.getMessage());
                                chatText.append("<br /> ");
                                txtChat.setText(chatText.toString());
                                scrollDown();
                                break;
                            case LobbyChat:
                                chatText.append(sysMsg.getMessage());
                                chatText.append("<br /> ");
                                txtChat.setText(chatText.toString());
                                scrollDown();
                                break;
                            /*case Challenge:
                                
                                new jfChallenge(s.getPlayer());
                                chatText.append("<b>You have received a challenge from ");
                                chatText.append(sysMsg.getMessage());
                                chatText.append("!</b><br />");
                                txtChat.setText(chatText.toString());
                                scrollDown();
                                
                                break;*/
                            default:
                                break;
                        }
                    } else if (newMsg instanceof PlayerStatusUpdate) {
                        updatePlayerStatus((PlayerStatusUpdate) newMsg);
                        
                    } else if (newMsg instanceof ChallengeMessage) {
                        ChallengeMessage cMsg = (ChallengeMessage) newMsg;
                        
                        switch (cMsg.getAction()) {
                            
                            case Request:
                                new jfChallenge(cMsg, mainLobby);
                                chatText.append("<b>You have received a challenge from ");
                                chatText.append(cMsg.getChallenger().getUserName());
                                chatText.append("!</b><br />");
                                txtChat.setText(chatText.toString());
                                scrollDown();
                                break;
                                
                            case Decline:
                                chatText.append("<b>Challenge Declined!</b><br />");
                                txtChat.setText(chatText.toString());
                                scrollDown();
                                showChallengeComponents(false);
                                challengePending = false;
                                break;
                                
                            case Accept:
                                
                                acceptChallenge(cMsg.getChallenged());
                                
                                
                                
                                break;
                            default:
                                break;
                        }
                        
                    }
                    
                } catch (IOException | ClassNotFoundException ex) {
                    System.out.println("Connection Closed");
                }
                
            }
            
        }
        
    }
    
    private void scrollDown(){
        txtChat.setCaretPosition(txtChat.getDocument().getLength());
    }
    
    protected void acceptChallenge(Player opponent){
        
        challengePending = false;
        showChallengeComponents(false);
        inGame = true;
        
        
        
        
        
        
        
        
        JFrame gameDisplay = new JFrame("Battle Boats!");
        gameDisplay.add(new GameDisplay(s, opponent));
        gameDisplay.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameDisplay.setResizable(false);
        gameDisplay.setVisible(true);
        
        gameDisplay.pack();
        gameDisplay.setLocationRelativeTo(null);
        
    }
    
    private void challengePlayer(){
        
        btnChallenge.setEnabled(false);
        
        try {
            sendData(new ChallengeMessage(CAction.Request,
                    arrPlayers.get(listPlayers.getSelectedIndex())));
            
            showChallengeComponents(true);
            challengePending  = true;
            
        } catch (IOException ex) {
            ex.printStackTrace();
            btnChallenge.setEnabled(true);
        }
        
    }
    
    private void updatePlayerStatus(PlayerStatusUpdate psUpdate){
        int intID = psUpdate.getPlayerID();
        for (Player player : arrPlayers) {
            if (intID == player.getID()) {
                player.setStatus(psUpdate.getUpdatedStatus());
                break;
            }
        }
        System.out.println("updated");
        refreshPlayerList(false);
    }
    
    private void sortPlayerList(){
        Collections.sort(arrPlayers);
        
    }
    
    private void refreshPlayerList(boolean sort){
        
        // Sort players and display new sorted list
        if (sort) { sortPlayerList(); }
        

        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                listModel.clear();
                arrPlayers.forEach((player) -> {
                    listModel.addElement(player);
                });
                lblCount.setText(String.valueOf(arrPlayers.size()));
                System.out.println("displayed");
            }
            
        });
        
        
        
        
    }
    
    private void buildPopupMenu(){
        
        popupMenu.add(itemChallenge = new JMenuItem("Challenge"));
        itemChallenge.setFont(itemChallenge.getFont().deriveFont(Font.BOLD));
        itemChallenge.addActionListener(new MenuListener());
        
        popupMenu.addSeparator();
        
        popupMenu.add(itemProfile = new JMenuItem("View Profile"));
        itemProfile.setFont(itemProfile.getFont().deriveFont(Font.PLAIN));
        itemProfile.addActionListener(new MenuListener());
        
    }
    
    private void showChallengeComponents(boolean show){
        
        SwingUtilities.invokeLater(new Runnable(){
            @Override
            public void run() {
                
                
                lblTime.setVisible(show);
                lblChallenge.setVisible(show);
                
            }

        });
    }
    
    private class MenuListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            // Menu action
            if ("Challenge".equals(ae.getActionCommand())) {
                
                // CHALLENGE player code
                //sendData(new SystemMessage(MsgType.Challenge, s.getPlayer().getUserName(),
                //        ((Player) arrPlayers.get(listPlayers.getSelectedIndex())).getID()));
                
                challengePlayer();
                
            } else if ("View Profile".equals(ae.getActionCommand())) {
                
                // PROFILE code
                jfProfile profile = new jfProfile(arrPlayers.get(listPlayers.getSelectedIndex()));
                
            }
        }
        
        
        
    }
    
    private class EnterListener extends KeyAdapter {

        @Override
        public void keyPressed(KeyEvent ke) {
            if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
                btnSend.doClick();
                ke.consume();
            }
        }
        
    }
    
    protected synchronized void sendData(Object toSend) throws IOException{
        s.writeObject(toSend);
    }
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jpMainLobby = new javax.swing.JPanel();
        scrollPlayers = new javax.swing.JScrollPane();
        listPlayers = new javax.swing.JList<>();
        scrollChat = new javax.swing.JScrollPane();
        txtChat = new javax.swing.JTextPane();
        scrollTextSend = new javax.swing.JScrollPane();
        txtSend = new javax.swing.JTextPane();
        btnSend = new javax.swing.JButton();
        lblPlayersOnline = new javax.swing.JLabel();
        btnRandom = new javax.swing.JButton();
        btnAI = new javax.swing.JButton();
        btnProfile = new javax.swing.JButton();
        lblChallenge = new javax.swing.JLabel();
        btnChallenge = new javax.swing.JButton();
        lblTime = new javax.swing.JLabel();
        lblCount = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("BattleBoats Lobby");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
        });

        scrollPlayers.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        listPlayers.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                listPlayersMouseReleased(evt);
            }
        });
        scrollPlayers.setViewportView(listPlayers);

        scrollChat.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        txtChat.setEditable(false);
        txtChat.setAutoscrolls(false);
        scrollChat.setViewportView(txtChat);

        scrollTextSend.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        scrollTextSend.setViewportView(txtSend);

        btnSend.setText("Send");
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        lblPlayersOnline.setText("Players Online: ");

        btnRandom.setText("Find Random Opponent");
        btnRandom.setEnabled(false);

        btnAI.setText("Play Against Computer");
        btnAI.setEnabled(false);

        btnProfile.setText("View Your Profile");
        btnProfile.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnProfileActionPerformed(evt);
            }
        });

        lblChallenge.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblChallenge.setForeground(new java.awt.Color(255, 0, 0));
        lblChallenge.setText("Challenge Pending: ");

        btnChallenge.setText("Challenge");
        btnChallenge.setEnabled(false);
        btnChallenge.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChallengeActionPerformed(evt);
            }
        });

        lblTime.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        lblTime.setForeground(new java.awt.Color(255, 0, 0));
        lblTime.setText("30");

        lblCount.setText("0");

        javax.swing.GroupLayout jpMainLobbyLayout = new javax.swing.GroupLayout(jpMainLobby);
        jpMainLobby.setLayout(jpMainLobbyLayout);
        jpMainLobbyLayout.setHorizontalGroup(
            jpMainLobbyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jpMainLobbyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpMainLobbyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollChat)
                    .addComponent(scrollTextSend)
                    .addGroup(jpMainLobbyLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSend))
                    .addGroup(jpMainLobbyLayout.createSequentialGroup()
                        .addComponent(btnAI)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jpMainLobbyLayout.createSequentialGroup()
                        .addComponent(btnRandom)
                        .addGap(18, 18, 18)
                        .addComponent(btnProfile)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                        .addComponent(lblChallenge)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblTime)
                        .addGap(4, 4, 4)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jpMainLobbyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrollPlayers, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jpMainLobbyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(jpMainLobbyLayout.createSequentialGroup()
                            .addComponent(lblPlayersOnline)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(lblCount, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(btnChallenge, javax.swing.GroupLayout.Alignment.LEADING)))
                .addContainerGap())
        );
        jpMainLobbyLayout.setVerticalGroup(
            jpMainLobbyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jpMainLobbyLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jpMainLobbyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jpMainLobbyLayout.createSequentialGroup()
                        .addGroup(jpMainLobbyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnRandom)
                            .addComponent(btnProfile))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnAI))
                    .addGroup(jpMainLobbyLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jpMainLobbyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnChallenge)
                            .addComponent(lblChallenge)
                            .addComponent(lblTime))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jpMainLobbyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblPlayersOnline)
                            .addComponent(lblCount))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jpMainLobbyLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jpMainLobbyLayout.createSequentialGroup()
                        .addComponent(scrollChat, javax.swing.GroupLayout.PREFERRED_SIZE, 316, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(scrollTextSend, javax.swing.GroupLayout.PREFERRED_SIZE, 53, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSend))
                    .addComponent(scrollPlayers))
                .addGap(18, 18, 18))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpMainLobby, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jpMainLobby, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        try {
            running = false;
            s.terminateNotify();
            s.terminateConnection();
            System.exit(0);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_formWindowClosing

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        
        if (txtSend.getText().isEmpty()) { return; }
        
        try {
            
            chatText.append("<b><font color=\"blue\">");
            chatText.append(s.getPlayer().getUserName());
            chatText.append(":</font></b> ");
            chatText.append(txtSend.getText());
            chatText.append("<br /> ");
            txtChat.setText(chatText.toString());
            sendData(new SystemMessage(MsgType.LobbyChat, txtSend.getText()));
            
            txtSend.setText("");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_btnSendActionPerformed

    private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed

    }//GEN-LAST:event_formKeyPressed

    private void listPlayersMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listPlayersMouseReleased
        
        int index = listPlayers.locationToIndex(evt.getPoint());
        if (index < 0) { return; }
        
        if (((Player) arrPlayers.get(index)).isMe() || challengePending || inGame ||
                ((Player) arrPlayers.get(index)).getStatus() != Status.InLobby) {
            itemChallenge.setEnabled(false);
            btnChallenge.setEnabled(false);
        } else {
            itemChallenge.setEnabled(true);
            btnChallenge.setEnabled(true);
        }

        // If Right Click, then show popup menu
        if (evt.isPopupTrigger()) {
            
            
            
            
            listPlayers.setSelectedIndex(index);
            
            
            
            popupMenu.show(this.listPlayers, evt.getX(), evt.getY());
        }
        
        
    }//GEN-LAST:event_listPlayersMouseReleased

    private void btnChallengeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChallengeActionPerformed
        challengePlayer();
    }//GEN-LAST:event_btnChallengeActionPerformed

    private void btnProfileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnProfileActionPerformed
        jfProfile profile = new jfProfile(s.getPlayer());
    }//GEN-LAST:event_btnProfileActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAI;
    private javax.swing.JButton btnChallenge;
    private javax.swing.JButton btnProfile;
    private javax.swing.JButton btnRandom;
    private javax.swing.JButton btnSend;
    private javax.swing.JPanel jpMainLobby;
    private javax.swing.JLabel lblChallenge;
    private javax.swing.JLabel lblCount;
    private javax.swing.JLabel lblPlayersOnline;
    private javax.swing.JLabel lblTime;
    private javax.swing.JList<String> listPlayers;
    private javax.swing.JScrollPane scrollChat;
    private javax.swing.JScrollPane scrollPlayers;
    private javax.swing.JScrollPane scrollTextSend;
    private javax.swing.JTextPane txtChat;
    private javax.swing.JTextPane txtSend;
    // End of variables declaration//GEN-END:variables
}
