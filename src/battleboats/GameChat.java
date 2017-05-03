
// Panel containing In-game chat

package battleboats;

import battleboats.internet.Player;
import battleboats.messages.GameMessage;
import battleboats.messages.GameMessage.GameAction;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

/**
 *
 * @author Robert Zwolinski
 */
public class GameChat extends JPanel {
    
    private JTextPane txtChat = new JTextPane();
    private JScrollPane scrollChat = new JScrollPane(txtChat);
    private JTextPane txtSend = new JTextPane();
    private JButton btnSend = new JButton("Send");
    private StringBuffer chatText = new StringBuffer();
    
    private String me;
    private Player opponent;
    private GameDisplay gd;
    
    public GameChat(String me, Player opponent, GameDisplay gd){
        
        super();
        
        this.me = me;
        this.opponent = opponent;
        this.gd = gd;
        
        this.setLayout(null);
        this.setBackground(Color.DARK_GRAY);
        this.setPreferredSize(new Dimension(1500, 250));
        this.setBounds(0, 650, 1500, 250);
        
        
        scrollChat.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        add(scrollChat);
        scrollChat.setBounds(10, 10, 1480, 180);
        txtChat.setContentType("text/html");
        txtChat.setEditable(false);
        
        
        add(txtSend);
        txtSend.setBounds(10, 200, 1350, 40);
        txtSend.addKeyListener(new EnterListener());
        
        add(btnSend);
        btnSend.setBounds(1370, 200, 120, 40);
        btnSend.addActionListener(new SendListener());
        btnSend.setFocusable(false);
        
        
        chatText.append("<b>Place Your Ships!</b> <br />");
        txtChat.setText(chatText.toString());
        
        
        txtSend.requestFocus();
        
    }
    
    private void sendData(GameMessage toSend){
        
        gd.sendData(toSend);
        
    }
    
    protected void receiveChat(GameMessage gameMsg){
        
        chatText.append("<b>");
        chatText.append(opponent.getUserName());
        chatText.append(": </b>");
        chatText.append(gameMsg.getMessage());
        chatText.append("<br />");
        txtChat.setText(chatText.toString());
        
        scrollDown();
        
        Assets.clipChatReceive.setFramePosition(0);
        Assets.clipChatReceive.start();
        
    }

    protected void receiveChat(String message){
        
        chatText.append(message);
        chatText.append("<br />");
        txtChat.setText(chatText.toString());
        
        scrollDown();
        
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
    
    private void scrollDown(){
        txtChat.setCaretPosition(txtChat.getDocument().getLength());
    }
    
    private class SendListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent ae) {
            
            if (txtSend.getText().isEmpty()) { return; }
        
            chatText.append("<b><font color=\"blue\">");
            chatText.append(me);
            chatText.append(":</font></b> ");
            chatText.append(txtSend.getText());
            chatText.append("<br /> ");
            txtChat.setText(chatText.toString());
            
            sendData(new GameMessage(GameAction.Chat, txtSend.getText(), opponent));
            
            Assets.clipChatSend.setFramePosition(0);
            Assets.clipChatSend.start();
            
            txtSend.setText("");
            
        }
    }
    
}
