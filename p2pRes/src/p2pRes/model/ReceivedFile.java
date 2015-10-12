package p2pRes.model;

import java.io.IOException;
import java.nio.ByteBuffer;

public class ReceivedFile extends SharedFile { 
	private long blockCounter = 0;//add a block map...
	
	
	public ReceivedFile(String path) throws IOException {	
		super(path, Mode.WRITE);
	}

	public void writeBlock(long blockNumber, byte[] block) {
		try {
			this.getFileChannel().position(blockNumber*BLOC_SIZE);
			this.getFileChannel().write(ByteBuffer.wrap(block));
			blockCounter++;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isComplete() {
		return blockCounter==this.blockNumbers()?true:false;
	}
}
