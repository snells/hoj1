package hoj;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Server extends Thread {
	private Socket client;
	private InputStream strIn;
	private OutputStream strOut;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	Tcp tcp;
	public Server(Tcp t, Socket s){
		client = s;
		tcp = t;
	}
	public void closeAll() {
		try {
		strIn.close();
		strOut.close();
		client.close();
		} catch(Exception e) {
		}
		tcp.connected = false;
		
	}
	@Override
	public void run() {
		try {
			System.out.println("Serving");
			strIn = client.getInputStream();
			strOut = client.getOutputStream();
			in = new ObjectInputStream(strIn);
			out = new ObjectOutputStream(strOut);
			try {
				while(true) {
					
				}
			
			} catch(Exception e) {
				System.out.println("Error while serving");
				closeAll();
			}
		} catch(IOException e) {
			System.out.println("IO error");
			closeAll();
		}
		closeAll();
	}
}
