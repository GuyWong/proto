package p2pRes.model;

import java.io.File;

import p2pRes.io.FileReader;
import p2pRes.io.ReaderException;
import p2pRes.utils.FileHashBuilder;
import p2pRes.utils.HashBuilderException;

public class TransferableFile {
	private final FileDescriptor descriptor;
	private final String filePath;
	
	private FileReader fileReader;

	public TransferableFile(String path, int blockSize) throws FileException {
		
		try {
			this.filePath = path;
			this.fileReader = new FileReader(path);
			this.descriptor = new FileDescriptor((new File(path)).getName(),
													this.fileReader.getFileSize(), 
													blockSize,
													(new FileHashBuilder(path)).build());
		} catch (ReaderException e) {
			throw new FileException("Can't open file " + path, e);
		} catch (HashBuilderException e) {
			throw new FileException("Can't compute file hash", e);
		}
	}
	
	public final FileDescriptor getDescriptor() {
		return descriptor;
	}
	
	public final String getFilePath() {
		return this.filePath;
	}
	
	public byte[] readBlock(int blockNumber) throws FileException {
		try {
			return fileReader.read(descriptor.getBlocksDescriptor().getPosition(blockNumber), 
									descriptor.getBlocksDescriptor().getBlockSize(blockNumber));
		} catch (ReaderException e) {
			throw new FileException("Can't read block " + blockNumber, e);
		}
	}
}
