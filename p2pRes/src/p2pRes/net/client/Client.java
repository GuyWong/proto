package p2pRes.net.client;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import p2pRes.common.StaticsValues;
import p2pRes.log.Logger;
import p2pRes.model.FileDescriptor;
import p2pRes.model.FileException;
import p2pRes.model.TransferableFile;
import p2pRes.net.processor.BlockProcessor;
import p2pRes.net.protocol.ClientProtocol;
import p2pRes.net.protocol.ProtocolException;
import p2pRes.utils.FileHashBuilder;
import p2pRes.utils.HashBuilderException;

public class Client {
	private String netAdress;
	private ClientProtocol serverConnection;
	
	public Client (String netAdress, int port) throws ClientException {
		this.netAdress = netAdress;

		try {
			ClientProtocol firstConnection = openNewChannel(netAdress, port);
			try {
				this.serverConnection = openNewChannel(netAdress, firstConnection.askForNewConnection());
			}
			finally {
				firstConnection.close();
			}
		} catch (ProtocolException e) {
			throw new ClientException("Failed to bind a new connection to " + netAdress + ":" + port, e);
		}
	}
	
	public void getFile (String outRep, String fileName) throws ClientException {
		FileDescriptor fileDescriptor = getFileDescriptorFromPeer(fileName, serverConnection);
		Logger.debug("Client - Get FD OK " + fileDescriptor.getBlocksDescriptor().getBlockNumbers());
		 
        BlockProcessor fileReceiver = new BlockProcessor(fileDescriptor.getBlocksDescriptor());
        ExecutorService blockFetcherPool = Executors.newFixedThreadPool(StaticsValues.MAX_CLIENT_CONNECTION);
        
        for (int i=0; i<StaticsValues.MAX_CLIENT_CONNECTION; i++) {
        	int port;
			try {
				port = serverConnection.askForNewConnection();
				Logger.debug("Client - new connection, port number: " + port);
			} catch (ProtocolException e) {
				throw new ClientException("Can't establish new connection", e);//Todo handle error, do not hardquit
			}
        	blockFetcherPool.execute(new PeerBlockFetcher(netAdress, port, outRep+"//"+fileName, fileDescriptor.getBlocksDescriptor(), fileReceiver));
        }
        
        while (!fileReceiver.isComplete()) { //TODO ugly !
        	try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {Logger.debug(e.getMessage());}
		}
        
        if (checkReceivedFile(outRep+"//"+fileName, fileDescriptor)) {
        	Logger.info("File hash check is fine !"); //TODO
        }
        else {
        	Logger.error("ERROR ! file hash not equals !!"); //TODO
        }
	}
	
	public void sendFile(String filePath) throws ClientException {
		TransferableFile transferableFile;
		try {
			transferableFile = new TransferableFile(filePath, StaticsValues.BLOC_SIZE);
		} catch (FileException e) {
			throw new ClientException("Error opening file to transfer " + filePath, e);
		}
			
		int portTransfertChannel;
		try {
			portTransfertChannel = serverConnection.sendFileDescriptor(transferableFile.getDescriptor());
		} catch (ProtocolException e) {
			throw new ClientException("Error sending file descriptor to peer", e);
		}	
		
		ClientProtocol transferChannel = openNewChannel(netAdress, portTransfertChannel);
			
		try {
			ExecutorService blockSenderPool = Executors.newFixedThreadPool(StaticsValues.MAX_CLIENT_CONNECTION);
			BlockProcessor fileSender = new BlockProcessor(transferableFile.getDescriptor().getBlocksDescriptor());
			
	        for (int i=0; i<StaticsValues.MAX_CLIENT_CONNECTION; i++) {
	        	int port;
				try {
					port = transferChannel.askForNewConnection();
					Logger.debug("Client - new connection, port number: " + port);
				} catch (ProtocolException e) {
					throw new ClientException("Can't establish new connection", e);//Todo handle error, do not hardquit
				}
				blockSenderPool.execute(new PeerBlockSender(netAdress, port, transferableFile, fileSender));
	        }
		}
		finally {
			try {			
				transferChannel.close();
			} catch (ProtocolException e) {
				throw new ClientException("Error closing connection", e);
			}
		}
	}
	
	public void close() throws ClientException {
		try {
			this.serverConnection.close();
		} catch (ProtocolException e) {
			throw new ClientException("Error closing connection", e);
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
	
	private boolean checkReceivedFile(String receivedFilePath, FileDescriptor fileDescriptor) { //TODO: proper impl
		try {
			return fileDescriptor.getFileHash().equals((new FileHashBuilder(receivedFilePath)).build());
		} catch (HashBuilderException e) {
			e.printStackTrace();//TODO: handle properly exception
			return false;
		}
	}
	
	private ClientProtocol openNewChannel(String netAdress, int portNumber) throws ClientException {
		try {
			return new ClientProtocol(new Socket(netAdress, portNumber));
		} catch (UnknownHostException e) {
			throw new ClientException("Can't find server " + netAdress + ":" + portNumber, e);
		} catch (IOException e) {
			throw new ClientException("Error opening channel " + netAdress + ":" + portNumber, e);
		} catch (ProtocolException e) {
			throw new ClientException("Client initialisation failed", e);
		}	
	}
}
