package p2pRes.conf;

import java.util.HashMap;
import java.util.Map;

public class Config {
	public static enum ELEMENT_NAME {APPLICATION_PORT, CLIENT_URL, CLIENT_PORT, RECEIVED_FILEPATH, SHARED_REPOSITORY};
	
	private Map<ELEMENT_NAME, String> config = new HashMap<ELEMENT_NAME, String>();
	
	public String getValue(ELEMENT_NAME element) {
		return config.get(element);
	}
	
	public void setValue(ELEMENT_NAME element, String value) {
		config.put(element, value);
	}
}
