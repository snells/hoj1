package hoj;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;


// if reading fails Server closes
public abstract class Server extends Thread {
	protected Socket client;
	protected InputStream strIn;
	protected OutputStream strOut;
	protected ObjectOutputStream out;
	protected ObjectInputStream in;
	protected volatile boolean running = false; // set to false to finish serving
	protected int id;
	private boolean closed = false;
	public Server() {
		
	}
	public void close() {
		if(!closed) { 
			closed = true;
			//System.out.println("Server " + getServId() + " Close function started");
		running = false;
		try {
		//strIn.close();
		//strOut.close();
		client.close();
		servClose();
		} catch(Exception e) {
		}
		//System.out.println("Server " + getServId() + " Close function finished");
		}
	}
	public boolean isRunning() {
		return running;
	}
	@Override
	public void start() {
		running = true;
		super.start();
	}
	@Override
	public void run() {
		try {
			System.out.println("Server " + getServId() + " Serving");
			strIn = client.getInputStream();
			strOut = client.getOutputStream();
			in = new ObjectInputStream(strIn);
			out = new ObjectOutputStream(strOut);
			servStart();
			while(running) {
				try {
					serve();
				} catch(Exception e) {
					System.out.println("SERVER Error while serving");
					e.printStackTrace();
					die();
				}
			}
		} catch(IOException e) {
			System.out.println("SERVER IO error");
			e.printStackTrace();
			die();
		}
		closeMsg();
		close();
	}
	public void setClient(Socket s) {
		client = s;
	}
	protected void write(int x) {
		if(running) {
			try {
				out.writeInt(x);
				out.flush();
			} catch (IOException e) {
				System.out.println("SERVER " + id + " ERROR write");
				e.printStackTrace();
				die();
			}
		}
		else {
			die();
		}
	}
	protected int read() {
		if(running) {
			int ret = 0;
			try {	
				ret = in.readInt();
			} catch(Exception e) {
				System.out.println("SERVER " + getServId() + " ERROR read");
			//e.printStackTrace();
				die();
				}
			return ret;
		}
		else {
			die();
			return -1;
		}
	}
	protected int getServId() {
		return id;
	}
		
	protected void die() {
		running = false;
	}
	protected void closeMsg() {
		System.out.println("Closing server " + id);
	}
	protected void servStart() {}
	protected void servClose() {}
	protected abstract void serve();
}

