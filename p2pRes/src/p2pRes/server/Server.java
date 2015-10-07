package p2pRes.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server implements Runnable {
	private int port;
	private ExecutorService threadPool;
	
	public Server(int port) {
		this.port = port;
		threadPool = Executors.newFixedThreadPool(5/*TODO parameter this*/);
	}
	
	@Override
	public void run() {
		try {
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(port);
			
			while (true) {
				threadPool.execute(new ClientConnection(server.accept()));
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
