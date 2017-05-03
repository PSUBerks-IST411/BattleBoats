/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleboats.messages;

import battleboats.internet.Player.Status;
import java.io.Serializable;

/**
 *
 * @author Robert Zwolinski
 */
public class PlayerStatusUpdate implements Serializable {
    
    private int intPlayerID;
    private Status updatedStatus;
    
    private int playerWin;
    private int playerLose;
    private int playerForfeit;
    
    public PlayerStatusUpdate(int intPlayerID, Status updatedStatus){
        this.intPlayerID = intPlayerID;
        this.updatedStatus = updatedStatus;
    }
    
    public PlayerStatusUpdate(int playerWin, int playerLose, int playerForfeit){
        this.playerWin = playerWin;
        this.playerLose = playerLose;
        this.playerForfeit = playerForfeit;
    }
    
    
    public int getPlayerID(){
        return intPlayerID;
    }
    
    public Status getUpdatedStatus(){
        return updatedStatus;
    }
    
    public int getPlayerWin(){
        return playerWin;
    }
    
    public int getPlayerLose(){
        return playerLose;
    }
    
    public int getPlayerForfeit(){
        return playerForfeit;
    }
    
}
