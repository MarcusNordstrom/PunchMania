package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Observable;
import java.util.Observer;



public class Server implements Observer{
	private ServerSocket serverSocketIs;
	private ServerSocket serverSocketClient;
	private ConnectionClient connectionClient = new ConnectionClient();
	private ConnectionIs connectionIs = new ConnectionIs();

	public Server(int portIs, int portClient, ServerUI serverui) {
		try {
			serverSocketIs = new ServerSocket(portIs);
			serverSocketClient = new ServerSocket(portClient);
		} catch (IOException e) {
			e.printStackTrace();
		}
		connectionIs.start();
		connectionClient.start();
		
		serverui.addManager(this);
	}

	public void Calculator(String value) {

	}
	/**
	 * Command from the 
	 */
	public void cmd() {
		
	}
	
	
	private class Is implements Runnable {
		private DataOutputStream dos;
		private DataInputStream dis;

		public Is(Socket socket) {
			try {
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());

			} catch(IOException e) {
				e.printStackTrace();
			}
		}

		public void run() {

		}
	}

	private class Client implements Runnable{
		private DataOutputStream dos;
		private DataInputStream dis;
		private String value;

		/*
		 * Creates the streams for values recived from IS, and stream for sending values.
		 */
		public Client(Socket socket) {
			try {
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());

			} catch(IOException e) {
				e.printStackTrace();
			}
		}

		/*
		 * Read UTF from embedded system and prints it, sends value to calculator.
		 */
		public void run() {
			try {
				while(true) {
					value = dis.readUTF();
					System.out.println(value);
					Calculator(value);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private class ConnectionIs extends Thread {
		/*
		 * Waiting for connection, if connection is made new clienthandler is created with socket recived as parameter.
		 * clienthandler run method is started.
		 */
		public void run() {
			System.out.println("port embedded system: " + serverSocketIs.getLocalPort() + "\n");
			while (true) {
				try {
					Socket  socketIs = serverSocketIs.accept();
					System.out.println("embedded!!!");
					Is is = new Is (socketIs);
					is.run();

				} catch (IOException e) {
					System.err.println(e);
				}
			}
		}

	}

	private class ConnectionClient extends Thread {
		/*
		 * Waiting for connection, if connection is made new clienthandler is created with socket recived as parameter.
		 * clienthandler run method is started.
		 */
		public void run() {
			System.out.println("port client: " + serverSocketClient.getLocalPort() + "\n");
			while (true) {
				try {
					Socket  socketClient = serverSocketClient.accept();
					System.out.println("CLIENT!!");
					Client ch = new Client (socketClient);
					ch.run();

				} catch (IOException e) {
					System.err.println(e);
				}
			}
		}
	}

	public static void main(String[] args) {
		Server server = new Server(12345, 12346, new ServerUI());
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		
		
	}
}
