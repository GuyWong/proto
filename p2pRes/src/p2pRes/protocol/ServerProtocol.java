package p2pRes.protocol;

import java.net.Socket;

import p2pRes.log.Logger;
import p2pRes.model.Block;
import p2pRes.model.FileDescriptor;
import p2pRes.protocol.response.AskForBlock;
import p2pRes.protocol.response.AskForEndConnection;
import p2pRes.protocol.response.AskForFileDefinition;
import p2pRes.protocol.response.ProtocolResponse;
import p2pRes.protocol.response.UnknownCommand;

public class ServerProtocol extends Protocol {	
	public ServerProtocol(Socket socket) throws ProtocolException {
		super(socket);
	}

	public ProtocolResponse handleInstruction() throws ProtocolException {
		byte clientCommand = this.readByte();
		Logger.debug("ServerProtocol - handleInstruction " + clientCommand);  
		if (Protocol.ASK_FILE_DESCRIPTOR == clientCommand) {
			Logger.debug("ServerProtocol - ASK_FILE_DESCRIPTOR");
			String fileName = new String(this.readBytes());

			return new AskForFileDefinition(fileName);
		}
		else if (Protocol.ASK_BLOCK == clientCommand) {
			int blockNumber = this.readInt();
			Logger.debug("ServerProtocol - ASK_BLOCK " + blockNumber);
			return new AskForBlock(blockNumber);
		}
		else if (Protocol.ASK_END_CONNECTION == clientCommand) {
			Logger.debug("ServerProtocol - ASK_END_CONNECTION");
			return new AskForEndConnection();
		}
		else {
			Logger.debug("Unknown command " + clientCommand);
			return new UnknownCommand();
		}			
	}
	
	public void sendFileDescriptor(FileDescriptor fileDescriptor) throws ProtocolException {
		this.sendObject(fileDescriptor);
	}
		
	public void sendBlock(byte[] bytes) throws ProtocolException {
		this.sendObject(new Block(bytes));
	}
}
