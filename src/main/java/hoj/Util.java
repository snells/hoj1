package hoj;

import java.io.ObjectInputStream;

public class Util {
	
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
