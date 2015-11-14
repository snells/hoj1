package hoj;




public class Main {
	public static void main(String[] args) {
		int sumServCount = 3;
		if(args.length < 3)
			return;
		String address = args[0];
		int port = Integer.parseInt(args[1]);
		int listenPort = Integer.parseInt(args[2]);
		
		MainServer ms = new MainServer();
		Tcp<MainServer> mainTcp = new Tcp<MainServer>(listenPort, ms);
		@SuppressWarnings("unchecked")
		Tcp<SumServer>[] sumTcp = new Tcp[3];
		for(int i = 0; i < sumServCount; i++) {
			SumServer ss = new SumServer();
			sumTcp[i] = new Tcp<SumServer>(9901 + i, ss);
		}
		Udp udp = new Udp();
		Timer timer = new Timer();
		udp.sendInt(address, port, listenPort);
		
		timer.start();
		
		// sends the port we are listening to target server using udp protocol
		// target server has 5 seconds to connect or we resend the port 
		// we try i times
		int i = 1;
		while(i < 3) {
			if(mainTcp.connected)
				break;
			timer.update();
			if(timer.getTime() > 5) {
				timer.reset();
				i++;
				if(i == 3) // Don't send call if we are not going to listen
					break;
				udp.sendInt(address, port, listenPort);
			}
		}
		// we have connection, now we wait for the session to end and then close all sockets we made
		while(mainTcp.connected)
			;
		mainTcp.close();
		for(int n = 0; n < sumServCount; n++)
			sumTcp[i].close();
		udp.close();
	}
}
