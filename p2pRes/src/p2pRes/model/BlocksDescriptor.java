package p2pRes.model;

import java.io.Serializable;

public class BlocksDescriptor implements Serializable {
	private static final long serialVersionUID = 8867386488925913177L;
	
	private final int blockSize;
	private final int blockNumbers;
	private final long fileSize;
	
	public BlocksDescriptor(long fileSize, 
							int blockSize) {
		this.blockSize = blockSize;
		this.fileSize = fileSize;
		this.blockNumbers = (int)(fileSize/blockSize + 1); //!\ avoid savage cast
	}
	
	public int getBlockNumbers() {
		return blockNumbers;
	}
	
	public int getBlockSize(int blockNumber) {
		return (int) ((blockNumber==(blockNumbers-1))?(fileSize%blockSize):blockSize);
	}
	
	public long getPosition(int blockNumber) {
		return ((long)blockNumber)*blockSize;
	}
	
	public long getFileSize() {
		return fileSize;
	}
}
