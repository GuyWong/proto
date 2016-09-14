package p2pRes.net.io.protocol.model;

import p2pRes.model.Block;

public class PushBlock extends ProtocolResponse {
	private final int blockNumber;
	private Block block;
	
	public PushBlock(int blockNumber, Block block) {
		super(Command.PUSH_BLOCK);
		this.blockNumber = blockNumber;
		this.block = block;
	}

	public int getBlockNumber() {
		return blockNumber;
	}
	
	public Block getBlock() {
		return this.block;
	}
}
