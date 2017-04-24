
// Server

package battleboats.Server;

import battleboats.internet.Player;
import battleboats.internet.SocketHandler;
import battleboats.messages.LoginMessage;
import battleboats.messages.PlayerListMessage;
import battleboats.messages.PlayerListMessage.PlayerListAction;
import battleboats.messages.SystemMessage;
import battleboats.messages.SystemMessage.MsgType;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;

/**
 *
 * @author Robert Zwolinski
 */
public class Server {
    
    private ServerSocket server;
    private SocketHandler sListen;
    
    private int count = 0;
    
    private DBConnect db;
    private final String dbPath = "resources/database/BattleBoatsDB.db";
    
    private boolean serverUp = true;
    
    
    // List of all active connections
    private ArrayList<SocketHandler> lobbyClients = new ArrayList<>();
    
    
    public static void main(String[] args) {
        new Server(9999, 100);
    }
    
    
    public Server(int intPort, int intMax){
        
        // Open Database connection
        db = new DBConnect(dbPath);
        
        
        
        try {
            server = new ServerSocket(intPort, intMax);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        try {
            while (serverUp){
                
                System.out.println("Listening");
                sListen = new SocketHandler(server.accept());
                increaseCount();
                
                ConnectionThread t1 = new ConnectionThread(sListen, count);
                
                t1.start();
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
    public class ConnectionThread extends Thread {
        
        private SocketHandler s;
        
        
        private boolean running = true;
        
        private int clientNo;
        
        public ConnectionThread(SocketHandler s, int conNo){
            this.s = s;
            clientNo = conNo;
            System.out.println(s.getSocket().getInetAddress().getHostAddress() + "   " + conNo);
        }
        
        @Override
        public void run() {
            
            try {
                
                while (running){
                    
                    Object newMsg = s.readObject();
                    
                    if (newMsg instanceof LoginMessage) {
                        LoginMessage loginMsg = (LoginMessage) newMsg;
                        
                        // Might need to make DB access synchronized here
                        if (db.playerLogin(loginMsg.getUserName(), loginMsg.getPwd())) {
                            s.writeObject(new SystemMessage(MsgType.LoginSuccess, "Success"));
                            
                            Player player = createPlayer(loginMsg.getUserName());
                            s.setPlayer(player);
                            s.writeObject(player); // Sends Player object to client
                            
                            addClient(s); // Add to Lobby
                            
                            
                            
                        } else {
                            s.writeObject(new SystemMessage(MsgType.LoginFail, "Failed"));
                            terminateConnection();
                        }
                        
                    } else if (newMsg instanceof SystemMessage) {
                        SystemMessage sysMsg = (SystemMessage) newMsg;
                        
                        if (sysMsg.getMsgType() == MsgType.TerminateConnection) {
                            //System.out.println(s.getPlayer());
                            terminateConnection();
                        } else if (sysMsg.getMsgType() == MsgType.LobbyChat) {
                            sendChat(sysMsg.getMessage(), s.getPlayer().getUserName(), s);
                        }
                    }
                    
                
                }
                
                
                try {
                    System.out.println("Closing Connection...");
                    this.join();

                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                    System.out.println("Could Not Close");
                }
                
                
            } catch (IOException | ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        
        
        private void terminateConnection() throws IOException{
            
            s.terminateConnection();
            running = false;
            
            removeClient(s);
            
            decreaseCount();
            System.out.println("Active Connections now: " + count);
            
            // TODO: Notify all other clients
            // Refresh list of Active people
            
        }
        
        
    }
    
    private synchronized void sendChat(String strMessage, String strUserName, SocketHandler fromS) throws IOException{
        // TODO: Possibly send out HTML to Bold the username
        SystemMessage chatMsg = new SystemMessage(MsgType.LobbyChat, strUserName + ": " + strMessage);
        for (SocketHandler clients : lobbyClients) {
            if (clients == fromS) { continue; }
            clients.writeObject(chatMsg);
        }
    }
    
    private synchronized void removeClient(SocketHandler s) throws IOException{
        
        if (lobbyClients.remove(s)){
            // Notify all other clients to remove from list
            PlayerListMessage removePlayer = new PlayerListMessage(PlayerListAction.Remove, s.getPlayer());
            SystemMessage playerLeft = new SystemMessage(MsgType.Event, "User " + s.getPlayer().getUserName() + 
                    " has left the lobby!");
            for (SocketHandler clients : lobbyClients) {
                clients.writeObject(removePlayer);
                clients.writeObject(playerLeft);
            }
        }
        
        
    }
    
    private synchronized Player createPlayer(String userName){
        return db.createPlayer(userName);
    }
    
    private synchronized void addClient(SocketHandler s) throws IOException{
        
        // Notify all other clients of new client
        PlayerListMessage newPlayer = new PlayerListMessage(PlayerListAction.Add, s.getPlayer());
        SystemMessage playerJoined = new SystemMessage(MsgType.Event, "User " + s.getPlayer().getUserName() + 
                " has just joined the lobby!");
        for (SocketHandler clients : lobbyClients) {
            clients.writeObject(newPlayer);
            clients.writeObject(playerJoined);
        }
        
        // Add to list of clients
        lobbyClients.add(s);
        
        // Send List of all players to new client
        Player[] players = new Player[lobbyClients.size()];
        for (int i = 0; i < players.length; i++) {
            players[i] = lobbyClients.get(i).getPlayer();
        }
        
        s.writeObject(new PlayerListMessage(PlayerListAction.EntireList, players));
        s.writeObject(new SystemMessage(MsgType.Event, "Welcome to the Battle Boats lobby!"));
        
    }
    
    // Count of clients connected (synchronized)
    private synchronized void increaseCount(){ count++; }
    private synchronized void decreaseCount(){ count--; }
    
    
    
    // Might not need this
    public class ConnectionThreadListener extends Thread {
        
        @Override
        public void run() {
            
            
            
        }
        
    }
    
}
