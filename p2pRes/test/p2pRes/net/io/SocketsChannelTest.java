package p2pRes.net.io;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.commons.io.FileDeleteStrategy;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import p2pRes.common.StaticsValues;
import p2pRes.model.Block;
import p2pRes.model.FileException;
import p2pRes.model.TransferableFile;
import p2pRes.net.server.Server;

public class SocketsChannelTest {
	private final static int PORT = 6667;
	private final static String NETADR = "127.0.0.1";
	private final static String TESTPATH = FileUtils.getTempDirectoryPath() + "/testJU/";
	private final static String TESTFILENAME = "test.txt";
	private final static String OUTPATH = TESTPATH+"out/";
	private final static String TESTSTRING = "THIS IS A BLOCK TEST";
	
	private File testPathRepo = new File(TESTPATH);
	private File outPathRepo = new File(OUTPATH);
	private File testFile = new File(TESTPATH+TESTFILENAME);
	
	private Server server = null;
	private ClientChannel clientChannel = null;
	private ExecutorService serverThread = Executors.newSingleThreadExecutor();
	
	@Test
	public void testOpenSubChannel() {
		openSubChannel();
		openSubChannel();
		openSubChannel();
	}
	
	public void openSubChannel() {
		ClientChannel subChannel1;
		ClientChannel subChannel2;
		ClientChannel subChannel3;
		try {
			subChannel1 = clientChannel.openSubChannel();
			subChannel2 = clientChannel.openSubChannel();
			subChannel3 = subChannel1.openSubChannel();
			subChannel1.close();
			subChannel2.close();
			subChannel3.close();
			if (!assertPortUnbinded(subChannel1.getPort())) {
				Assert.fail("Port " + clientChannel.getPort() + " not unbinded");
			}
			if (!assertPortUnbinded(subChannel2.getPort())) {
				Assert.fail("Port " + clientChannel.getPort() + " not unbinded");
			}
			if (!assertPortUnbinded(subChannel3.getPort())) {
				Assert.fail("Port " + clientChannel.getPort() + " not unbinded");
			}
		} catch (ChannelException e) { Assert.fail(e.getMessage()); }
	}
	
	@Test
	public void testTransferBlock(){
		initTestFs();
		try {
			ClientChannel subChannel;
			ClientChannel transferChannel;
			
			try {
				subChannel = clientChannel.openSubChannel();
				try {
					TransferableFile transferableFile;
					try {
						transferableFile = new TransferableFile(TESTPATH+TESTFILENAME, StaticsValues.BLOC_SIZE);
						transferChannel = subChannel.openTransferChannel(transferableFile.getDescriptor());
						try {
							transferChannel.sendBlock(0, new Block(transferableFile.readBlock(0)));
						} finally { transferChannel.close(); }
						if (!assertPortUnbinded(transferChannel.getPort())) {
							Assert.fail("Port " + transferChannel.getPort() + " not unbinded");
						}
					} catch (FileException e) { Assert.fail(e.getMessage()); }
				} finally { subChannel.close(); }
				if (!assertPortUnbinded(subChannel.getPort())) {
					Assert.fail("Port " + subChannel.getPort() + " not unbinded");
				}
			} catch (ChannelException e) { Assert.fail(e.getMessage()); }
			
			try { Thread.sleep(2000); } catch (InterruptedException e) { }
		} finally { tearDownTestFs(); }
	}
	
	@Before
	public void setUp() {
		server = new Server(PORT, "./", OUTPATH);
		serverThread.execute(server);

		try {
			clientChannel = new ClientChannel(NETADR, PORT);
		} catch (ChannelException e) { Assert.fail(e.getMessage()); }
	}
	
	@After
	public void tearDown() {
		serverThread.shutdownNow();
		try {
			clientChannel.close();
		} catch (ChannelException e) { Assert.fail(e.getMessage()); }
	
		try { Thread.sleep(2000); } catch (InterruptedException e) { }
		if (!assertPortUnbinded(clientChannel.getPort())) {
			Assert.fail("Port " + clientChannel.getPort() + " not unbinded");
		}
	}
	
	private boolean assertPortUnbinded(int port) {
		try {
			Thread.sleep(200);
		} catch (InterruptedException e1) { }
		
		ServerSocket serverSocket;
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			return false;
		}
		
		try {
			serverSocket.close();
		} catch (IOException e) {}
		return true;
	}
	
	private void initTestFs() {
		testPathRepo.mkdirs();
		testPathRepo.setWritable(true);
		outPathRepo.mkdirs();
		outPathRepo.setWritable(true);
		
		try {
			FileWriter writer = new FileWriter(testFile);
			try {
				writer.write(TESTSTRING);
			}
			finally { writer.close(); }
		} catch (IOException e) {
			Assert.fail(e.getMessage());
		}
	}
	
	private void tearDownTestFs() {
		FileDeleteStrategy.FORCE.deleteQuietly(new File(OUTPATH+TESTFILENAME));
		FileDeleteStrategy.FORCE.deleteQuietly(testFile);
		FileDeleteStrategy.FORCE.deleteQuietly(outPathRepo);
		FileDeleteStrategy.FORCE.deleteQuietly(testPathRepo);

	}
}
