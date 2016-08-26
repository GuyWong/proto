package p2pRes.net.client;

import p2pRes.model.Block;
import p2pRes.model.FileException;
import p2pRes.model.TransferableFile;
import p2pRes.net.processor.BlockProcessorException;
import p2pRes.net.processor.BlockProcessorVisitor;
import p2pRes.net.protocol.ClientProtocol;
import p2pRes.net.protocol.ProtocolException;

public class BlockProcessorSender implements BlockProcessorVisitor {
	private ClientProtocol peer;
	private TransferableFile transferableFile;
	 
	public BlockProcessorSender(ClientProtocol clientProtocol, TransferableFile transferableFile) throws BlockProcessorException {
		this.peer = clientProtocol;
		this.transferableFile = transferableFile;
	}

	public void process(int blockNumber) throws BlockProcessorException {
		try {
			peer.sendBlock(blockNumber, new Block(transferableFile.readBlock(blockNumber)));
		} catch (ProtocolException e) {
			throw new BlockProcessorException("Sending block " + blockNumber + " failed", e);
		} catch (FileException e) {
			throw new BlockProcessorException("Sending block " + blockNumber + " failed", e);
		}
	}
}
