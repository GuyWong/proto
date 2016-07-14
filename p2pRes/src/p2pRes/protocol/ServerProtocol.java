package p2pRes.protocol;

import java.net.Socket;
import p2pRes.model.Block;
import p2pRes.model.FileDescriptor;
import p2pRes.protocol.response.AskForBlock;
import p2pRes.protocol.response.AskForEndConnection;
import p2pRes.protocol.response.AskForFileDefinition;
import p2pRes.protocol.response.AskForNewConnection;
import p2pRes.protocol.response.ProtocolResponse;
import p2pRes.protocol.response.UnknownCommand;

public class ServerProtocol extends Protocol {	
	public ServerProtocol(Socket socket) throws ProtocolException {
		super(socket);
	}

	public ProtocolResponse handleInstruction() throws ProtocolException {
		byte clientCommand = this.readByte();
		if (Protocol.ASK_FILE_DESCRIPTOR == clientCommand) {
			return new AskForFileDefinition(new String(this.readBytes()));
		}
		else if (Protocol.ASK_BLOCK == clientCommand) {
			return new AskForBlock(this.readInt());
		}
		else if (Protocol.ASK_NEW_CONNECTION == clientCommand) {
			return new AskForNewConnection();
		}
		else if (Protocol.ASK_END_CONNECTION == clientCommand) {
			return new AskForEndConnection();
		}
		else {
			return new UnknownCommand();
		}			
	}
	
	public void sendFileDescriptor(FileDescriptor fileDescriptor) throws ProtocolException {
		this.sendObject(fileDescriptor);
	}
		
	public void sendBlock(byte[] bytes) throws ProtocolException {
		this.sendObject(new Block(bytes));
		//this.sendBytes(bytes);
	}
	
	public void sendPortNumber(int portNumber) throws ProtocolException {
		this.sendInt(portNumber);
	}
}
