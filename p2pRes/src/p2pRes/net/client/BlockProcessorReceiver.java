package p2pRes.net.client;

import p2pRes.io.FileWriter;
import p2pRes.io.WriterException;
import p2pRes.model.Block;
import p2pRes.model.BlocksDescriptor;
import p2pRes.net.processor.BlockProcessorException;
import p2pRes.net.processor.BlockProcessorVisitor;
import p2pRes.net.protocol.ClientProtocol;
import p2pRes.net.protocol.ProtocolException;

public class BlockProcessorReceiver implements BlockProcessorVisitor {
	private FileWriter writer;
	private ClientProtocol peer;
	private BlocksDescriptor blocksDescriptor;
	 
	public BlockProcessorReceiver(ClientProtocol clientProtocol, BlocksDescriptor blocksDescriptor, String outFilePath) throws BlockProcessorException {
		this.peer = clientProtocol;
		this.blocksDescriptor = blocksDescriptor;
		try {
			this.writer = new FileWriter(outFilePath);
		} catch (WriterException e) {
			throw new BlockProcessorException("Error opening " + outFilePath, e);
		}
	}

	public void process(int blockNumber) throws BlockProcessorException {
		try {
			Block block = this.getBlockFromPeer(blockNumber, peer);
			if (block == null) {
				throw new ClientException("Invalid block received, block number: " + blockNumber);
			}
			
			long fileOffset = blocksDescriptor.getPosition(blockNumber);
			try {
				this.writer.write(fileOffset, block.getValue());
			} catch (WriterException e) {
				throw new BlockProcessorException("Writing of block at position " + fileOffset + " failed", e);
			}
		} catch (ClientException e) {
			e.printStackTrace();//TODO:handle exception
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
