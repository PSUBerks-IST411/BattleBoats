
// Purpose of this class is to generate SHA-1 hashes for passwords

package battleboats.security;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

/**
 *
 * @author Robert Zwolinski
 */
public class hashSHA1 {
    
    
    
    public hashSHA1(){
        
    }
    
    
    public String getHash(String strToHash){
        
        try {
            
            MessageDigest mdHash = MessageDigest.getInstance("SHA-1");
            
            return byteArrToHex(mdHash.digest(getByteArr(strToHash)));
            
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        
        return "ERROR";
    }
    
    public String getHash(char[] charToHash){
        
        try {
            
            MessageDigest mdHash = MessageDigest.getInstance("SHA-1");
            
            return byteArrToHex(mdHash.digest(getByteArr(charToHash)));
            
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        
        return "ERROR";
    }
    
    
    private byte[] getByteArr(String strToArr){
        
        int len = strToArr.length();
        byte[] byteArr = new byte[len];
            
        for (int i = 0; i < len; i++){
            byteArr[i] = (byte)strToArr.charAt(i);
        }
        
        return byteArr;
    }
    
    private byte[] getByteArr(char[] charToByte){
        
        byte[] byteArr = new byte[charToByte.length];
            
        for (int i = 0; i < charToByte.length; i++){
            byteArr[i] = (byte)charToByte[i];
        }
        
        return byteArr;
    }
    
    
    private static String byteArrToHex(final byte[] hash) {
        
        Formatter formatToHex = new Formatter();
        
        for (byte b : hash) {
            formatToHex.format("%02x", b);
        }
        
        return formatToHex.toString();
    }
    
}
