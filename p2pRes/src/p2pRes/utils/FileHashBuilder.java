package p2pRes.utils;

import p2pRes.io.FileReader;
import p2pRes.io.ReaderException;
import p2pRes.log.Logger;
import p2pRes.model.BlocksDescriptor;

public class FileHashBuilder {
	private final static int BLOCKSIZE = 1024*1024*10; //1MO blockSize

	private final BlocksDescriptor descriptor;
	
	private FileReader fileReader;
		
	public FileHashBuilder(String filePath) throws HashBuilderException {
		try {
			this.fileReader = new FileReader(filePath);
			this.descriptor = new BlocksDescriptor(this.fileReader.getFileSize(), BLOCKSIZE);
		} catch (ReaderException e) {
			throw new HashBuilderException("Error opening file " + filePath, e);
		}
	}
	
	public String build() throws HashBuilderException {
		StringBuffer strBuff = new StringBuffer();
		for (int i = 0; i<this.descriptor.getBlockNumbers(); i++) {
			Logger.info("Read block " + i + "/" + this.descriptor.getBlockNumbers());

			strBuff.append((new HashBuilder(readBlock(i))).build());
		}
		
		Logger.info(strBuff.toString());
		return strBuff.toString();
	}
	
	private byte[] readBlock(int blockNumber) throws HashBuilderException {
		try {
			return this.fileReader.read(this.descriptor.getPosition(blockNumber), 
												this.descriptor.getBlockSize(blockNumber));
		} catch (ReaderException e) {
			throw new HashBuilderException("Error reading block " + blockNumber, e);
		}
	}
}
