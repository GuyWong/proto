package p2pRes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server implements Runnable {
	private int port;
	
	public Server(int port) {
		this.port = port;
	}
	
	@Override
	public void run() {
		try {
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(port);
			Socket client = server.accept();
			
			//PrintWriter out = new PrintWriter(client.getOutputStream(), true);                   
	        BufferedReader in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			
	        String inputLine;
            while ((inputLine = in.readLine()) != null) {
                //out.println(inputLine);
            	System.out.println("received: " + inputLine);
            }
	        
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		// TODO Auto-generated method stub
		
	}

}
