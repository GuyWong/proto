package p2pRes.handler;

import org.apache.pivot.collections.ArrayList;

public class ErrorHandler {
	private ArrayList<Exception> errors = new ArrayList<Exception>(); 
	
	public synchronized void addError(Exception e) {
		errors.add(e);
	}
	
	public synchronized boolean hasErrors() {
		return !errors.isEmpty();
	}
	
	public synchronized void clear() {
		errors.clear();
	}
	
	public synchronized String toString() {
		StringBuffer stringBuffer = new StringBuffer();
		for (Exception e : errors) {
			stringBuffer.append(e.getMessage()).append("\n");
		}
		return stringBuffer.toString();
	}
}
