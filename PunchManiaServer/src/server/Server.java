package server;

import java.awt.Dimension;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JFrame;

import common.HighScoreList;

public class Server {
	private ServerSocket serverSocketIs;
	private ServerSocket serverSocketClient;
	private ConnectionClient connectionClient = new ConnectionClient();
	private ConnectionIs connectionIs = new ConnectionIs();
	private HighScoreList hsList;
	private String values;
	private String TEST = "-15,-303,169;-15,-303,169;-15,-303,169;-15,-303,169;-14,-283,197;-13,-115,162;-13,-115,162;-13,-115,162;-13,-115,162;-9,-77,142;-9,-77,142;-9,-77,142;-9,-77,142;-9,-77,142;-9,-77,142;-9,-77,142;-11,-45,132;-11,-45,132;-11,-45,132;-11,-45,132;-11,-45,132;-11,-45,132;-11,-45,132;-10,-16,132;-10,-16,132;-10,-16,132;-10,-16,132;-10,-16,132;-10,-16,132;-3,10,130;-3,10,130;-3,10,130;-3,10,130;-3,10,130;-3,10,130;-3,10,130;4,28,125;4,28,125;4,28,125;4,28,125;4,28,125;4,28,125;4,28,125;2,30,119;2,30,119;2,30,119;2,30,119;2,30,119;2,30,119;8,30,115;8,30,115;8,30,115;8,30,115;8,30,115;8,30,115;8,30,115;13,28,114;13,28,114;13,28,114;13,28,114;13,28,114;13,28,114;12,20,117;12,20,117;12,20,117;12,20,117;12,20,117;12,20,117;12,20,117;9,12,120;9,12,120;9,12,120;9,12,120;9,12,120;9,12,120;9,12,120;6,5,122;6,5,122;6,5,122;6,5,122;6,5,122;6,5,122;2,0,122;2,0,122;2,0,122;2,0,122;2,0,122;2,0,122;2,0,122;0,-5,124;0,-5,124;0,-5,124;0,-5,124;0,-5,124;0,-5,124;0,-5,124;-2,-12,125;-2,-12,125;-2,-12,125;-2,-12,125;-2,-12,125;-2,-12,125;-7,-13,128;-7,-13,128;-7,-13,128;-7,-13,128;-7,-13,128;" + 
			"";

	public Server(int portIs, int portClient, ServerUI serverui) {
		try {
			Calculator calculator = new Calculator(TEST);
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

	private HighScoreList newHSList() {
		HighScoreList hl = new HighScoreList();

		hl.add("Sebbe", 10);
		hl.add("Sebbe", 10);
		hl.add("Sebbe", 15);
		hl.add("Benji", 5);
		hl.add("Stefan", 15);
		return hl;
	}


	/**
	 * Command from the
	 */
	public void cmd() {

	}

	public HighScoreList getHSList() {
		return hsList;
	}

	private class Is implements Runnable {
		private DataOutputStream dos;
		private DataInputStream dis;

		public Is(Socket socket) {
			try {
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {
			String oldpacket = ".";
			while (true) {
				String packet = null;
				byte[] string = new byte[2000];
				try {
					dis.readFully(string);
					String str = new String(string);
					System.out.println(str);
					Calculator cal = new Calculator(str);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private class Client implements Runnable {
		private DataOutputStream dos;
		private DataInputStream dis;
		private String value;

		/*
		 * Creates the streams for values recived from IS, and stream for sending
		 * values.
		 */
		public Client(Socket socket) {
			try {
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		/*
		 * Read UTF from embedded system and prints it, sends value to calculator.
		 */
		public void run() {
			try {
				while (true) {
					value = dis.readUTF();
					System.out.println(value);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class ConnectionIs extends Thread {
		/*
		 * Waiting for connection, if connection is made new clienthandler is created
		 * with socket recived as parameter. clienthandler run method is started.
		 */
		public void run() {
			System.out.println("port embedded system: " + serverSocketIs.getLocalPort() + "\n");
			while (true) {
				try {
					Socket socketIs = serverSocketIs.accept();
					System.out.println("embedded!!!");
					Is is = new Is(socketIs);
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
