package hoj;

import java.io.IOException;
import java.io.ObjectInputStream;

// Asynchronous reading
// One instance of this object can read only once
public class Aread extends Thread {
	private boolean ready;
	private volatile int ret;
	ObjectInputStream in;
	public Aread(ObjectInputStream i) {
		in = i;
		ready = false;
	}
	@Override
	public void run() {
		try {
			ret = in.readInt();
			ready = true;
		} catch (IOException e) {
			System.out.println("AREAD error");
			e.printStackTrace();
			ready = true; // needs proper way of handling error
			return;
			
		}
	}
	public boolean isReady() {
		return ready;
	}
	public int getRet() {
		return ret;
	}
}
