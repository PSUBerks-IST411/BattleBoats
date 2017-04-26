
// Server

package battleboats.Server;

import battleboats.internet.Player;
import battleboats.internet.Player.Status;
import battleboats.internet.SocketHandler;
import battleboats.messages.LoginMessage;
import battleboats.messages.PlayerListMessage;
import battleboats.messages.PlayerListMessage.PlayerListAction;
import battleboats.messages.PlayerStatusUpdate;
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
    private volatile ArrayList<SocketHandler> lobbyClients = new ArrayList<>();
    
    
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
                            s.writeObject(new SystemMessage(MsgType.LoginFail, "Incorrect Login"));
                            terminateConnection();
                        }
                        
                    } else if (newMsg instanceof SystemMessage) {
                        SystemMessage sysMsg = (SystemMessage) newMsg;
                        
                        switch (sysMsg.getMsgType()) {
                            case TerminateConnection:
                                terminateConnection();
                                break;
                            case LobbyChat:
                                sendChat(sysMsg.getMessage(), s.getPlayer().getUserName(), s);
                                break;
                            case Challenge:
                                sendToAll(new PlayerStatusUpdate(s.getPlayer().getID(), Status.InQueue), null);
                                updateClientStatus(s, Status.InQueue);
                                sendTo(new SystemMessage(MsgType.Challenge, s.getPlayer().getUserName(),
                                        s.getPlayer().getID()), sysMsg.getID());
                                
                                // TODO
                                // Probably start a new thread here awaiting responce
                                // 60 second time-out (kill thread if no action)
                                
                                break;
                            default:
                                break;
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
            
            // Just for diagnostics
            decreaseCount();
            System.out.println("Active Connections now: " + count);
            
            
        }
        
        
    }
    
    private synchronized void sendTo(Object message, int intToID) throws IOException{
        
        for (SocketHandler clients : lobbyClients) {
            if (clients.getPlayer().getID() == intToID) {
                clients.writeObject(message);
                break; 
            }
        }
        
    }
    
    private synchronized void sendToAll(Object message, SocketHandler fromS) throws IOException{
        
        for (SocketHandler clients : lobbyClients) {
            if (clients == fromS) { continue; }
            clients.writeObject(message);
        }
        
    }
    
    private synchronized void sendChat(String strMessage, String strUserName, SocketHandler fromS) throws IOException{
        // Send out HTML to Bold the username
        SystemMessage chatMsg = new SystemMessage(MsgType.LobbyChat, "<b>" + strUserName + ":</b> " + strMessage);
        sendToAll(chatMsg, fromS);
        
        
        
        
        // TESTINGG
        //PlayerStatusUpdate pStatus = new PlayerStatusUpdate(fromS.getPlayer().getID(), Status.InGame);
        //sendToAll(pStatus, null);
        
        
    }
    
    private synchronized void updateClientStatus(SocketHandler s, Status status){
        
        for (SocketHandler client : lobbyClients) {
            if (client == s) {
                client.getPlayer().setStatus(status);
            }
        }
        
    }
    
    private synchronized void removeClient(SocketHandler s) throws IOException{
        
        if (lobbyClients.remove(s)){
            // Notify all other clients to remove from list
            PlayerListMessage removePlayer = new PlayerListMessage(PlayerListAction.Remove, s.getPlayer());
            SystemMessage playerLeft = new SystemMessage(MsgType.Event, "<i>User " + s.getPlayer().getUserName() + 
                    " has left the lobby!</i>");
            sendToAll(removePlayer, null);
            sendToAll(playerLeft, null); // Will generate this locally instead
        }
        
        
    }
    
    private synchronized Player createPlayer(String userName){
        return db.createPlayer(userName);
    }
    
    private synchronized void addClient(SocketHandler s) throws IOException{
        
        // Notify all other clients of new client
        PlayerListMessage newPlayer = new PlayerListMessage(PlayerListAction.Add, s.getPlayer());
        SystemMessage playerJoined = new SystemMessage(MsgType.Event, "<i>User " + s.getPlayer().getUserName() + 
                " has just joined the lobby!</i>");
        sendToAll(newPlayer, null);
        sendToAll(playerJoined, null);
        
        // Add to list of clients
        lobbyClients.add(s);
        
        // Send List of all players to new client
        ArrayList<Player> players = new ArrayList<>();
        int len = lobbyClients.size();
        for (int i = 0; i < len; i++) {
            players.add(lobbyClients.get(i).getPlayer());
        }
        
        s.writeObject(new PlayerListMessage(PlayerListAction.EntireList, players));
        s.writeObject(new SystemMessage(MsgType.Event, "<i>Welcome to the Battle Boats lobby!</i>"));
        
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
