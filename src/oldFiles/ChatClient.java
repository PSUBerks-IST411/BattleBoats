/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package oldFiles;

import java.io.*;
import java.net.*;
import java.util.Scanner;

/**
 *
 * @author rgs19
 */

public class ChatClient {

	private String host;
	private int port;

	public static void main(String[] args) throws IOException {
            
                // 98.114.8.244 - main server
                // USE 127.0.0.1 if working on this while main server is not up
                
                //TODO: Get machine's IP address (dynamic)
		new ChatClient("104.39.14.85", 9999).run();
	}

	public ChatClient(String IP, int port) {
		host = IP;
		this.port = port;
	}
        
	public void run() throws IOException {
            
		Socket client = new Socket(host, port);
		System.out.println("Connected to BattleBoats server!");

		
		new Thread(new IncomingMessagesHandler(client.getInputStream())).start();

                String username = "testuser"; //just added this for now, wasn't sure what the best way would be to fetch the username from LoginPanel - RGS
		
		System.out.print("User " + username + " has joined the chat");
                
		
		System.out.println("[Please type your message:]");
		
                Scanner cc = new Scanner(System.in);
                PrintStream output = new PrintStream(client.getOutputStream());
                while (cc.hasNextLine()) {
			output.println(username + ": " + cc.nextLine());
		}
		
		output.close();
		cc.close();
		client.close();
	}
}

class IncomingMessagesHandler implements Runnable {

	private InputStream server;

	public IncomingMessagesHandler(InputStream server) {
		this.server = server;
	}

	@Override
	public void run() {
		// receive server messages and print them
		Scanner s = new Scanner(server);
		while (s.hasNextLine()) {
			System.out.println(s.nextLine());
		}
		s.close();
	}
}
