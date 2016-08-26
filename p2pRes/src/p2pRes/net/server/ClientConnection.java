package p2pRes.net.server;

import java.net.Socket;

public abstract class ClientConnection implements Runnable {
	private Server serverInstance;
	private Socket clientSocket;
	
	public ClientConnection(Server serverInstance, 
							Socket clientSocket) {
		this.serverInstance = serverInstance;
		this.clientSocket = clientSocket;
	}
	
	protected Socket getClientSocket() {
		return clientSocket;
	}
	
	protected Server getServerInstance() {
		return serverInstance;
	}
}