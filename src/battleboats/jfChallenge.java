
package battleboats;

import battleboats.internet.Player;
import battleboats.messages.ChallengeMessage;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.Timer;

/**
 *
 * @author Robert Zwolinski
 */
public class jfChallenge extends javax.swing.JFrame {

    private MainLobby mainLobby;
    private ChallengeMessage cMsg;
    private Timer timer = new Timer(1000, new TimeListener());
    private int intTime = 30;
    
    
    /**
     * Creates new form jfChallenge
     */
    public jfChallenge(ChallengeMessage cMsg, MainLobby mainLobby) {
        initComponents();
        setVisible(true);
        setLocationRelativeTo(null);
        
        this.mainLobby = mainLobby;
        this.cMsg = cMsg;
        
        setInfo(cMsg.getChallenger());
        timer.start();
    }

    private void setInfo(Player player){
        
        txtUser.setText(player.getUserName());
        txtWins.setText(String.valueOf(player.getWins()));
        txtLosses.setText(String.valueOf(player.getLosses()));
        txtForfeits.setText(String.valueOf(player.getForfeits()));
        
        
    }
    
    private void accept(){
        
        cMsg.setAction(ChallengeMessage.CAction.Accept);
        try {
            
            mainLobby.sendData(cMsg);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        mainLobby.acceptChallenge(cMsg.getChallenged(), cMsg.getChallenger());
        
        ((Window) getRootPane().getParent()).dispose();
        
    }
    
    private void decline(){
        
        cMsg.setAction(ChallengeMessage.CAction.Decline);
        
        try {
            
            mainLobby.sendData(cMsg);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        ((Window) getRootPane().getParent()).dispose();
        
    }
    
    private class TimeListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            intTime--;
            lblTime.setText(String.valueOf(intTime));
            
            if (intTime <= 0) {
                timer.stop();
                decline();
            }
            
        }
        
    }
    
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblIncoming = new javax.swing.JLabel();
        lblUser = new javax.swing.JLabel();
        lblWins = new javax.swing.JLabel();
        lblLosses = new javax.swing.JLabel();
        lblForfeits = new javax.swing.JLabel();
        txtForfeits = new javax.swing.JTextField();
        txtLosses = new javax.swing.JTextField();
        txtWins = new javax.swing.JTextField();
        txtUser = new javax.swing.JTextField();
        lblTime = new javax.swing.JLabel();
        btnAccept = new javax.swing.JButton();
        btnDecline = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Incoming Challenge!");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        lblIncoming.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        lblIncoming.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblIncoming.setText("Incoming Challenge");

        lblUser.setText("User: ");

        lblWins.setText("Wins: ");

        lblLosses.setText("Losses: ");

        lblForfeits.setText("Forfeits: ");

        txtForfeits.setEditable(false);

        txtLosses.setEditable(false);

        txtWins.setEditable(false);

        txtUser.setEditable(false);

        lblTime.setFont(new java.awt.Font("Dialog", 1, 36)); // NOI18N
        lblTime.setForeground(new java.awt.Color(187, 0, 0));
        lblTime.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTime.setText("30");

        btnAccept.setText("Accept");
        btnAccept.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAcceptActionPerformed(evt);
            }
        });

        btnDecline.setText("Decline");
        btnDecline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeclineActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblIncoming, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addGroup(layout.createSequentialGroup()
                                    .addComponent(lblForfeits)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(txtForfeits))
                                .addGroup(layout.createSequentialGroup()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                            .addGap(13, 13, 13)
                                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(lblUser, javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(lblWins, javax.swing.GroupLayout.Alignment.TRAILING)))
                                        .addComponent(lblLosses, javax.swing.GroupLayout.Alignment.TRAILING))
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtUser)
                                        .addComponent(txtWins)
                                        .addComponent(txtLosses, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnDecline, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(lblTime, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAccept, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 21, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblIncoming, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblUser)
                            .addComponent(txtUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblWins)
                            .addComponent(txtWins, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblLosses)
                            .addComponent(txtLosses, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblForfeits)
                            .addComponent(txtForfeits, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(btnAccept, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblTime, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnDecline, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(1, 1, 1)))
                .addContainerGap(30, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnDeclineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeclineActionPerformed
        timer.stop();
        decline();
    }//GEN-LAST:event_btnDeclineActionPerformed

    private void btnAcceptActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAcceptActionPerformed
        // Accept Challenge
        timer.stop();
        accept();
    }//GEN-LAST:event_btnAcceptActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        timer.stop();
        decline();
    }//GEN-LAST:event_formWindowClosing


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAccept;
    private javax.swing.JButton btnDecline;
    private javax.swing.JLabel lblForfeits;
    private javax.swing.JLabel lblIncoming;
    private javax.swing.JLabel lblLosses;
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lblUser;
    private javax.swing.JLabel lblWins;
    private javax.swing.JTextField txtForfeits;
    private javax.swing.JTextField txtLosses;
    private javax.swing.JTextField txtUser;
    private javax.swing.JTextField txtWins;
    // End of variables declaration//GEN-END:variables
}
