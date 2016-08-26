package p2pRes;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import p2pRes.common.StaticsValues;
import p2pRes.log.Logger;
import p2pRes.net.server.Server;

public class SrvMain {
	
	public static void main(String[] args) {
		Logger.info("SrvMain...");
		
		ExecutorService thread = Executors.newSingleThreadExecutor();
		thread.execute(new Server(6667, StaticsValues.HARD_CODED_SHAREDPATH, StaticsValues.HARD_CODED_SRVOUTPATH));
		Logger.info(" Running into " + StaticsValues.HARD_CODED_SHAREDPATH);
		Logger.info(" Out path " + StaticsValues.HARD_CODED_SRVOUTPATH);
		while (!thread.isTerminated()) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {Logger.debug(e.getMessage());}
		}
		
		Logger.info("SrvMain end...");
	}
}
