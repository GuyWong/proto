package p2pRes.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import p2pRes.log.Logger;
import p2pRes.model.FileHandlerException;
import p2pRes.model.TransferableFile;
import p2pRes.protocol.ProtocolException;
import p2pRes.protocol.ServerProtocol;
import p2pRes.protocol.response.AskForFileDefinition;
import p2pRes.protocol.response.ProtocolResponse;
import p2pRes.stats.StatInfo;

public class MainClientConnection extends ClientConnection {
	private static final int BLOC_SIZE = 1024*100;//To be parametized //100ko seems the best
												  //100ko = 214748to max file size
												  //1ko = 2147to max file size
	
	private ExecutorService childConnectionsPool;
	private String sharedRep;
	
	public MainClientConnection(Server serverInstance, 
								Socket clientSocket, 
								int maxClientConnections, 
								String sharedRep) {
		super(serverInstance, clientSocket);
		this.sharedRep = sharedRep;
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
						transferableFile = new TransferableFile(filePath, BLOC_SIZE);
					} catch (FileHandlerException e) {
						throw new ServerException("Can't read file " + filePath, e);
					}
					serverProtocol.sendFileDescriptor(transferableFile.getDescriptor());
				}
				if (ProtocolResponse.Command.ASK_NEWCONNECTION == response.getCommand()) {
					int portNumber = this.getServerInstance().bindNewPort();
					Logger.info("MainClientConnection - Opening new connection - port " + portNumber);  
					ServerSocket server = new ServerSocket(portNumber);
					serverProtocol.sendPortNumber(portNumber);
					Logger.info("MainClientConnection - port number sent - port " + portNumber);  
					this.childConnectionsPool.execute(new TransfertClientConnection(this.getServerInstance(), server.accept(), transferableFile));
				}
				if (ProtocolResponse.Command.ASK_ENDCONNECTION == response.getCommand()) {
					break;
				}
				//handle the unknown command case
			}
		}
		catch (ProtocolException e) {
			//TODO: everything is fatal for now, handle a more subtle return status
			e.printStackTrace();
		} catch (ServerException e) {
			//TODO: everything is fatal for now, handle a more subtle return status
			e.printStackTrace();
		} catch (IOException e) {
			//TODO: everything is fatal for now, handle a more subtle return status
			e.printStackTrace();
		}
	}
}
