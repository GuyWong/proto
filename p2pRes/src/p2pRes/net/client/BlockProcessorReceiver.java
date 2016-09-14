package p2pRes.net.client;

import p2pRes.io.FileWriter;
import p2pRes.io.WriterException;
import p2pRes.model.Block;
import p2pRes.model.BlocksDescriptor;
import p2pRes.net.io.ChannelException;
import p2pRes.net.io.ClientChannel;
import p2pRes.net.processor.BlockProcessorException;
import p2pRes.net.processor.BlockProcessorVisitor;

public class BlockProcessorReceiver implements BlockProcessorVisitor {
	private FileWriter writer;
	private ClientChannel clientChannel;
	private BlocksDescriptor blocksDescriptor;
	 
	public BlockProcessorReceiver(ClientChannel clientChannel, BlocksDescriptor blocksDescriptor, String outFilePath) throws BlockProcessorException {
		this.clientChannel = clientChannel;
		this.blocksDescriptor = blocksDescriptor;
		try {
			this.writer = new FileWriter(outFilePath);
		} catch (WriterException e) {
			throw new BlockProcessorException("Error opening " + outFilePath, e);
		}
	}

	public void process(int blockNumber) throws BlockProcessorException {
		Block block;
		try {
			block = this.getBlockFromPeer(blockNumber, clientChannel);
			if (block == null) {
				throw new BlockProcessorException("Invalid block received, block number: " + blockNumber);
			}
		} catch (ClientException e) {
			throw new BlockProcessorException("Can't receive block " + blockNumber + " from peer", e);
		}

		long fileOffset = blocksDescriptor.getPosition(blockNumber);
		try {
			this.writer.write(fileOffset, block.getValue());
		} catch (WriterException e) {
			throw new BlockProcessorException("Writing of block at position " + fileOffset + " failed", e);
		}
	}

	private Block getBlockFromPeer(int blockNumber, ClientChannel clientChannel) throws ClientException {
		try {
			return clientChannel.getBlock(blockNumber);
		} catch (ChannelException e) {
			throw new ClientException("Error getting block " + blockNumber + " from peer", e);
		}
	}
}
