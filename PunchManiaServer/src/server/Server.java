package server;

import java.awt.Dimension;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JFrame;
import common.HighScoreList;
import common.Message;
import common.Queue;

public class Server {
	private ServerSocket serverSocketIs;
	private ServerSocket serverSocketClient;
	private ConnectionClient connectionClient = new ConnectionClient();
	private ConnectionIs connectionIs = new ConnectionIs(this);
	private HighScoreList hsList;
	private String values;
	private Client client;

	public Server(int portIs, int portClient, ServerUI serverui) {
		try {
			serverSocketIs = new ServerSocket(portIs);
			serverSocketClient = new ServerSocket(portClient);
		} catch (IOException e) {
			e.printStackTrace();
		}
		connectionIs.start();
		connectionClient.start();

		hsList = newHSList();
		serverui.addManager(this);
	}
	
	public void notifyClient() {
		client.sendHighscore();
	}
	
	private HighScoreList newHSList() {
		HighScoreList hl = new HighScoreList();

		hl.add("Sebbe", 10);
		hl.add("Sebbe", 10);
		hl.add("Sebbe", 15);
		hl.add("Benji", 5);
		hl.add("Stefan", 15);
		return hl;
	}

	
	public HighScoreList getHSList() {
		return hsList;
	}

	private class Is implements Runnable {
		private DataOutputStream dos;
		private DataInputStream dis;
		private Server server;

		public Is(Socket socket, Server server) {
			this.server = server;
			try {
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			String oldpacket = ".";
			boolean connected = true;
			while (connected) {
				String packet = null;
				byte[] string = new byte[1000];
				try {
					dis.readFully(string);
					String str = new String(string);
					//System.out.println(str);
					Calculator cal = new Calculator(str, server);
				} catch (IOException e) {
					connected = false;
				}
			}
		}
	}

	private class Client implements Runnable {
		private ObjectOutputStream oos;
		private ObjectInputStream ois;
		private String value;
		private Queue queue;
		private HighScoreList hsl;
		private Calculator calc;
		

		/*
		 * Creates the streams for values recived from IS, and stream for sending
		 * values.
		 */
		public Client(Socket socket) {
			try {
				ois = new ObjectInputStream(socket.getInputStream());
				oos = new ObjectOutputStream(socket.getOutputStream());
				oos.writeObject(queue);
				oos.flush();
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		

		public void sendHighscore() {
			
			try {
				hsl.add(queue.pop(), calc.getScore());
				oos.writeObject(new Message(hsl, Message.NEW_HIGHSCORELIST));
				oos.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		/*
		 * Read UTF from embedded system and prints it, sends value to calculator.
		 */
		public void run() {
			boolean connected = true;
			try {
				while (connected) {
					Object obj = ois.readObject();
					if(obj instanceof Message) {
						Message readMessage = (Message) obj;
						switch (readMessage.getInstruction()) {
					
						case 3:
							queue.add((String)readMessage.getPayload());
							oos.writeObject(new Message(queue, Message.NEW_QUEUE));
							oos.flush();
							break;
							
						default:
							break;
						}
					}
				}
			} catch (IOException e) {
				connected = false;
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				connected = false;
				e.printStackTrace();
			}
		}
	}

	private class ConnectionIs extends Thread {
		private Server server;
		/*
		 * Waiting for connection, if connection is made new clienthandler is created
		 * with socket recived as parameter. clienthandler run method is started.
		 */
		public ConnectionIs(Server server) {
			this.server = server;
		}
		private Is is = null;
		public void run() {
			System.out.println("port embedded system: " + serverSocketIs.getLocalPort() + "\n");
			while (true) {
				try {
					Socket socketIs = serverSocketIs.accept();
					System.out.println("embedded!!!");
					is = new Is(socketIs, server);
					is.run();

				} catch (IOException e) {
					System.err.println(e);
				}
			}
		}
	}

	private class ConnectionClient extends Thread {
		/*
		 * Waiting for connection, if connection is made new clienthandler is created
		 * with socket recived as parameter. clienthandler run method is started.
		 */
		public void run() {
			System.out.println("port client: " + serverSocketClient.getLocalPort() + "\n");
			while (true) {
				try {
					Socket socketClient = serverSocketClient.accept();
					System.out.println("CLIENT!!");
					Client ch = new Client(socketClient);
					ch.run();

				} catch (IOException e) {
					System.err.println(e);
				}
			}
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		ServerUI serverui = new ServerUI();
		frame = new JFrame("PUNCH MANIA");
		frame.setResizable(false);
		frame.setPreferredSize(new Dimension(500, 600));
		frame.add(serverui);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Server server = new Server(12345, 12346, serverui);
	}
}
