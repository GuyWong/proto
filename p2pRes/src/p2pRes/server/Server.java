package p2pRes.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server implements Runnable {
	private BindedPorts ports;
	private String sharedRep;
	private ExecutorService threadPool;
	
	
	public Server(int port, String sharedRep) {
		this.ports = new BindedPorts(port);
		this.sharedRep = sharedRep;
		threadPool = Executors.newFixedThreadPool(5/*TODO parameter this*/);
	}
	
	public int bindNewPort() {
		return this.ports.bind();
	}
	
	public void unbindPort(int port) {
		this.ports.unbind(port);
	}
	
	public void run() {
		try {
			@SuppressWarnings("resource")
			ServerSocket server = new ServerSocket(ports.bind());
			
			threadPool.execute(new MainClientConnection(this, server.accept(), 5, sharedRep));
		} catch (IOException e) {
			e.printStackTrace();//TODO
		} 
	}
	
	private class BindedPorts {
		private int startingPort;
		private Object lock = new Object();
		private Set<Integer> ports = new HashSet<Integer>();
		
		public BindedPorts(int startingPort) {
			this.startingPort = startingPort;
		}
		
		public int bind(){
			synchronized(lock) {
				int candidatePort = startingPort;
				while (ports.contains(candidatePort)) {
					candidatePort++;
				}
				
				ports.add(candidatePort);
				return candidatePort;
			}
		}
		
		public void unbind(int portNumber){
			synchronized(lock) {
				ports.remove(portNumber);
			}
		}
	}
}
