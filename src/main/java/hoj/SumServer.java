package hoj;

import java.net.Socket;

public class SumServer extends Server {
	public volatile int[] locker;
	int maxLen;
	volatile int i = 0;
	int id;
	public SumServer() {}
	public SumServer(int index, int[] v) {
		id = index;
		locker = v;
		maxLen = v.length;
	}
	public void serve() {
		int x = read();
		if(running == false) // die
			return;
		System.out.println("SumServer " + id + " read number: " + x);
		if(x == 0) {
			die();
			return;
		}
		if(i == maxLen)
			return;
		locker[i++] = x;
	}
	public int getSum() {
		int sum = 0;
		for(int n = 0; n < i; n++)
			sum += locker[n];
		return sum;
	}
	public int getNumCount() {
		return i;
	}
	@Override
	protected void closeMsg() {
		System.out.println("SumServer Closing server " + id);
	}
	@Override
	protected int read() {
		System.out.println("SERVER " + id + " READING");
		return super.read();
	}
	@Override
	protected void die() {
		//System.out.println("SERVER " + id + " DIE");
		super.die();
	}
	@Override
	protected int getServerId() {
		return id;
	}
}
