package p2pRes.net.io;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;
import p2pRes.log.Logger;
import p2pRes.model.Block;
import p2pRes.model.FileDescriptor;
import p2pRes.net.io.protocol.ProtocolException;
import p2pRes.net.io.protocol.ServerProtocol;
import p2pRes.net.io.protocol.model.ProtocolResponse;

public class ServerChannel {
	private BindedPorts bindedPorts = BindedPorts.getInstance();	
	
	private final int port;
	
	private ServerSocket server;
	private ServerProtocol serverProtocol = null;
		
	public ServerChannel(int askedPort) throws ChannelException {
		this.port = bindedPorts.bindNewPort(askedPort);
		try {
			this.server = new ServerSocket(this.port);
		} catch (IOException e) {
			throw new ChannelException("Error opening new channel", e);
		}
	}
	
	/**
	 * Wait until a new client connect, return current channel instance
	 * */
	public ServerChannel waitForClientConnection() throws ChannelException {
		try {
			if (serverProtocol!=null) { serverProtocol.close (); }
			serverProtocol = new ServerProtocol(server.accept());

			return this;
		} catch (ProtocolException e) {
			throw new ChannelException("Error initiating protocol", e);
		} catch (IOException e) {
			throw new ChannelException("Error getting client connection", e);
		}
	}
	
	public ProtocolResponse waitForClientCommand() throws ChannelException {
		this.checkConnection();
		try {
			return serverProtocol.handleInstruction();
		} catch (ProtocolException e) {
			throw new ChannelException("Error receiving client command", e);
		}
	}
	
	public void sendBlock(Block block) throws ChannelException {
		this.checkConnection();
		try {
			serverProtocol.sendBlock(block.getValue());
		} catch (ProtocolException e) {
			throw new ChannelException("Error sending block", e);
		}
	}
	
	public ServerChannel openSubChannel() throws ChannelException {
		this.checkConnection();
		ServerChannel subChannel = new ServerChannel(port);
		Logger.info("Server - Opening new connection - port " + subChannel.getPort());  
		try {
			serverProtocol.sendPortNumber(subChannel.getPort());
		} catch (ProtocolException e) {
			throw new ChannelException("Error opening new connection", e);
		}
		return subChannel;
	}
	
	public void sendFileDescriptor(FileDescriptor fileDescriptor) throws ChannelException {
		this.checkConnection();
		try {
			serverProtocol.sendFileDescriptor(fileDescriptor);
		} catch (ProtocolException e) {
			throw new ChannelException("Error sending file descriptor " + fileDescriptor.getFileName(), e);
		}
	}
	
	public void close() throws ChannelException {
		try {
			if (serverProtocol!=null) { serverProtocol.close (); }
			server.close();
		} catch (IOException e) {
			throw new ChannelException("Error closing channel", e);
		} catch (ProtocolException e) {
			throw new ChannelException("Error closing channel", e);
		}
		Logger.debug("Closing ok, unbinding port " + port);
		bindedPorts.unbind(port);
	}
	
	public String toString() {
		return "ServerChannel@" + server.getLocalSocketAddress();
	}
	
	public int getPort() {
		return port;
	}
	
	private void checkConnection() throws ChannelException {
		if (serverProtocol==null) { throw new ChannelException("No active current connection"); }
	}
	
	private static class BindedPorts {
		private static BindedPorts _instance = null;
		
		private Object lock = new Object();
		private Set<Integer> ports = new HashSet<Integer>();

		public static synchronized BindedPorts getInstance() {
			if (_instance == null) {
				_instance = new BindedPorts();
			}
			return _instance;
		}
		
		private BindedPorts() {}
		
		public int bindNewPort(int startingPort){
			synchronized(lock) {
				int candidatePort = startingPort;
				while (ports.contains(candidatePort)) {
					candidatePort++;
				}
				
				ports.add(candidatePort);
				return candidatePort;
			}
		}
		
		public void unbind(int portNumber){
			synchronized(lock) {
				ports.remove(portNumber);
			}
		}
	}
}
