package p2pRes.testTools;

import java.util.concurrent.Executors;

public abstract class SimpleTestRunner<T> {
	private Exception error;
	private T resultValue;
	private Object _lock = new Object();
	
	abstract public T toBeRun() throws Exception;
	
	public SimpleTestRunner() {
		error = null;
		resultValue = null;
		Executors.newSingleThreadExecutor().execute(new Runnable() {
			public void run() {
				try {
					synchronized(_lock) { resultValue = toBeRun(); }
				} 
				catch (Exception e) { synchronized(_lock) { error = e; } } 
			}
		});		
	}

	public boolean hasResult() { return resultValue!=null; } //!\ warning non sync method !
    public T getResult() { synchronized(_lock) {return resultValue;} }
	
    public boolean hasError() { return error!=null; } //!\ warning non sync method !
	public Exception getError() { synchronized(_lock) { return error; } }
}
