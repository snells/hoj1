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
	int id = 0; // there is only one instance of MainServer and it's id = 0, rest servers start indexing from 1
	Tcp<SumServer>[] sumTcp;
	volatile int[][] locker; // place where the SumServers save values they receive, we don't actually use this for anything though

	
	public MainServer() {}
	public MainServer(Socket s) {
		client = s;
	}
	
	
	// This method is executed last after we call close method defined in the super class Server
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
	// creates right amount of SumServers from the first value we received from server Y
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
	
	// this method is called on every iteration the server is running 
	@Override
	public void serve() {
		
		// this block gets the first value from the server
		// if there is error or no response in 5 seconds we exit
		if(!gotFirstNum) {
			System.out.println("Trying to get first number");
			int[] ret = Util.timedRead(in,  5); // tries to read for 5 seconds 
			if(ret[0] == 0) { // read failed or timed out
				die();
				return;
			}
			int firstRet = ret[1]; // the read value
			//if(running == false) // error reading, server dies
				//return;
			System.out.println("First number is " + firstRet);
			gotFirstNum = true;
			sumServCount = firstRet;
			sumServStart();
			for(int i = 0; i < sumServCount; i++) // sending sum server ports
				write(i + sumServPort);
			timer.start(); // timer needs to start be started only once
		}
		
		if(timer.getTime() > 60) { // no messages for minute so we die
			die();
			return;
		}
		System.out.print("Server 0 Reading command: ... ");
		int x = read();
		if(running == false) // if read failed no point to continue  
			return; 
		System.out.println(x);
		timer.reset();
		
		/*
		 * x is the command we received from server Y
		 * 0 means we need to shut down
		 * 1 means send sum of all values received by the SumServers 
		 * 2 means send SumServers number that has the biggest sum
		 * 3 means send how many numbers in total the SumServers have received
		 */
		
		
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

