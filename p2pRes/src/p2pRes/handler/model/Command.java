package p2pRes.handler.model;

public class Command {
	private String url;
	private int port;
	private String filePath;
	
	public Command(String url, int port, String filePath) {
		this.url = url;
		this.port = port;
		this.filePath = filePath;
	}

	public String getUrl() {
		return url;
	}
	
	public int getPort() {
		return port;
	}

	public String getFilePath() {
		return filePath;
	}
}
