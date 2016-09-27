package p2pRes.net.io;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import p2pRes.model.Block;
import p2pRes.model.FileDescriptor;
import p2pRes.net.io.protocol.ClientProtocol;
import p2pRes.net.io.protocol.ProtocolException;

public class ClientChannel {
	private final String serverAdress;
	private final int serverPort;
	private ClientProtocol clientProtocol;
	
	public ClientChannel(String serverAdress, int serverPort) throws ChannelException {
		this.serverAdress = serverAdress;
		this.serverPort = serverPort;
		try {
			clientProtocol =  new ClientProtocol(new Socket(serverAdress, serverPort));
		} catch (UnknownHostException e) {
			throw new ChannelException("Can't find server " + serverAdress + ":" + serverPort, e);
		} catch (IOException e) {
			throw new ChannelException("Error opening channel " + serverAdress + ":" + serverPort, e);
		} catch (ProtocolException e) {
			throw new ChannelException("Client initialisation failed", e);
		}
	}
	
	public ClientChannel openSubChannel() throws ChannelException {
		int subPort;
		try {
			subPort = clientProtocol.askForNewConnection();
		} catch (ProtocolException e) {
			throw new ChannelException("Error opening sub channel " + serverAdress, e);
		}
		
		return new ClientChannel(serverAdress, subPort);
	}
	
	public FileDescriptor getFileDescriptor(String fileName) throws ChannelException {
		try {
			return clientProtocol.getFileDescriptor(fileName);
		} catch (ProtocolException e) {
			throw new ChannelException("Error getting file descriptor of " + fileName + " @" + serverAdress + ":" + serverPort, e);
		}
	}
	
	public Block getBlock(int blockNumber) throws ChannelException {
		try {
			return clientProtocol.askForBlock(blockNumber);
		} catch (ProtocolException e) {
			throw new ChannelException("Error getting block " + blockNumber + " @" + serverAdress + ":" + serverPort, e);
		}
	}
	
	public ClientChannel openTransferChannel(FileDescriptor fileDescriptor) throws ChannelException {
		try {
			int subPort = clientProtocol.askFileTransferConnection(fileDescriptor);
			return new ClientChannel(serverAdress, subPort);
		} catch (ProtocolException e) {
			throw new ChannelException("Error sending file descriptor " + fileDescriptor.getFileName() + " @" + serverAdress + ":" + serverPort, e);
		}
	}
	
	public void sendBlock(int blockNumber, Block block) throws ChannelException {
		try {
			clientProtocol.sendBlock(blockNumber, block);
		} catch (ProtocolException e) {
			throw new ChannelException("Error sending block " + blockNumber + " @" + serverAdress + ":" + serverPort, e);
		}
	}
	
	public void close() throws ChannelException {
		try {			
			clientProtocol.close();
		} catch (ProtocolException e) {
			throw new ChannelException("Error closing connection", e);
		}
	}
	
	protected int getPort() {
		return serverPort;
	}
}
