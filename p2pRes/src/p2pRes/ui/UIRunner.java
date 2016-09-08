package p2pRes.ui;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.Display;
import p2pRes.handler.ApplicationHandler;
import p2pRes.handler.CommandHandler;
import p2pRes.handler.ConfigurationHandler;

public class UIRunner implements Application   {
	private CommandHandler commandHandler = null;
	private ConfigurationHandler configurationHandler = null;
	private UIMain mainWindow;

	public void startup(Display display, Map<String, String> properties) throws Exception {
		ApplicationHandler.getInstance().register(this);
		if (commandHandler==null) {
			throw new UIException("Command handler not registered");
		}
		if (configurationHandler==null) {
			throw new UIException("Configuration handler not registered");
		}	
		
		BXMLSerializer bxmlSerializer = new BXMLSerializer();
		mainWindow = (UIMain)bxmlSerializer.readObject(UIMain.class, "UIMain.bxml");
		mainWindow.register(this);
		
		mainWindow.open(display);
	}

	public void register(CommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}
	
	public void register(ConfigurationHandler configurationHandler) {
		this.configurationHandler = configurationHandler;
	}
	
	public CommandHandler getCommandhandler() {
		return commandHandler;
	}
	
	public ConfigurationHandler getConfigurationHandler() {
		return configurationHandler;
	}
	
	public boolean shutdown(boolean optional) throws Exception {
		mainWindow.close();
		return true;
	}

	public void suspend() throws Exception {
		// TODO Auto-generated method stub
		
	}

	public void resume() throws Exception {
		// TODO Auto-generated method stub
		
	}
}
