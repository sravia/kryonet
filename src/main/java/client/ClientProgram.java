package client;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

public class ClientProgram extends Listener {
	static Client client;
	static String ip = "localhost";
	static int tcpPort = 27960, udpPort = 27960;
	
	static boolean messageReceived = false;
	
	public static void main(String[] args) throws Exception {
		System.out.println("Connecting to the server...");
		client = new Client();
		
		client.getKryo().register(PacketMessage.class);

		client.start();
		client.connect(5000, ip, tcpPort, udpPort);
		
		client.addListener(new ClientProgram());
		
		System.out.println("Connected! The client program is now waiting for a packet...\n");
		
		while(!messageReceived){
			Thread.sleep(1000);
		}
		
		System.out.println("Client will now exit.");
		System.exit(0);
	}
	
	public void received(Connection c, Object p){
		if(p instanceof PacketMessage){
			PacketMessage packet = (PacketMessage) p;
			System.out.println("received a message from the host: "+packet.message);
			messageReceived = true;
		}
	}
}
