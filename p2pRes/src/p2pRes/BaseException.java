package p2pRes;

public class BaseException extends Exception { //Find me a nice name
	private static final long serialVersionUID = 1664366149999390234L;

	public BaseException(Throwable e) {
		super(e);
	}
	
	public BaseException(String err) {
		super(err);
	}
	
	public BaseException(String err, Throwable e) {
		super(err, e);
	}
}
