package p2pRes.ui;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import org.apache.pivot.collections.Map;
import org.apache.pivot.io.FileList;
import org.apache.pivot.wtk.Application;
import org.apache.pivot.wtk.Component;
import org.apache.pivot.wtk.Display;
import org.apache.pivot.wtk.DropAction;
import org.apache.pivot.wtk.DropTarget;
import org.apache.pivot.wtk.ImageView;
import org.apache.pivot.wtk.Manifest;
import org.apache.pivot.wtk.Window;
import p2pRes.handler.CommandHandler;
import p2pRes.handler.ConfigurationHandler;
import p2pRes.handler.ApplicationHandler;
import p2pRes.handler.model.Command;

public class UIMain implements Application  {
	private CommandHandler commandHandler = null;
	private ConfigurationHandler configurationHandler = null;
	
	private Window window = new Window();
	private ImageView image = new ImageView();
	
	public void startup(Display display, Map<String, String> properties) throws Exception {
		ApplicationHandler.getInstance().register(this);

		if (commandHandler==null) {
			throw new UIException("Command handler not registered");
		}
		if (configurationHandler==null) {
			throw new UIException("Configuration handler not registered");
		}
		
		
		image.setImage("/p2pRes/ui/vortex.jpg");
		
		image.setDropTarget(new DropTarget() {
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
        });
		
		
		
		window.setTitle("ProtoUI");
        window.setMaximized(true);
        window.setContent(image);
        window.open(display);
        
        //image.setDragSource(dragSource);
	}
	
	public boolean shutdown(boolean optional) throws Exception {
		window.close();
		return true;
	}

	public void suspend() throws Exception {}

	public void resume() throws Exception {}
	
	public void register(CommandHandler commandHandler) {
		this.commandHandler = commandHandler;
	}
	
	public void register(ConfigurationHandler configurationHandler) {
		this.configurationHandler = configurationHandler;
	}
}
