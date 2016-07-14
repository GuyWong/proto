package p2pRes.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class FileWriter {
	private SeekableByteChannel fileChannel;
	
	public FileWriter(String path) throws WriterException {
		try {
			this.fileChannel = Files.newByteChannel(Paths.get(path), 
					StandardOpenOption.CREATE, 
					StandardOpenOption.TRUNCATE_EXISTING, 
					StandardOpenOption.WRITE);
		} catch (IOException e) {
			throw new WriterException("Can't open file " + path, e);
		}
	}
	
	public synchronized void write(long position, byte[] data) throws WriterException {
		try {
			this.fileChannel.position(position);
			this.fileChannel.write(ByteBuffer.wrap(data));
		} catch (IOException e) {
			throw new WriterException("Can't write data on file", e);
		}
		
	}
}
