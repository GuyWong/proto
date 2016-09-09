package p2pRes.ui;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import org.apache.pivot.beans.BXML;
import org.apache.pivot.beans.Bindable;
import org.apache.pivot.collections.Map;
import org.apache.pivot.collections.Sequence;
import org.apache.pivot.io.FileList;
import org.apache.pivot.util.Resources;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.DropAction;
import org.apache.pivot.wtk.DropTarget;
import org.apache.pivot.wtk.ImageView;
import org.apache.pivot.wtk.Manifest;
import org.apache.pivot.wtk.TextArea;
import org.apache.pivot.wtk.TextArea.Paragraph;
import org.apache.pivot.wtk.TextAreaContentListener;
import org.apache.pivot.wtk.Window;
import p2pRes.handler.CommandHandler;
import p2pRes.handler.ConfigurationHandler;
import p2pRes.conf.Config;
import p2pRes.handler.ApplicationHandler;
import p2pRes.handler.model.Command;

public class UIMain extends Window implements Bindable {
	private CommandHandler commandHandler = null;
	private ConfigurationHandler configurationHandler = null;
	
	@BXML private TextArea sharedRepoText;
	@BXML private TextArea receivedFilePathText;
	@BXML private TextArea appPortText;
	@BXML private TextArea clientUrlText;
	@BXML private TextArea clientPortText;
	@BXML private ImageView teleportVortexImage;
	
    public void initialize(Map<String, Object> namespace, URL location, Resources resources) {       
    	commandHandler = ApplicationHandler.getInstance().getCommandHandler();
    	configurationHandler = ApplicationHandler.getInstance().getConfigurationHandler();
    	
    	sharedRepoText.setText(configurationHandler.getConfigValue(Config.ELEMENT_NAME.SHARED_REPOSITORY));
        sharedRepoText.getTextAreaContentListeners().add(new ConfigTextAreaContentListener(Config.ELEMENT_NAME.SHARED_REPOSITORY));
        
        receivedFilePathText.setText(configurationHandler.getConfigValue(Config.ELEMENT_NAME.RECEIVED_FILEPATH));
        receivedFilePathText.getTextAreaContentListeners().add(new ConfigTextAreaContentListener(Config.ELEMENT_NAME.RECEIVED_FILEPATH));
        
        appPortText.setText(""+configurationHandler.getConfigValue(Config.ELEMENT_NAME.APPLICATION_PORT));
        appPortText.getTextAreaContentListeners().add(new ConfigTextAreaContentListener(Config.ELEMENT_NAME.APPLICATION_PORT));
        
        clientUrlText.setText(configurationHandler.getConfigValue(Config.ELEMENT_NAME.CLIENT_URL));
        clientUrlText.getTextAreaContentListeners().add(new ConfigTextAreaContentListener(Config.ELEMENT_NAME.CLIENT_URL));
        
        clientPortText.setText(""+configurationHandler.getConfigValue(Config.ELEMENT_NAME.CLIENT_PORT));
        clientPortText.getTextAreaContentListeners().add(new ConfigTextAreaContentListener(Config.ELEMENT_NAME.CLIENT_PORT));
    	
    	teleportVortexImage.setDropTarget(new TeleportDropTarget());  
	}
	
    private class ConfigTextAreaContentListener implements TextAreaContentListener {
    	private Config.ELEMENT_NAME elementName;
    	
    	public ConfigTextAreaContentListener(Config.ELEMENT_NAME elementName) { this.elementName = elementName; }
    	
		public void paragraphInserted(TextArea textArea, int index) {}
		public void paragraphsRemoved(TextArea textArea, int index, Sequence<Paragraph> removed) {}

		public void textChanged(TextArea textArea) {
			configurationHandler.updateElement(elementName, textArea.getText());
		}
    }
    
	private class TeleportDropTarget implements DropTarget {
        public DropAction dragEnter(Component component, Manifest dragContent,
                int supportedDropActions, DropAction userDropAction) {
                DropAction dropAction = null;

                if (dragContent.containsFileList()
                    && DropAction.COPY.isSelected(supportedDropActions)) {
                    dropAction = DropAction.COPY;
                }

                return dropAction;
            }

            public void dragExit(Component component) { }

            public DropAction dragMove(Component component, Manifest dragContent,
                int supportedDropActions, int x, int y, DropAction userDropAction) {
                return (dragContent.containsFileList() ? DropAction.COPY : null);
            }

            public DropAction userDropActionChange(Component component, Manifest dragContent,
                int supportedDropActions, int x, int y, DropAction userDropAction) {
                return (dragContent.containsFileList() ? DropAction.COPY : null);
            }

            public DropAction drop(Component component, Manifest dragContent,
            						int supportedDropActions, int x, int y, DropAction userDropAction) {
            if (dragContent.containsFileList()) {
            	try {
					FileList fileList = dragContent.getFileList();
					
					for	(Iterator<File> it = fileList.iterator(); it.hasNext(); ) {
						commandHandler.pushInFileTransferCmd(new Command(configurationHandler.getConfigValue(Config.ELEMENT_NAME.CLIENT_URL), 
																			Integer.parseInt(configurationHandler.getConfigValue(Config.ELEMENT_NAME.CLIENT_PORT)), 
																			it.next().getAbsolutePath()));
					}
				} catch (IOException e) {
					e.printStackTrace();// TODO Auto-generated catch block
				}
            }
            
            dragExit(component);

            return DropAction.COPY;
        }
	}
}
