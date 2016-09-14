package p2pRes.net.server;

import p2pRes.net.io.ChannelException;
import p2pRes.net.io.ServerChannel;

public abstract class ClientConnection implements Runnable {
	private ServerChannel serverChannel;
	
	public ClientConnection(ServerChannel serverChannel) throws ServerException {
		this.serverChannel = serverChannel;
		try {
			serverChannel.waitForClientConnection();
		} catch (ChannelException e) {
			throw new ServerException("Error on client connection", e);
		}
	}
	
	protected ServerChannel getServerChannel() {
		return serverChannel;
	}
}