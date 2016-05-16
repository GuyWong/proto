package p2pRes.server;

import java.io.IOException;
import java.net.Socket;

import p2pRes.log.Logger;
import p2pRes.model.TransferableFile;
import p2pRes.protocol.ProtocolException;
import p2pRes.protocol.ServerProtocol;
import p2pRes.protocol.response.AskForBlock;
import p2pRes.protocol.response.AskForFileDefinition;
import p2pRes.protocol.response.ProtocolResponse;
import p2pRes.stats.StatInfo;

public class ClientConnection implements Runnable {
	private Socket client;
	private String sharedRep;
	
	public ClientConnection(Socket client, String sharedRep) {
		this.sharedRep = sharedRep;
		this.client = client;
	}
	
	@Override
	public void run() {
		Logger.info("ClientConnection - Running... ");  
		StatInfo clientConnectionStat = new StatInfo("clientConnectionStat");
		clientConnectionStat.start();
		try {
		    ServerProtocol serverProtocol = new ServerProtocol(client);
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
		}
		
		clientConnectionStat.end();
		Logger.info("ClientConnection - Ending...");  
		Logger.info("Total time - " + clientConnectionStat.getStatTime() / 1000 + " sec");  
		
	}
}