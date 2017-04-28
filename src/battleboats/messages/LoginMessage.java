/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleboats.messages;

import java.io.Serializable;

/**
 *
 * @author Robert Zwolinski
 */
public class LoginMessage implements Serializable {
    
    private final String userName;
    private final String pwdHash;
    
    public LoginMessage(String strUser, String strPass){
        userName = strUser;
        pwdHash = strPass;
    }
    
    
    public String getUserName(){
        return userName;
    }
    
    public String getPwd(){
        return pwdHash;
    }
    
}
