package p2pRes.net.io;

import p2pRes.BaseException;

public class ChannelException extends BaseException {
	private static final long serialVersionUID = -2218143174180035758L;

	public ChannelException(String err) {
		super(err);
	}
	
	public ChannelException(String err, Exception e) {
		super(err);
	}
}
