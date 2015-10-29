package p2pRes.protocol;

import java.net.Socket;
import p2pRes.model.FileDescriptor;
import p2pRes.protocol.response.AskForBlock;
import p2pRes.protocol.response.AskForEndConnection;
import p2pRes.protocol.response.AskForFileDefinition;
import p2pRes.protocol.response.ProtocolResponse;
import p2pRes.protocol.response.UnknownCommand;

public class ServerProtocol extends Protocol {	
	public ServerProtocol(Socket socket) {
		super(socket);
	}

	public ProtocolResponse handleInstruction() {
		byte clientCommand = this.readByte();
		System.out.println("ServerProtocol - handleInstruction " + clientCommand);  
		if (Protocol.ASK_FILE_DESCRIPTOR == clientCommand) {
			System.out.println("ServerProtocol - ASK_FILE_DESCRIPTOR");
			String fileName = new String(this.readBytes());

			return new AskForFileDefinition(fileName);
		}
		else if (Protocol.ASK_BLOCK == clientCommand) {
			long blockNumber = this.readLong();
			System.out.println("ServerProtocol - ASK_BLOCK " + blockNumber);
			return new AskForBlock(blockNumber);
		}
		else if (Protocol.ASK_END_CONNECTION == clientCommand) {
			System.out.println("ServerProtocol - ASK_END_CONNECTION");
			return new AskForEndConnection();
		}
		else {
			System.out.println("Unknown command " + clientCommand);
			return new UnknownCommand();
		}			
	}
	
	public void sendFileDescriptor(FileDescriptor fileDescriptor) {
		this.sendObject(fileDescriptor);
	}
		
	public void sendBlock(byte[] bytes) {
		this.sendBytes(bytes);
	}
}
