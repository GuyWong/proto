package p2pRes.model;

import p2pRes.BaseException;

public class FileHandlerException extends BaseException {
	private static final long serialVersionUID = -8696198903178268515L;
	
	public FileHandlerException(String err, Throwable e) {
		super(err, e);
	}
}
