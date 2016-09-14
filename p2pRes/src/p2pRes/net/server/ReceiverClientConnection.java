package p2pRes.net.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import p2pRes.io.FileWriter;
import p2pRes.io.WriterException;
import p2pRes.log.Logger;
import p2pRes.model.Block;
import p2pRes.model.BlocksDescriptor;
import p2pRes.net.io.ChannelException;
import p2pRes.net.io.ServerChannel;
import p2pRes.net.io.protocol.model.ProtocolResponse;
import p2pRes.net.io.protocol.model.PushBlock;
import p2pRes.net.processor.BlockProcessor;
import p2pRes.net.processor.BlockProcessorException;
import p2pRes.net.processor.BlockProcessorVisitor;

public class ReceiverClientConnection extends ClientConnection {
	private final BlocksDescriptor blockDescriptor;
	private final String destinationFilePath;
	private final int maxClientConnections;
	private BlockProcessor blockProcessor;
	private BlockProcessorVisitor bpv;
	private ExecutorService childConnectionsPool;
	private FileWriter writer;
	
	public ReceiverClientConnection(ServerChannel serverChannel, 
									BlocksDescriptor blockDescriptor,
									BlockProcessor blockProcessor,
									String destinationFilePath,
									int maxClientConnections) throws ServerException {
		super(serverChannel);
		this.blockDescriptor = blockDescriptor;
		this.blockProcessor = blockProcessor;
		this.bpv = new BlockProcessorVisitor() {
			public void process(int blockNumber) throws BlockProcessorException {/*Do nothing, only count processed blocks*/}
		};
		this.destinationFilePath = destinationFilePath;
		this.maxClientConnections = maxClientConnections;
		this.childConnectionsPool = Executors.newFixedThreadPool(maxClientConnections);
		try {
			this.writer = new FileWriter(destinationFilePath);
		} catch (WriterException e) {
			throw new ServerException("Can't open destination file " + destinationFilePath, e);
		}
	}

	public void run() {
		try {
			while (true) {
				ProtocolResponse response = this.getServerChannel().waitForClientCommand();
				if (ProtocolResponse.Command.PUSH_BLOCK == response.getCommand()) {
					this.writeBlock(((PushBlock)response).getBlockNumber(), ((PushBlock)response).getBlock());
					try {
						this.blockProcessor.processBlock(bpv);
					} catch (BlockProcessorException e) {
						throw new ServerException("Error processing block " + ((PushBlock)response).getBlockNumber(), e);
					}
				}
				if (ProtocolResponse.Command.ASK_NEWCONNECTION == response.getCommand()) {
					this.childConnectionsPool.execute(new ReceiverClientConnection(this.getServerChannel().openSubChannel(),
																					blockDescriptor,
																					blockProcessor,
																					destinationFilePath,
																					maxClientConnections));
				}
				if (ProtocolResponse.Command.ASK_ENDCONNECTION == response.getCommand()) {
					Logger.info("Ending server connection...");
					break;
				}
				//handle the unknown command case
			}
		} catch (ServerException e) {
			e.printStackTrace();//TODO: everything is fatal for now, handle a more subtle return status
		} catch (ChannelException e) {
			e.printStackTrace();//TODO: everything is fatal for now, handle a more subtle return status
		}
		finally {
			try {
				Logger.info("Closing ReceiverClientConnection... " + this.getServerChannel());
				this.getServerChannel().close();
			} catch (ChannelException e) {
				e.printStackTrace();// TODO Auto-generated catch block
			}
		}
	}
	
	private void writeBlock(int blockNumber, Block block)  throws ServerException {
		long fileOffset = blockDescriptor.getPosition(blockNumber);
		try {
			this.writer.write(fileOffset, block.getValue());
		} catch (WriterException e) {
			throw new ServerException("Writing of block at position " + fileOffset + " failed", e);
		}
	}
}
