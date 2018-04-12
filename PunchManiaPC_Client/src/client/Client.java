package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import javax.swing.JOptionPane;
import common.HighScoreList;
import common.Queue;

public class Client {
	private Socket socket;
	private ObjectInputStream ois; 
	private String name = "";
	private String list = "";
	private UIHighScore uiHS;
	private UIQueue uiQ;
	private DataReader dr;

	private HighScoreList hsl;
	private Queue queue;


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
	 * Sending high score to high score list
	 */
	public void updateUIHighScore() {
		hsl = new HighScoreList();
		hsl.add("A", 10);
		hsl.add("B", 10);
		hsl.add("C", 15);
		hsl.add("D", 5);
		hsl.add("E", 15);

		for(int i = 0; i < hsl.getTopTen().size(); i++) {
			list += hsl.getUser(i).getUser() + ", " + hsl.getUser(i).getScore() + "\n";
		}
		uiHS.updateHighScore(list);
	}


	/**
	 * Sending queue to queue list. 
	 */
	public void updateUIQueue() {
		queue = new Queue();
		queue.add("A");
		queue.add("B");
		queue.add("C");
		queue.add("D");
		
		for (int i = 0; i < 4; i++) {
			name += queue.peekAt(i) + "\n";
		}

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

	}
}
