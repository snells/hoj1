package hoj;

import java.net.Socket;

public class SumServer extends Server {
	public SumServer() {}
	public SumServer(Socket s) {
		client = s;
	}
	public void serve() {}
}
