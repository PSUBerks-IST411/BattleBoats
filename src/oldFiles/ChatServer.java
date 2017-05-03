/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oldFiles;

/**
 *
 * @author rgs19
 */

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer {

	private int port;
	private List<PrintStream> clients;
	private ServerSocket server;

	public static void main(String[] args) throws IOException {
		new ChatServer(9999).run();
	}

	public ChatServer(int port) {
		this.port = port;
		this.clients = new ArrayList<PrintStream>();
	}

	public void run() throws IOException {
		server = new ServerSocket(port) {
			protected void finalize() throws IOException {
				this.close();
			}
		};
		System.out.println("Port 9999 is now open.");

		while (true) {
			// accepts connection to a client
			Socket client = server.accept();
			System.out.println("Connection established with client: " + client.getInetAddress().getHostAddress());
			
			// add client message to list
			this.clients.add(new PrintStream(client.getOutputStream()));
			
			// new thread creation for client handling
			new Thread(new ClientHandler(this, client.getInputStream())).start();
		}
	}

	void broadcastMessages(String msg) {
		for (PrintStream client : this.clients) {
			client.println(msg);
		}
	}
}

class ClientHandler implements Runnable {

	private ChatServer server;
	private InputStream client;

	public ClientHandler(ChatServer server, InputStream client) {
		this.server = server;
		this.client = client;
	}

	@Override
	public void run() {
		String message;
		
		// when there is a new message, broadcast to all
		Scanner cs = new Scanner(this.client);
		while (cs.hasNextLine()) {
			message = cs.nextLine();
			server.broadcastMessages(message);
		}
		cs.close();
	}
}