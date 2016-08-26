package p2pRes.net.client;

import java.util.List;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import p2pRes.log.Logger;
import p2pRes.model.TransferableFile;
import p2pRes.net.processor.BlockProcessor;
import p2pRes.net.processor.BlockProcessorException;
import p2pRes.net.protocol.ClientProtocol;
import p2pRes.net.protocol.ProtocolException;

public class PeerBlockSender implements Runnable  {
	private Socket server;
	private ClientProtocol clientProtocol;
	private BlockProcessor blockProcessor;
	private BlockProcessorSender sender;
	private List<Exception> errorStack = new ArrayList<Exception>();
	
	public PeerBlockSender (String netAdress, 
								int port,
								TransferableFile transferableFile,
								BlockProcessor blockProcessor) throws ClientException {
		try {
			this.server = new Socket(netAdress, port);	
		} catch (IOException e) {// handle error if port is already bind
			throw new ClientException("Client initialisation failed", e);
		}
		
		this.clientProtocol = initClientProtocol(server);
		this.blockProcessor = blockProcessor;
		try {
			this.sender = new BlockProcessorSender(clientProtocol, transferableFile);
		} catch (BlockProcessorException e) {
			throw new ClientException("Error initializing receiver, can't open file " + transferableFile.getFilePath(), e);
		}
	}
	
	public void run() {		
		while (!blockProcessor.isComplete()) { //TODO: add and exit condition
			try {
				blockProcessor.processBlock(sender);
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
