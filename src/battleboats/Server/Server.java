// TODO: Check to make sure the same IP cannot have multiple connections

package battleboats.Server;

import battleboats.messages.LoginMessage;
import battleboats.messages.SystemMessage;
import battleboats.messages.SystemMessage.MsgType;
import battleboats.security.hashSHA1;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Robert Zwolinski
 */
public class Server {
    
    private ServerSocket server;
    private Socket client;
    
    private ObjectOutputStream oOutStream;
    private ObjectInputStream oInStream;
    
    private DBConnect db;
    private final String dbPath;
    
    
    public static void main(String[] args) {
        new Server(9999, 100);
    }
    
    public Server(int intPort, int intMax){
        
        int count = 0;
        //dbPath = getClass().getResource("/database/BattleBoatsDB.db").toString();
        dbPath = "resources/database/BattleBoatsDB.db";
        
        hashSHA1 hashThis = new hashSHA1();
        System.out.println(hashThis.getHash("password"));
        
        
        
        // Open Database connection
        db = new DBConnect(dbPath);
        
        // Not Case Sensitive
        System.out.println(db.playerLogin("TESTuser", "testpass"));
        
        
        try {
            server = new ServerSocket(intPort, intMax);
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        try {
            while (true){
                
                System.out.println("Listening");
                client = server.accept();
                count++;
                
                ConnectionThread t1 = new ConnectionThread(client, count);
                t1.start();
            }
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
    }
    
    public class ConnectionThread extends Thread {
        
        private Socket acceptCon;
        
        private ObjectOutputStream outStream;
        private ObjectInputStream inStream;
        
        private String strIncoming;
        
        private boolean running = true;
        
        private int clientNo;
        
        public ConnectionThread(Socket inSocket, int conNo){
            acceptCon = inSocket;
            clientNo = conNo;
            System.out.println(acceptCon.getInetAddress().getHostAddress() + "   " + conNo);
            //listen();
        }
        
        @Override
        public void run() {
            
            try {
                outStream = new ObjectOutputStream(acceptCon.getOutputStream());
                inStream = new ObjectInputStream(acceptCon.getInputStream());
                
                
                while (running){
                    /*strIncoming = inStream.readUTF();
                
                    System.out.println("Client #" + clientNo + "  IP: " + acceptCon.getInetAddress().getHostAddress());
                    System.out.println("Message: " + strIncoming);
                    
                    outStream.writeUTF("Message Received: " + strIncoming);
                    outStream.flush();*/
                    
                    Object newMsg = inStream.readObject();
                    
                    if (newMsg instanceof LoginMessage) {
                        LoginMessage loginMsg = (LoginMessage) newMsg;
                        
                        if (db.playerLogin(loginMsg.getUserName(), loginMsg.getPwd())) {
                            outStream.writeObject(new SystemMessage(MsgType.Login, "Success"));
                        } else {
                            outStream.writeObject(new SystemMessage(MsgType.Login, "Failed"));
                            inStream.close();
                            outStream.close();
                            running = false;
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
                
                
            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            }
        }
        
        
    }
    
}
