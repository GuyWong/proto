package p2pRes.model;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import p2pRes.io.MultiChannelFileWriter;
import p2pRes.log.Logger;

public class ReceivedFile { 
	private Set<Integer> blockSet = new HashSet<Integer>();
	private MultiChannelFileWriter writer;
	private FileDescriptor descriptor;
	
	
	public ReceivedFile(String path, FileDescriptor descriptor) throws IOException {	
		this.writer = new MultiChannelFileWriter(path);
		this.descriptor = descriptor;
	}
	
	public boolean isComplete() {
		return blockSet.size()==this.descriptor.getBlockNumbers();
	}
	
	public boolean isWrited(int blockNumber) {
		return blockSet.contains(blockNumber);
	}
	
	public final FileDescriptor getDescriptor() {
		return descriptor;
	}

	public void writeBlock(int blockNumber, byte[] block) {
		try {
			Logger.debug(" ReceivedFile - writing block " + blockNumber + " size " + block.length);
			writer.write(this.descriptor.getPosition(blockNumber), block);
			this.blockSet.add(blockNumber);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
