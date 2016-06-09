package p2pRes.client;

import java.io.IOException;
import java.net.Socket;

import p2pRes.log.Logger;
import p2pRes.model.Block;
import p2pRes.model.FileDescriptor;
import p2pRes.model.ReceivedFile;
import p2pRes.protocol.ClientProtocol;
import p2pRes.protocol.ProtocolException;

public class Client {
	private Socket server;
	
	public Client (String netAdress, int port) throws ClientException {
		try {
			this.server = new Socket(netAdress, port);
		} catch (IOException e) {
			throw new ClientException("Client initialisation failed", e);
		}
	}
	
	public void getFile (String outRep, String fileName) throws ClientException {
		ClientProtocol clientProtocol = initClientProtocol(server);
		
		try {
			FileDescriptor fileDescriptor = getFileDescriptorFromPeer(fileName, clientProtocol);
			Logger.debug("Client - Get FD OK " + fileDescriptor.getBlockNumbers());
			 
	        ReceivedFile receivedFile = initReceivingFile(outRep+"//"+fileName, fileDescriptor);  
	        for (int blockNumber=0; blockNumber<receivedFile.getDescriptor().getBlockNumbers(); blockNumber++) {
	        	Logger.debug(" writing " + fileName + " block " + blockNumber);
	        	Block block = getBlockFromPeer(blockNumber, clientProtocol);
				receivedFile.writeBlock(blockNumber, block.getValue());
	        }
		}
		finally {
			try {
				clientProtocol.askEndConnection();
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private ClientProtocol initClientProtocol(Socket server) throws ClientException {
		try {
			return new ClientProtocol(server);
		} catch (ProtocolException e) {
			throw new ClientException("Client initialisation failed", e);
		}
	}
	
	private ReceivedFile initReceivingFile(String path, FileDescriptor fileDescriptor) throws ClientException {
		try {
			return new ReceivedFile(path, fileDescriptor);
		} catch (IOException e) {
			throw new ClientException("Error initializing ReceivingFile " + path, e);
		}
	}
	
	private FileDescriptor getFileDescriptorFromPeer(String fileName, ClientProtocol peer) throws ClientException {
		//TODO: handle notFoundResponse
		try {
			return peer.getFileDescriptor(fileName);
		} catch (ProtocolException e) {
			throw new ClientException("Error getting file descriptor for " + fileName + " from peer", e);
		}
	}
	
	private Block getBlockFromPeer(int blockNumber, ClientProtocol peer) throws ClientException {
		try {
			return peer.askForBlock(blockNumber);
		} catch (ProtocolException e) {
			throw new ClientException("Error getting block " + blockNumber + " from peer", e);
		}
	}
}
