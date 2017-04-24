/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleboats;

import battleboats.messages.SystemMessage;
import battleboats.messages.SystemMessage.MsgType;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;

/**
 *
 * @author Robert Zwolinski
 */
public class BattleBoats {

    
    private JFrame gameDisplay;
    private JFrame login;
    private LoginPanel pLogin;
    
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        BattleBoats gameBB = new BattleBoats();
        gameBB.init();
        
    }
    
    private void init(){
        
        // WARNING: this must be initialized of there will be errors
        // Initializes all graphics and sound files
        Assets.init();
        
        
        
        gameDisplay = new JFrame("Battle Boats!");
        gameDisplay.add(new GameDisplay());
        gameDisplay.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameDisplay.setResizable(false);
        gameDisplay.setVisible(true);
        
        //gameDisplay.add(new GameChat());
        
        gameDisplay.pack();
        gameDisplay.setLocationRelativeTo(null);
        
        
        
        
        login = new JFrame("Login");
        login.add(pLogin = new LoginPanel());
        login.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        login.setResizable(false);
        login.setVisible(true);
        login.pack();
        login.setLocationRelativeTo(null);
        login.addWindowListener(new closeListener());
        
    }
    
    // This code is only for testing purposes
    private class closeListener extends WindowAdapter {


        @Override
        public void windowClosing(WindowEvent we) {
            pLogin.sendMessage(new SystemMessage(MsgType.TerminateConnection, null));
            System.exit(0);
        }

        
    }
    
}
