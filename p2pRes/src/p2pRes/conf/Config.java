package p2pRes.conf;

public class Config {
	private int applicationPort;
	private String clientUrl;
	private int clientPort;
	private String outPath;
	private String sharedRepository;
	
	public int getApplicationPort() {
		return this.applicationPort;
	}
	
	public void setApplicationPort(int applicationPort) {
		this.applicationPort = applicationPort;
	}
	
	public String getClientUrl() {
		return this.clientUrl;
	}
	
	public void setClientUrl(String clientUrl) {
		this.clientUrl = clientUrl;
	}
	
	public int getClientPort() {
		return clientPort;
	}
	
	public void setClientPort(int clientPort) {
		this.clientPort = clientPort;
	}
	
	public String getOutPath() {
		return outPath;
	}
	
	public void setOutPath(String outPath) {
		this.outPath = outPath;
	}
	
	public String getSharedRepository() {
		return sharedRepository;
	}
	
	public void setSharedRepository(String sharedRepository) {
		this.sharedRepository = sharedRepository;
	}
}
