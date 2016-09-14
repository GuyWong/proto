package p2pRes.net.client;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import p2pRes.common.StaticsValues;
import p2pRes.log.Logger;
import p2pRes.model.FileDescriptor;
import p2pRes.model.FileException;
import p2pRes.model.TransferableFile;
import p2pRes.net.io.ChannelException;
import p2pRes.net.io.ClientChannel;
import p2pRes.net.processor.BlockProcessor;
import p2pRes.utils.FileHashBuilder;
import p2pRes.utils.HashBuilderException;

public class Client {
	private ClientChannel clientChannel;
	
	public Client (String netAdress, int port) throws ClientException {
		try {
			ClientChannel firstConnection = new ClientChannel(netAdress, port);
			try {
				this.clientChannel = firstConnection.openSubChannel();
			}
			finally {
				firstConnection.close();
			}
		} catch (ChannelException e) {
			throw new ClientException("Failed to bind a new connection to " + netAdress + ":" + port, e);
		}
	}
	
	public void getFile (String outRep, String fileName) throws ClientException {
		FileDescriptor fileDescriptor = getFileDescriptorFromPeer(fileName, clientChannel);
		Logger.debug("Client - Get FD OK " + fileDescriptor.getBlocksDescriptor().getBlockNumbers());
		 
        BlockProcessor fileReceiver = new BlockProcessor(fileDescriptor.getBlocksDescriptor());
        ExecutorService blockFetcherPool = Executors.newFixedThreadPool(StaticsValues.MAX_CLIENT_CONNECTION);
        
        for (int i=0; i<StaticsValues.MAX_CLIENT_CONNECTION; i++) {
        	try {
				blockFetcherPool.execute(new PeerBlockFetcher(clientChannel.openSubChannel(), outRep+"//"+fileName, fileDescriptor.getBlocksDescriptor(), fileReceiver));
			} catch (ChannelException e) {
				Logger.error("ERROR opening new channel " + e.getMessage()); //TODO
				i--;
			}
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
			
		ClientChannel transferChannel;
		try {
			transferChannel = this.clientChannel.openTransferChannel(transferableFile.getDescriptor());
		} catch (ChannelException e) {
			throw new ClientException("Error sending file descriptor to peer", e);
		}	
			
		try {
			ExecutorService blockSenderPool = Executors.newFixedThreadPool(StaticsValues.MAX_CLIENT_CONNECTION);
			BlockProcessor fileSender = new BlockProcessor(transferableFile.getDescriptor().getBlocksDescriptor());
			
	        for (int i=0; i<StaticsValues.MAX_CLIENT_CONNECTION; i++) {
				try {
					blockSenderPool.execute(new PeerBlockSender(transferChannel.openSubChannel(), transferableFile, fileSender));
				} catch (ChannelException e) {
					Logger.error("ERROR opening new channel " + e.getMessage()); //TODO
					i--;
				}
	        }
		}
		finally {
			try {			
				transferChannel.close();
			} catch (ChannelException e) {
				throw new ClientException("Error closing connection", e);
			}
		}
	}
	
	public void close() throws ClientException {
		try {
			this.clientChannel.close();
		} catch (ChannelException e) {
			throw new ClientException("Error closing connection", e);
		}
	}
	
	private FileDescriptor getFileDescriptorFromPeer(String fileName, ClientChannel clientChannel) throws ClientException {
		//TODO: handle notFoundResponse
		try {
			return clientChannel.getFileDescriptor(fileName);
		} catch (ChannelException e) {
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
}
