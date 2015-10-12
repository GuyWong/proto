package p2pRes.model;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public abstract class SharedFile {
	public final static int BLOC_SIZE = 1024; //need to be internal
	private final long blockNumbers;
	private final long fileSize;
	
	private SeekableByteChannel fileChannel;
	
	protected enum Mode {READ, WRITE}
	
	public SharedFile(String path, Mode mode) throws IOException { //Send a proper exception
		fileChannel = getChannel(path, mode);
		fileSize = fileChannel.size();
		blockNumbers = fileSize/BLOC_SIZE + 1;
	}
	
	private SeekableByteChannel getChannel(String path, Mode mode) throws IOException {
		if (mode == Mode.WRITE) {
			return Files.newByteChannel(Paths.get(path), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
		}
		return Files.newByteChannel(Paths.get(path));
	}
	
	public long blockNumbers() {
		return blockNumbers;
	}
	
	public int getBlockSize(long blockNumber) { //need to be internal
		return (int) ((blockNumber==blockNumbers)?(fileSize%BLOC_SIZE):BLOC_SIZE);
	}
	
	protected SeekableByteChannel getFileChannel() {
		return fileChannel;
	}
}
