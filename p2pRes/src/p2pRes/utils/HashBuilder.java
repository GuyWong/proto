package p2pRes.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashBuilder {
	private final static String ALGO = "SHA-256";
	
	private final byte[] message;
	
	public HashBuilder(byte[] message) {
		this.message = message;
	}
 
    public String build() {
        return hash(message, ALGO);
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
 
    private String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return stringBuffer.toString();
    }
}
