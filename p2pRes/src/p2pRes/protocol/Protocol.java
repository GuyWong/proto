package p2pRes.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

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
			socketSender.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected long readLong() {
		try {
			byte[] encodedValue = new byte[Long.SIZE / Byte.SIZE];
			socketReader.read(encodedValue);
			return ByteBuffer.wrap(encodedValue).getLong();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return -1;
		}
	}
	
	protected void sendLong(long value) {
		try {
			socketSender.write(ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(value).array());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	protected byte[] readBytes() {
		int size = (int)this.readLong();
		byte[] buffer = new byte[size]; //todo : don't allocate everytime
		try {
			System.out.println("Protocol - readBytes size " + size);
			socketReader.read(buffer, 0, size);
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
			this.sendLong(bytes.length);
			socketSender.write(bytes, 0, bytes.length);
			System.out.println("Protocol - sendBytes size " + bytes.length);
			socketSender.flush();
			this.waitForAcknowlegment();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	protected void sendObject(Object obj) {
		try {
			socketObjectSender.writeObject(obj);
			socketObjectSender.flush();
			this.waitForAcknowlegment();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	protected Object readObject() {
		try {
			Object obj = socketObjectReader.readObject();
			socketObjectSender.flush();
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
				//System.out.println("Protocol - ACK received");
				return;
			}	
			System.out.println("Protocol - not ACK... " + command);
		}
	}
	
	private void sendAcknowlegment() {
		this.sendByte(ACK);
		//System.out.println("Protocol - ACK send...");
	}
}
