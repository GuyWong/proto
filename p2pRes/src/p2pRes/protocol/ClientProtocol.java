package p2pRes.protocol;

import java.net.Socket;
import p2pRes.log.Logger;
import p2pRes.model.Block;
import p2pRes.model.FileDescriptor;

public class ClientProtocol extends Protocol {
	public ClientProtocol(Socket socket) throws ProtocolException {
		super(socket);
	}
	
	public FileDescriptor getFileDescriptor(String fileName) throws ProtocolException {
		Logger.debug("ClientProtocol - ASK_FILE_DESCRIPTOR");
		this.sendByte(Protocol.ASK_FILE_DESCRIPTOR);
		this.sendBytes(fileName.getBytes());
		return (FileDescriptor)this.readObject();	
	}
	
	public int askForNewConnection() throws ProtocolException {
		Logger.debug("ClientProtocol - ASK_NEW_CONNECTION");
		this.sendByte(Protocol.ASK_NEW_CONNECTION);
		int val = this.readInt();
		Logger.debug("ClientProtocol - askForNewConnection trash " + val );
		val = this.readInt();// fucking bug ! fix this
		Logger.debug("ClientProtocol - askForNewConnection " + val );
		return val;
		//return this.readInt();
	}
	
	public void askEndConnection() throws ProtocolException {
		Logger.debug("ClientProtocol - ASK_END_CONNECTION");
		this.sendByte(Protocol.ASK_END_CONNECTION);
	}
	
	public Block askForBlock(int blockNumber) throws ProtocolException {
		Logger.debug("ClientProtocol - ASK_BLOCK " + blockNumber);
		this.sendByte(Protocol.ASK_BLOCK);
		this.sendInt(blockNumber);
		
		Block block = (Block)this.readObject();		
		if (!block.checkHash()) {
			throw new ProtocolException("Error checking hash of block " + blockNumber);
		}
		
		return block;	
	}
}
