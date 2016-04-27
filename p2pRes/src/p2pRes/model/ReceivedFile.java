package p2pRes.model;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class ReceivedFile { 
	private long blockCounter = 0;//add a block map...
	private SeekableByteChannel fileChannel;
	private FileDescriptor descriptor;
	
	
	public ReceivedFile(String path, FileDescriptor descriptor) throws IOException {	
		this.fileChannel = Files.newByteChannel(Paths.get(path), StandardOpenOption.CREATE, 
																	StandardOpenOption.TRUNCATE_EXISTING, 
																	StandardOpenOption.WRITE);
		this.descriptor = descriptor;
	}
	
	public boolean isComplete() {
		return blockCounter==this.descriptor.getBlockNumbers()?true:false;
	}
	
	public final FileDescriptor getDescriptor() {
		return descriptor;
	}

	public void writeBlock(long blockNumber, byte[] block) {
		try {
			System.out.println(" ReceivedFile - writing block " + blockNumber + " size " + block.length);
			this.fileChannel.position(this.descriptor.getPosition(blockNumber));
			this.fileChannel.write(ByteBuffer.wrap(block));
			this.blockCounter++;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
