package p2pRes.handler;

import org.apache.pivot.collections.ArrayList;

public class ErrorHandler {
	private ArrayList<Exception> errors = new ArrayList<Exception>(); 
	
	public synchronized void addError(Exception e) {
		errors.add(e);
	}
}
