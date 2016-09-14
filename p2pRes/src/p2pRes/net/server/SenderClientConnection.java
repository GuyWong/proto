package p2pRes.net.server;

import p2pRes.log.Logger;
import p2pRes.model.Block;
import p2pRes.model.FileException;
import p2pRes.model.TransferableFile;
import p2pRes.net.io.ChannelException;
import p2pRes.net.io.ServerChannel;
import p2pRes.net.io.protocol.model.AskForBlock;
import p2pRes.net.io.protocol.model.ProtocolResponse;

public class SenderClientConnection extends ClientConnection {
	private TransferableFile transferableFile;
	
	public SenderClientConnection(ServerChannel serverChannel,
									TransferableFile transferableFile) throws ServerException {
		super(serverChannel);
		this.transferableFile = transferableFile;
	}

	public void run() {
		try {
			while (true) {
				ProtocolResponse response = this.getServerChannel().waitForClientCommand();
				if (ProtocolResponse.Command.ASK_FOR_BLOCK == response.getCommand()) {
					try {
						this.getServerChannel().sendBlock(new Block(transferableFile.readBlock(((AskForBlock)response).getBlockNumber())));
					} catch (FileException e) {
					} catch (ChannelException e) {
						e.printStackTrace();// TODO handle with error collector
					}
				}
				if (ProtocolResponse.Command.ASK_ENDCONNECTION == response.getCommand()) {
					Logger.info("Ending server connection...");
					break;
				}
				//handle the unknown command case
			}
		}
		catch (ChannelException e) {
			//TODO: everything is fatal for now, handle a more subtle return status
			e.printStackTrace();
		}
		finally {
			try {
				Logger.info("Closing SenderClientConnection... " + this.getServerChannel());
				this.getServerChannel().close();
			} catch (ChannelException e) {
				e.printStackTrace();// TODO Auto-generated catch block
			}
		}
	}
}
