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
import p2pRes.handler.ApplicationHandler;
import p2pRes.handler.model.Command;

public class UIMain extends Window implements Bindable {
	private CommandHandler commandHandler = null;
	private ConfigurationHandler configurationHandler = null;
	
	@BXML private TextArea sharedRepoText;
	@BXML private TextArea outPathText;
	@BXML private TextArea appPortText;
	@BXML private TextArea clientUrlText;
	@BXML private TextArea clientPortText;
	@BXML private ImageView teleportVortexImage;
	
    public void initialize(Map<String, Object> namespace, URL location, Resources resources) {       
        teleportVortexImage.setDropTarget(new TeleportDropTarget());
	}
    
    public void register(UIRunner uiRunner) {
    	this.configurationHandler = uiRunner.getConfigurationHandler();
    	this.commandHandler = uiRunner.getCommandhandler();
    	
        sharedRepoText.setText(configurationHandler.getConfig().getSharedRepository());
        sharedRepoText.getTextAreaContentListeners().add(new TextAreaContentListener() {
			public void paragraphInserted(TextArea textArea, int index) {}
			public void paragraphsRemoved(TextArea textArea, int index, Sequence<Paragraph> removed) {}
			public void textChanged(TextArea textArea) {
				configurationHandler.getConfig().setSharedRepository(textArea.getText());
				configurationHandler.updateConfig();
			}});
        
        outPathText.setText(configurationHandler.getConfig().getOutPath());
        outPathText.getTextAreaContentListeners().add(new TextAreaContentListener() {
			public void paragraphInserted(TextArea textArea, int index) {}
			public void paragraphsRemoved(TextArea textArea, int index, Sequence<Paragraph> removed) {}
			public void textChanged(TextArea textArea) {
				configurationHandler.getConfig().setOutPath(textArea.getText());
				configurationHandler.updateConfig();
			}});
        
        appPortText.setText(""+configurationHandler.getConfig().getApplicationPort());
        appPortText.getTextAreaContentListeners().add(new TextAreaContentListener() {
			public void paragraphInserted(TextArea textArea, int index) {}
			public void paragraphsRemoved(TextArea textArea, int index, Sequence<Paragraph> removed) {}
			public void textChanged(TextArea textArea) {
				configurationHandler.getConfig().setApplicationPort(Integer.parseInt(textArea.getText()));
				configurationHandler.updateConfig();
			}});
        
        clientUrlText.setText(configurationHandler.getConfig().getClientUrl());
        clientUrlText.getTextAreaContentListeners().add(new TextAreaContentListener() {
			public void paragraphInserted(TextArea textArea, int index) {}
			public void paragraphsRemoved(TextArea textArea, int index, Sequence<Paragraph> removed) {}
			public void textChanged(TextArea textArea) {
				configurationHandler.getConfig().setClientUrl(textArea.getText());
				configurationHandler.updateConfig();
			}});
        
        clientPortText.setText(""+configurationHandler.getConfig().getClientPort());
        clientPortText.getTextAreaContentListeners().add(new TextAreaContentListener() {
			public void paragraphInserted(TextArea textArea, int index) {}
			public void paragraphsRemoved(TextArea textArea, int index, Sequence<Paragraph> removed) {}
			public void textChanged(TextArea textArea) {
				configurationHandler.getConfig().setClientPort(Integer.parseInt(textArea.getText()));
				configurationHandler.updateConfig();
			}});
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

            public void dragExit(Component component) {
                // empty block
            }

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
                DropAction dropAction = null;

                if (dragContent.containsFileList()) {
                	try {
						FileList fileList = dragContent.getFileList();
						
						for	(Iterator<File> it = fileList.iterator(); it.hasNext(); ) {
							commandHandler.pushInFileTransferCmd(new Command(ApplicationHandler.getInstance().getConfig().getClientUrl(), 
																				ApplicationHandler.getInstance().getConfig().getClientPort(), 
																				it.next().getAbsolutePath()));
						}
						
						dropAction = DropAction.COPY;
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
                }
                
                dragExit(component);

                return dropAction;
            }
	}
}
