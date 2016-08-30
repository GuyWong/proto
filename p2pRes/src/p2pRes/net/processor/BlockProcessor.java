package p2pRes.net.processor;

import java.util.HashSet;
import java.util.Set;
import p2pRes.model.BlocksDescriptor;

public class BlockProcessor { 
	public final static int NO_MORE_BLOCK_AVAILABLE = -1;
	
	private Set<Integer> writedBlocksSet = new HashSet<Integer>();
	private Set<Integer> assignedBlocksSet = new HashSet<Integer>();
	private BlocksDescriptor blocksDescriptor;	
	
	public BlockProcessor(BlocksDescriptor blocksDescriptor) {	
		this.blocksDescriptor = blocksDescriptor;
	}
	
	public synchronized boolean isComplete() {
		return this.writedBlocksSet.size()==this.blocksDescriptor.getBlockNumbers();
	}
	
	private synchronized int assignNewEmptyBlockNumber() {
		for (int i=0; i<this.blocksDescriptor.getBlockNumbers(); i++) {
			if (!this.assignedBlocksSet.contains(i)) {
				this.assignedBlocksSet.add(i);
				return i;
			}
		}
		return NO_MORE_BLOCK_AVAILABLE;
	}
	
	private synchronized void finalizeBlockNumber(int blockNumber) {
		this.writedBlocksSet.add(blockNumber);
	}
	
	private synchronized void unassignBlockNumber(int blockNumber) {
		this.assignedBlocksSet.remove(blockNumber);
	}

	/**
	 * Process a new block
	 * Warning, visitor processing not thread safe ! (has to be for perfs)
	 * @param BlockProcessor - Implementation, how to process the block
	 * @throws BlockProcessorException
	 */
	public void processBlock(BlockProcessorVisitor visitor) throws BlockProcessorException {
		int blockNumber = this.assignNewEmptyBlockNumber();
		if (blockNumber == NO_MORE_BLOCK_AVAILABLE) { return; }
		
		try {
			visitor.process(blockNumber);
			this.finalizeBlockNumber(blockNumber);
		} catch (BlockProcessorException e) {
			unassignBlockNumber(blockNumber);
			throw new BlockProcessorException("Processing of block " + blockNumber + " failed", e);
		}
	}
}

