package p2pRes.model;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import p2pRes.io.FileWriter;
import p2pRes.log.Logger;

/**
 * Thread safe object
 */
public class FileHandler { 
	private Set<Integer> writedBlocksSet = new HashSet<Integer>();
	private Set<Integer> assignedBlocksSet = new HashSet<Integer>();
	private FileWriter writer;
	private FileDescriptor descriptor;
	
	
	public FileHandler(String path, FileDescriptor descriptor) throws IOException {	
		this.writer = new FileWriter(path);
		this.descriptor = descriptor;
	}
	
	public boolean isComplete() {
		synchronized (writedBlocksSet) {
			return this.writedBlocksSet.size()==this.descriptor.getBlockNumbers();
		}
	}
	
	public int assignNewEmptyBlockNumber() {
		synchronized (assignedBlocksSet) {
			for (int i=0; i<descriptor.getBlockNumbers(); i++) {
				if (!assignedBlocksSet.contains(i)) {
					this.assignedBlocksSet.add(i);
					return i;
				}
			}
			return -1;
		}
	}
	
	public final FileDescriptor getDescriptor() {
		return this.descriptor;
	}

	public void writeBlock(int blockNumber, byte[] block) {
		synchronized (writedBlocksSet) {
			try {
				Logger.debug(" ReceivedFile - writing block " + blockNumber + " size " + block.length);
				this.writer.write(this.descriptor.getPosition(blockNumber), block);
				this.writedBlocksSet.add(blockNumber);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
