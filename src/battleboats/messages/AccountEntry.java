
package battleboats.messages;

import java.io.Serializable;

/**
 *
 * @author casey_000
 */
public class AccountEntry implements Serializable
{
    private final String strUserName;
    private final String strPassword;
    
    public AccountEntry(String strUserName, String strPassword){
        this.strUserName = strUserName;
        this.strPassword = strPassword;
    }
    
    public String getUserName()
    {
        return strUserName;
    }
    
    public String getPassword()
    {
        return strPassword;
    }
    
}
