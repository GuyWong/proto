package p2pRes.model;

import java.io.Serializable;

public class FileDescriptor implements Serializable {
	private static final long serialVersionUID = 6464457465596608623L;
	
	private static final int BLOC_SIZE = 1024*100;//To be parametized //100ko seems the best
	private final long blockNumbers;
	private final long fileSize;
	
	public FileDescriptor(long fileSize) {
		this.fileSize = fileSize;
		this.blockNumbers = fileSize/BLOC_SIZE + 1;
	}
	
	public long getBlockNumbers() {
		return blockNumbers;
	}
	
	public int getBlockSize(long blockNumber) {
		return (int) ((blockNumber==(blockNumbers-1))?(fileSize%BLOC_SIZE):BLOC_SIZE);
	}
	
	public long getPosition(long blockNumber) {
		return blockNumber*BLOC_SIZE;
	}
	
	public long getFileSize() {
		return fileSize;
	}
}
