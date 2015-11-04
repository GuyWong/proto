package p2pRes;

import java.util.concurrent.Executors;
import p2pRes.client.Client;
import p2pRes.client.ClientException;
import p2pRes.server.Server;

public class SayHi {

	public static void main(String[] args) {
		System.out.println("Hi!");

		Executors.newSingleThreadExecutor().execute(new Server(6667, /*to be asked in protocol*/"C://Utils//Workspace//Test//")); 
		
		try {
			//(new Client("127.0.0.1", 6667)).getFile("C://Utils//Workspace//Test//out//", "test1.txt");
			//(new Client("127.0.0.1", 6667)).getFile("C://Utils//Workspace//Test//out//", "test1.jpg");
			//(new Client("127.0.0.1", 6667)).getFile("C://Utils//Workspace//Test//out//", "test1.pdf");
			(new Client("127.0.0.1", 6667)).getFile("C://Utils//Workspace//Test//out//", "test1.mkv");
		} catch (ClientException e) {
			e.printStackTrace();
		}
		finally {
			System.out.println("this is the end...");
		}
	}
}
