
package battleboats.messages;

import battleboats.internet.Player;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Robert Zwolinski
 */
public class PlayerListMessage implements Serializable {
    
    public enum PlayerListAction { EntireList, Add, Remove }
    
    private PlayerListAction plAction;
    private ArrayList<Player> players;
    private Player player;
    
    public PlayerListMessage(PlayerListAction plAction, ArrayList<Player> players){
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
    
    public ArrayList<Player> getPlayers(){
        return players;
    }
    
    public Player getPlayer(){
        return player;
    }
    
}
