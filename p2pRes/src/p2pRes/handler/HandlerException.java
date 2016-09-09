package p2pRes.handler;

import p2pRes.BaseException;

public class HandlerException extends BaseException {
	private static final long serialVersionUID = -4772079978292765456L;
	
	public HandlerException(String err) {
		super(err);
	}
	
	public HandlerException(String err, Exception e) {
		super(err, e);
	}
}
