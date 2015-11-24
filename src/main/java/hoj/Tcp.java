package hoj;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/* kuuntelee porttia ja antaa Server luokan toteutuksen palvella asiakasta
 */
public class Tcp<T extends Server> extends Thread {
	private int port;
	private boolean connected = false;
	private ServerSocket serverSocket;
	private Socket socket;
	private T server;
	
	public Tcp(int port, T server) {
		this.port = port;
		this.server = server;
	}
	
	@Override 
	public void run() {
		// kuuntelee porttia ja lopettaa jos se ei onnistu
		try {
			serverSocket = new ServerSocket(port);
			socket = serverSocket.accept();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		connected = true;
		server.setClient(socket);
		server.start();
		System.out.println("Palvelee asiakasta " + socket.getInetAddress());
		
		while(server.isRunning()) // odottaa että serveri lopetaa
			;
		connected = false;
		System.out.println("Tcp portilla " + port + " valmis, server id " + server.getServId());
	}
	
	public boolean isConnected() {
		return connected;
	}
	public T getServer() {
		return server;
	}
	public void close() {
		connected = false;
		if(server != null)
			server.die();
		if(!serverSocket.isClosed())
			try {
				serverSocket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
