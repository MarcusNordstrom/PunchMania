package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import common.HighScoreList;
import common.Queue;

public class ServerUI extends JPanel {
	
	
	private JTextArea taCmdArea = new JTextArea();
	private JScrollPane spCmdArea = new JScrollPane(taCmdArea);
	private JTextField tfCmdField = new JTextField();
	private Server server;
	private int cmdIndex = 0;
	private ArrayList<String> lastCmds = new ArrayList<String>();
	private MySql ms;

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
				server.sendSetHighscore();
				break;
			
			case "addHS":
				add(cmd);
				server.sendSetHighscore();
				break;
				
			case "sendHS":
				server.sendSetHighscore();
				
				break;
				
			case "exit":
				System.exit(0);
				break;
				
			case "addQ":
				addQ(cmd);
				server.sendQueue();
				break;
				
			case "sendQ":
				server.sendQueue();
				break;
				
			case "removeQ":
				removeQ(cmd);
				server.sendQueue();
				break;
				
			case "delay":
				delayOne();
				break;
				
			case "kek":
				for(int i = 0; i <100; i++) {
					print("kek",0);
				}
				break;
			case "clearHS":
				clearHS();
				server.sendSetHighscore();
				break;
			case "help":
				print("print [string]", 0);
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

	public void getQ() {
		Queue q = server.getQueue();
		print("Queue: ",0);
		String ret = "";
		for(int i = 0; i < q.size();i++) {
			ret += q.peekAt(i) + "\n";
		}
		print(ret , 0);
		
	}
	private void removeQ(String[] cmd) {
		Queue q = server.getQueue();
		for(int i = 1; i < cmd.length;i++) {
			q.remove(cmd[i]);
			print("removing : " + cmd[i] + " from queue" , 0);
		}
		System.out.println("user removed");
	}
	private void addQ(String[] cmd) {
		Queue q = server.getQueue();
		for(int i = 1; i < cmd.length;i++) {
			q.add(cmd[i]);
			print("Adding : " + cmd[i] + " to queue" , 0);
		}
	}
	
	private void delayOne() {
		Queue q = server.getQueue();
		q.dropOne();
	}
	
	private void add(String[] cmd) {
		
		HighScoreList hl = server.getHSList();
		for(int i = 1; i < cmd.length;i+=2) {
			hl.add(cmd[i], Integer.parseInt(cmd[i+1]));
			print("Adding : " + cmd[i] + " to Highscore" , 0);
		}
		server.writeData(hl);
	}

	private void remove(String[] cmd) {
		HighScoreList hl = server.getHSList();
		for(int i = 1; i < cmd.length;i++) {
			hl.remove(cmd[i]);
			print("Removing : " + cmd[i] , 0);
		}
		server.writeData(hl);
	}
	private void clearHS() {
		HighScoreList hl = server.getHSList();
		hl.removeAll();
		server.writeData(hl);
	}
	private void clear() {
		taCmdArea.setText("");
		
	}

	private void getScore(String string) {
		
		HighScoreList hl = server.getHSList();
		print(string,0);
		int j = 0;
		for(int i = 0; i < hl.size();i++) {
			if(string.equals(hl.getUser(i).getUser())) {
				print(""+hl.getUser(i).getScore(),0);
				j++;
			}
		}
		if(j==0) {
			print("No score logged for this user",0);
		}
	}

	private void getHSList() {
		HighScoreList hl = server.getHSList();
		String ret = "";
		String name;
		int score;
		for(int i = 0; i < hl.size();i++) {
			ret += hl.getUser(i).getUser() + "  " + hl.getUser(i).getScore() + "\n";
		}
		print(ret , 0);
		
	}

	public void print(String text, int i) {
		taCmdArea.setText(taCmdArea.getText() + text.substring(i) + "\n");
	}

	public void addManager(Server server) {
		this.server = server;
		print("Server open", 0);
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame = new JFrame("PUNCH MANIA");
		frame.setResizable(false);
		frame.setPreferredSize(new Dimension(500, 600));
		frame.add(new ServerUI());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
