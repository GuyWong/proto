package p2pRes.net.io.protocol.model;



public class AskForFileDefinition extends ProtocolResponse {
	private String fileName;
	
	public AskForFileDefinition(String fileName) {
		super(Command.ASK_FOR_FILEDEFINITION);
		this.fileName = fileName;
	}

	public String getFileName() {
		return fileName;
	}
}
