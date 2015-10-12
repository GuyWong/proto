package p2pRes.model;

import java.io.IOException;
import java.nio.ByteBuffer;

public class TransferableFile extends SharedFile {

	public TransferableFile(String path) throws IOException {
		super(path, Mode.READ);
	}
	
	public byte[] readBlock(long blockNumber) {
		ByteBuffer buffer = ByteBuffer.allocate(getBlockSize(blockNumber));
		
		try {
			this.getFileChannel().position(blockNumber*BLOC_SIZE);
			this.getFileChannel().read(buffer);
			return buffer.array();
		} catch (IOException e1) {
			e1.printStackTrace();
			return null;
		}
	}
	

}
