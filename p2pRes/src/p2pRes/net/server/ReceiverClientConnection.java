package p2pRes.net.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import p2pRes.io.FileWriter;
import p2pRes.io.WriterException;
import p2pRes.log.Logger;
import p2pRes.model.Block;
import p2pRes.model.BlocksDescriptor;
import p2pRes.net.protocol.ProtocolException;
import p2pRes.net.protocol.ServerProtocol;
import p2pRes.net.protocol.response.ProtocolResponse;
import p2pRes.net.protocol.response.PushBlock;

public class ReceiverClientConnection extends ClientConnection {
	private final BlocksDescriptor blockDescriptor;
	private final String destinationFilePath;
	private final int maxClientConnections;
	private ExecutorService childConnectionsPool;
	private FileWriter writer;
	
	public ReceiverClientConnection(Server serverInstance, 
									Socket clientSocket, 
									BlocksDescriptor blockDescriptor,
									String destinationFilePath,
									int maxClientConnections) throws ServerException {
		super(serverInstance, clientSocket);
		this.blockDescriptor = blockDescriptor;
		this.destinationFilePath = destinationFilePath;
		this.maxClientConnections = maxClientConnections;
		this.childConnectionsPool = Executors.newFixedThreadPool(maxClientConnections);
		try {
			this.writer = new FileWriter(destinationFilePath);
		} catch (WriterException e) {
			throw new ServerException("Can't open destination file " + destinationFilePath, e);
		}
	}

	@SuppressWarnings("resource")
	public void run() {
		try {
		    ServerProtocol serverProtocol = new ServerProtocol(this.getClientSocket());
		    
			while (true) {
				ProtocolResponse response = serverProtocol.handleInstruction();
				if (ProtocolResponse.Command.PUSH_BLOCK == response.getCommand()) {
					this.writeBlock(((PushBlock)response).getBlockNumber(), ((PushBlock)response).getBlock());
				}
				if (ProtocolResponse.Command.ASK_NEWCONNECTION == response.getCommand()) {
					int portNumber = this.getServerInstance().bindNewPort();
					Logger.info("MainClientConnection - Opening new connection - port " + portNumber);  
					ServerSocket server = new ServerSocket(portNumber); //FIXME: finally close this object (handle with an encapsulating SrvChannelObject)
					serverProtocol.sendPortNumber(portNumber);
					Logger.info("MainClientConnection - port number sent - port " + portNumber);  
					
					ReceiverClientConnection connection = new ReceiverClientConnection(this.getServerInstance(), 
																						server.accept(),
																						blockDescriptor,
																						destinationFilePath,
																						maxClientConnections);
					this.childConnectionsPool.execute(connection);
					/*this.childConnectionsPool.execute(new ReceiverClientConnection(this.getServerInstance(), 
																					server.accept(),
																					blockDescriptor,
																					destinationFilePath));*/
				}
				if (ProtocolResponse.Command.ASK_ENDCONNECTION == response.getCommand()) {
					Logger.info("Ending server connection...");
					break;
				}
				//handle the unknown command case
			}
		}
		catch (ProtocolException e) {
			e.printStackTrace();//TODO: everything is fatal for now, handle a more subtle return status
		} catch (IOException e) {
			e.printStackTrace();//TODO: everything is fatal for now, handle a more subtle return status
		} catch (ServerException e) {
			e.printStackTrace();//TODO: everything is fatal for now, handle a more subtle return status
		}
	}
	
	private synchronized void writeBlock(int blockNumber, Block block)  throws ServerException {
		long fileOffset = blockDescriptor.getPosition(blockNumber);
		try {
			this.writer.write(fileOffset, block.getValue());
		} catch (WriterException e) {
			throw new ServerException("Writing of block at position " + fileOffset + " failed", e);
		}
	}
}
