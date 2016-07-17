package p2pRes.client;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import p2pRes.log.Logger;
import p2pRes.model.FileDescriptor;
import p2pRes.model.FileHandler;
import p2pRes.model.FileHandlerException;
import p2pRes.protocol.ClientProtocol;
import p2pRes.protocol.ProtocolException;
import p2pRes.utils.FileHashBuilder;
import p2pRes.utils.HashBuilderException;

public class Client {
	private final static int MAX_CLIENT_CONNECTION = 5;//to be parametrized
	String netAdress;
	private Socket server;
	private ExecutorService blockFetcherPool;
	
	public Client (String netAdress, int port) throws ClientException {
		this.netAdress = netAdress;
		this.blockFetcherPool = Executors.newFixedThreadPool(MAX_CLIENT_CONNECTION);
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
			Logger.debug("Client - Get FD OK " + fileDescriptor.getBlocksDescriptor().getBlockNumbers());
			 
	        FileHandler receivedFile = initReceivingFile(outRep+"//"+fileName, fileDescriptor); 
	        
	        for (int i=0; i<MAX_CLIENT_CONNECTION; i++) {
	        	int port;
				try {
					port = clientProtocol.askForNewConnection();
					Logger.debug("Client - new connection, port number: " + port);
				} catch (ProtocolException e) {
					throw new ClientException("Can't establish new connection", e);//Todo handle error, do not hardquit
				}
	        	blockFetcherPool.execute(new PeerBlockFetcher(netAdress, port, receivedFile));
	        }
	        
	        while (!receivedFile.isComplete()) { //TODO ugly !
	        	try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {Logger.debug(e.getMessage());}
			}
	        
	        if (checkReceivedFile(outRep+"//"+fileName, fileDescriptor)) {
	        	Logger.error("ERROR ! file hash not equal !!"); //TODO
	        }
	        else {
	        	Logger.info("File hash check is fine !"); //TODO
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
	
	private boolean checkReceivedFile(String receivedFilePath, FileDescriptor fileDescriptor) { //TODO: proper impl
		try {
			return fileDescriptor.getFileHash().equals((new FileHashBuilder(receivedFilePath)).build());
		} catch (HashBuilderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}
	
	private ClientProtocol initClientProtocol(Socket server) throws ClientException {
		try {
			return new ClientProtocol(server);
		} catch (ProtocolException e) {
			throw new ClientException("Client initialisation failed", e);
		}
	}
	
	private FileHandler initReceivingFile(String path, FileDescriptor fileDescriptor) throws ClientException {
		try {
			return new FileHandler(path, fileDescriptor);
		} catch (FileHandlerException e) {
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
}
