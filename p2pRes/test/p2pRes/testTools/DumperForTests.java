package p2pRes.testTools;

public class DumperForTests {
	public static String dump(byte[] bytes) {
		StringBuffer strBuf = new StringBuffer("{");
		for (int i=0; i<bytes.length-1; i++) {
			strBuf.append(bytes[i]+",");
		}
		strBuf.append(bytes[bytes.length-1]+"}");
		
		return strBuf.toString();
	}
}
