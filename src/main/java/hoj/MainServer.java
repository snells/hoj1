package hoj;

import java.net.Socket;

public class MainServer extends Server {
	public MainServer() {}
	public MainServer(Socket s) {
		client = s;
	}
	public void serve() {}
	


}
