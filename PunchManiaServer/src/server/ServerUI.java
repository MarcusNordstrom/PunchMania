package server;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import common.HighScoreList;

public class ServerUI extends JPanel {

	private JTextArea taCmdArea = new JTextArea();
	private JTextField tfCmdField = new JTextField();
	private Server server;

	public ServerUI() {
		setLayout(new BorderLayout());
		setBackground(Color.BLACK);
		setForeground(Color.WHITE);
		taCmdArea.setEditable(false);
		taCmdArea.setBackground(Color.BLACK);
		taCmdArea.setForeground(Color.WHITE);
		tfCmdField.setBackground(Color.BLACK);
		tfCmdField.setForeground(Color.WHITE);
		

		add(taCmdArea, BorderLayout.CENTER);
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
				}

			}

			private void readCmd() {
				String fullCmd = tfCmdField.getText();
				String[] cmd = fullCmd.split(" ");

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
						
					case "clear":
						clear();
						break;
						
					case "remove":
						remove(cmd);
						break;
					case "exit":
						System.exit(0);
						break;
					
					case "add":
						add(cmd);
						break;
						
					case "sendHS":
						server.notifyClient();
						break;
						
						
					default:
						print("unknown command: " + fullCmd, 0);
				}

			}

			private void add(String[] cmd) {
				
				HighScoreList hl = server.getHSList();
				for(int i = 1; i < cmd.length;i+=2) {
					hl.add(cmd[i], Integer.parseInt(cmd[i+1]));
					print("Adding : " + cmd[i] , 0);
				}
				getHSList();
			}

			private void remove(String[] cmd) {
				HighScoreList hl = server.getHSList();
				for(int i = 1; i < cmd.length;i++) {
					hl.remove(cmd[i]);
					print("Removing : " + cmd[i] , 0);
				}
				getHSList();
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
				for(int i = 0; i < hl.size();i++) {
					ret += hl.getUser(i).getUser() + "  " + hl.getUser(i).getScore() + "\n";
				}
				print(ret , 0);
				
			}

			private void print(String fullCmd, int i) {
				taCmdArea.setText(taCmdArea.getText() + fullCmd.substring(i) + "\n");
			}
		});
	}

	public void addManager(Server server) {
		this.server = server;
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
