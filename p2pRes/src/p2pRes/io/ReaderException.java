package p2pRes.io;

import p2pRes.BaseException;

public class ReaderException extends BaseException {
	private static final long serialVersionUID = 2580254931032591423L;
	
	public ReaderException(String err, Throwable e) {
		super(err, e);
	}
}
