package p2pRes.model;

import java.io.Serializable;
import p2pRes.utils.HashBuilder;

public class Block implements Serializable  {
	private static final long serialVersionUID = -3184058293974299717L;
	
	private final byte[] value;
	private final String hash;
	
	public Block(byte[] value) { 
		this.value = value;
		this.hash = (new HashBuilder(value)).build();
	}
	
	public boolean checkHash() {
		return hash.equals((new HashBuilder(value)).build());
	}
	
	public byte[] getValue() {
		return value;
	}
	
	protected String getHashValue() {
		return hash;
	}
}
