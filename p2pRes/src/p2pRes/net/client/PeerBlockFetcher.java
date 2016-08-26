package p2pRes.net.client;

import java.util.List;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import p2pRes.log.Logger;
import p2pRes.model.BlocksDescriptor;
import p2pRes.net.processor.BlockProcessor;
import p2pRes.net.processor.BlockProcessorException;
import p2pRes.net.protocol.ClientProtocol;
import p2pRes.net.protocol.ProtocolException;

public class PeerBlockFetcher implements Runnable  {
	private Socket server;
	private ClientProtocol clientProtocol;
	private BlockProcessor blockProcessor;
	private BlockProcessorReceiver receiver;
	private List<Exception> errorStack = new ArrayList<Exception>();
	
	public PeerBlockFetcher(String netAdress, 
								int port,
								String receivingFilePath, 
								BlocksDescriptor blocksDescriptor,
								BlockProcessor blockProcessor) throws ClientException {
		try {
			this.server = new Socket(netAdress, port);	
		} catch (IOException e) {// handle error if port is already bind
			throw new ClientException("Client initialisation failed", e);
		}
		
		this.clientProtocol = initClientProtocol(server);
		this.blockProcessor = blockProcessor;
		try {
			this.receiver = new BlockProcessorReceiver(clientProtocol, blocksDescriptor, receivingFilePath);
		} catch (BlockProcessorException e) {
			throw new ClientException("Error initializing receiver " + receivingFilePath, e);
		}
	}
	
	public void run() {		
		while (!blockProcessor.isComplete()) { //TODO: add and exit condition
			try {
				blockProcessor.processBlock(receiver);
				
			} catch (BlockProcessorException e) {
				errorStack.add(e);
				Logger.error("Error processing block " + e.getMessage());
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
}
