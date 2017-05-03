
package battleboats.messages;

import battleboats.internet.Player;
import java.io.Serializable;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author Robert Zwolinski
 */
public class PlayerListMessage implements Serializable {
    
    public enum PlayerListAction { EntireList, Add, Remove }
    
    private PlayerListAction plAction;
    private CopyOnWriteArrayList<Player> players;
    private Player player;
    
    public PlayerListMessage(PlayerListAction plAction, CopyOnWriteArrayList<Player> players){
        this.plAction = plAction;
        this.players = players;
    }
    
    public PlayerListMessage(PlayerListAction plAction, Player player){
        this.plAction = plAction;
        this.player = player;
    }
    
    public PlayerListAction getPLAction(){
        return plAction;
    }
    
    public CopyOnWriteArrayList<Player> getPlayers(){
        return players;
    }
    
    public Player getPlayer(){
        return player;
    }
    
}
