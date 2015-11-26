package hoj;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/* kuuntelee porttia ja antaa Server luokan toteutuksen palvella asiakasta.
 */
public class Tcp<T extends Server> extends Thread {
	private int port;					//portti jota kuuntelee
	private boolean connected = false;	//yhteyden tila
	private ServerSocket serverSocket;	
	private Socket socket;
	private T server;
	
	public Tcp(int port, T server) { //luodaan uusi Tcp-olio jolle annetaan haluttu portti ja server
		this.port = port;
		this.server = server;
	}
	
	@Override 
	public void run() {
		// kuuntelee annettua porttia ja lopettaa jos se ei onnistu
		try {
			serverSocket = new ServerSocket(port);
			socket = serverSocket.accept();
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		connected = true;
		server.setClient(socket);	//tarvitsee soketin ett‰ pystyy kirjoittamaan ja lukemaan//
		server.start();				//k‰ynnistet‰‰n serveri, serveri k‰y omassa threadissaan//
		System.out.println("Palvelee asiakasta " + socket.getInetAddress()); //ja palvelee asiakasta//
		
		while(server.isRunning()&&connected) //odottaa ett‰ serveri lopettaa palvelun//
			;
		connected = false;
		System.out.println("Tcp portilla " + port + " valmis, server id " + server.getServerId());
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
