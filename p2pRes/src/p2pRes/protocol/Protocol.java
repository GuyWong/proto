package p2pRes.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;

import p2pRes.log.Logger;


//TODO : Fix that ugly behavior, every protocol part need to own his sender. 
//if you reset a sender it wont crash after because you need a acknowledge send
public abstract class Protocol {
	protected final static byte ASK_FILE_DESCRIPTOR = 0;
	protected final static byte ASK_BLOCK = 1;
	protected final static byte ACK = 2;
	protected final static byte ASK_END_CONNECTION = 3;

	private InputStream socketReader;
	private OutputStream socketSender;

	private ObjectOutputStream socketObjectSender;
	private ObjectInputStream socketObjectReader;			
	
	
	public Protocol(Socket socket) throws ProtocolException {
		try {
			socketReader = socket.getInputStream();
			socketSender = socket.getOutputStream();

			socketObjectSender = new ObjectOutputStream(socketSender);
			socketObjectReader = new ObjectInputStream(socketReader);						
		} catch (IOException e) {
			throw new ProtocolException(e);
		}
	}
		
	protected byte readByte() throws ProtocolException {
		try {
			return (byte) socketReader.read();
		} catch (IOException e) {
			throw new ProtocolException(e);
		}
	}
	
	protected void sendByte(byte value) throws ProtocolException {
		try {
			socketSender.write(value);
			socketSender.flush();
		} catch (IOException e) {
			throw new ProtocolException(e);
		}
	}
	
	protected int readInt() throws ProtocolException {
		try {
			byte[] encodedValue = new byte[Integer.SIZE / Byte.SIZE];
			socketReader.read(encodedValue);
			return ByteBuffer.wrap(encodedValue).getInt();
		} catch (IOException e) {
			throw new ProtocolException(e);
		}
	}
	
	protected void sendInt(int value) throws ProtocolException {
		try {
			socketSender.write(ByteBuffer.allocate(Integer.SIZE / Byte.SIZE).putInt(value).array());
		} catch (IOException e) {
			throw new ProtocolException(e);
		}
	}
	
	/*protected long readLong() throws ProtocolException {
		try {
			byte[] encodedValue = new byte[Long.SIZE / Byte.SIZE];
			socketReader.read(encodedValue);
			return ByteBuffer.wrap(encodedValue).getLong();
		} catch (IOException e) {
			throw new ProtocolException(e);
		}
	}
	
	protected void sendLong(long value) throws ProtocolException {
		try {
			socketSender.write(ByteBuffer.allocate(Long.SIZE / Byte.SIZE).putLong(value).array());
		} catch (IOException e) {
			throw new ProtocolException(e);
		}
	}*/
	
	
	protected byte[] readBytes() throws ProtocolException {
		int size = (int)this.readInt();
		byte[] buffer = new byte[size]; //todo : don't allocate everytime
		try {
			Logger.debug("Protocol - readBytes size " + size);
			socketReader.read(buffer, 0, size);
			this.sendAcknowlegment();
			return buffer;
		} catch (IOException e) {
			throw new ProtocolException(e);
		}
	}
	
	protected void sendBytes(byte[] bytes) throws ProtocolException {
		try {
			this.sendInt(bytes.length);
			socketSender.write(bytes, 0, bytes.length);
			Logger.debug("Protocol - sendBytes size " + bytes.length);
			socketSender.flush();
			this.waitForAcknowlegment();
		} catch (IOException e) {
			throw new ProtocolException(e);
		}
	}
	
	protected void sendObject(Object obj) throws ProtocolException {
		try {
			try {
				socketObjectSender.writeObject(obj);
				socketObjectSender.flush();
				this.waitForAcknowlegment();
			}
			finally {
				socketObjectSender.reset();
			}			
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
}
