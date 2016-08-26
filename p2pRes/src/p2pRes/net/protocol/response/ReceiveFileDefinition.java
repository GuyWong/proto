package p2pRes.net.protocol.response;

import p2pRes.model.FileDescriptor;

public class ReceiveFileDefinition extends ProtocolResponse {
	private FileDescriptor fileDescriptor;
	
	public ReceiveFileDefinition(FileDescriptor fileDescriptor) {
		super(Command.PUSH_FILE_DESCRIPTOR);
		this.fileDescriptor = fileDescriptor;
	}

	public FileDescriptor getFileDescriptor() {
		return fileDescriptor;
	}
}
