package p2pRes.server;

import java.net.Socket;

public abstract class ClientConnection implements Runnable {
	//private ExecutorService childConnectionsPool;
	private Server serverInstance;
	private Socket clientSocket;
	//private String sharedRep;
	
	public ClientConnection(Server serverInstance, 
							Socket clientSocket//, 
							//int maxClientConnections, 
							/*String sharedRep*/) {
		//this.sharedRep = sharedRep;
		this.serverInstance = serverInstance;
		this.clientSocket = clientSocket;
		//this.childConnectionsPool = Executors.newFixedThreadPool(maxClientConnections);
	}
	
	protected Socket getClientSocket() {
		return clientSocket;
	}
	
	protected Server getServerInstance() {
		return serverInstance;
	}
	
	/*@SuppressWarnings("resource")
	@Override
	public void run() {
		Logger.info("ClientConnection - Running... ");  
		StatInfo clientConnectionStat = new StatInfo("clientConnectionStat");
		clientConnectionStat.start();
		try {
		    ServerProtocol serverProtocol = new ServerProtocol(clientSocket);
		    TransferableFile transferableFile = null;
		    
			while (true) {
				ProtocolResponse response = serverProtocol.handleInstruction();
				if (ProtocolResponse.Command.ASK_FOR_FILEDEFINITION == response.getCommand()) {
					String filePath = sharedRep + "//" + ((AskForFileDefinition)response).getFileName();
					try {
						transferableFile = new TransferableFile(filePath);
					} catch (IOException e) {
						throw new ServerException("Can't read file " + filePath, e);
					}
					serverProtocol.sendFileDescriptor(transferableFile.getDescriptor());
				}
				if (ProtocolResponse.Command.ASK_FOR_BLOCK == response.getCommand()) {
					serverProtocol.sendBlock(transferableFile.readBlock(((AskForBlock)response).getBlockNumber()));
				}
				if (ProtocolResponse.Command.ASK_NEWCONNECTION == response.getCommand()) {
					int portNumber = this.serverInstance.bindNewPort();
					ServerSocket server = new ServerSocket(portNumber);
					this.childConnectionsPool.execute(new ClientConnection(this.serverInstance, server.accept(), 0, sharedRep) );
					serverProtocol.sendPortNumber(portNumber);
				}
				if (ProtocolResponse.Command.ASK_ENDCONNECTION == response.getCommand()) {
					break;
				}
			}
		}
		catch (ProtocolException e) {
			//TODO: everything is fatal for now, handle a more subtle return status
			e.printStackTrace();
		}
		catch (ServerException e) {
			//TODO: everything is fatal for now, handle a more subtle return status
			e.printStackTrace();
		} catch (IOException e) {
			//TODO: everything is fatal for now, handle a more subtle return status
			e.printStackTrace();
		}
		
		clientConnectionStat.end();
		Logger.info("ClientConnection - Ending...");  
		Logger.info("Total time - " + clientConnectionStat.getStatTime() / 1000 + " sec");  
		
	}*/
}