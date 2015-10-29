package p2pRes.client;

import java.io.IOException;
import java.net.Socket;
import p2pRes.model.FileDescriptor;
import p2pRes.model.ReceivedFile;
import p2pRes.protocol.ClientProtocol;

public class Client {
	private Socket server;
	
	public Client (String netAdress, int port) {
		try {
			this.server = new Socket(netAdress, port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void getFile (String outRep, String fileName) {
		ClientProtocol clientProtocol = new ClientProtocol(server);
		try {
			FileDescriptor fileDescriptor = clientProtocol.getFileDescriptor(fileName);
			
			System.out.println("Client - Get FD OK " + fileDescriptor.getBlockNumbers());
			
			try {      
		        ReceivedFile receivedFile = new ReceivedFile(outRep+"//"+fileName, fileDescriptor);	    
	
		        for (long blockNumber=0; blockNumber<receivedFile.getDescriptor().getBlockNumbers(); blockNumber++) {
		        	System.out.println(" writing " + fileName + " block " + blockNumber);
		        	//readOneBlock(blockNumber, socket.getInputStream(), socket.getOutputStream(), receivedFile);
		        	byte[] block = clientProtocol.askForBlock(blockNumber);
					receivedFile.writeBlock(blockNumber, block);
		        }
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		finally {
			clientProtocol.askEndConnection();
		}
	}
}
