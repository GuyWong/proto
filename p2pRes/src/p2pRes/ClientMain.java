package p2pRes;

import p2pRes.client.Client;
import p2pRes.client.ClientException;
import p2pRes.log.Logger;

public class ClientMain {
	public static void main(String[] args) {
		Logger.info("ClientMain...");

		try {
			//(new Client("192.168.1.76", 6667)).getFile("D://Dev//Workspace//test//out//", "test.mkv");
			(new Client("127.0.0.1", 6667)).getFile("D://Dev//Workspace//test//out//", "test.mp4");
		} catch (ClientException e) {
			e.printStackTrace();
		}
		finally {
			Logger.info("this is the end...");
		}
	}
}
