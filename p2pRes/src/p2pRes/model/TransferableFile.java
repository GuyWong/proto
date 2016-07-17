package p2pRes.model;

import p2pRes.io.FileReader;
import p2pRes.io.ReaderException;
import p2pRes.utils.FileHashBuilder;
import p2pRes.utils.HashBuilderException;

public class TransferableFile {
	private final FileDescriptor descriptor;
	
	private FileReader fileReader;
	
	public TransferableFile(String path, int blockSize) throws FileHandlerException {
		
		try {
			this.fileReader = new FileReader(path);
			this.descriptor = new FileDescriptor(this.fileReader.getFileSize(), 
													blockSize,
													(new FileHashBuilder(path)).build());
		} catch (ReaderException e) {
			throw new FileHandlerException("Can't open file " + path, e);
		} catch (HashBuilderException e) {
			throw new FileHandlerException("Can't compute file hash", e);
		}
	}
	
	public final FileDescriptor getDescriptor() {
		return descriptor;
	}
	
	public byte[] readBlock(int blockNumber) throws FileHandlerException {
		try {
			return fileReader.read(descriptor.getBlocksDescriptor().getPosition(blockNumber), 
									descriptor.getBlocksDescriptor().getBlockSize(blockNumber));
		} catch (ReaderException e) {
			throw new FileHandlerException("Can't read block " + 	blockNumber, e);
		}
	}
}
