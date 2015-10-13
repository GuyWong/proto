package p2pRes.protocol;

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

public class Protocol {
	public static void getFile(Socket socket, String outRep) {
		try {      
	        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
	        String fileName = readInfo(in);        
	        long targetSize = Long.parseLong(readInfo(in));
	        
	        ReceivedFile receivedFile = new ReceivedFile(outRep+"//"+fileName, targetSize);	    

	        for (long blockNumber=0; blockNumber<receivedFile.getDescriptor().getBlockNumbers(); blockNumber++) {
	        	System.out.println(" writing " + fileName + " block " + blockNumber);
	        	readOneBlock(blockNumber, socket.getInputStream(), receivedFile);
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
	
	private static void readOneBlock(long blockNumber, InputStream socketReader, ReceivedFile receivedFile) throws IOException { //need a context
		byte[] buffer = new byte[receivedFile.getDescriptor().getBlockSize(blockNumber)];
		socketReader.read(buffer, 0, buffer.length);
		receivedFile.writeBlock(blockNumber, buffer);
	}
	
	public static void sendFile(Socket socket, String filePath) {
		
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			try {
				out.println((new File(filePath)).getName());
				
				TransferableFile transferableFile = new TransferableFile(filePath);
				out.println(transferableFile.getDescriptor().getFileSize());
				
				OutputStream os = socket.getOutputStream();
				try {
					for (int blockNumber=0; blockNumber<transferableFile.getDescriptor().getBlockNumbers(); blockNumber++) {
						os.flush();
						byte[] stream = transferableFile.readBlock(blockNumber);
						os.write(stream, 0, stream.length);
					}
				}
				finally {
					os.close();
				}
			}
			finally {
				out.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
