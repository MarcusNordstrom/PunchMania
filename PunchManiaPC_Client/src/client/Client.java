package client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;

public class Client implements Observer {
	private Socket socket;
	private DataInputStream dis; 
	private int data;


	public Client(String ip, int port) {
		try {
			socket = new Socket(ip, port);
			dis = new DataInputStream(socket.getInputStream());


		} catch (IOException e) {}
	}


	public void update(Observable o, Object arg) {
		try {
			data = dis.readInt();
			System.out.println(data);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
