package p2pRes;

import java.util.concurrent.Executors;
import p2pRes.client.Client;
import p2pRes.client.ClientException;
import p2pRes.log.Logger;
import p2pRes.server.Server;

public class SayHi {

	public static void main(String[] args) {
		Logger.info("Hi!");

		Executors.newSingleThreadExecutor().execute(new Server(6667, /*to be asked in protocol*/"C://Dev//workspace//test//")); 
		
		try {
			//(new Client("127.0.0.1", 6667)).getFile("C://Dev//workspace//test//out//", "test1.txt");
			//(new Client("127.0.0.1", 6667)).getFile("C://Dev//workspace//test//out//", "test1.jpg");
			//(new Client("127.0.0.1", 6667)).getFile("C://Dev//workspace//test//out//", "test1.zip");
			(new Client("127.0.0.1", 6667)).getFile("C://Dev//workspace//test//out//", "test.mkv");
		} catch (ClientException e) {
			e.printStackTrace();
		}
		finally {
			Logger.info("this is the end...");
		}
	}
}
