
package battleboats.Server;

import battleboats.messages.AccountEntry;
import battleboats.internet.Player;
import java.sql.*;

/**
 *
 * @author Robert Zwolinski
 */
public class DBConnect {
    
    private final String DB_CLASS = "org.sqlite.JDBC";
    private final String JDBC_STRING = "jdbc:sqlite:";
    
    private Connection dbC;
    private ResultSet rs;
    
    public PreparedStatement sqlFindPlayer;
    public PreparedStatement sqlGetPlayer;
    public PreparedStatement sqlCheckPlayer;
    public PreparedStatement sqlAddPlayer;
    
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
        
        sqlGetPlayer = dbC.prepareStatement(
                "SELECT playerID, userName, wins, losses, forfeits " +
                "FROM Player " + 
                "WHERE userName like ?");
        
        sqlCheckPlayer = dbC.prepareStatement(
                "SELECT userName " + 
                "FROM Player " + 
                "WHERE userName LIKE ?");
        
        sqlAddPlayer = dbC.prepareStatement(
                "INSERT INTO Player (userName, password) " + 
                "VALUES (?, ?)");
    }
    
    public boolean playerLogin(String userName, String password){
        
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
    
    public Player createPlayer(String userName){
        
        try {
            
            sqlGetPlayer.setString(1, userName);
            rs = sqlGetPlayer.executeQuery();
            
            return new Player(rs.getInt("playerID"), rs.getString("userName"), rs.getInt("wins"), 
                    rs.getInt("losses"), rs.getInt("forfeits"));
            
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        System.out.println("Error Creating Player from DB");
        return null; // Something went wrong
    }
    
    public boolean checkPlayer(AccountEntry aeCheckPlayer){
        
        try {
            
            sqlCheckPlayer.setString(1, aeCheckPlayer.getUserName());
            rs = sqlCheckPlayer.executeQuery();
            
            if (!rs.isBeforeFirst()){
                return true;
            }
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return false;
    } 
    
    public boolean addPlayer(AccountEntry aeNewPlayer){
        
        try {
            
            sqlAddPlayer.setString(1, aeNewPlayer.getUserName());
            sqlAddPlayer.setString(2, aeNewPlayer.getPassword());
            sqlAddPlayer.executeUpdate();
            
            return true;
            
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
        return false;
    }
}
