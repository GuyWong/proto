package p2pRes.net.protocol;

import p2pRes.net.protocol.ClientProtocol;
import p2pRes.net.protocol.ProtocolException;
import p2pRes.net.protocol.ServerProtocol;
import p2pRes.testTools.SimpleTestRunner;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ProtocolTest {
	private final static int PORT_NUMBER = 6667;
	private final static String LOCALHOST = "127.0.0.1";
	
	//Test Values
	private final static byte byteValue = (byte) 127;
	private final static int intValue = 12345;
	private final static byte[] bytesValue = "TestBytes".getBytes();
	private final static String objectValue = "TestObject";
	
	private ServerProtocol serverProtocol;
	private ServerSocket serverSocket;
	private ClientProtocol clientProtocol;
	private Socket clientSocket;
	
	@Test
	public void testByte() {
		SimpleTestRunner<Byte> runner = new SimpleTestRunner<Byte>(){
			@Override
			public Byte toBeRun() throws Exception {
				return clientProtocol.readByte();
			}
		};
		try {
			serverProtocol.sendByte(byteValue);
		} catch (ProtocolException e) { Assert.fail(e.getMessage()); }
		
		Byte result = waitForResult(runner, 5000);
		if (runner.hasError()) { Assert.fail(runner.getError().getMessage()); }
		Assert.assertNotNull("Can't get result", result);
		Assert.assertEquals(byteValue, result);
	}
	
	@Test
	public void testInt() {
		SimpleTestRunner<Integer> runner = new SimpleTestRunner<Integer>(){
			@Override
			public Integer toBeRun() throws Exception {
				return clientProtocol.readInt();
			}
		};
		try {
			serverProtocol.sendInt(intValue);
		} catch (ProtocolException e) { Assert.fail(e.getMessage()); }
		
		Integer result = waitForResult(runner, 5000);
		if (runner.hasError()) { Assert.fail(runner.getError().getMessage()); }
		Assert.assertNotNull("Can't get result", result);
		Assert.assertEquals(intValue, result);
	}
	
	@Test
	public void testBytes() {
		SimpleTestRunner<byte[]> runner = new SimpleTestRunner<byte[]>(){
			@Override
			public byte[] toBeRun() throws Exception {
				return clientProtocol.readBytes();
			}
		};
		
		try {
			serverProtocol.sendBytes(bytesValue);
		} catch (Exception e) { Assert.fail(e.getMessage()); }
		
		byte[] result = waitForResult(runner, 5000);
		if (runner.hasError()) { Assert.fail(runner.getError().getMessage()); }
		Assert.assertNotNull("Can't get result", result);
		Assert.assertEquals(new String(bytesValue), new String(result));
	}
	
	@Test
	public void testObject() {
		SimpleTestRunner<Object> runner = new SimpleTestRunner<Object>(){
			@Override
			public Object toBeRun() throws Exception {
				return clientProtocol.readObject();
			}
		};
		
		try {
			serverProtocol.sendObject(objectValue);
		} catch (Exception e) { Assert.fail(e.getMessage()); }
		
		Object result = waitForResult(runner, 5000);
		if (runner.hasError()) { Assert.fail(runner.getError().getMessage()); }
		Assert.assertNotNull("Can't get result", result);
		Assert.assertEquals(objectValue, result);
	}
	
	@Test
	public void testEmptyChannelByte() {
		this.testByte();
		assertEmptySocketChannel(clientProtocol);
	}
	
	@Test
	public void testEmptyChannelInt() {
		this.testInt();
		assertEmptySocketChannel(clientProtocol);
	}
	
	@Test
	public void testEmptyChannelBytes() {
		this.testBytes();
		assertEmptySocketChannel(clientProtocol);
	}
	
	@Test
	public void testEmptyChannelObject() {
		this.testObject();
		assertEmptySocketChannel(clientProtocol);
	}
	
	@Test
	public void chainTest() {
		try {
			this.testByte();
			this.testInt();
			this.testBytes();
			this.testObject();
			this.testByte();
			this.testInt();
			this.testBytes();
			this.testObject();
			assertEmptySocketChannel(clientProtocol);
		} catch (Exception e) { Assert.fail(e.getMessage()); }
		
	}
	
	@Before
	public void setUp() {
		SimpleTestRunner<ServerSocket> runner = new SimpleTestRunner<ServerSocket>(){
			@Override
			public ServerSocket toBeRun() throws Exception {
				ServerSocket serverSocket = new ServerSocket(PORT_NUMBER);
				serverProtocol = new ServerProtocol(serverSocket.accept());
				return serverSocket;
			}	
		};
		
		try {
			clientSocket = new Socket(LOCALHOST, PORT_NUMBER);
			clientProtocol = new ClientProtocol(clientSocket);
		}
		catch (Exception e) { Assert.fail(e.getMessage()); }

		serverSocket = waitForResult(runner, 5000);
		if (runner.hasError()) { Assert.fail(runner.getError().getMessage()); }
		Assert.assertNotNull("Can't get serverSocket", serverSocket);
	}
	
	@After
	public void tearDown() {
		if (serverSocket!=null) {
			try {
				clientSocket.close();
			} catch (IOException e) {
				Assert.fail(e.getMessage());
			}
		}
		if (serverSocket!=null) {
			try {
				serverSocket.close();
			} catch (IOException e) {
				Assert.fail(e.getMessage());
			}
		}
	}
	
	public void assertEmptySocketChannel(final ClientProtocol clientProtocol) {
		SimpleTestRunner<Byte> runner = new SimpleTestRunner<Byte>(){
			@Override
			public Byte toBeRun() throws Exception {
				return clientProtocol.readByte();
			}	
		};

		if (runner.hasError()) { Assert.fail(runner.getError().getMessage()); }

		Byte result = waitForResult(runner, 100);
		Assert.assertNull("Non empty socket channel " + result, result);
	}
	
	public <T> T waitForResult(SimpleTestRunner<T> runner, long timeoutInMs) {
		long targetTimeout = System.currentTimeMillis() + timeoutInMs;
		long sleepTime = (timeoutInMs+1)/10L;

		while(!runner.hasResult()) {
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) { Assert.fail(e.getMessage()); }
			if (System.currentTimeMillis()>targetTimeout) {
				return null;
			}
		}
		
		return runner.getResult();
	}
}
