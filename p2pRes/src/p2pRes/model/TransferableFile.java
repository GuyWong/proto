package p2pRes.model;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TransferableFile {
	private FileDescriptor descriptor;
	private SeekableByteChannel fileChannel;
	
	public TransferableFile(String path) throws IOException {
		this.fileChannel = Files.newByteChannel(Paths.get(path));
		this.descriptor = new FileDescriptor(this.fileChannel.size());
	}
	
	public final FileDescriptor getDescriptor() {
		return descriptor;
	}
	
	public byte[] readBlock(int blockNumber) {
		ByteBuffer buffer = ByteBuffer.allocate(descriptor.getBlockSize(blockNumber));
		
		try {
			fileChannel.position(descriptor.getPosition(blockNumber));
			fileChannel.read(buffer);
			return buffer.array();
		} catch (IOException e1) {
			e1.printStackTrace();//TODO
			return null;
		}
	}
}
