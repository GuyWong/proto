package p2pRes.common;

public class StaticsValues { //FIXME: Temporary, vars had to be parametrized
	public final static int MAX_CLIENT_CONNECTION = 5;//to be parametrized
	
	public static final int BLOC_SIZE = 1024*100;//To be parametized //100ko seems the best
	  //100ko = 214748to max file size
	  //1ko = 2147to max file size
	
	public final static String HARD_CODED_URL = "127.0.0.1"; //TO BE REMOVED
	public final static int HARD_CODED_PORT = 6667; //TO BE REMOVED
	
	public final static String HARD_CODED_SHAREDPATH = "C://Utils//dev//test//";
	public final static String HARD_CODED_SRVOUTPATH = "C://Utils//dev//test////srvout//";
}
