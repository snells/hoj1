package hoj;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Main {
	public static void main(String[] args) {
		if(args.length < 3)
			return;
		String address = args[0];
		int port = Integer.parseInt(args[1]);
		int listenPort = Integer.parseInt(args[2]);
		Tcp tcp = new Tcp();
		tcp.listen(listenPort);
		Udp udp = new Udp();
		float waitTime = 0f;
		float endWait = 5f;
		boolean waiting = true;
		float oldTime = System.currentTimeMillis();
		while(waiting) {
			if(tcp.connected)
				break;
			if(waitTime >= endWait) {
				udp.sendInt(address, port, listenPort);
				waitTime = 0;
			}
		float newTime = System.currentTimeMillis();
		waitTime += (newTime - oldTime) / Math.pow(10, 9); // nano seconds to seconds
		oldTime = newTime;
		}
	}
}
