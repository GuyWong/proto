package p2pRes.model;

import java.io.Serializable;

public class FileDescriptor implements Serializable {
	private static final long serialVersionUID = 6464457465596608623L;
	
	private static final int BLOC_SIZE = 1024*100;//To be parametized //100ko seems the best
												  //100ko = 214748to max file size
												  //1ko = 2147to max file size
	private final int blockNumbers;
	private final long fileSize;
	
	public FileDescriptor(long fileSize) {
		this.fileSize = fileSize;
		this.blockNumbers = (int)(fileSize/BLOC_SIZE + 1); //!\ avoid savage cast
	}
	
	public int getBlockNumbers() {
		return blockNumbers;
	}
	
	public int getBlockSize(int blockNumber) {
		return (int) ((blockNumber==(blockNumbers-1))?(fileSize%BLOC_SIZE):BLOC_SIZE);
	}
	
	public long getPosition(int blockNumber) {
		return ((long)blockNumber)*BLOC_SIZE;
	}
	
	public long getFileSize() {
		return fileSize;
	}
}
