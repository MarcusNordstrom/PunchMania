package client;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JOptionPane;




public class Client implements Observer {
	private Socket socket;
	private DataInputStream dis; 
	private int data = 0;
	private String test;
	private UIHighScore uiHS;


	public Client(UIHighScore uiHS) {
		this.uiHS = uiHS;
	}

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


	public void updateUI() {
		test = JOptionPane.showInputDialog("skriv");
		System.out.println("client: " + test);
		uiHS.updateHighScore(test);
	}


	public static void main(String[] args) throws IOException {
		Client client = null;
		client = new Client("IPADRESS",12346);

			UIHighScore uiHS = new UIHighScore();
			Client cli = new Client(uiHS);
			cli.updateUI();
	}
}
