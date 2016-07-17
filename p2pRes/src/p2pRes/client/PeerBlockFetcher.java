package p2pRes.client;

import java.util.List;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import p2pRes.log.Logger;
import p2pRes.model.Block;
import p2pRes.model.FileHandler;
import p2pRes.model.FileHandlerException;
import p2pRes.protocol.ClientProtocol;
import p2pRes.protocol.ProtocolException;

public class PeerBlockFetcher implements Runnable  {
	private Socket server;
	private ClientProtocol clientProtocol;
	private FileHandler fileHandler;
	private List<Exception> errorStack = new ArrayList<Exception>();
	
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
	
	public void run() {		
		while (!fileHandler.isComplete()) {
			int blockNumber = fileHandler.assignNewEmptyBlockNumber();
			if (blockNumber == FileHandler.NO_MORE_BLOCK_AVAILABLE) { break; }
			try {
				Block block = this.getBlockFromPeer(blockNumber, clientProtocol);
				if (block == null) {
					throw new ClientException("Invalid block received, block number: " + blockNumber);
				}
				
				try {
					fileHandler.writeBlock(blockNumber, block.getValue());
					
				} catch (FileHandlerException e) {
					fileHandler.unassignBlockNumber(blockNumber);
					throw new ClientException("Writing of block " + blockNumber + " failed", e);
				}
				Logger.debug("PeerBlockFetcher - " 
								+ server.getLocalAddress() + ":" + server.getPort() 
								+ " block " + blockNumber + " size "  + block.getValue().length 
								+ " writed...");
			} catch (ClientException e) {
				//TODO: handle fatal errors (max peer retry etc...)
				errorStack.add(e);
				Logger.debug("PeerBlockFetcher error!! " + e.getMessage());
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
