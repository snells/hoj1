package hoj;

import java.io.IOException;
import java.net.Socket;

public class MainServer extends Server {
	Timer timer;
	public MainServer() {}
	public MainServer(Socket s) {
		client = s;
	}
	@Override
	protected void servStart() {
		timer = new Timer();
	}
	public void serve() {
		System.out.println("Trying to get first number");
		//getFirstNumber();
		try {
			System.out.println(in.readInt());
		} catch(Exception e) {}
		System.out.println("Got first number");
		//running = false;
		
		
	}
	
	private void getFirstNumber() {
		Aread ar = new Aread(in);
		timer.start();
		ar.start();
		while(!ar.newVal) {
			timer.update();
			if(timer.getTime() > 5) {
				System.out.println("Never got initial nubmer, trying to shutdown");
				try {
					in.close();
				} catch (IOException e) {
				}
				running = false; // closes connection
			}
				
		}
	}

}
