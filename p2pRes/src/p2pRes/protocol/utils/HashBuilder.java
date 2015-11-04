package p2pRes.protocol.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import p2pRes.protocol.ProtocolException;

public class HashBuilder {
    public static String generateMD5(byte[] message) throws ProtocolException {
        return hash(message, "MD5");
    }
 
    public static String generateSHA1(byte[] message) throws ProtocolException {
        return hash(message, "SHA-1");
    }
 
    public static String generateSHA256(byte[] message) throws ProtocolException {
        return hash(message, "SHA-256");
    }
 
    private static String hash(byte[] message, String algorithm) throws ProtocolException {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm);
            byte[] hashedBytes = digest.digest(message);
            return convertByteArrayToHexString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new ProtocolException("Could not generate hash from String", e);
        }
    }
 
    private static String convertByteArrayToHexString(byte[] arrayBytes) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < arrayBytes.length; i++) {
            stringBuffer.append(Integer.toString((arrayBytes[i] & 0xff) + 0x100, 16).substring(1));
        }
        return stringBuffer.toString();
    }
}
