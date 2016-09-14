package p2pRes.net.io.protocol.model;

public abstract class ProtocolResponse {
	public enum Command {ASK_FOR_FILEDEFINITION, ASK_OPEN_FILEPUSH_CHANNEL, ASK_FOR_BLOCK, PUSH_BLOCK, ASK_NEWCONNECTION, ASK_ENDCONNECTION, UNKNOWN_COMMAND}
		
	private final Command command;
	
	public ProtocolResponse(Command command) {
		this.command = command;
	}
	
	public Command getCommand() {
		return command;
	}
}
