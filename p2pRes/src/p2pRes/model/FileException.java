package p2pRes.model;

import p2pRes.BaseException;

public class FileException extends BaseException {
	private static final long serialVersionUID = -8870891318205572511L;
	
	public FileException(String err, Throwable e) {
		super(err, e);
	}
}
