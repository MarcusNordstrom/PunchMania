package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.sound.midi.SysexMessage;
import javax.swing.JOptionPane;
import common.HighScoreList;
import common.Queue;

public class Client extends Thread {
	private Socket socket;
	private ObjectInputStream ois;
	private String name = "";
	private String list = "";
	private UIHighScore uiHS;
	private UIQueue uiQ;
	private DataReader dr;

	private static String ip = "192.168.1.13";
	private static int port = 12346;

	private HighScoreList hsl;
	private Queue queue;

	/**
	 * Empty constructor
	 */
	public Client() {
	}

	/**
	 * Constructs using the given UIHighScore and UIQueue
	 * 
	 * @param uiHS
	 * @param uiQ
	 */
	public Client(UIHighScore uiHS, UIQueue uiQ) {
		this.uiHS = uiHS;
		this.uiQ = uiQ;
	}

	/**
	 * Constructs using a given IP-address and port
	 * 
	 * @param ip
	 * @param port
	 */

	public Client(String ip, int port) {
		System.out.println("Connecting to Server");
		if (connect(ip, port)) {
			dr = new DataReader();
			dr.start();
		} else {
			try {
				retry(ip, port);
			} catch (InterruptedException e) {
				System.err.println("Thread interrupted");
			}
		}
	}

	/**
	 * Constructor
	 *
	 */

	private class DataReader extends Thread {
		public void run() {
			try {
				ois = new ObjectInputStream(socket.getInputStream());
				System.out.println("DataReader@Client.java: Reading data...");
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

		for (int i = 0; i < hsl.getTopTen().size(); i++) {
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

	public boolean connect(String ip, int port) {
		try {
			socket = new Socket(ip, port);
			System.out.println("Successful connection!");
			return true;
		} catch (UnknownHostException e) {
			System.err.println("Host could not be found!");
			return false;
		} catch (IOException e) {
			System.err.println("Could not connect to host");
			return false;
		}
	}

	public void retry(String ip, int port) throws InterruptedException {
		while (!connect(ip, port)) {
			System.err.print("Reconnecting in ");
			for(int i = 10; i >= 0; i--) {
				System.err.print(i + " ");
				this.sleep(1000);
			}
			System.err.println();
		}
	}

	/**
	 * Executes the client using a static ip and port
	 * 
	 * @param args
	 * @throws IOException
	 */

	public static void main(String[] args) throws IOException {
		Client client = null;
		client = new Client(ip, port);

		// UIHighScore uiHS = new UIHighScore();
		// UIQueue uiQ = new UIQueue();
		// Client cli = new Client(uiHS, uiQ);
		// cli.updateUIHighScore();
		// cli.updateUIQueue();

	}
}
