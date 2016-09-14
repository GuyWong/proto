package p2pRes.net.io.protocol;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public abstract class Protocol {
	protected final static byte ASK_FILE_DESCRIPTOR = 0;
	protected final static byte ASK_OPEN_FILEPUSH_CHANNEL = 1;	
	protected final static byte ASK_BLOCK = 2;
	protected final static byte PUSH_BLOCK = 3;
	protected final static byte ACK = 4;
	protected final static byte ASK_NEW_CONNECTION = 5;
	protected final static byte ASK_END_CONNECTION = 6;	

	private ObjectOutputStream socketObjectSender;
	private ObjectInputStream socketObjectReader;	
	private Socket socket;
	
	public Protocol(Socket socket) throws ProtocolException {
		this.socket = socket;
		try {
			socketObjectSender = new ObjectOutputStream(socket.getOutputStream());
			socketObjectReader = new ObjectInputStream(socket.getInputStream());						
		} catch (IOException e) {
			throw new ProtocolException(e);
		}
	}
		
	protected byte readByte() throws ProtocolException {
		try { 
			byte res =  (byte) socketObjectReader.readByte();
			return res;
		} catch (IOException e) {
			throw new ProtocolException(e);
		}
	}
	
	protected void sendByte(byte value) throws ProtocolException {
		try {
			socketObjectSender.writeByte(value);
			socketObjectSender.reset();
			socketObjectSender.flush();
		} catch (IOException e) {
			throw new ProtocolException(e);
		}
	}
	
	protected int readInt() throws ProtocolException {
		try {
			int res = socketObjectReader.readInt();
			return res;
		} catch (IOException e) {
			throw new ProtocolException(e);
		}
	}
	
	protected void sendInt(int value) throws ProtocolException {
		try {
			socketObjectSender.writeInt(value);
			socketObjectSender.reset();
			socketObjectSender.flush();
		} catch (IOException e) {
			throw new ProtocolException(e);
		}
	}

	protected byte[] readBytes() throws ProtocolException {
		int size = (int)this.readInt();
		byte[] buffer = new byte[size]; //todo : don't allocate everytime
		try {
			socketObjectReader.read(buffer, 0, size);
			this.sendAcknowlegment();
			return buffer;
		} catch (IOException e) {
			throw new ProtocolException(e);
		}
	}
	
	protected void sendBytes(byte[] bytes) throws ProtocolException {
		try {
			this.sendInt(bytes.length);
			socketObjectSender.write(bytes, 0, bytes.length);
			socketObjectSender.reset();
			socketObjectSender.flush();
			this.waitForAcknowlegment();
		} catch (IOException e) {
			throw new ProtocolException(e);
		}
	}
	
	protected void sendObject(Object obj) throws ProtocolException {
		try {
			socketObjectSender.writeObject(obj);
			socketObjectSender.reset();
			socketObjectSender.flush();
				
			this.waitForAcknowlegment();	
		} catch (IOException e) {
			throw new ProtocolException(e);
		}		
	}
	
	protected Object readObject() throws ProtocolException {
		try {
			Object obj = socketObjectReader.readObject();
			this.sendAcknowlegment();
			
			return obj;			
		} catch (IOException e) {
			throw new ProtocolException(e);
		} catch (ClassNotFoundException e) {
			throw new ProtocolException("No object found on socket stream", e);
		}		
	}
	
	private void waitForAcknowlegment() throws ProtocolException {
		while (true/*set an escape timeout*/) {
			byte command = this.readByte();
			if (ACK==command) {
				return;
			}	
			throw new ProtocolException("Unexpected byte received " + command + " ACK expected");
		}
	}
	
	private void sendAcknowlegment() throws ProtocolException {
		this.sendByte(ACK);
	}
	
	protected Socket getSocket() {
		return this.socket;
	}
	
	public void close() throws ProtocolException {
		try {
			socket.close();
		} catch (IOException e) {
			throw new ProtocolException("Error closing connection", e);
		}
	}
}
