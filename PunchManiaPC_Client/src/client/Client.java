package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import javax.swing.JOptionPane;




public class Client {
	private Socket socket;
	private ObjectInputStream ois; 
	private int data = 0;
	private String test;
	private UIHighScore uiHS;
	private DataReader dr;


	public Client(UIHighScore uiHS) {
		this.uiHS = uiHS;
	}

	public Client(String ip, int port) {
		try {
			socket = new Socket(ip, port);
			ois = new ObjectInputStream(socket.getInputStream());
			dr = new DataReader();
			dr.start();
		} catch (IOException e) {}
	}

	
	private class DataReader extends Thread {
		public void run() {
			try {
				ois = new ObjectInputStream(socket.getInputStream());
				System.out.println("Reading data...");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
	}


	public void updateUI() {
		test = JOptionPane.showInputDialog("skriv");
		System.out.println("client: " + test);
		uiHS.updateHighScore(test);
	}


	public static void main(String[] args) throws IOException {
		Client client = null;
		client = new Client("127.0.0.1",12346);

		UIHighScore uiHS = new UIHighScore();
		Client cli = new Client(uiHS);
		cli.updateUI();
	}
}
