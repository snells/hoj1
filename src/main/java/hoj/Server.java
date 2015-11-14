package hoj;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public abstract class Server extends Thread {
	protected Socket client;
	protected InputStream strIn;
	protected OutputStream strOut;
	protected ObjectOutputStream out;
	protected ObjectInputStream in;
	protected boolean running = true; // set to false to finish serving
	public Server() {}
	public void close() {
		try {
		strIn.close();
		strOut.close();
		client.close();
		} catch(Exception e) {
		}
	}
	@Override
	public void run() {
		try {
			System.out.println("Serving");
			strIn = client.getInputStream();
			strOut = client.getOutputStream();
			in = new ObjectInputStream(strIn);
			out = new ObjectOutputStream(strOut);
			while(running) {
				try {
					serve();
				} catch(Exception e) {
					System.out.println("SERVER Error while serving");
					e.printStackTrace();
				}
			}
		} catch(IOException e) {
			System.out.println("SERVER IO error");
			e.printStackTrace();
			close();
		}
		close();
	}
	public void setClient(Socket s) {
		client = s;
	}
	
	protected abstract void serve();
}

