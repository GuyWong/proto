package p2pRes;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class SayHi {

	public static void main(String[] args) {
		System.out.println("Hi!");
		
		
		Thread serverThread = new Thread(new Server(6667));
		serverThread.start();
		try {
			try {
				Socket client = new Socket("127.0.0.1", 6667);
				
				PrintWriter out = new PrintWriter(client.getOutputStream(), true);
				out.println("TOTO");
				out.println("BLABLA");
				
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		finally {
			serverThread.dumpStack();
		}
	}
}
