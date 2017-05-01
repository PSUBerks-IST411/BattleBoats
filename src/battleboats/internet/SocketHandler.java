
package battleboats.internet;

import battleboats.messages.SystemMessage;
import battleboats.messages.SystemMessage.MsgType;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 *
 * @author Robert Zwolinski
 */
public class SocketHandler {
    
    private Socket socket;
    private ObjectOutputStream outStream;
    private ObjectInputStream inStream;
    
    private Player player; // A connection will be linked to a player (server-side)
    
    
    public SocketHandler(Socket socketIn) throws IOException{
        socket = socketIn;
        setStreams();
    }
    
    public SocketHandler(InetAddress addressIn, int portIn) throws IOException{
        socket = new Socket(addressIn, portIn);
        setStreams();
    }
    
    private void setStreams() throws IOException{
        outStream = new ObjectOutputStream(socket.getOutputStream());
        inStream = new ObjectInputStream(socket.getInputStream());
    }
    
    
    public synchronized void writeObject(Object objectIn) throws IOException{
        outStream.writeObject(objectIn);
        outStream.flush();
    }
    
    public Object readObject() throws IOException, ClassNotFoundException{
        return inStream.readObject();
    }
    
    
    public void terminateNotify() throws IOException{
        writeObject(new SystemMessage(MsgType.TerminateConnection, null));
    }
    
    public void terminateConnection() throws IOException{
        inStream.close();
        outStream.close();
        socket.close();
    }
    
    public void setPlayer(Player playerIn){
        player = playerIn;
    }
    
    public Player getPlayer(){
        return player;
    }
    
    public ObjectOutputStream getOutStream(){
        return outStream;
    }
    
    public ObjectInputStream getInStream(){
        return inStream;
    }
    
    public Socket getSocket(){
        return socket;
    }
    
    public boolean isClosed(){
        return socket.isClosed();
    }
    
}
