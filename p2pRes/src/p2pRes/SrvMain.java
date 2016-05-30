package p2pRes;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import p2pRes.log.Logger;
import p2pRes.server.Server;

public class SrvMain {
	
	public static void main(String[] args) {
		Logger.info("SrvMain...");
		
		ExecutorService thread = Executors.newSingleThreadExecutor();
		String path = "D://Dev//Workspace//test//";
		thread.execute(new Server(6667, /*to be asked in protocol*/path));
		Logger.info(" Running into " + path);
		while (!thread.isTerminated()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {Logger.debug(e.getMessage());}
		}
		
		Logger.info("SrvMain end...");
	}
}
