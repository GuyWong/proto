package p2pRes.client;

import java.io.IOException;
import java.net.Socket;

import p2pRes.log.Logger;
import p2pRes.model.Block;
import p2pRes.model.FileHandler;
import p2pRes.protocol.ClientProtocol;
import p2pRes.protocol.ProtocolException;

public class PeerBlockFetcher implements Runnable  { //rename it, somethink like blockfetcherworker
	private Socket server;
	private ClientProtocol clientProtocol;
	private FileHandler fileHandler;
	
	public PeerBlockFetcher (String netAdress, 
								int port, 
								FileHandler fileHandler) throws ClientException {
		try {
			this.server = new Socket(netAdress, port);	
		} catch (IOException e) {// handle error if port is already bind
			throw new ClientException("Client initialisation failed", e);
		}
		
		this.clientProtocol = initClientProtocol(server);
		this.fileHandler = fileHandler;
	}
	
	@Override
	public void run() {
		
		try {
			Thread.sleep(1000000);
		} catch (InterruptedException e) {Logger.debug(e.getMessage());}
		
		while (!fileHandler.isComplete()) {
			int blockNumber = fileHandler.assignNewEmptyBlockNumber();
			try {
				Block block = this.getBlockFromPeer(blockNumber, clientProtocol);
				
				if (block == null) {
					throw new ClientException("Invalid block received, block number: " + blockNumber);
				}
				
				fileHandler.writeBlock(blockNumber, block.getValue());
			} catch (ClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private ClientProtocol initClientProtocol(Socket server) throws ClientException {
		try {
			return new ClientProtocol(server);
		} catch (ProtocolException e) {
			throw new ClientException("Client initialisation failed", e);
		}
	}
	
	private Block getBlockFromPeer(int blockNumber, ClientProtocol peer) throws ClientException {
		try {
			return peer.askForBlock(blockNumber);
		} catch (ProtocolException e) {
			throw new ClientException("Error getting block " + blockNumber + " from peer", e);
		}
	}
}
