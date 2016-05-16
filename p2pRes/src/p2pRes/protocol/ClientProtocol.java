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
	
	public void askEndConnection() throws ProtocolException {
		Logger.debug("ClientProtocol - ASK_END_CONNECTION");
		this.sendByte(Protocol.ASK_END_CONNECTION);
	}
	
	public Block askForBlock(long blockNumber) throws ProtocolException {
		Logger.debug("ClientProtocol - ASK_BLOCK " + blockNumber);
		this.sendByte(Protocol.ASK_BLOCK);
		this.sendLong(blockNumber);
		
		Block block = (Block)this.readObject();		
		if (!block.checkHash()) {
			throw new ProtocolException("Error checking hash of block " + blockNumber);
		}
		
		return block;	
	}
}
