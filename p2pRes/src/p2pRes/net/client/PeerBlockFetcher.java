package p2pRes.net.client;

import java.util.List;
import java.util.ArrayList;
import p2pRes.log.Logger;
import p2pRes.model.BlocksDescriptor;
import p2pRes.net.io.ClientChannel;
import p2pRes.net.processor.BlockProcessor;
import p2pRes.net.processor.BlockProcessorException;

public class PeerBlockFetcher implements Runnable  {
	private BlockProcessor blockProcessor;
	private BlockProcessorReceiver receiver;
	private List<Exception> errorStack = new ArrayList<Exception>();
	
	public PeerBlockFetcher(ClientChannel clientChannel,
								String receivingFilePath, 
								BlocksDescriptor blocksDescriptor,
								BlockProcessor blockProcessor) throws ClientException {
		this.blockProcessor = blockProcessor;
		try {
			this.receiver = new BlockProcessorReceiver(clientChannel, blocksDescriptor, receivingFilePath);
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
}
