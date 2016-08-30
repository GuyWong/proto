package p2pRes.net.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import p2pRes.common.StaticsValues;
import p2pRes.log.Logger;
import p2pRes.model.FileDescriptor;
import p2pRes.model.FileException;
import p2pRes.model.TransferableFile;
import p2pRes.net.processor.BlockProcessor;
import p2pRes.net.protocol.ProtocolException;
import p2pRes.net.protocol.ServerProtocol;
import p2pRes.net.protocol.response.AskForFileDefinition;
import p2pRes.net.protocol.response.ProtocolResponse;
import p2pRes.net.protocol.response.ReceiveFileDefinition;
import p2pRes.stats.StatInfo;
import p2pRes.utils.FileHashBuilder;
import p2pRes.utils.HashBuilderException;

public class MainClientConnection extends ClientConnection {
	private ExecutorService childConnectionsPool;
	private final int maxClientConnections;
	private final String sharedRep;
	private final String outRep;
	
	public MainClientConnection(Server serverInstance, 
								Socket clientSocket, 
								int maxClientConnections, 
								String sharedRep,
								String outRep) {
		super(serverInstance, clientSocket);
		this.sharedRep = sharedRep;
		this.outRep = outRep;
		this.maxClientConnections = maxClientConnections;
		this.childConnectionsPool = Executors.newFixedThreadPool(maxClientConnections);
	}
	
	
	/**
	 * Need to handle the case where the same client asking more than one file
	 */
	@SuppressWarnings("resource")
	public void run() {
		Logger.info("MainClientConnection - Running... ");  
		StatInfo clientConnectionStat = new StatInfo("clientConnectionStat");
		clientConnectionStat.start();
		try {
		    ServerProtocol serverProtocol = new ServerProtocol(this.getClientSocket());
		    TransferableFile transferableFile = null;
		    
			while (true) {
				ProtocolResponse response = serverProtocol.handleInstruction();
				if (ProtocolResponse.Command.ASK_FOR_FILEDEFINITION == response.getCommand()) {
					String filePath = sharedRep + "//" + ((AskForFileDefinition)response).getFileName();
					try {
						transferableFile = new TransferableFile(filePath, StaticsValues.BLOC_SIZE);
					} catch (FileException e) {
						throw new ServerException("Can't read file " + filePath, e);
					}
					serverProtocol.sendFileDescriptor(transferableFile.getDescriptor());
				}
				if (ProtocolResponse.Command.PUSH_FILE_DESCRIPTOR == response.getCommand()) {
					int portNumber = this.getServerInstance().bindNewPort();
					Logger.info("MainClientConnection - Opening new connection - port " + portNumber);  
					ServerSocket server = new ServerSocket(portNumber);
					serverProtocol.sendPortNumber(portNumber);
					Logger.info("MainClientConnection - port number sent - port " + portNumber);  
					
					final FileDescriptor fileDescriptor = ((ReceiveFileDefinition)response).getFileDescriptor();
					final BlockProcessor blockProcessor = new BlockProcessor(fileDescriptor.getBlocksDescriptor());
					final String outFilePath = outRep + "//" + fileDescriptor.getFileName();
					this.childConnectionsPool.execute(new ReceiverClientConnection(this.getServerInstance(), 
																					server.accept(), 
																					fileDescriptor.getBlocksDescriptor(),
																					blockProcessor,
																					outFilePath,
																					maxClientConnections)); //TODO: mutualize new connection opening
					Executors.newSingleThreadExecutor().execute(new Runnable() {			
						public void run() {
							monitorReceivedFile(blockProcessor, outFilePath, fileDescriptor);
						}
					});
				}
				if (ProtocolResponse.Command.ASK_NEWCONNECTION == response.getCommand()) {
					int portNumber = this.getServerInstance().bindNewPort();
					Logger.info("MainClientConnection - Opening new connection - port " + portNumber);  
					ServerSocket server = new ServerSocket(portNumber);
					serverProtocol.sendPortNumber(portNumber);
					Logger.info("MainClientConnection - port number sent - port " + portNumber);  
					this.childConnectionsPool.execute(new SenderClientConnection(this.getServerInstance(), server.accept(), transferableFile));
				}
				if (ProtocolResponse.Command.ASK_ENDCONNECTION == response.getCommand()) {
					break;
				}
				//handle the unknown command case
			}
		}
		catch (ProtocolException e) {
			e.printStackTrace();//TODO: everything is fatal for now, handle a more subtle return status
		} catch (ServerException e) {
			e.printStackTrace();//TODO: everything is fatal for now, handle a more subtle return status
		} catch (IOException e) {
			e.printStackTrace();//TODO: everything is fatal for now, handle a more subtle return status
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
