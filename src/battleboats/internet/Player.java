
package battleboats.internet;

import java.io.Serializable;

/**
 *
 * @author Robert Zwolinski
 */
public class Player implements Serializable {
    
    private int intID;
    private String strUserName;
    private int intWins;
    private int intLosses;
    private int intForfeits;
    private String strIP; // Might not need this (could be bad for security)
    private Status status;
    
    
    public enum Status { InLobby, InGame, InQueue };
    
    
    public Player(int intID, String strUserName, int intWins, int intLosses, int intForfeits){
        
        this.intID = intID;
        this.strUserName = strUserName;
        this.intWins = intWins;
        this.intLosses = intLosses;
        this.intForfeits = intForfeits;
        
        status = Status.InLobby;
        
    }
    
    
    public String getUserName(){
        return strUserName;
    }
    
    @Override
    public String toString(){
        return strUserName;
    }
    
}
