package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import javax.swing.JOptionPane;

import common.HighScoreList;


public class Client {
	private Socket socket;
	private ObjectInputStream ois; 
	private String test, name;
	private UIHighScore uiHS;
	private UIQueue uiQ;
	private DataReader dr;
	
	private HighScoreList hsl;

	public Client() {}
	public Client(UIHighScore uiHS, UIQueue uiQ) {
		this.uiHS = uiHS;
		this.uiQ = uiQ;
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

	
	/**
	 * hämta arrayList 
	 */
	public void getArrayList() {
		hsl = new HighScoreList();
		hsl.add("Sebbe", 10);
		hsl.add("Sebbe", 10);
		hsl.add("Sebbe", 15);
		hsl.add("Benji", 5);
		hsl.add("Stefan", 15);
		
		System.out.println(hsl.getTopTen().size());
	}
	
	
	
	
	/**
	 * Sending a string to the high score list
	 */
	public void updateUIHighScore() {
		test = JOptionPane.showInputDialog("skriv till High Score");
		System.out.println(test + " skrivs in i High Score");
		uiHS.updateHighScore(test);
	}

	
	/**
	 * Sending a string to the queue list. 
	 */
	public void updateUIQueue() {
		name = JOptionPane.showInputDialog("Lägg i kö");
		System.out.println(name + " läggs till i kön");
		uiQ.updateQueue(name);
	}


	public static void main(String[] args) throws IOException {
		Client client = null;
		client = new Client("83.249.10.9",12346);

		UIHighScore uiHS = new UIHighScore();
		UIQueue uiQ = new UIQueue();
		Client cli = new Client(uiHS, uiQ);
		cli.updateUIHighScore();
		cli.updateUIQueue();
		
		Client cli1 = new Client();
		cli1.getArrayList();
	}
}
