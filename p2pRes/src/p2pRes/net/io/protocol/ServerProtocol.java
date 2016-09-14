package p2pRes.net.io.protocol;

import java.net.Socket;
import p2pRes.model.Block;
import p2pRes.model.FileDescriptor;
import p2pRes.net.io.protocol.model.AskForBlock;
import p2pRes.net.io.protocol.model.AskForEndConnection;
import p2pRes.net.io.protocol.model.AskForFileDefinition;
import p2pRes.net.io.protocol.model.AskForNewConnection;
import p2pRes.net.io.protocol.model.AskOpenFilePushChannel;
import p2pRes.net.io.protocol.model.ProtocolResponse;
import p2pRes.net.io.protocol.model.PushBlock;
import p2pRes.net.io.protocol.model.UnknownCommand;

public class ServerProtocol extends Protocol {	
	public ServerProtocol(Socket socket) throws ProtocolException {
		super(socket);
	}

	public ProtocolResponse handleInstruction() throws ProtocolException {
		byte clientCommand = this.readByte();
		if (Protocol.ASK_FILE_DESCRIPTOR == clientCommand) {
			return new AskForFileDefinition(new String(this.readBytes()));
		}
		if (Protocol.ASK_OPEN_FILEPUSH_CHANNEL == clientCommand) {
			return new AskOpenFilePushChannel((FileDescriptor)this.readObject());
		}
		else if (Protocol.ASK_BLOCK == clientCommand) {
			return new AskForBlock(this.readInt());
		}
		else if (Protocol.PUSH_BLOCK == clientCommand) {
			int blockNumber = this.readInt();
			Block block = (Block)this.readObject();
			if (!block.checkHash()) {
				throw new ProtocolException("Error checking hash of block " + blockNumber);
			}
			
			return new PushBlock(blockNumber, block);
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
	}
	
	public void sendPortNumber(int portNumber) throws ProtocolException {
		this.sendInt(portNumber);
	}
}
