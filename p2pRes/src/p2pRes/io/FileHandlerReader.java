/*package p2pRes.io;

import p2pRes.file.BlockProcessor;
import p2pRes.file.BlockProcessorException;
import p2pRes.model.FileDescriptor;

public class FileHandlerReader extends BlockProcessor {
	private FileWriter writer;
	
	public FileHandlerReader(String path, FileDescriptor descriptor) throws BlockProcessorException {
		super(descriptor);
		
		try {
			this.writer = new FileWriter(path);
		} catch (WriterException e) {
			throw new BlockProcessorException("Writer initializing on file " + path + " failed", e);
		}
	}

	protected void operateBlockOnFile(long fileOffset, byte[] block) throws BlockProcessorException {
		try {
			this.writer.write(fileOffset, block);
		} catch (WriterException e) {
			throw new BlockProcessorException("Writing of block at position " + fileOffset + " failed", e);
		}
	}
}*/
