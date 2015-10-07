package p2pRes.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ClientConnection implements Runnable {
	private Socket client;
	
	public ClientConnection(Socket client) {
		this.client = client;
	}
	
	@Override
	public void run() {
		try {
			System.out.println("Running client... " + this.toString());             
	        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
	        String inputLine;
            while ((inputLine = in.readLine()) != null) {
            	System.out.println("received: " + inputLine);
            }
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}