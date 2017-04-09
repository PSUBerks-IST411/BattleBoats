
package battleboats.Server;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Robert Zwolinski
 */
public class DBConnect {
    
    private static final String DB_CLASS = "org.sqlite.JDBC";
    private static final String JDBC_STRING = "jdbc:sqlite:";
    
    private Connection dbC;
    private ResultSet rs;
    
    public static PreparedStatement sqlFindPlayer;
    
    
    public DBConnect(String strPath){
        try {
            connectToDB(strPath);
            prepareStatements();
            
        } catch (SQLException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
    
    public void connectToDB(String strPath) throws ClassNotFoundException, SQLException{
        
        Class.forName(DB_CLASS);
        dbC = DriverManager.getConnection(JDBC_STRING + strPath);
        
        //return dbC;
    }
    
    
    private void prepareStatements() throws SQLException{
        
        sqlFindPlayer = dbC.prepareStatement(
                "SELECT userName, password " + 
                "FROM Player " + 
                "WHERE userName like ? and password like ?");
        
    }
    
    public boolean playerLogin(String userName, String password){
        
        // Temporarily using plaintext password until hashing is implemented
        
        try {

            sqlFindPlayer.setString(1, userName);
            sqlFindPlayer.setString(2, password);
            rs = sqlFindPlayer.executeQuery();
            
            
            // Return wether the userName/password combo was in the database
            if (!rs.isBeforeFirst()) {
                return false;
            }
            
            
        } catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
        
        
        
        return true;
        
    }
    
}
