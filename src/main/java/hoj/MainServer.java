package hoj;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class MainServer extends Server {
	Timer timer = new Timer();
	private boolean gotFirstNum = false;
	private int sumServCount;
	private int sumServPort = 9901;
	int sumMaxNumCount = 100;
	int id = 0;
	Tcp<SumServer>[] sumTcp;
	volatile int[][] locker;

	
	public MainServer() {}
	public MainServer(Socket s) {
		client = s;
	}
	
	@Override
	protected void servClose() {
		for(Tcp<SumServer> p : sumTcp) {
			try {
				p.close();
			} catch(Exception e) {
				System.out.println("ERROR closing server " + id);
				e.printStackTrace();
			}
		}
	}
	@SuppressWarnings("unchecked")
	private void sumServStart() {
		locker = new int[sumServCount][sumMaxNumCount];
		sumTcp = new Tcp[sumServCount];
		System.out.println("Starting sum servers");
		for(int i = 0; i < sumServCount; i++) {
			SumServer ss = new SumServer(i + 1, locker[i]); // Main servers id = 0, sum servers start from 1 
			sumTcp[i] = new Tcp<SumServer>(sumServPort + i, ss);
			sumTcp[i].start();
		}
	}
	public void serve() {
		if(!gotFirstNum) {
			System.out.println("Trying to get first number");
			int firstRet = read();
			if(running == false) // error reading, server dies
				return;
			System.out.println("First number is " + firstRet);
			gotFirstNum = true;
			sumServCount = firstRet;
			sumServStart();
			for(int i = 0; i < sumServCount; i++) // sending sum server ports
				write(i + sumServPort);
			timer.start();
		}
		
		if(timer.getTime() > 60) // no messages for minute so we die
			die();
		System.out.print("Server 0 Reading command: ... ");
		int x = read();
		if(running == false)
			return;
		System.out.println(x);
		timer.reset();  
		
		if(x == 0)
			die();
		
		else if(x == 1) {
			int sum = 0;
			for(int n = 0; n < sumServCount; n++) 
				sum += sumTcp[n].getServ().getSum(); // we could just take the values from locker
			System.out.println("Total sum " + sum);
			write(sum);
		}
		
		// Our indexing start at 0.
		// Client servers indexing starts from 1
		else if(x == 2) {
			int sum = 0;
			int servNum = 0;
			for(int n = 0; n < sumServCount; n++) {
				 int tmp = sumTcp[n].getServ().getSum();
				 if(tmp >= sum) {
					 sum = tmp;
					 servNum = n;
				 }
			}
			System.out.println("SumServer " + (servNum + 1) + " has biggest sum: " + sum);
			write(servNum + 1);
		}
		else if(x == 3) {
			int sum = 0;
			for(int n = 0; n < sumServCount; n++) 
				sum += sumTcp[n].getServ().getNumCount();
			System.out.println("Total number count " + sum);
			write(sum);
		}
		else
			write(-1);	
	
	}
	@Override
	protected int getServId() {
		return id;
	}
	
	@Override
	public void die() {
		running = false;
		System.out.println("Server 0 killed");
		if(timer != null && timer.isActive())
			timer.quit();
	}
	
}

