/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleboats;

import javax.swing.JFrame;

/**
 *
 * @author Robert Zwolinski
 */
public class BattleBoats {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Assets.init();
        
        JFrame gameDisplay = new JFrame("Battle Boats!");
        gameDisplay.add(new GameDisplay());
        gameDisplay.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameDisplay.setSize(1200, 700);
        gameDisplay.setResizable(false);
        gameDisplay.setLocationRelativeTo(null);
        gameDisplay.setVisible(true);
        gameDisplay.pack();
        
    }
    
}
