package p2pRes.stats;

import java.util.HashMap;
import java.util.Map;

public class Stats {
	private Map<String, StatInfo> stats = new HashMap<String, StatInfo>();
	
	public StatInfo getNewCounter(String counterName) {
		StatInfo instance = new StatInfo(counterName);
		stats.put(instance.getCounterName(), instance);
		return instance;
	}
}
