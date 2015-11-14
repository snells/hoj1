package hoj;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


// Listens to port and starts one tcp connection
// Should server handle multiple connections?
// For the given task this is enough 
public class Tcp<T extends Server> extends Thread {
	private int port;
	public boolean connected = false;
	ServerSocket ss;
	Socket cs;
	T serv;
	public Tcp(int p, T servClass) {
		port = p;
		serv = servClass;
	}
	@Override 
	public void run() {
		try {
			ss = new ServerSocket(port);
		} catch(IOException e) {
			System.out.println("TCP ERROR cannot create socket");
			e.printStackTrace();
			return;
		}
		try {
			cs = ss.accept();
		} catch (IOException e) {
			System.out.println("TCP ERROR io error");
			e.printStackTrace();
			return;
		}
		
		connected = true;
		serv.setClient(cs);
		serv.start();
		System.out.println("Connection from " + cs.getInetAddress());
		
		while(serv.running) // waiting for session to end
			;
		connected = false;
	}
	public void close() {
		connected = false;
		if(!ss.isClosed())
			try {
				ss.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		
	}
}






/*
public class Tcp extends Thread {
	public boolean connected = false;
	public boolean running = true;
	MainServer serv;
	ServerSocket ss;
	ServerSocket[] sumServSockets;
	public Tcp() {
	}
	
	
	private void startSumServers(int port, int x) {
		sumServSockets = new ServerSocket[x];
		for(int i = 0; i < x; i++)
			sumSockets[i] = new SumServer()
	}
	public boolean listen(int port) {
		try {
		ss = new ServerSocket(port);
		} catch(IOException e) {
			System.out.println("Error creating socket");
			return false;
		}
		Socket cs;
		while(running) {
			if(!connected) { // serving only one at time
			try {
				try {
				cs = ss.accept();
				cs.setSoTimeout(5000); // 5 sec timeout
				System.out.println("Connection from " + cs.getInetAddress());
				connected = true;
				serv = new MainServer(cs);
				serv.start();
				} catch(SocketException e) { // catches timeout
					System.out.println("Timeout");
						serv.closeAll();
						connected = false;
				}
			} catch(IOException e) {
				System.out.println("Connection error");
				serv.closeAll();
				connected = false;
				running = false;
				return false;
			}
			
		}
		}
		return true;
	}
	
	public void closeAll() {
		try {
			ss.close();
		} catch(IOException e) {
			System.out.println("Error closing.\n wtf");
		}
	}
}
*/