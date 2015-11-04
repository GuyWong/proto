package p2pRes.protocol;

import java.net.Socket;
import p2pRes.model.FileDescriptor;

public class ClientProtocol extends Protocol {
	public ClientProtocol(Socket socket) throws ProtocolException {
		super(socket);
	}
	
	public FileDescriptor getFileDescriptor(String fileName) throws ProtocolException {
		System.out.println("ClientProtocol - ASK_FILE_DESCRIPTOR");
		this.sendByte(Protocol.ASK_FILE_DESCRIPTOR);
		this.sendBytes(fileName.getBytes());
		return (FileDescriptor)this.readObject();	
	}
	
	public void askEndConnection() throws ProtocolException {
		System.out.println("ClientProtocol - ASK_END_CONNECTION");
		this.sendByte(Protocol.ASK_END_CONNECTION);
	}
	
	public byte[] askForBlock(long blockNumber) throws ProtocolException {
		System.out.println("ClientProtocol - ASK_BLOCK " + blockNumber);
		this.sendByte(Protocol.ASK_BLOCK);
		this.sendLong(blockNumber);
		return this.readBytes();	
	}
}
