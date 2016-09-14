package p2pRes.net.client;

import p2pRes.model.Block;
import p2pRes.model.FileException;
import p2pRes.model.TransferableFile;
import p2pRes.net.io.ChannelException;
import p2pRes.net.io.ClientChannel;
import p2pRes.net.processor.BlockProcessorException;
import p2pRes.net.processor.BlockProcessorVisitor;

public class BlockProcessorSender implements BlockProcessorVisitor {
	private  ClientChannel clientChannel;
	private TransferableFile transferableFile;
	 
	public BlockProcessorSender( ClientChannel clientChannel, TransferableFile transferableFile) throws BlockProcessorException {
		this.clientChannel = clientChannel;
		this.transferableFile = transferableFile;
	}

	public void process(int blockNumber) throws BlockProcessorException {
		try {
			clientChannel.sendBlock(blockNumber, new Block(transferableFile.readBlock(blockNumber)));
		} catch (ChannelException e) {
			throw new BlockProcessorException("Sending block " + blockNumber + " failed", e);
		} catch (FileException e) {
			throw new BlockProcessorException("Sending block " + blockNumber + " failed", e);
		}
	}
}
