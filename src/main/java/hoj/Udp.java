package hoj;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.io.IOException;
import hoj.Util;


public class Udp {
	private DatagramSocket socketOut;
	
	public Udp() {
			try {
			socketOut = new DatagramSocket();
			} catch (Exception e) {
				System.out.println("UDP Error creating socket");
				System.exit(1);
		}
	}
	
	private void connectOut(InetAddress addr, int port) {
		if(socketOut.isConnected())
			socketOut.disconnect();
		socketOut.connect(addr, port);	
	}
		
	// returns false if there was error
	public boolean sendInt(String address, int port, int data) {
		InetAddress addr;
		try {
			addr = InetAddress.getByName(address);
		} catch(UnknownHostException e) {
			System.out.println("UDP Unkown host");
			return false;
		}
		connectOut(addr, port);
		byte[] msg = Util.toBytes(data);
		DatagramPacket packet = new DatagramPacket(msg, 4, addr, port);
		try {
			socketOut.send(packet);
		} catch(IOException e) {
			System.out.println("UDP Error sending data");
			return false;
		}
		return true;
	}
	
	
	
	
	public void close() {
		if(!socketOut.isClosed())
			socketOut.close();
	}
}
