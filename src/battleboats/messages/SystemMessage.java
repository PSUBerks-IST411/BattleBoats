
// This class will be used for the system messages being sent
// to and from the clients

package battleboats.messages;

import java.io.Serializable;

/**
 *
 * @author Robert Zwolinski
 */
public class SystemMessage implements Serializable {

    private final MsgType msgType;
    private String strMessage;
    private int intID;

    public SystemMessage(MsgType msgType, String strMessage){

        this.msgType = msgType;
        this.strMessage = strMessage;

    }
    
    public SystemMessage(MsgType msgType, String strMessage, int intID){

        this.msgType = msgType;
        this.strMessage = strMessage;
        this.intID = intID;

    }
    
    // Login - for login related messages
    // Action -
    // Event - Lobby events
    // TerminateConnection - tells the server to terminate connection
    public enum MsgType {LoginSuccess, LoginFail, Action, Event, 
        LobbyChat, Challenge, TerminateConnection};
    
    
    
    public MsgType getMsgType(){
        return msgType;
    }
    
    public String getMessage(){
        return strMessage;
    }
    
    public int getID(){
        return intID;
    }
}
