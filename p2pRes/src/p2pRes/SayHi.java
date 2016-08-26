package p2pRes;

import java.util.concurrent.Executors;
import org.apache.pivot.wtk.DesktopApplicationContext;

import p2pRes.log.Logger;
import p2pRes.net.client.Client;
import p2pRes.net.client.ClientException;
import p2pRes.net.server.Server;
import p2pRes.ui.UIMain;

public class SayHi {

	public static void main(String[] args) {
		DesktopApplicationContext.main(UIMain.class, args);
		
		//DesktopApplicationContext.
		
		/*Logger.info("Hi!");

		Executors.newSingleThreadExecutor().execute(new Server(6667, /*to be asked in protocol*//*"D://Dev//Workspace//test//")); 
		
		try {
			//(new Client("127.0.0.1", 6667)).getFile("C://Dev//workspace//test//out//", "test1.txt");
			//(new Client("127.0.0.1", 6667)).getFile("C://Dev//workspace//test//out//", "test1.jpg");
			//(new Client("127.0.0.1", 6667)).getFile("C://Dev//workspace//test//out//", "test1.zip");
			(new Client("127.0.0.1", 6667)).getFile("D://Dev//Workspace//test//out//", "test.mkv");
		} catch (ClientException e) {
			e.printStackTrace();
		}
		finally {
			Logger.info("this is the end...");
		}*/
	}
}
