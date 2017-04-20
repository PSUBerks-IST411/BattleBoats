// This class will be used for the system messages being sent
// to the clients

package battleboats.messages;

import java.io.Serializable;

/**
 *
 * @author Robert Zwolinski
 */
public class SystemMessage implements Serializable {

    private final MsgType msgType;
    private final String strMessage;

    public SystemMessage(MsgType msgType, String strMessage){

        this.msgType = msgType;
        this.strMessage = strMessage;

    }
    
    
    public enum MsgType {Login, Action, Event};
    
    
    
    public MsgType getMsgType(){
        return msgType;
    }
    
    public String getMessage(){
        return strMessage;
    }
    
}
