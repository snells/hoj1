package hoj;


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
}
