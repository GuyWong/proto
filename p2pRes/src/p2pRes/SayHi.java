package p2pRes;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import p2pRes.protocol.Protocol;
import p2pRes.server.Server;

public class SayHi {

	public static void main(String[] args) {
		System.out.println("Hi!");
			
		Executors.newSingleThreadExecutor().execute(new Server(6667, "C://Utils//Workspace//Test//out")); 
		
		try {
			try {
				Protocol.sendFile(new Socket("127.0.0.1", 6667), "C://Utils//Workspace//Test//test1.txt");
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
