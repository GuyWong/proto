package p2pRes.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server implements Runnable {
	private int port;
	private String sharedRep;
	private ExecutorService threadPool;
	
	public Server(int port, String sharedRep) {
		this.port = port;
		this.sharedRep = sharedRep;
		threadPool = Executors.newFixedThreadPool(5/*TODO parameter this*/);
	}
	
	@Override
	public void run() {
		try {
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(port);
			
			threadPool.execute(new ClientConnection(server.accept(), sharedRep));
		} catch (IOException e) {
			e.printStackTrace();//TODO
		} 
	}
}
