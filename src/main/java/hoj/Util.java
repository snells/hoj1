package hoj;

import java.nio.ByteBuffer;

public class Util {

	// int = 32bit 
	public static byte[] toBytes(int x) {
		return ByteBuffer.allocate(4).putInt(x).array();
	}
}
