package p2pRes.net.io.protocol;

import p2pRes.BaseException;

public class ProtocolException extends BaseException {
	private static final long serialVersionUID = -4763771202660517558L;

	public ProtocolException(Throwable e) {
		super(e);
	}
	
	public ProtocolException(String err) {
		super(err);
	}
	
	public ProtocolException(String err, Throwable e) {
		super(err, e);
	}
}
