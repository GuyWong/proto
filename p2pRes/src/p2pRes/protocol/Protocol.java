package p2pRes.protocol;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import p2pRes.io.FileUtils;

public class Protocol {
	public static void getFile(Socket socket, String outRep) {
		try {      
	        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			
	        String fileName;
	        while ((fileName = in.readLine()) != null) {
	        	System.out.println("fileName: " + fileName);
	        	if (fileName.length()>0) { break; }
	        }
	         
	        char[] mybytearray = new char[1024];
	        FileOutputStream fos = new FileOutputStream(outRep+"//"+fileName);
	        BufferedOutputStream bos = new BufferedOutputStream(fos);
		    try {
		        int bytesRead = in.read(mybytearray, 0, mybytearray.length);
		        bos.write((new String(mybytearray)).getBytes(), 0, bytesRead);
	        }
	        finally {
	        	bos.close();
	        }
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void sendFile(Socket socket, String filePath) {
		
		try {
			PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
			try {
				out.println((new File(filePath)).getName());
				
				OutputStream os = socket.getOutputStream();
				try {
					os.flush();
					byte[] stream = FileUtils.readFile(filePath);
					os.write(stream, 0, stream.length);
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
