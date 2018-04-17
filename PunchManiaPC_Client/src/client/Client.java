package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.sound.midi.SysexMessage;
import javax.swing.JOptionPane;
import common.HighScoreList;
import common.Message;
import common.Queue;

public class Client extends Thread {
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private String name = "";
	private String list = "";

	private UIHighScore uiHS;
	private UIQueue uiQ;

	private DataReader dr;
	private Message message;

	private static String ip = "127.0.0.1";
	private static int port = 12346;

	private HighScoreList hsl;
	private Queue queue;


	/**
	 * Empty constructor
	 */
	public Client() {}

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
	 * the constructor also uses the start method to execute the thread which calls the 
	 * run method.
	 * @param ip
	 * @param port
	 */

	public Client(String ip, int port) {
		System.out.println("Connecting to Server");
		if (connect(ip, port)) {
			dr = new DataReader();
			dr.start();
			uiHS = new UIHighScore();
			uiQ = new UIQueue(this);
		} else {
			try {
				retry(ip, port);
			} catch (InterruptedException e) {
				System.err.println("Thread interrupted");
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
	 * Sending a queue to queue list.
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

	/**
	 * Sends a newly created user to the server and sends a message to the queue.
	 * @param user
	 */
	public void sendUser(String user) {
		System.out.println("tog emot " + user);
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.writeObject(new Message(user, message.NEW_USER_TO_QUEUE));
			oos.flush();
		} catch (IOException e) {
			System.err.println("Socket interrupted");
		}
	}

	/**
	 * method that uses the given ip and port to check if the connection is established.
	 * @param ip
	 * @param port
	 * @return true or false if connected or not.
	 */
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

	/**
	 * method retries to establish a connection 
	 * @param ip
	 * @param port
	 * @throws InterruptedException
	 */
	public void retry(String ip, int port) throws InterruptedException {
		while (!connect(ip, port)) {
			System.err.print("Reconnecting in ");
			for (int i = 10; i >= 0; i--) {
				System.err.print(i + " ");
				this.sleep(1000);
			}
			System.err.println();
		}
	}



	/**
	 * SubClass
	 * When the run method is executed it creates a new stream and returns an input stream and reads the data.
	 */

	private class DataReader extends Thread {
		private Client client;

		public DataReader() {
			uiHS = new UIHighScore();
			updateUIHighScore();
			uiQ = new UIQueue(client);
		}


		public void run() {
			try {
				ois = new ObjectInputStream(socket.getInputStream());
				Object obj = ois.readObject();

				if(obj instanceof Message) {
					Message readMessage = (Message) obj;
					switch (readMessage.getInstruction()) {
					case 1:
						System.out.println("Queue! yeah");
						break;
					case 2:
						System.out.println("highscorelist! yeah");
						break;
					default:
						break;
					}
				}
				System.out.println("DataReader@Client.java: Reading data...");
			} catch (IOException e1) {
				e1.printStackTrace();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		}

	}


	/**
	 * Executes the client using a static ip and port
	 * 
	 * @param args
	 * @throws IOException
	 */

	public static void main(String[] args) {
		Client client = null;
		client = new Client(ip, port);
		
	}
}
