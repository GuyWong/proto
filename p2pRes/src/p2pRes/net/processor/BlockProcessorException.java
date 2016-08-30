package p2pRes.net.processor;

import p2pRes.BaseException;

public class BlockProcessorException extends BaseException {
	private static final long serialVersionUID = -8696198903178268515L;
	
	public BlockProcessorException(String err) {
		super(err);
	}
	
	public BlockProcessorException(String err, Throwable e) {
		super(err, e);
	}
}
