package p2pRes;

import java.util.concurrent.Executors;

import p2pRes.log.Logger;
import p2pRes.net.client.Client;

public class ClientMain {
	public static void main(String[] args) {
		Logger.info("ClientMain...");

		try {
			//(new Client("192.168.1.76", 6667)).getFile("D://Dev//Workspace//test//out//", "test.mkv");
			
			Executors.newSingleThreadExecutor().execute(new Runnable() { //FIXME; Implements a producer/consumer instead
				public void run() {
					try {
						(new Client("127.0.0.1", 6667)).getFile("C://Utils//dev//test//out", "test.avi");
					} 
					catch (Exception e) {e.printStackTrace();} 
				}
			});
			
			Executors.newSingleThreadExecutor().execute(new Runnable() { //FIXME; Implements a producer/consumer instead
				public void run() {
					try {
						(new Client("127.0.0.1", 6667)).getFile("C://Utils//dev//test//out2", "test.avi");
					} 
					catch (Exception e) {e.printStackTrace();} 
				}
			});
			
			/*Executors.newSingleThreadExecutor().execute(new Runnable() { //FIXME; Implements a producer/consumer instead
				public void run() {
					try {
						(new Client("127.0.0.1", 6667)).sendFile("C://Utils//dev//test//test.mp4");
					} 
					catch (Exception e) {e.printStackTrace();} 
				}
			});*/
		}
		finally {
			Logger.info("this is the end...");
		}
	}
}
