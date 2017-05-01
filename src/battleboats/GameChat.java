/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleboats;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 *
 * @author Robert Zwolinski
 */
public class GameChat extends JPanel {
    
    private JLabel lblTitle = new JLabel("CHAT");
    
    public GameChat(){
        
        super();
        
        this.setLayout(null);
        this.setBackground(Color.DARK_GRAY);
        this.setPreferredSize(new Dimension(1500, 250));
        this.setBounds(0, 650, 1500, 250);
        
        lblTitle.setFont(new Font(Font.DIALOG, Font.BOLD, 30));
        
        add(lblTitle);
        lblTitle.setBounds(600, 100, 100, 100);
        
    }
    
}
