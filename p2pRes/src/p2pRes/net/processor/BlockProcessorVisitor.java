package p2pRes.net.processor;

public interface BlockProcessorVisitor {
	abstract void process(int blockNumber) throws BlockProcessorException;
}
