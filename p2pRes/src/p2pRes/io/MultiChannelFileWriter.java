package p2pRes.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class MultiChannelFileWriter {
	private SeekableByteChannel fileChannel;
	
	public MultiChannelFileWriter(String path) throws IOException {
		this.fileChannel = Files.newByteChannel(Paths.get(path), 
				StandardOpenOption.CREATE, 
				StandardOpenOption.TRUNCATE_EXISTING, 
				StandardOpenOption.WRITE);
	}
	
	public void write(int position, byte[] data) throws IOException {
		this.fileChannel.position(position);
		this.fileChannel.write(ByteBuffer.wrap(data));
	}
}
