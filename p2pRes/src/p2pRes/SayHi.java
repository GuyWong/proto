package p2pRes;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;

import p2pRes.server.Server;

public class SayHi {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		System.out.println("Hi!");
			
		Executors.newSingleThreadExecutor().execute(new Server(6667)); 
		
		try {
			try {
				Socket client1 = new Socket("127.0.0.1", 6667);
				Socket client2 = new Socket("127.0.0.1", 6667);
				Socket client3 = new Socket("127.0.0.1", 6667);
				
				PrintWriter out1 = new PrintWriter(client1.getOutputStream(), true);
				PrintWriter out2 = new PrintWriter(client2.getOutputStream(), true);
				PrintWriter out3 = new PrintWriter(client3.getOutputStream(), true);
				out1.println("TOTO");
				out2.println("BLABLA");
				out3.println("POUET");
				
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		finally {
			System.out.println("this is the end...");
		}
	}
}
