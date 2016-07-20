package p2pRes.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SuppressWarnings("unused")
public class HashBuilder {
	private final static String ALGO_SHA1 = "SHA-1";
	private final static String ALGO_SHA256 = "SHA-256";
	private final static String ALGO_SHA2 = "SHA-2";
	private final static String ALGO_SKEIN512 = "SKEIN-512";
	
	private final static String ALGO = ALGO_SKEIN512;
	
	private final byte[] message;
	
	public HashBuilder(byte[] message) {
		this.message = message;
	}
 
    public String build() {
    	if (ALGO_SKEIN512.equals(ALGO)) {
    		return hashSkein512(message);
    	}
    	return hash(message, ALGO);
    }
    
    public String buildSkein512() {
        return hashSkein512(message);
    }
    
    private String hash(byte[] message, String algorithm) {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            digest.reset();
            byte[] hashedBytes = digest.digest(message);
            return convertByteArrayToHexString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }
    
    private String hashSkein512(byte[] message) {
    	byte[] digest = new byte[64];
    	Skein512.hash(message, digest);
    	return convertByteArrayToHexString(digest);
    }
 
    private String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return stringBuffer.toString();
    }
}
