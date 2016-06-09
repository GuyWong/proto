package p2pRes.protocol.response;

public class AskForBlock extends ProtocolResponse {

	private final int blockNumber;
	
	public AskForBlock(int blockNumber) {
		super(Command.ASK_FOR_BLOCK);
		this.blockNumber = blockNumber;
	}

	public int getBlockNumber() {
		return blockNumber;
	}
}
