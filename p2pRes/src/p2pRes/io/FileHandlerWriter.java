/*package p2pRes.io;

import p2pRes.file.BlockProcessor;
import p2pRes.file.BlockProcessorException;
import p2pRes.model.FileDescriptor;

public class FileHandlerWriter extends BlockProcessor {
	private FileReader reader;
	
	public FileHandlerWriter(String path, FileDescriptor descriptor) throws BlockProcessorException {
		super(descriptor);
		
		try {
			this.reader = new FileReader(path);
		} catch (ReaderException e) {
			throw new BlockProcessorException("Writer initializing on file " + path + " failed", e);
		}
	}

	protected void operateBlockOnFile(long fileOffset, byte[] block) throws BlockProcessorException {
		try {
			this.reader.read(fileOffset, 0);
		} catch (ReaderException e) {
			throw new BlockProcessorException("Writing of block at position " + fileOffset + " failed", e);
		}
	}
}*/
