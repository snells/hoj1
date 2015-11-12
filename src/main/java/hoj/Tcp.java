package hoj;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.TimeoutException;

public class Tcp {
	private int port;
	public boolean connected = false;
	public boolean running = true;
	Server serv;
	ServerSocket ss;
	public Tcp() {
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
				serv = new Server(this, cs);
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
