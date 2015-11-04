package p2pRes.server;

import p2pRes.BaseException;

public class ServerException extends BaseException {
	private static final long serialVersionUID = -1592454898703672914L;
	
	public ServerException(String err, Throwable e) {
		super(err, e);
	}
}
