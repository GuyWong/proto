package p2pRes.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileReader {
	private SeekableByteChannel fileChannel;
	
	public FileReader(String path) throws ReaderException {
		try {
			this.fileChannel = Files.newByteChannel(Paths.get(path));
		} catch (IOException e) {
			throw new ReaderException("Bad / invalid file, can't open " + path, e);
		}
	}
	
	public long getFileSize() throws ReaderException {
		try {
			return this.fileChannel.size();
		} catch (IOException e) {
			throw new ReaderException("Bad / invalid file, can't get size", e);
		}
	}
	
	public byte[] read(long position, int sizeToRead) throws ReaderException {
		ByteBuffer buffer = ByteBuffer.allocate(sizeToRead);
		
		try {
			fileChannel.position(position);
			fileChannel.read(buffer);
			return buffer.array();
		} catch (IOException e) {
			throw new ReaderException("Can't read data on file", e);
		}
	}
}
