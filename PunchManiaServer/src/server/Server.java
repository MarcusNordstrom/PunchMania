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
	public static final int ENABLE = 1;
	public static final int DISABLE = 2;
	public static final int IS_HIGHSCORE = 3;
	public static final int QUEUE = 4;
	public static final int HIGHSCORE = 5;
	public static final int TOP_HIGHSCORE = 6;
	public static final int SEND_QUEUE = 8;
	public static final int SEND_HARDPUNCH_HIGHSCORE = 7;
	public static final int SEND_FASTPUNCH_HIGHSCORE = 9;
	
	public static final String FASTPUNCH_MODE = "HARD";
	public static final String HARDPUNCH_MODE = "FAST";
	

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
				sendHardPunchHighscore();
				sendFastPunchHighscore();
				if(ms.queueSize() == 0) {
					setSend(DISABLE);
				}else {
					setSend(ENABLE);
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
			client.clientMethods(SEND_QUEUE);
			break;
		case 5:
			client.clientMethods(SEND_HARDPUNCH_HIGHSCORE);
			break;
		case 6:
			client.clientMethods(SEND_FASTPUNCH_HIGHSCORE);
		}
	}

	public void start() {
		lastCheckSum = ms.getCheckSum();
		timer.scheduleAtFixedRate(task, 500, 500);
	}

	public void sendQueue() {
		setSend(QUEUE);
	}

	public void sendHardPunchHighscore() {
		setSend(HIGHSCORE);
	}

	public void sendFastPunchHighscore() {
		setSend(SEND_HARDPUNCH_HIGHSCORE);
	}

	public void newHs(int score) {
		if(score > ms.getTop1()) {
			setSend(IS_HIGHSCORE);
			client.clientMethods(TOP_HIGHSCORE);
		}
	}

	public void setScore(int score, String x, String y, String z) {
		if(ms.queueSize() == 0) {
			setSend(DISABLE);
		}
		else {
			setSend(ENABLE);
			newHs(score);
			ms.setMySql(ms.popQueue(), score, x, y, z);
		}
		client.clientMethods(SEND_HARDPUNCH_HIGHSCORE);
	}

	public void broadcastQueue() {
		for(ClientHandler sendq : clientList) {
			sendq.sendQueue();
		}
	}

	public void addQueue(String name) {
		setSend(ENABLE);
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
				for (ClientHandler sendHardPunchHighscore : clientList) {
					sendHardPunchHighscore.sendHardPunchHighscore();
					broadcastQueue();
				}
				break;
			case 8: 
				for (ClientHandler sendQueue : clientList) {
					sendQueue.sendQueue();
				}
				break;
			case 9:
				for(ClientHandler sendFastPunchHighscore : clientList) {
					sendFastPunchHighscore.sendFastPunchHighScore();
					broadcastQueue();
				}
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
				sendHardPunchHighscore();
				sendFastPunchHighScore();
				while (connected) {
					try {
						Message message = (Message) ois.readObject();
						System.out.println(message.getInstruction());
						switch (message.getInstruction()) {
						case 3:
							ui.print("User for queue received from: Client", 0);
							System.out.println(message.getPayload());
							String newtoqueue = (String) message.getPayload();
							addQueue(newtoqueue);
							if(ms.queueSize() == 0) {
								setSend(DISABLE);
							}
							broadcastQueue();
							break;

						case 5:
							ui.print("User requested hs case 5", 0);
							String name = (String) message.getPayload();
							sendNameScore(name);
							break;

						case 7: 
							ui.print("User requested hs case 7", 0); 
							HighScoreList hslNameScore = (HighScoreList) message.getPayload(); 
							sendXYZ(hslNameScore.getUser(0).getUser(), hslNameScore.getUser(0).getScore()); 
							break;
							
						case 9:
							ui.print("Game mode chosen", 0);
							String mode = (String)message.getPayload();
							if(mode.equals(FASTPUNCH_MODE)) {
								isSendByte((byte)4);
							} else if(mode.equals(HARDPUNCH_MODE)) {
								isSendByte((byte)5);
							} else {
								System.err.println("Error, no mode equals: " + mode);
							}
							break;	
						case 11:
							ui.print("FastPunchScore requested", 0);
							String nameFastPunch = (String) message.getPayload();
							sendNameScoreFastPunch(nameFastPunch);
							break;
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

			public void sendNameScore(String name) {
				try {
					ui.print("Sending Highscore list to client", 0);
					hsList = ms.getUserScore(name);
					oos.writeObject(new Message(hsList, Message.SERVER_SEND_PLAYERSCORES_HARDPUNCH));
					oos.reset();
					oos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			public void sendNameScoreFastPunch(String name) {
				try {
					ui.print("Sending Highscore list to client", 0);
					hsList = ms.getFastPunch(name);
					System.out.println("name");
					oos.writeObject(new Message(hsList, Message.SERVER_SEND_PLAYERSCORES_FASTPUNCH));
					oos.reset();
					oos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			public void sendHardPunchHighscore() {
				try {
					ui.print("Sending Highscore list to client", 0);
					hsList = ms.getAllScore();
					if(ms.queueSize() == 0) {
						setSend(DISABLE);
					}
					oos.writeObject(new Message(hsList, Message.NEW_HIGHSCORELIST_HARDPUNCH));
					oos.reset();
					oos.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			public void sendFastPunchHighScore() {
				try {
					ui.print("Sending FastPunchHighscore list to client", 0);
					hsList = ms.getAllScoreFastPunch();
					if(ms.queueSize() == 0) {
						setSend(DISABLE);
					}
					oos.writeObject(new Message(hsList, Message.NEW_HIGHSCORELIST_FASTPUNCH));
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

			public void sendXYZ(String name, int score) { 
				try { 
					oos.writeObject(new Message(ms.getXYZ(name, score), Message.SERVER_SEND_HSDETAILS)); 
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
						setSend(DISABLE);
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
