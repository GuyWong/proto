package p2pRes.net.io.protocol;

import java.io.IOException;
import java.net.Socket;
import p2pRes.model.Block;
import p2pRes.model.FileDescriptor;

public class ClientProtocol extends Protocol { //Modelize socket opening and clozing on a dedicated channel object
	public ClientProtocol(Socket socket) throws ProtocolException {
		super(socket);
	}
	
	public FileDescriptor getFileDescriptor(String fileName) throws ProtocolException {
		this.sendByte(Protocol.ASK_FILE_DESCRIPTOR);
		this.sendBytes(fileName.getBytes());
		return (FileDescriptor)this.readObject();	
	}
	
	public int askFileTransferConnection(FileDescriptor fileDescriptor) throws ProtocolException {
		this.sendByte(Protocol.ASK_OPEN_FILEPUSH_CHANNEL);
		this.sendObject(fileDescriptor);
		return this.readInt();
	}
	
	public int askForNewConnection() throws ProtocolException {
		this.sendByte(Protocol.ASK_NEW_CONNECTION);
		return this.readInt();
	}
	
	public Block askForBlock(int blockNumber) throws ProtocolException {
		this.sendByte(Protocol.ASK_BLOCK);
		this.sendInt(blockNumber);
		
		Block block = (Block)this.readObject();		
		if (!block.checkHash()) {
			throw new ProtocolException("Error checking hash of block " + blockNumber);
		}
		
		return block;	
	}
	
	public void sendBlock(int blockNumber, Block block) throws ProtocolException {
		this.sendByte(Protocol.PUSH_BLOCK);
		this.sendInt(blockNumber);
		this.sendObject(block);
	}
	
	public void close() throws ProtocolException {
		try {
			try {
				this.sendByte(Protocol.ASK_END_CONNECTION);
			} catch (ProtocolException e) {
				throw new ProtocolException("Error closing remote connection");
			}
		}
		finally {
			try {
				this.getSocket().close();
			} catch (IOException e) {
				throw new ProtocolException("Error closing connection");
			}
		}
	}
}
