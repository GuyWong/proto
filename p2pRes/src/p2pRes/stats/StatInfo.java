package p2pRes.stats;

public class StatInfo {
	private String statName;
	private long startTime;
	private long statTime = -1;//todo
	
	public StatInfo(String statName) {
		this.statName  = statName;
	}
	
	public String getCounterName() {
		return statName;
	}
	
	public void start() {
		startTime = System.currentTimeMillis();
	}
	
	public void end() {
		statTime = startTime - System.currentTimeMillis();
	}
	
	public long getStatTime() {
		return statTime;
	}
}
