package p2pRes.handler;

import java.io.File;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import p2pRes.conf.Config;
import p2pRes.io.FileReader;
import p2pRes.io.FileWriter;
import p2pRes.io.ReaderException;
import p2pRes.io.WriterException;

public class ConfigurationHandler {
	private final static String FILECONFIGURATION_NAME = "teleport.config";
	
	private Config config;
	
	public ConfigurationHandler(ErrorHandler errorHandler) {
		if (!(new File(FILECONFIGURATION_NAME)).exists()) {
			config = createDefault();
			try {
				writeConfig(FILECONFIGURATION_NAME, config);
			} catch (HandlerException e) {
				errorHandler.addError(new HandlerException("Error initializing config file", e));
			}
		}
		else {
			try {
				config = loadConfig(FILECONFIGURATION_NAME);
			} catch (HandlerException e) {
				config = createDefault();
				errorHandler.addError(new HandlerException("Can't load the configuration file, setting default Value", e));
			}
		}
	}

	public String getConfigValue(Config.ELEMENT_NAME elementName) { return config.getValue(elementName); }
	
	public void updateElement(Config.ELEMENT_NAME elementName, String value) throws HandlerException {
		this.config.setValue(elementName, value);
		this.writeConfig(FILECONFIGURATION_NAME, config);
	}
	
	private Config loadConfig(String path) throws HandlerException {
		try {
			FileReader reader = new FileReader(path);
			String read = new String(reader.read(0, (int)reader.getFileSize()));
			
			Gson gson = new GsonBuilder().create();
			return gson.fromJson(read, Config.class);		
		} catch (ReaderException e) {
			throw new HandlerException("Error reading config file " + path, e);
		}		
	}
	
	private void writeConfig(String path, Config config) throws HandlerException {
		Gson gson = new GsonBuilder().create();
		String serializedConf = gson.toJson(config);
		
		try {
			FileWriter writer = new FileWriter(path, true);
			writer.write(0, serializedConf.getBytes());		
		} catch (WriterException e) {
			throw new HandlerException("Error writing config file " + path, e);
		}
	}
	
	private Config createDefault() {
		Config config = new Config();
		config.setValue(Config.ELEMENT_NAME.APPLICATION_PORT, ""+6667);
		config.setValue(Config.ELEMENT_NAME.CLIENT_URL, "127.0.0.1");
		config.setValue(Config.ELEMENT_NAME.CLIENT_PORT, ""+6667);
		config.setValue(Config.ELEMENT_NAME.SHARED_REPOSITORY, "./");
		config.setValue(Config.ELEMENT_NAME.RECEIVED_FILEPATH, "./");
		return config;
	}
}
