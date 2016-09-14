package p2pRes.net.server;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import p2pRes.common.StaticsValues;
import p2pRes.log.Logger;
import p2pRes.model.FileDescriptor;
import p2pRes.model.FileException;
import p2pRes.model.TransferableFile;
import p2pRes.net.io.ChannelException;
import p2pRes.net.io.ServerChannel;
import p2pRes.net.io.protocol.model.AskForFileDefinition;
import p2pRes.net.io.protocol.model.AskOpenFilePushChannel;
import p2pRes.net.io.protocol.model.ProtocolResponse;
import p2pRes.net.processor.BlockProcessor;
import p2pRes.stats.StatInfo;
import p2pRes.utils.FileHashBuilder;
import p2pRes.utils.HashBuilderException;

public class MainClientConnection extends ClientConnection {
	private ExecutorService childConnectionsPool;
	private final int maxClientConnections;
	private final String sharedRep;
	private final String outRep;
	
	public MainClientConnection(ServerChannel serverChannel, 
								int maxClientConnections, 
								String sharedRep,
								String outRep) throws ServerException {
		super(serverChannel);
		this.sharedRep = sharedRep;
		this.outRep = outRep;
		this.maxClientConnections = maxClientConnections;
		this.childConnectionsPool = Executors.newFixedThreadPool(maxClientConnections);
	}
	
	
	/**
	 * Need to handle the case where the same client asking more than one file
	 */
	public void run() {
		Logger.info("MainClientConnection - Running... ");  
		StatInfo clientConnectionStat = new StatInfo("clientConnectionStat");
		clientConnectionStat.start();
		try {
		    TransferableFile transferableFile = null;
		    
			while (true) {
				ProtocolResponse response = this.getServerChannel().waitForClientCommand();
				if (ProtocolResponse.Command.ASK_FOR_FILEDEFINITION == response.getCommand()) {
					String filePath = sharedRep + "//" + ((AskForFileDefinition)response).getFileName();
					try {
						transferableFile = new TransferableFile(filePath, StaticsValues.BLOC_SIZE);
					} catch (FileException e) {
						throw new ServerException("Can't read file " + filePath, e);
					}
					this.getServerChannel().sendFileDescriptor(transferableFile.getDescriptor());
				}
				if (ProtocolResponse.Command.ASK_OPEN_FILEPUSH_CHANNEL == response.getCommand()) {
					final AskOpenFilePushChannel askOpenFilePushChannel = (AskOpenFilePushChannel)response;
					final BlockProcessor blockProcessor = new BlockProcessor(askOpenFilePushChannel.getFileDescriptor().getBlocksDescriptor());
					final String outFilePath = outRep + "//" + askOpenFilePushChannel.getFileDescriptor().getFileName();
					
					this.childConnectionsPool.execute(new ReceiverClientConnection(this.getServerChannel().openSubChannel(), 
																					askOpenFilePushChannel.getFileDescriptor().getBlocksDescriptor(),
																					blockProcessor,
																					outFilePath,
																					maxClientConnections)); //TODO: mutualize new connection opening
					Executors.newSingleThreadExecutor().execute(new Runnable() {			
						public void run() {
							monitorReceivedFile(blockProcessor, outFilePath, askOpenFilePushChannel.getFileDescriptor());
						}
					});
				}
				if (ProtocolResponse.Command.ASK_NEWCONNECTION == response.getCommand()) {
					this.childConnectionsPool.execute(new SenderClientConnection(this.getServerChannel().openSubChannel(), transferableFile));
				}
				if (ProtocolResponse.Command.ASK_ENDCONNECTION == response.getCommand()) {
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
				Logger.info("Closing MainClientConnection... " + this.getServerChannel());
				this.getServerChannel().close();
			} catch (ChannelException e) {
				e.printStackTrace();// TODO Auto-generated catch block
			}
		}
	}
	
	private void monitorReceivedFile(BlockProcessor blockProcessor, 
										String receivedFilePath, 
										FileDescriptor fileDescriptor) { //TODO: implement as a handler
		while (!blockProcessor.isComplete()) {
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {Logger.debug(e.getMessage());}
		}	
		
		try {
			if ( fileDescriptor.getFileHash().equals((new FileHashBuilder(receivedFilePath)).build())) {
				Logger.info("File hash check is fine !" + receivedFilePath); //TODO
				return;
			}
		} catch (HashBuilderException e) {
			e.printStackTrace();//TODO: handle properly exception
		}
		Logger.error("ERROR ! file hash not equals !!" + receivedFilePath); //TODO
	}
}
