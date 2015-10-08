package p2pRes.server;

import java.net.Socket;
import p2pRes.protocol.Protocol;

public class ClientConnection implements Runnable {
	private Socket client;
	private String outRep;
	
	public ClientConnection(Socket client, String outRep) {
		this.outRep = outRep;
		this.client = client;
	}
	
	@Override
	public void run() {
		System.out.println("Running client... " + this.toString());             
        
		Protocol.getFile(client, outRep);
	}
}