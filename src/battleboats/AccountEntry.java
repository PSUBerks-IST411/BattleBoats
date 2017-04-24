/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleboats;

/**
 *
 * @author casey_000
 */
public class AccountEntry 
{
    public String strUsername;
    public String strPassword;
    
    public void setUserName(String strUserName)
    {
        strUsername = strUserName;
    }
    
    public void setPassword(String strPW)
    {
        strPassword = strPW;
    }
    
    public String getUserName()
    {
        return strUsername;
    }
    
    public String getPassword()
    {
        return strPassword;
    }
    
}
