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
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import common.HighScoreList;
import common.Message;
import common.Queue;
import server.Server.Client.ClientHandler;

public class Server {
	public static final int enable = 1;
	public static final int disable = 2;
	public static final int ishighscore = 3;
	public static final int Queue = 4;
	public static final int highscore = 5;
	public static final int TopHighscore = 6;
	public static final int sendHighscore = 7;
	public static final int sendQueue = 8;
	

	private Calculator cal = new Calculator(this);
	private ArrayList<ClientHandler> clientList = new ArrayList<ClientHandler>();
	private Timer timer = new Timer();

	private ServerSocket serverSocketIs;
	private ServerSocket serverSocketClient;
	private HighScoreList hsList;

	private Client client;
	private Queue queue;
	private IS is;
	private ServerUI ui;
	public MySql ms;

	private long lastCheckSum;

	public Server(int portIs, int portClient, ServerUI serverui) {
		try {
			serverSocketIs = new ServerSocket(portIs);
			serverSocketClient = new ServerSocket(portClient);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.ui = serverui;
		ui.addManager(this);
		queue = new Queue();
		client = new Client(serverSocketClient);
		is = new IS(serverSocketIs);
		ms = new MySql();
		ui.print("SQL connected",0);
		start();
	}

	public boolean isSendByte(byte send) {
		return is.sendByte(send);
	}

	TimerTask task = new TimerTask() {
		public void run() {
			if(lastCheckSum != ms.getCheckSum()) {
				lastCheckSum = ms.getCheckSum();
				sendQueue();
				sendHighscore();
				if(ms.queueSize() == 0) {
					setSend(disable);
				}else {
					setSend(enable);
				}
			}
		}
	};

	public void setSend(int i) {
		switch(i) {
		case 1:
			isSendByte((byte)1);
			break;
		case 2:
			isSendByte((byte)2);
			break;
		case 3:
			isSendByte((byte)3);
			break;
		case 4:
			client.clientMethods(sendQueue);
			break;
		case 5:
			client.clientMethods(sendHighscore);
			break;
		}
	}

	public void start() {
		lastCheckSum = ms.getCheckSum();
		timer.scheduleAtFixedRate(task, 500, 500);
	}

	public void sendQueue() {
		setSend(Queue);
	}

	public void sendHighscore() {
		setSend(highscore);
	}

	public void newHs(int score) {
		if(score > ms.getTop1()) {
			setSend(ishighscore);
			client.clientMethods(TopHighscore);
		}
	}

	public void setScore(int score, String x, String y, String z) {
		if(ms.queueSize() == 0) {
			setSend(disable);
		}
		else {
			setSend(enable);
			newHs(score);
			ms.setMySql(ms.popQueue(), score, x, y, z);
		}
		client.clientMethods(sendHighscore);
	}

	public void broadcastQueue() {
		for(ClientHandler sendq : clientList) {
			sendq.sendQueue();
		}
	}

	public void addQueue(String name) {
		setSend(enable);
		queue.add(name);
		ms.toQueue(name);
	}

	public Queue getQueue() {
		return queue;
	}

	public class Client {
		private ServerSocket socket;

		/*
		 * Creates the streams for values recived from IS, and stream for sending
		 * values.
		 */
		public Client(ServerSocket serverSocketClient) {
			this.socket = serverSocketClient;
			new ConnectionClient().start();
		}
		
		public void clientMethods(int i) {
			switch (i) {
			case 6: 
				for (ClientHandler sendTop : clientList) {
					sendTop.topHighscore();
				}	
				break;

			case 7: 
				for (ClientHandler sendHighscore : clientList) {
					sendHighscore.sendHighscore();
					broadcastQueue();
				}
				break;
			case 8: 
				for (ClientHandler sendQueue : clientList) {
					sendQueue.sendQueue();
				}
				break;
			}
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
					sendQueue();
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
				if (queue.size() > 0) {
					sendQueue();
				}
				sendHighscore();
				while (connected) {
					try {
						Message message = (Message) ois.readObject();
						switch (message.getInstruction()) {
						case 3:
							ui.print("User for queue received from: Client", 0);
							System.out.println(message.getPayload());
							String newtoqueue = (String) message.getPayload();
							addQueue(newtoqueue);
							if(ms.queueSize() == 0) {
								setSend(disable);
							}
							broadcastQueue();
							break;

						case 5:
							ui.print("User requested hs", 0);
							String name = (String) message.getPayload();
							sendNameScore(name);

						case 7: 
							ui.print("User requested hs", 0); 
							HighScoreList hslNameScore = (HighScoreList) message.getPayload(); 
							sendXYZ(hslNameScore.getUser(0).getUser(), hslNameScore.getUser(0).getScore()); 
						} 

					} catch (IOException | ClassNotFoundException e) {
						try {
							ois.close();
							oos.close();
							socket.close();
						} catch (IOException e2) {
							System.out.println("Stream close");
						}
						connected = false;
						ui.print("Client disconnected", 0);
						clientList.remove(this);
						this.interrupt();
					}
				}
			}

			public void sendXYZ(String name, int score) { 
				try { 
					oos.writeObject(new Message(ms.getXYZ(name, score), Message.HSDETAILS)); 
					oos.reset(); 
					oos.flush(); 
				} catch (IOException e) { 
					e.printStackTrace(); 
				} 
			}

			public void sendNameScore(String name) {
				try {
					ui.print("Sending Highscore list to client", 0);
					hsList = ms.getUserScore(name);
					System.out.println(hsList.getUser(0).getUser()); 
					System.out.println(hsList.getUser(0).getScore()); 
					System.out.println("name");
					oos.writeObject(new Message(hsList, Message.PLAYERSCORES));
					oos.reset();
					oos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			public void sendQueue() {
				try {
					ui.print("Sending queue to client", 0);
					queue = ms.getQueue();
					oos.writeObject(new Message(queue, Message.NEW_QUEUE));
					oos.reset();
					oos.flush();
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("Request to send queue failed");
				}
			}

			public void topHighscore() {
				try {
					ui.print("NEW HIGHSCORE!", 0);
					String name = ms.getTop1Name();
					oos.writeObject(new Message(name, Message.NEW_HS));
					System.out.println("TOP USER SENT");
					oos.reset();
					oos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			public void sendHighscore() {
				try {
					ui.print("Sending Highscore list to client", 0);
					hsList = ms.getAllScore();
					if(ms.queueSize() == 0) {
						setSend(disable);
					}
					oos.writeObject(new Message(hsList, Message.NEW_HIGHSCORELIST));
					oos.reset();
					oos.flush();
				} catch (IOException e) {
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

		public boolean sendByte(byte send) {
			if (dos != null) {
				try {
					dos.writeByte(send);
					return true;
				} catch (IOException e) {
					e.printStackTrace();
					return false;
				}
			}
			return false;
		}

		public class ISHandler implements Runnable {

			public ISHandler(Socket socket) {
				try {
					dis = new DataInputStream(socket.getInputStream());
					dos = new DataOutputStream(socket.getOutputStream());
					if(ms.queueSize() == 0) {
						setSend(disable);
					}

				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			public void run() {
				boolean connected = true;
				while (connected) {
					byte[] string = new byte[1000];
					try {
						dis.readFully(string);
						String str = new String(string);
						int score = cal.calculateScore(str);
						ui.print("New score: " + score, 0);
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
		Server server = new Server(12345, 12346, serverui);
	}

}
