package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import common.HighScoreList;
import common.Queue;
import common.UserList;
/**
 * PunchmaniaUI
 *
 */
public class ServerUI extends JPanel {

	private static final long serialVersionUID = 7511514150396821495L;
	private JTextArea taCmdArea = new JTextArea();
	private JScrollPane spCmdArea = new JScrollPane(taCmdArea);
	private JTextField tfCmdField = new JTextField();
	private Server server;
	private int cmdIndex = 0;
	private ArrayList<String> lastCmds = new ArrayList<String>();

	/**
	 * Constructor for setting up the looks and feel
	 */
	public ServerUI() {
		setLayout(new BorderLayout());
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);
		taCmdArea.setEditable(false);
		taCmdArea.setBackground(Color.BLACK);
		taCmdArea.setForeground(Color.WHITE);
		taCmdArea.setSelectionColor(Color.GREEN);
		tfCmdField.setBackground(Color.BLACK);
		tfCmdField.setForeground(Color.WHITE);


		add(spCmdArea, BorderLayout.CENTER);
		add(tfCmdField, BorderLayout.SOUTH);

		tfCmdField.addKeyListener(new KeyListener() {

			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					readCmd();
					tfCmdField.setText("");
				}else if(e.getKeyCode() == KeyEvent.VK_UP && cmdIndex != lastCmds.size()){
					tfCmdField.setText(lastCmds.get(cmdIndex++));
				}else if(e.getKeyCode() == KeyEvent.VK_DOWN && cmdIndex > 0) {
					tfCmdField.setText(lastCmds.get(--cmdIndex));
				}

			}
		});
	}
	/**
	 * Read the commandline a use corresponding method
	 */
	private void readCmd() {
		String fullCmd = tfCmdField.getText();
		String[] cmd = fullCmd.split(" ");
		lastCmds.add(fullCmd);
		cmdIndex = 0;

		switch (cmd[0]) {
		case "print":
			print(fullCmd, 5);
			break;

		case "getHSList":
			getHSList();
			break;

		case "getScore":
			getScore(cmd[1]);
			break;

		case "getQ":
			getQ();
			break;

		case "clear":
			clear();
			break;

		case "removeHS":
			remove(cmd);
			server.setSend(Server.HIGHSCORE);
			break;

		case "addHS":
			add(cmd);
			server.setSend(Server.HIGHSCORE);
			break;

		case "sendHS":
			server.setSend(Server.HIGHSCORE);

			break;

		case "exit":
			System.exit(0);
			break;

		case "addQ":
			addQ(cmd);
			server.setSend(Server.QUEUE);
			break;

		case "sendQ":
			server.setSend(Server.QUEUE);
			print("Sending queue", 0);
			break;

		case "removeQ":
			removeQ(cmd);
			if(server.ms.queueSize() == 0) {
			}
			server.setSend(Server.QUEUE);
			break;

		case "delay":
			delayOne();
			break;

		case "clearQ":
			clearQ();
			if(server.ms.queueSize() == 0) {
			}
			server.setSend(Server.QUEUE);
			break;

		case "kek":
			for(int i = 0; i <100; i++) {
				print("kek",0);
			}
			break;

		case "clearHS":
			clearHS();
			server.setSend(Server.HIGHSCORE);
			break;

		case "hard":
			server.isSendByte((byte)5);
			print("Hardpunchmode to IS", 0);
			break;

		case "fast":
			server.isSendByte((byte)4);
			print("Fastpunchmode to IS", 0);
			break;

		case "help":
			print("fast/hard",0);
			print("- sends byte to IS to chose game mode", 0);
			print("print [string]", 0);
			print("clearQ", 0);
			print("- clears Q, deletes all users in Q", 0);
			print("- prints a string", 0);
			print("getHSList" , 0);
			print("- returns the Highscore list", 0);
			print("getScore [user]", 0);
			print("- returns the score of a user", 0);
			print("getQ", 0);
			print("- returns the current queue", 0);
			print("clear", 0);
			print("- clears the window", 0);
			print("removeHS [user]", 0);
			print("- removes a user from the Highscore list", 0);
			print("addHS [user] [score]", 0);
			print("- adds a user to the Highscore list", 0);
			print("sendHS", 0);
			print("- sends the Highscore list to all online", 0);
			print("exit", 0);
			print("- closes the server", 0);
			print("addQ [user]", 0);
			print("- adds a person to the current queue", 0);
			print("sendQ", 0);
			print("- sends the current queue to all online", 0);
			print("removeQ [user]", 0);
			print("- removes a person from the current queue", 0);
			print("delay", 0);
			print("- switches first from queue to second in queue", 0);
			print("kek", 0);
			print("- type and find out", 0);
			print("clearHS", 0);
			print("- clears all from the Highscore list", 0);
			break;
		default:
			print("unknown command: " + fullCmd, 0);
		}

	}	
	/**
	 * Clear the current queue
	 */
	public void clearQ() {
		server.ms.DeleteQueueList();
	}
	/**
	 * Get the current queue
	 */
	public void getQ() {
		server.ms.getQueue();

	}
	/**
	 * Remove a set of users for the queue
	 * @param cmd
	 */
	private void removeQ(String[] cmd) {
		for(int i = 1; i < cmd.length;i++) {
			String name = cmd[i];
			server.ms.deleteQueue(name);
			print("removing : " + cmd[i] + " from queue" , 0);
		}
		System.out.println("user removed");
	}
	/**
	 * Add a set of users to the queue
	 * @param cmd
	 */
	private void addQ(String[] cmd) {
		for(int i = 1; i < cmd.length;i++) {
			String name = cmd[i];
			server.ms.toQueue(name);
			print("Adding : " + cmd[i] + " to queue" , 0);

		}
	}
	/**
	 * Removed during development
	 */
	@Deprecated
	private void delayOne() {
		Queue q = server.getQueue();
		q.dropOne();
	}
	/**
	 * Add new user to HardPunch highscorelist with set score
	 * @param cmd
	 */
	private void add(String[] cmd) {
		for(int i = 1; i < cmd.length;i+=2) {
			String name = cmd[i];
			int score = Integer.parseInt(cmd[i+1]);
			server.ms.setMySql(name, score);
			print("Adding : " + cmd[i] + " to Highscore" , 0);
		}
	}
	/**
	 * Remove a set of users from Hardpuch highscorelist
	 * @param cmd
	 */
	private void remove(String[] cmd) {
		for(int i = 1; i < cmd.length;i++) {
			String name = cmd[i];
			server.ms.Delete(name);
			print("Removing : " + cmd[i] , 0);
		}
	}
	/**
	 * Clear the hardpunch highscorelist
	 */
	private void clearHS() {
		server.ms.DeleteHSList();
	}
	/**
	 * Clear the commandline
	 */
	private void clear() {
		taCmdArea.setText("");

	}
	/**
	 * Get score for specific user from Hardpunch
	 * @param string
	 */
	private void getScore(String string) {
		print(string,0);
		server.ms.getUserScore(string);
		print("" + server.ms.getUserScore(string), 0);
	}
	/**
	 * Get the HardPunch highscorelist
	 */
	private void getHSList() {
		HighScoreList list = server.ms.getAllScore();
		for(UserList score : list.getTopTen()) {
			print(score.getUser() + ": " + score.getScore(),0);
		}
	}
	/**
	 * Print to the commandline
	 * @param text
	 * @param i
	 */
	public synchronized void print(String text, int i) {
		taCmdArea.setText(taCmdArea.getText() + text.substring(i) + "\n");
	}
	/**
	 * Connect server to UI
	 * @param server
	 */
	public void addManager(Server server) {
		this.server = server;
		print("Server open", 0);
	}
}
