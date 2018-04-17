package server;

import java.awt.Dimension;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;

import common.HighScoreList;
import common.Message;
import common.Queue;
import server.Server.Client.ClientHandler;

public class Server {
	private ServerSocket serverSocketIs;
	private ServerSocket serverSocketClient;
	private HighScoreList hsList;
	private String values;
	private Client client;
	private Queue queue;
	private IS is;
	private ServerUI ui;
	private Trådpool pool;
	private ArrayList<ClientHandler> clientList = new ArrayList<ClientHandler>();

	public Server(int portIs, int portClient, ServerUI serverui, int Threads) {
		pool = new Trådpool(Threads);
		try {
			serverSocketIs = new ServerSocket(portIs);
			serverSocketClient = new ServerSocket(portClient);
		} catch (IOException e) {
			e.printStackTrace();
		}
		pool.start();
		hsList = newHSList();		//temp HS list with users

		this.ui = serverui;
		ui.addManager(this);
		queue = new Queue();
		client = new Client(serverSocketClient);
		is = new IS(serverSocketIs);
	}
	
	public void sendQueue() {
		client.sendQueue();
	}
	
	public void notifyClient() {
		// client.sendHighscore();
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

	public void addToQueue(String name) {
		queue.add(name);
	}

	public Queue getQueue() {
		return queue;
	}


	public class Client {
		private ServerSocket socket;
		private ClientHandler ch;

		/*
		 * Creates the streams for values recived from IS, and stream for sending
		 * values.
		 */
		public Client(ServerSocket serverSocketClient) {
			this.socket = serverSocketClient;
			new ConnectionClient().start();
		}
		
		public void sendQueue() {
			for(ClientHandler sendq : clientList) {
				sendq.sendQueue();
			}		
		}
		
		public void sendHS() {
			ch.sendHighscore();
		}

		public class ClientHandler extends Thread {
			private Socket socket;
			private ObjectOutputStream oos;
			private ObjectInputStream ois;
			
			public ClientHandler(Socket socketClient) {
				this.socket = socketClient;
				try {
					oos = new ObjectOutputStream(socket.getOutputStream());
					ois = new ObjectInputStream(socket.getInputStream());
				} catch (IOException e) {
					e.printStackTrace();
				}
				this.start();
			}

			/*
			 * Read message from Client and prints it, sends value to calculator.
			 */
			public synchronized void run() {
				boolean connected = true;
				sendQueue();
				while (connected) {
					try {
						Message message = (Message)ois.readObject();
						switch(message.getInstruction()) {
						case 3:
							ui.print("User for queue received from: Client", 0);
							System.out.println(message.getPayload());
							String newtoqueue = (String)message.getPayload();
							addToQueue(newtoqueue);
							break;
						}


					} catch (IOException | ClassNotFoundException e) {
						try {
							ois.close();
							oos.close();
							socket.close();
						} catch (IOException e2) {
							//e2.printStackTrace();
							System.out.println("Stream close");
						}
						connected = false;
						ui.print("Client disconnected", 0);
						clientList.remove(this);
						this.interrupt();
					}
				}
			}

			public void sendQueue() {
				try {
					ui.print("Sending queue to client", 0);
					oos.writeObject(new Message(queue, Message.NEW_QUEUE));
					oos.flush();
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("sendQ sucks");
				}
			}

			public void sendHighscore() {
				try {
					ui.print("Sending Highscore list to client", 0);
					oos.writeObject(new Message(hsList, Message.NEW_HIGHSCORELIST));
					oos.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		private class ConnectionClient extends Thread {
			/*
			 * Waiting for connection, if connection is made new clienthandler is created
			 * with socket recived as parameter. clienthandler run method is started.
			 */
			public void run() {

				while (true) {
					try {
						ui.print("Client-port open on: " + socket.getLocalPort(), 0);
						Socket socketClient = socket.accept();
						ui.print("Client connected", 0);
						clientList.add(new ClientHandler(socketClient));
					} catch (IOException e) {
						System.err.println(e);
					}
				}
			}
		}

	}

	public class IS {

		private DataOutputStream dos;
		private DataInputStream dis;
		private ISHandler ish;

		public IS(ServerSocket serverSocketIs) {

			new ConnectionIs().start();

		}

		public void newHandler(Socket socket) {
			ish = new ISHandler(socket);
			ish.run();
		}

		public class ISHandler implements Runnable {
			private Socket socket;

			public ISHandler(Socket socket) {
				this.socket = socket;
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
						// System.out.println(str);
					} catch (IOException e) {
						connected = false;
					}
				}
			}
		}

		private class ConnectionIs extends Thread {

			/*
			 * Waiting for connection, if connection is made new clienthandler is created
			 * with socket recived as parameter. clienthandler run method is started.
			 */

			private ISHandler is = null;

			public void run() {
				ui.print("", 0);
				while (true) {
					try {
						ui.print("IS-port open on: " + serverSocketIs.getLocalPort(), 0);
						Socket socketIs = serverSocketIs.accept();
						ui.print("Embedded connected", 0);
						newHandler(socketIs);

					} catch (IOException e) {
						System.err.println(e);
					}
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
		Server server = new Server(12345, 12346, serverui, 50);
	}


}
