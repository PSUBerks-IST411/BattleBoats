
// This will be used to send on-going game related messages between clients and server
// Things like player moves will use this

package battleboats.messages;

import battleboats.internet.Player;
import java.awt.Point;
import java.io.Serializable;

/**
 *
 * @author Robert Zwolinski
 */
public class GameMessage implements Serializable {
    
    public enum GameAction { Shot, Result_Hit, Result_Miss, ShipSank, AllShipsSunk, SkipTurn, 
                            RequestTurn, First, Second, Ready, Forfeit, AFK };
    
    Player toPlayer;
    Player fromPlayer;
    GameAction gAction;
    Point location;
    //Message type eg. player move, player move result, ready, chat, etc..
    //Message
    
    public GameMessage(GameAction gAction, Player toPlayer){
        this.gAction = gAction;
        this.toPlayer = toPlayer;
    }
    
    public GameMessage(GameAction gAction){
        this.gAction = gAction;
    }
    
    public GameMessage(GameAction gAction, Point location, Player toPlayer){
        this.gAction = gAction;
        this.location = location;
        this.toPlayer = toPlayer;
    }
    
    public void setFromPlayer(Player fromPlayer){
        this.fromPlayer = fromPlayer;
    }
    
    public GameAction getAction(){
        return gAction;
    }
    
    public Player getToPlayer(){
        return toPlayer;
    }
    
    public Player getFromPlayer(){
        return fromPlayer;
    }
    
    public Point getLocation(){
        return location;
    }
}
