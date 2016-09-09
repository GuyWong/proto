package p2pRes.ui;

import p2pRes.BaseException;

public class UIException extends BaseException {
	private static final long serialVersionUID = -2618084275303086056L;
	
	public UIException(String err) {
		super(err);
	}
	
	public UIException(String err, Exception e) {
		super(err, e);
	}
}
