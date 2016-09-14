package p2pRes.net.io.protocol.model;

import p2pRes.model.FileDescriptor;

public class AskOpenFilePushChannel extends ProtocolResponse {
	private FileDescriptor fileDescriptor;
	
	public AskOpenFilePushChannel(FileDescriptor fileDescriptor) {
		super(Command.ASK_OPEN_FILEPUSH_CHANNEL);
		this.fileDescriptor = fileDescriptor;
	}

	public FileDescriptor getFileDescriptor() {
		return fileDescriptor;
	}
}
