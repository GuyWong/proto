package p2pRes.protocol.response;

public class AskForBlock extends ProtocolResponse {

	private final long blockNumber;
	
	public AskForBlock(long blockNumber) {
		super(Command.ASK_FOR_BLOCK);
		this.blockNumber = blockNumber;
	}

	public long getBlockNumber() {
		return blockNumber;
	}
}
