package p2pRes.model;

import java.io.Serializable;

public class FileDescriptor implements Serializable {
	private static final long serialVersionUID = 6464457465596608623L;

	private final String fileName;
	private final BlocksDescriptor blocksDescriptor;
	private final String fileHash;
	
	public FileDescriptor(String fileName,
							long fileSize, 
							int blockSize,
							String fileHash) {
		this.fileName = fileName;
		this.blocksDescriptor = new BlocksDescriptor(fileSize, blockSize);
		this.fileHash = fileHash;
	}

	public String getFileName() {
		return fileName;
	}
	
	public String getFileHash() {
		return fileHash;
	}
	
	public BlocksDescriptor getBlocksDescriptor() {
		return blocksDescriptor;
	}
}
