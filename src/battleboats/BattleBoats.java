
// Main Launcher class

package battleboats;

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
        
        
        
        /*gameDisplay = new JFrame("Battle Boats!");
        gameDisplay.add(new GameDisplay());
        gameDisplay.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        gameDisplay.setResizable(false);
        gameDisplay.setVisible(true);
        
        gameDisplay.pack();
        gameDisplay.setLocationRelativeTo(null);*/
        
        
        
        
        login = new JFrame("Login");
        login.add(pLogin = new LoginPanel());
        login.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        login.setResizable(false);
        login.setVisible(true);
        login.pack();
        login.setLocationRelativeTo(null);
        pLogin.setDefaultButton();
        
    }
    
}
