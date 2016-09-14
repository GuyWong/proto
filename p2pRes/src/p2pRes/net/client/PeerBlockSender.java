package p2pRes.net.client;

import java.util.List;
import java.util.ArrayList;
import p2pRes.log.Logger;
import p2pRes.model.TransferableFile;
import p2pRes.net.io.ClientChannel;
import p2pRes.net.processor.BlockProcessor;
import p2pRes.net.processor.BlockProcessorException;

public class PeerBlockSender implements Runnable  {
	private BlockProcessor blockProcessor;
	private BlockProcessorSender sender;
	private List<Exception> errorStack = new ArrayList<Exception>();
	
	public PeerBlockSender (ClientChannel clientChannel,
								TransferableFile transferableFile,
								BlockProcessor blockProcessor) throws ClientException {
		this.blockProcessor = blockProcessor;
		try {
			this.sender = new BlockProcessorSender(clientChannel, transferableFile);
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
}
