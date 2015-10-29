package p2pRes.server;

import java.io.IOException;
import java.net.Socket;

import p2pRes.model.TransferableFile;
import p2pRes.protocol.ServerProtocol;
import p2pRes.protocol.response.AskForBlock;
import p2pRes.protocol.response.AskForFileDefinition;
import p2pRes.protocol.response.ProtocolResponse;

public class ClientConnection implements Runnable {
	private Socket client;
	private String sharedRep;
	
	public ClientConnection(Socket client, String sharedRep) {
		this.sharedRep = sharedRep;
		this.client = client;
	}
	
	@Override
	public void run() {
		System.out.println("ClientConnection - Running... ");             	
	    ServerProtocol serverProtocol = new ServerProtocol(client);
	    TransferableFile transferableFile = null;
	    
		while (true) {
			ProtocolResponse response = serverProtocol.handleInstruction();
			if (ProtocolResponse.Command.ASK_FOR_FILEDEFINITION == response.getCommand()) {
				try {
					transferableFile = new TransferableFile(sharedRep + "//" + ((AskForFileDefinition)response).getFileName());
					serverProtocol.sendFileDescriptor(transferableFile.getDescriptor());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ProtocolResponse.Command.ASK_FOR_BLOCK == response.getCommand()) {
				serverProtocol.sendBlock(transferableFile.readBlock(((AskForBlock)response).getBlockNumber()));
			}
			if (ProtocolResponse.Command.ASK_ENDCONNECTION == response.getCommand()) {
				break;
			}
		}
		
		System.out.println("ClientConnection - Ending...");  
	}
	
	//TODO getFile
}