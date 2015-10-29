/*package p2pRes.protocol;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import p2pRes.model.ReceivedFile;
import p2pRes.model.TransferableFile;

public class Protocold {
	private final static byte ASK_FILE_DESCRIPTOR = 0;
	private final static byte ASK_BLOCK = 1;
	
	private final static byte[] ACK = "ACK".getBytes();
	
	public static void getFile(Socket socket, String outRep) {
		try {      
	        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
	        String fileName = readInfo(in);        
	        long targetSize = Long.parseLong(readInfo(in));
	        
	        ReceivedFile receivedFile = new ReceivedFile(outRep+"//"+fileName, targetSize);	    

	        for (long blockNumber=0; blockNumber<receivedFile.getDescriptor().getBlockNumbers(); blockNumber++) {
	        	System.out.println(" writing " + fileName + " block " + blockNumber);
	        	readOneBlock(blockNumber, socket.getInputStream(), socket.getOutputStream(), receivedFile);
	        }
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static String readInfo(BufferedReader socketReader) throws IOException {
		String info;
		while ((info = socketReader.readLine()) != null) {
        	System.out.println("info: " + info);
        	if (info.length()>0) { break; }
        }
        return info;
	} 
	
	private static void readOneBlock(long blockNumber, 
										InputStream socketReader, 
										OutputStream socketSender, 
										ReceivedFile receivedFile) throws IOException { //need a context
		byte[] buffer = new byte[receivedFile.getDescriptor().getBlockSize(blockNumber)];
		socketReader.read(buffer, 0, buffer.length);
		receivedFile.writeBlock(blockNumber, buffer);
		String ack = new String(ACK) + blockNumber;
		socketSender.write(ack.getBytes());
	}
	
	public static void sendFile(Socket socket, String filePath) {
		
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			try {
				out.println((new File(filePath)).getName());
				
				TransferableFile transferableFile = new TransferableFile(filePath);
				out.println(transferableFile.getDescriptor().getFileSize());
				
				
				InputStream socketReader = socket.getInputStream();
				byte[] ackBuffer = new byte[ACK.length + 4];
				
				OutputStream socketSender = socket.getOutputStream();
				
				try {
					for (int blockNumber=0; blockNumber<transferableFile.getDescriptor().getBlockNumbers(); blockNumber++) {
						//socketSender.flush();
						byte[] stream = transferableFile.readBlock(blockNumber);
						socketSender.write(stream, 0, stream.length);
						
						//wait for acknowlegment
						socketReader.read(ackBuffer);
						System.out.println(" ackReceived for block " + blockNumber + " : " + new String(ackBuffer));
					}
				}
				finally {
					socketSender.close();
					socketReader.close();
				}
			}
			finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}*/
