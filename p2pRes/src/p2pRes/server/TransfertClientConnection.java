package p2pRes.server;

import java.net.Socket;

import p2pRes.log.Logger;
import p2pRes.model.FileHandlerException;
import p2pRes.model.TransferableFile;
import p2pRes.protocol.ProtocolException;
import p2pRes.protocol.ServerProtocol;
import p2pRes.protocol.response.AskForBlock;
import p2pRes.protocol.response.ProtocolResponse;

public class TransfertClientConnection extends ClientConnection {
	private TransferableFile transferableFile;
	
	public TransfertClientConnection(Server serverInstance, 
										Socket clientSocket,
										TransferableFile transferableFile) {
		super(serverInstance, clientSocket);
		this.transferableFile = transferableFile;
	}

	public void run() {
		try {
		    ServerProtocol serverProtocol = new ServerProtocol(this.getClientSocket());
		    
			while (true) {
				ProtocolResponse response = serverProtocol.handleInstruction();
				if (ProtocolResponse.Command.ASK_FOR_BLOCK == response.getCommand()) {
					try {
						serverProtocol.sendBlock(transferableFile.readBlock(((AskForBlock)response).getBlockNumber()));
					} catch (FileHandlerException e) {
						// TODO handle with error collector
						e.printStackTrace();
					}
				}
				if (ProtocolResponse.Command.ASK_ENDCONNECTION == response.getCommand()) {
					Logger.info("Ending server connection...");
					break;
				}
				//handle the unknown command case
			}
		}
		catch (ProtocolException e) {
			//TODO: everything is fatal for now, handle a more subtle return status
			e.printStackTrace();
		}
	}
}
