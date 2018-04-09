package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


public class Server{
	private ServerSocket serverSocket;
	private Connection connection = new Connection();

	public Server(int port) {
		try {
			serverSocket = new ServerSocket(port);
		} catch (IOException e) {
			e.printStackTrace();
		}
		connection.start();

	}

	private class ClientHandler implements Runnable{
		private DataOutputStream dos;
		private DataInputStream dis;
		private String value;

		/*
		 * Creates the streams for values recived from IS, and stream for sending values.
		 */
		public ClientHandler(Socket socket) {
			try {
				dis = new DataInputStream(socket.getInputStream());
				dos = new DataOutputStream(socket.getOutputStream());

			} catch(IOException e) {
				e.printStackTrace();
			}
		}
		
		public void Calculator(String value) {
			//Calculate punchforce
		}

		/*
		 * Read UTF from embedded system and prints it, sends value to calculator.
		 * 
		 */
		public void run() {
			try {
				value = dis.readUTF();
				System.out.println(value);
				Calculator(value);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public class Connection extends Thread {
		
		/*
		 * Waiting for connection, if connection is made new clienthandler is created with socket recived as parameter.
		 * clienthandler run method is started.
		 */

		public void run() {
			System.out.println("port: " + serverSocket.getLocalPort() + "\n");
			while (true) {
				try {
					Socket socket = serverSocket.accept();
					ClientHandler ch = new ClientHandler(socket);
					ch.run();

				} catch (IOException e) {
					System.err.println(e);
				}
			}
		}
	}
	public static void main(String[] args) {
		Server server = new Server(12345);
	}
}
