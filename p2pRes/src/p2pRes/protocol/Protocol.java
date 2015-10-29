package p2pRes.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public abstract class Protocol {
	protected final static byte ASK_FILE_DESCRIPTOR = 0;
	protected final static byte ASK_BLOCK = 1;
	protected final static byte ACK = 2;
	protected final static byte ASK_END_CONNECTION = 3;

	private InputStream socketReader;
	private OutputStream socketSender;

	private ObjectOutputStream socketObjectSender;
	private ObjectInputStream socketObjectReader;			
	
	
	public Protocol(Socket socket) {
		try {
			socketReader = socket.getInputStream();
			socketSender = socket.getOutputStream();

			socketObjectSender = new ObjectOutputStream(socketSender);
			socketObjectReader = new ObjectInputStream(socketReader);						
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	protected byte readByte() {
		try {
			return (byte) socketReader.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	protected void sendByte(byte value) {
		try {
			socketSender.write(value);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected int readInt() {
		try {
			return socketReader.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	protected void sendInt(int value) {
		try {
			socketSender.write(value);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected long readLong() {
		try {
			return (long)socketReader.read();//TODO transfer long!
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	protected void sendLong(long value) {
		try {
			socketSender.write((int)value);//TODO transfer long!
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	protected byte[] readBytes() {
		int size = this.readInt();
		byte[] buffer = new byte[size]; //todo : don't allocate everytime
		try {
			socketReader.read(buffer);
			this.sendAcknowlegment();
			return buffer;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	protected void sendBytes(byte[] bytes) {
		try {
			this.sendInt(bytes.length);
			socketSender.write(bytes);
			this.waitForAcknowlegment();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void sendObject(Object obj) {
		try {
			socketObjectSender.writeObject(obj);
			this.waitForAcknowlegment();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	protected Object readObject() {
		try {
			Object obj = socketObjectReader.readObject();
			this.sendAcknowlegment();
			return obj;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}		
	}
	
	private void waitForAcknowlegment() {
		while (true/*set an escape timeout*/) {
			byte command = this.readByte();
			if (ACK==command) {
				System.out.println("Protocol - ACK received");
				return;
			}		
		}
	}
	
	private void sendAcknowlegment() {
		this.sendByte(ACK);
		System.out.println("Protocol - ACK send...");
	}
}
