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
	
	public ConfigurationHandler() {
		if (!(new File(FILECONFIGURATION_NAME)).exists()) {
			config = createDefault();
			writeConfig(FILECONFIGURATION_NAME, config);
		}
		else {
			config = loadConfig(FILECONFIGURATION_NAME);
		}
	}
	
	public Config getConfig() {
		return config;
	}
	
	public void updateConfig() {
		writeConfig(FILECONFIGURATION_NAME, config);
	}
	
	private Config loadConfig(String path) {
		try {
			FileReader reader = new FileReader(path);
			String read = new String(reader.read(0, (int)reader.getFileSize()));
			
			Gson gson = new GsonBuilder().create();
			return gson.fromJson(read, Config.class);
			
		} catch (ReaderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	}
	
	private void writeConfig(String path, Config config) {
		Gson gson = new GsonBuilder().create();
		String serializedConf = gson.toJson(config);
		
		try {
			FileWriter writer = new FileWriter(path, true);
			writer.write(0, serializedConf.getBytes());		
		} catch (WriterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Config createDefault() {
		Config config = new Config(); //TODO
		config.setApplicationPort(6667);
		config.setClientUrl("127.0.0.1");
		config.setClientPort(6667);
		config.setSharedRepository("./");
		config.setOutPath("./");
		return config;
	}
}
