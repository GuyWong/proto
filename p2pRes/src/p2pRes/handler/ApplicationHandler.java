package p2pRes.handler;

import java.util.concurrent.Executors;
import p2pRes.conf.Config;
import p2pRes.handler.model.Command;
import p2pRes.net.client.Client;
import p2pRes.net.client.ClientException;
import p2pRes.net.server.Server;
import p2pRes.ui.UIMain;

/**
 * Singleton
 */
public class ApplicationHandler {
	private static ApplicationHandler instance = null;
	
	private CommandHandler commandHandler = new CommandHandler();
	private ConfigurationHandler configurationHandler = new ConfigurationHandler();

	public static synchronized ApplicationHandler getInstance() {
		if (instance==null) {
			instance = new ApplicationHandler();
		}
		return instance;
	}
	
	private ApplicationHandler() {		
		Executors.newSingleThreadExecutor().execute(new Server(configurationHandler.getConfig().getApplicationPort(), 
																configurationHandler.getConfig().getSharedRepository(), 
																configurationHandler.getConfig().getOutPath()));
		
		Executors.newSingleThreadExecutor().execute(new Runnable() { //FIXME; Implements a producer/consumer instead
			public void run() {
				try {
					while (true) {
						if (commandHandler.hasWaitingCommand()) {
							handleCommand(commandHandler.popOutFileTransferCmd());
						}	
					}
				} 
				catch (Exception e) {  } 
			}
		});	
	}
	
	private void handleCommand(Command command) { //do it in the command handler
		try {
			(new Client(configurationHandler.getConfig().getClientUrl(), 
						configurationHandler.getConfig().getClientPort())).sendFile(command.getFilePath());
		} catch (ClientException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	public void register(UIMain uiMain) {
		uiMain.register(commandHandler);
		uiMain.register(configurationHandler);
	}
	
	public CommandHandler getCommandHandler() {
		return commandHandler;
	}
	
	public Config getConfig() {
		return configurationHandler.getConfig();
	}
}
