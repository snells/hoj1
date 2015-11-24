package hoj;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.IOException;
import hoj.Util;


public class Udp {
	private DatagramSocket socket;
	
	public Udp() {
			try {
			socket = new DatagramSocket();
			} catch (Exception e) {
				e.printStackTrace();
		}
	}
	
	private void connect(InetAddress address, int port) {
		if(socket.isConnected())
			socket.disconnect();
		socket.connect(address, port);	
	}
		
	// true jos onnistui muuten false
	public boolean sendInt(String address, int port, int data) {
		InetAddress target;
		try {
			target = InetAddress.getByName(address);
		} catch(UnknownHostException e) {
			e.printStackTrace();
			return false;
		}
		connect(target, port);
		// paketti tavuiksi
		byte[] message = Integer.toString(data).getBytes();
		DatagramPacket packet = new DatagramPacket(message, 4, target, port);
		try {
			socket.send(packet);
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	public void close() {
		if(!socket.isClosed())
			socket.close();
	}
}
