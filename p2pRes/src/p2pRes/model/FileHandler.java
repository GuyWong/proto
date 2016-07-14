package p2pRes.model;

import java.util.HashSet;
import java.util.Set;
import p2pRes.io.FileWriter;
import p2pRes.io.WriterException;

/**
 * Thread safe object
 */
public class FileHandler { 
	public final static int NO_MORE_BLOCK_AVAILABLE = -1;
	
	private Set<Integer> writedBlocksSet = new HashSet<Integer>();
	private Set<Integer> assignedBlocksSet = new HashSet<Integer>();
	private FileWriter writer;
	private FileDescriptor descriptor;	
	
	public FileHandler(String path, FileDescriptor descriptor) throws FileHandlerException {	
		try {
			this.writer = new FileWriter(path);
		} catch (WriterException e) {
			throw new FileHandlerException("Writer initializing on file " + path + " failed", e);
		}
		this.descriptor = descriptor;
	}
	
	public synchronized boolean isComplete() {
		return this.writedBlocksSet.size()==this.descriptor.getBlockNumbers();
	}
	
	public synchronized int assignNewEmptyBlockNumber() {
		for (int i=0; i<this.descriptor.getBlockNumbers(); i++) {
			if (!this.assignedBlocksSet.contains(i)) {
				this.assignedBlocksSet.add(i);
				return i;
			}
		}
		return NO_MORE_BLOCK_AVAILABLE;
	}
	
	public synchronized void unassignBlockNumber(int blockNumber) {
		this.assignedBlocksSet.remove(blockNumber);
	}
	
	public final FileDescriptor getDescriptor() {
		return this.descriptor;
	}

	public synchronized void writeBlock(int blockNumber, byte[] block) throws FileHandlerException {
		try {
			this.writer.write(this.descriptor.getPosition(blockNumber), block);
			this.writedBlocksSet.add(blockNumber);
		} catch (WriterException e) {
			throw new FileHandlerException("Writing of block " + blockNumber + " failed", e);
		}
	}
}
