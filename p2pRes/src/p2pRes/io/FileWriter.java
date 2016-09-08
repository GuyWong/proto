package p2pRes.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.Map;

/**
 * Synchronized upon file
 * */
public class FileWriter {
	private static Map<String, Object> locks = new HashMap<String, Object>();
	
	private final String filePath;
	
	private SeekableByteChannel fileChannel;
	
	public FileWriter(String path) throws WriterException {
		this.filePath = path;
		this.fileChannel = initChannel(path, false); 
	}
	
	public FileWriter(String path, boolean truncate) throws WriterException {
		this.filePath = path;
		this.fileChannel = initChannel(path, truncate); 
	}
	
	private synchronized SeekableByteChannel initChannel(String path, boolean truncate) throws WriterException {
		try {
			if (!locks.containsKey(path)) {
				SeekableByteChannel channel =  Files.newByteChannel(Paths.get(path), 
																	StandardOpenOption.CREATE, 
																	StandardOpenOption.TRUNCATE_EXISTING, 
																	StandardOpenOption.WRITE);
				locks.put(path, new Object());
				return channel;
			}	
			else {
				if (truncate==true) {
					return Files.newByteChannel(Paths.get(path), StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
				}
				return Files.newByteChannel(Paths.get(path), StandardOpenOption.WRITE);
			}
		} catch (IOException e) {
			throw new WriterException("Can't open file " + path, e);
		}
	}
	
	public void write(long position, byte[] data) throws WriterException {
		try {
			synchronized (locks.get(filePath)) {
				this.fileChannel.position(position);
				this.fileChannel.write(ByteBuffer.wrap(data));
			}
		} catch (IOException e) {
			throw new WriterException("Can't write data on file", e);
		}
	}
	
	public synchronized void close() {
		locks.remove(filePath);
	}
}
