package hoj;

import java.io.IOException;
import java.io.ObjectInputStream;

public class Aread extends Thread {
	public boolean newVal = false;
	public int ret;
	ObjectInputStream in;
	public Aread(ObjectInputStream i) {
		in = i;
	}
	@Override
	public void run() {
		try {
			ret = in.readInt();
		} catch (IOException e) {
			System.out.println("AREAD ERROR io error");
			return;
		}
		newVal = true;
	}
}
