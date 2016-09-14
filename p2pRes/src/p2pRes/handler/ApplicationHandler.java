package p2pRes.handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import p2pRes.conf.Config;
import p2pRes.handler.model.Command;
import p2pRes.log.Logger;
import p2pRes.net.client.Client;
import p2pRes.net.client.ClientException;
import p2pRes.net.server.Server;
import p2pRes.ui.UIException;

/**
 * Singleton
 */
public class ApplicationHandler {
	private static ApplicationHandler instance = null;
	
	private ErrorHandler errorHandler = new ErrorHandler();
	private CommandHandler commandHandler = new CommandHandler();
	private ConfigurationHandler configurationHandler = new ConfigurationHandler(errorHandler);
	
	
	private ExecutorService serverService = null;
	private ExecutorService commandService = null;

	public static synchronized ApplicationHandler getInstance() {
		if (instance==null) {
			instance = new ApplicationHandler();
		}
		return instance;
	}
	
	private ApplicationHandler() {}
	
	public void startService() {
		if (serverService!=null) {
			this.endService();
		}
		
		serverService = Executors.newSingleThreadExecutor();
		serverService.execute(new Server(Integer.parseInt(configurationHandler.getConfigValue(Config.ELEMENT_NAME.APPLICATION_PORT)), 
																configurationHandler.getConfigValue(Config.ELEMENT_NAME.SHARED_REPOSITORY), 
																configurationHandler.getConfigValue(Config.ELEMENT_NAME.RECEIVED_FILEPATH)));
		
		if (commandService==null) {
			commandService = Executors.newSingleThreadExecutor();
			commandService.execute(new Runnable() { //FIXME; Implements a producer/consumer instead
				public void run() {
					try {
						while (true) {
							if (commandHandler.hasWaitingCommand()) {
								handleCommand(commandHandler.popOutFileTransferCmd());
							}	
							handleErrors();
							Thread.sleep(100);
						}
					} 
					catch (Exception e) { errorHandler.addError(e); } 
					handleErrors();
				}
			});	
		}	
	}
	
	public void endService() {
		serverService.shutdown();
		serverService = null;
		commandService.shutdown();
		commandService = null;
	}
	
	public void handleErrors() {
		if (errorHandler.hasErrors()) {
			Logger.error(errorHandler.toString());
			errorHandler.clear();
		}
	}
	
	private void handleCommand(Command command) throws UIException { //do it in the command handler
		try {
			(new Client(configurationHandler.getConfigValue(Config.ELEMENT_NAME.CLIENT_URL), 
						Integer.parseInt(configurationHandler.getConfigValue(Config.ELEMENT_NAME.CLIENT_PORT))))
					.sendFile(command.getFilePath());
		} catch (ClientException e) {
			throw new UIException("Error sendig command " + command, e);
		}
	}
	
	public CommandHandler getCommandHandler() {
		return commandHandler;
	}
	
	public ConfigurationHandler getConfigurationHandler() {
		return configurationHandler;
	}
	
	public ErrorHandler getErrorHandler() {
		return errorHandler;
	}
}
