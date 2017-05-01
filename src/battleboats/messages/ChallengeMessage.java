
package battleboats.messages;

import battleboats.internet.Player;
import java.io.Serializable;

/**
 *
 * @author Robert Zwolinski
 */
public class ChallengeMessage implements Serializable {
    
    public enum CAction { Request, Accept, Decline, Cancel };
    
    private CAction action;
    private Player challenged;
    private Player challenger;
    
    public ChallengeMessage(CAction action, Player challenged){
        this.action = action;
        this.challenged = challenged;
    }
    
    // The Server will be responsible for setting the 'challenger'
    
    
    public void setChallenger(Player challenger){
        this.challenger = challenger;
    }
    
    public CAction getAction(){
        return action;
    }
    
    public void setAction(CAction action){
        this.action = action;
    }
    
    public Player getChallenger(){
        return challenger;
    }
    
    public Player getChallenged(){
        return challenged;
    }
    
}
