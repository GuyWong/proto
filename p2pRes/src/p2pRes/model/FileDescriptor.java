package p2pRes.model;

import java.io.Serializable;

public class FileDescriptor implements Serializable {
	private static final long serialVersionUID = 6464457465596608623L;

	private final BlocksDescriptor blocksDescriptor;
	private final String fileHash;
	
	public FileDescriptor(long fileSize, 
							int blockSize,
							String fileHash) {
		this.blocksDescriptor = new BlocksDescriptor(fileSize, blockSize);
		this.fileHash = fileHash;
	}

	public String getFileHash() {
		return fileHash;
	}
	
	public BlocksDescriptor getBlocksDescriptor() {
		return blocksDescriptor;
	}
}
