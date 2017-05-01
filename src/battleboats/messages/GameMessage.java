
// This will be used to send on-going game related messages between clients and server
// Things like player moves will use this

package battleboats.messages;

import battleboats.internet.Player;
import java.io.Serializable;

/**
 *
 * @author Robert Zwolinski
 */
public class GameMessage implements Serializable {
    
    Player toPlayer;
    Player fromPlayer;
    //Message type eg. player move, player move result, ready, chat, etc..
    //Message
    
    public GameMessage(Player toPlayer){
        this.toPlayer = toPlayer;
    }
    
    
    
    
    public void setFromPlayer(Player fromPlayer){
        this.fromPlayer = fromPlayer;
    }
    
}
