package p2pRes.protocol.response;

public abstract class ProtocolResponse {
	public enum Command {ASK_FOR_FILEDEFINITION, ASK_FOR_BLOCK, ASK_ENDCONNECTION, UNKNOWN_COMMAND}
	
	
	private final Command command;
	
	public ProtocolResponse(Command command) {
		this.command = command;
	}
	
	public Command getCommand() {
		return command;
	}
}
