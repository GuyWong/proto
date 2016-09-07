package p2pRes;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import p2pRes.log.Logger;
import p2pRes.net.server.Server;

public class SrvMain {
	public final static String HARD_CODED_SRVOUTPATH = "C://Utils//dev//test////srvout//";
	public final static String HARD_CODED_SHAREDPATH = "C://Utils//dev//test//";
	
	
	public static void main(String[] args) {
		Logger.info("SrvMain...");
		
		ExecutorService thread = Executors.newSingleThreadExecutor();
		thread.execute(new Server(6667, HARD_CODED_SHAREDPATH, HARD_CODED_SRVOUTPATH));
		Logger.info(" Running into " + HARD_CODED_SHAREDPATH);
		Logger.info(" Out path " + HARD_CODED_SRVOUTPATH);
		while (!thread.isTerminated()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {Logger.debug(e.getMessage());}
		}
		
		Logger.info("SrvMain end...");
	}
}
