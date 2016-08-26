package p2pRes.net.client;

import p2pRes.BaseException;

public class ClientException extends BaseException {
	private static final long serialVersionUID = 9009847716220972319L;
	
	public ClientException(String err) {
		super(err);
	}
	
	public ClientException(String err, Throwable e) {
		super(err, e);
	}
}
