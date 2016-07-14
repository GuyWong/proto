package p2pRes.protocol;

import java.net.Socket;
import p2pRes.model.Block;
import p2pRes.model.FileDescriptor;

public class ClientProtocol extends Protocol {
	public ClientProtocol(Socket socket) throws ProtocolException {
		super(socket);
	}
	
	public FileDescriptor getFileDescriptor(String fileName) throws ProtocolException {
		this.sendByte(Protocol.ASK_FILE_DESCRIPTOR);
		this.sendBytes(fileName.getBytes());
		return (FileDescriptor)this.readObject();	
	}
	
	public int askForNewConnection() throws ProtocolException {
		this.sendByte(Protocol.ASK_NEW_CONNECTION);
		return this.readInt();
	}
	
	public void askEndConnection() throws ProtocolException {
		this.sendByte(Protocol.ASK_END_CONNECTION);
	}
	
	public Block askForBlock(int blockNumber) throws ProtocolException {
		this.sendByte(Protocol.ASK_BLOCK);
		this.sendInt(blockNumber);
		
		Block block = (Block)this.readObject();		
		if (!block.checkHash()) {
			throw new ProtocolException("Error checking hash of block " + blockNumber);
		}
		//Block block = new Block(this.readBytes());
		
		return block;	
	}
}
