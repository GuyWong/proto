package p2pRes.net.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import p2pRes.common.StaticsValues;
import p2pRes.log.Logger;
import p2pRes.net.io.ChannelException;
import p2pRes.net.io.ServerChannel;
import p2pRes.net.io.protocol.model.ProtocolResponse;

public class Server implements Runnable {
	private final int port;
	private final String sharedRep;
	private final String outRep;
	private final ExecutorService threadPool;
	
	public Server(int port, String sharedRep, String outRep) {
		this.port = port;
		this.sharedRep = sharedRep;
		this.outRep = outRep;
		this.threadPool = Executors.newFixedThreadPool(StaticsValues.MAX_CLIENT_CONNECTION/*TODO parameter this*/);
	}
	
	public void run() {
		try {
			ServerChannel serverChannel = new ServerChannel(port);
			try {
				while (true) {
					serverChannel.waitForClientConnection();
					try {
					    while (true) {
					    	ProtocolResponse response = serverChannel.waitForClientCommand();
					    	Logger.info("Server - instruction read " + response.getCommand().toString());  
					    	if (ProtocolResponse.Command.ASK_NEWCONNECTION == response.getCommand()) {
								threadPool.execute(new MainClientConnection(serverChannel.openSubChannel(), 5, sharedRep, outRep));
							}
							if (ProtocolResponse.Command.ASK_ENDCONNECTION == response.getCommand()) {
								Logger.info("Ending server connection...");
								break;
							}
					    }
					}
					catch (ChannelException e) {			
						e.printStackTrace();//TODO: everything is fatal for now, handle a more subtle return status
					} catch (ServerException e) {
						e.printStackTrace();//TODO: everything is fatal for now, handle a more subtle return status
					}
				}
			}
			finally {
				Logger.info("Closing server...");
				serverChannel.close();
			}
		} catch (ChannelException e) {
			e.printStackTrace();//TODO: everything is fatal for now, handle a more subtle return status
		} 
	}
}
