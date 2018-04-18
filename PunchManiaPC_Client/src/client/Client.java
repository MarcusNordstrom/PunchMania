package client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import common.HighScoreList;
import common.Message;
import common.Queue;

public class Client extends Thread {
	private Socket socket;
	private ObjectOutputStream oos;
	private String name = "";
	private String list = "";

	private UIHighScore uiHS;
	private UIQueue uiQ;

	private DataReader dr;
	private Message message;

	private static String ip = "192.168.1.42";
	private static int port = 12346;

	private HighScoreList hsl;
	private Queue queue;

	/**
	 * Constructs using a given IP-address and port the constructor also uses the
	 * start method to execute the thread which calls the run method.
	 * 
	 * @param ip
	 * @param port
	 */

	public Client(String ip, int port) {
		String input = JOptionPane.showInputDialog("Enter IP", "192.168.1.");
		System.out.println("Connecting to Server");
		connect(input, port);

	}

	/**
	 * Sends a newly created user to the server and sends a message to the queue.
	 * 
	 * @param user
	 */
	public synchronized void sendUser(String user) {
		try {
			oos.writeObject(new Message(user, message.NEW_USER_TO_QUEUE));
			oos.reset();
			oos.flush();
			System.out.println(user + " skickad");
		} catch (IOException e) {
			System.err.println("Socket interrupted");
		}
	}

	/**
	 * method that uses the given ip and port to check if the connection is
	 * established.
	 * 
	 * @param ip
	 * @param port
	 * @return true or false if connected or not.
	 */
	public void connect(String ip, int port) {
		try {
			socket = new Socket(ip, port);
			System.out.println("Successful connection!");
			connected();
		} catch (UnknownHostException e) {
			System.err.println("Host could not be found!");
			retry(ip, port);
		} catch (IOException e) {
			System.err.println("Could not connect to host");
			retry(ip, port);
		}
	}
	public void connected() {
		try {
			oos = new ObjectOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			retry(ip, port);
		}
		dr = new DataReader(this);
		dr.start();
	}

	/**
	 * method retries to establish a connection
	 * 
	 * @param ip
	 * @param port
	 * @throws InterruptedException
	 */
	public void retry(String ip, int port) {
		System.err.print("Reconnecting in ");
		for (int i = 5; i >= 0; i--) {
			System.err.print(i + " ");
			try {
				this.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.err.println();
		connect(ip, port);
	}

	/**
	 * SubClass When the run method is executed it creates a new stream and returns
	 * an input stream and reads the data.
	 */

	private class DataReader extends Thread {
		private Client client;
		private ObjectInputStream ois;

		public DataReader(Client client) {
			this.client = client;
			uiHS = new UIHighScore();
			uiQ = new UIQueue(this.client);
		}

		public void run() {
			boolean connected = true;
			Object obj;
			try {
				ois = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			while (connected) {
				try {
					obj = ois.readObject();
					if (obj instanceof Message) {
						Message readMessage = (Message) obj;
						switch (readMessage.getInstruction()) {
						case 1:	
							System.out.println((Queue)readMessage.getPayload());
							uiQ.updateQueue(readMessage.getPayload());
							break;
						case 2:
							System.out.println("highscorelist!");
							uiHS.updateHighScore(readMessage.getPayload());
							break;
						default:
							break;
						}
						readMessage = null;
					}
				} catch (IOException e1) {
					connected = false;
					retry(ip, port);
					e1.printStackTrace();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				}
				obj = null;
				try {
					ois.reset();
				} catch (IOException e) {
					e.printStackTrace();
				}

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
		try {
			UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		Client client = null;
		client = new Client(ip, port);

	}
}
