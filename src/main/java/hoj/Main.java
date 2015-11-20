package hoj;




public class Main {
	// main starts the tcp session that will start MainServer
	// When tcp connection is established main function is done
	// Should main function be the last one to finish?
	public static void main(String[] args) {
		if(args.length < 3)
			return;
		String address = args[0];
		int port = Integer.parseInt(args[1]);
		int listenPort = Integer.parseInt(args[2]);
		
		MainServer ms = new MainServer();
		Tcp<MainServer> mainTcp = new Tcp<MainServer>(listenPort, ms);
		
		mainTcp.start();
		
		Timer timer = new Timer();
		timer.start();
		
		// sends the port we are listening to target server using udp protocol
		// target server has 5 seconds to connect or we resend the port 
		// we try 5 times
		Udp udp = new Udp();
		int i = 1;
		boolean resend = true;
		while(i < 5) {
			if(resend) {
				udp.sendInt(address, port, listenPort);
				resend = false;
			}
			// once the mainTcp is connected we can close udp
			if(mainTcp.isConnected()) {
				timer.quit();
				break;
			}
			if(timer.getTime() >= 5) {
				resend = true;
				timer.reset();
				i++;
			}
		}
		if(timer.isActive())
			timer.quit();
		udp.close();
	}
}
