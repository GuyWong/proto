package p2pRes.ui;

import org.apache.pivot.beans.BXMLSerializer;
import org.apache.pivot.collections.Map;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.Display;

import p2pRes.handler.ApplicationHandler;

public class UIRunner implements Application {
	private UIMain mainWindow;

	public void startup(Display display, Map<String, String> properties) throws Exception {
		ApplicationHandler.getInstance().startService();
		
		BXMLSerializer bxmlSerializer = new BXMLSerializer();
		mainWindow = (UIMain)bxmlSerializer.readObject(UIMain.class, "UIMain.bxml");

		mainWindow.open(display);
	}
	
	public boolean shutdown(boolean optional) throws Exception {
		ApplicationHandler.getInstance().endService();
		mainWindow.close();		
		return false;
	}

	public void suspend() throws Exception {}
	public void resume() throws Exception {}
}
