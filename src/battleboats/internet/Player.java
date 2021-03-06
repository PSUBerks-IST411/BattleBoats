
package battleboats.internet;

import java.io.Serializable;

/**
 *
 * @author Robert Zwolinski
 */
public class Player implements Serializable, Comparable {
    
    private final int intID;
    private String strUserName;
    private int intWins;
    private int intLosses;
    private int intForfeits;
    private String strIP; // Might not need this (could be bad for security)
    private Status status;
    private boolean isMe = false;
    
    
    public enum Status { InLobby, InGame, InQueue, Offline };
    
    
    public Player(int intID, String strUserName, int intWins, int intLosses, int intForfeits){
        
        this.intID = intID;
        this.strUserName = strUserName;
        this.intWins = intWins;
        this.intLosses = intLosses;
        this.intForfeits = intForfeits;
        
        status = Status.InLobby;
        
    }
    
    public void addWin(){
        intWins++;
    }
    
    public void addLoss(){
        intLosses++;
    }
    
    public void addForfeit(){
        intForfeits++;
    }
    
    public String getUserName(){
        return strUserName;
    }
    
    public boolean isMe(){
        return isMe;
    }
    
    public void setIsMe(boolean me){
        isMe = me;
    }
    
    public Status getStatus(){
        return status;
    }
    
    public void setStatus(Status status){
        this.status = status;
    }
    
    public int getID(){
        return intID;
    }
    
    public int getWins(){
        return intWins;
    }
    
    public int getLosses(){
        return intLosses;
    }
    
    public int getForfeits(){
        return intForfeits;
    }
    
    @Override
    public String toString(){
        
        String strYou = isMe ? " (You)" : "";
        
        switch (status) {
            case InGame:
                return strUserName + " (In-Game)" + strYou;
            case InQueue:
                return strUserName + " (In-Queue)" + strYou;
            case InLobby:
                return strUserName + strYou;
            default:
                return strUserName + strYou;
        }
        
    }
    
    @Override
    public int compareTo(Object t) {
        return strUserName.compareToIgnoreCase(((Player) t).getUserName());
    }
    
}
