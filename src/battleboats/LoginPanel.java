
// Login Panel

package battleboats;

import battleboats.internet.Player;
import battleboats.internet.SocketHandler;
import battleboats.messages.LoginMessage;
import battleboats.messages.SystemMessage;
import battleboats.messages.SystemMessage.MsgType;
import battleboats.security.hashSHA1;
import java.awt.Window;
import java.io.IOException;
import java.net.InetAddress;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Robert Zwolinski
 */
public class LoginPanel extends javax.swing.JPanel {

    private SocketHandler s; // Using 's' so it is quick and easy to type
    
    private hashSHA1 genHash = new hashSHA1();
    
    
    /**
     * Creates new form LoginPanel2
     */
    public LoginPanel() {
        initComponents();
    }

    private void login(){
        
        // To login use UserName: testuser, Password: password
        // UserName's are not case sensitive. Passwords are case sensitive.
        if (!connectServer()) { return; } // If can't connect, exit out of method
        
        String strUser = jtfUserName.getText();
        String strPass = genHash.getHash(jpfPassword.getPassword());
        
        LoginMessage loginMsg = new LoginMessage(strUser, strPass);
        
        
        try {
            sendMessage(loginMsg);
            
            Object newMsg = s.readObject();
            
            if (newMsg instanceof SystemMessage) {
                SystemMessage sysMsg = (SystemMessage) newMsg;
                
                if (sysMsg.getMsgType() == MsgType.LoginSuccess) {
                    
                    //JOptionPane.showMessageDialog(null, sysMsg.getMessage());
                    
                    // Receive Player object from server
                    Player player = (Player) s.readObject();
                    player.setIsMe(true);
                    s.setPlayer(player);
                    
                    MainLobby mainLobby = new MainLobby(s); // Pass SocketHandler to MainLobby
                    mainLobby.setVisible(true);
                    
                    ((Window) getRootPane().getParent()).dispose();
                    
                } else if (sysMsg.getMsgType() == MsgType.LoginFail) {
                    
                    JOptionPane.showMessageDialog(null, sysMsg.getMessage());
                    terminateConnection();
                    
                }
            }
            
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        } 
    }
    
    
    private boolean connectServer(){
        
        try {
            // 98.114.8.244 - main server
            // USE 127.0.0.1 if working on this while main server is not up
            
            s = new SocketHandler(InetAddress.getByName("104.39.13.48"), 9999);
            return true;
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Can't connect to Server",
                    "Timeout", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            return false;
        }
        
    }
    
    public void sendMessage(Object msgOut){
        
        if (s != null && !s.isClosed()) {
            try {
                
                s.writeObject(msgOut);
                
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        
    }
    
    private void terminateConnection() throws IOException{
        s.terminateConnection();
    }
    
    private void showCreateAccount(){
        JFrame jfAccountCreation = new JFrame("Create New Account");
        jfAccountCreation.setContentPane(new jpAccountCreation());
        
        jfAccountCreation.setVisible(true);
        jfAccountCreation.setResizable(false);
        jfAccountCreation.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        jfAccountCreation.pack();
        jfAccountCreation.setLocationRelativeTo(null);
    }
    
    protected void setDefaultButton(){
        this.getRootPane().setDefaultButton(jbLogIn);
    }
    
    /**
     * This method is called from within the constructor to initialize the form. WARNING: Do NOT
     * modify this code. The content of this method is always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        jtfUserName = new javax.swing.JTextField();
        lblUserName = new javax.swing.JLabel();
        lblPassword = new javax.swing.JLabel();
        jpfPassword = new javax.swing.JPasswordField();
        jbCreateAccount = new javax.swing.JButton();
        jbLogIn = new javax.swing.JButton();
        jbPlayOffline = new javax.swing.JButton();

        lblTitle.setFont(new java.awt.Font("Dialog", 1, 48)); // NOI18N
        lblTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblTitle.setText("BattleBoats");

        lblUserName.setText("UserName:");

        lblPassword.setText("Password:");

        jbCreateAccount.setText("Create New Account");
        jbCreateAccount.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbCreateAccountActionPerformed(evt);
            }
        });

        jbLogIn.setText("Log In");
        jbLogIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbLogInActionPerformed(evt);
            }
        });

        jbPlayOffline.setText("Play Offline");
        jbPlayOffline.setEnabled(false);
        jbPlayOffline.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbPlayOfflineActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(71, 71, 71)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblUserName, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblPassword, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(jpfPassword)
                                    .addComponent(jtfUserName, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 166, Short.MAX_VALUE))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jbCreateAccount)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 169, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jbLogIn, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jbPlayOffline, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(78, 78, 78)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jtfUserName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblUserName))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jpfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPassword))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jbLogIn)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jbPlayOffline))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(jbCreateAccount, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(16, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jbCreateAccountActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbCreateAccountActionPerformed
        showCreateAccount();
    }//GEN-LAST:event_jbCreateAccountActionPerformed

    private void jbLogInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbLogInActionPerformed
        login();
    }//GEN-LAST:event_jbLogInActionPerformed

    private void jbPlayOfflineActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbPlayOfflineActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jbPlayOfflineActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jbCreateAccount;
    private javax.swing.JButton jbLogIn;
    private javax.swing.JButton jbPlayOffline;
    private javax.swing.JPasswordField jpfPassword;
    private javax.swing.JTextField jtfUserName;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUserName;
    // End of variables declaration//GEN-END:variables
}
