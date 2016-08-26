package p2pRes.net.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import p2pRes.log.Logger;
import p2pRes.net.protocol.ProtocolException;
import p2pRes.net.protocol.ServerProtocol;
import p2pRes.net.protocol.response.ProtocolResponse;


public class Server implements Runnable {
	private BindedPorts ports;
	private String sharedRep;
	private String outRep;
	private ExecutorService threadPool;
	
	public Server(int port, String sharedRep, String outRep) {
		this.ports = new BindedPorts(port);
		this.sharedRep = sharedRep;
		this.outRep = outRep;
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
			
			while (true) {//Main loop //FIXME: handle the empty Protocol.readByte bug
				try {
				    ServerProtocol serverProtocol = new ServerProtocol(server.accept());
				    
				    while (true) {
				    	ProtocolResponse response = serverProtocol.handleInstruction();
				    	Logger.info("Server - instruction read " + response.getCommand().toString());  
				    	if (ProtocolResponse.Command.ASK_NEWCONNECTION == response.getCommand()) {
							int portNumber = bindNewPort();
							Logger.info("Server - Opening new connection - port " + portNumber);  
							@SuppressWarnings("resource")
							ServerSocket clientServerConnection = new ServerSocket(portNumber);
							serverProtocol.sendPortNumber(portNumber);
							Logger.info("Server - port number sent - port " + portNumber);  
							threadPool.execute(new MainClientConnection(this, clientServerConnection.accept(), 5, sharedRep, outRep));
						}
						if (ProtocolResponse.Command.ASK_ENDCONNECTION == response.getCommand()) {
							Logger.info("Ending server connection...");
							break;
						}
				    }
				}
				catch (ProtocolException e) {
					//TODO: everything is fatal for now, handle a more subtle return status
					e.printStackTrace();
				} catch (IOException e) {
					//TODO: everything is fatal for now, handle a more subtle return status
					e.printStackTrace();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();//TODO: everything is fatal for now, handle a more subtle return status
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
