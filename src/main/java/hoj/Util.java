package hoj;

import java.io.ObjectInputStream;

public class Util {
 
	public static byte[] toBytes(int x) {
		int zero = 48; // Character 0 in ascii
		int len = 0;
		for(int a = x; a > 0; a /= 10)
			len++;
		byte[] b = new byte[len];
		int dec = 0;
		for(int i = 0; i < len; i++) {
			int exp = len - 1;
			int tmp = x / (int)Math.pow(10, exp);
			b[i] = (byte)(zero + tmp - dec);
			x -= tmp * (int)Math.pow(10, exp); 
		}
		printBytes(b);
		System.out.println(len);
		return b;
	}
	
	public static void printBytes(byte[] v) {
		System.out.print("[");
		for(byte b : v)
			System.out.print(" " + b);
		System.out.print(" ]\n");
	}
	
	// returns ret = int[2] array where ret[0] == 1 ? success : timed out 
	// ret[1] is return value
	public static int[] timedRead(ObjectInputStream in, int timeOut) {
		int[] ret = new int[2];
		Timer timer = new Timer();
		Aread ar = new Aread(in);
		timer.start();
		ar.start();
		ret[0] = 1;
		while(!ar.isReady()) {
			if(timer.getTime() > timeOut) {
				ret[0] = 0;
				break;
			}
		}
		timer.quit();
		ret[1] = ar.getRet();;
		return ret;
	}
	
	
}
