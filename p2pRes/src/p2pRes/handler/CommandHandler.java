package p2pRes.handler;

import java.util.LinkedList;
import java.util.List;
import p2pRes.handler.model.Command;

public class CommandHandler { //TODO synchronize me !!!!!
	private List<Command> commandList = new LinkedList<Command>();
	
	public synchronized void pushInFileTransferCmd(Command fileTransferCommand) {
		commandList.add(fileTransferCommand);
	}
	
	public synchronized Command popOutFileTransferCmd() { //TODO use a blocking queue ?
		if (this.hasWaitingCommand()) {
			return commandList.remove(0);
		}
		return new Command("", 0, "");//TODO create type EMPTYFILETYRANSFERCMD
	}
	
	public synchronized boolean hasWaitingCommand() {
		return !commandList.isEmpty();
	}
	
}
