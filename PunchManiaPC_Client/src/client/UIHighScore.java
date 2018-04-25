package client;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.*;

import common.HighScoreList;
import common.Queue;
import common.UserList;

public class UIHighScore extends JPanel {
	private JLabel lblHighScore = new JLabel("High Score");
	private JTextArea taNames = new JTextArea();
	private JScrollPane jScroll = new JScrollPane(taNames);
	private JFrame frame;

	/**
	 * Creating panel
	 */
	public UIHighScore() {
		setLayout(new BorderLayout());
		add(lblHighScore, BorderLayout.NORTH);
		add(jScroll, BorderLayout.CENTER);
		taNames.setEditable(false);
		jScroll.setAutoscrolls(true);
		
		frame = new JFrame("PUNCH MANIA - HIGH SCORE");
		frame.setResizable(false);
		frame.setPreferredSize(new Dimension(500,600));
		frame.add(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	}
	
	
	/**
	 * Updating the high score with a string sent from the client. 
	 * @param info : a string from client adding to high score. 
	 */
	public void updateHighScore(String info) {
		taNames.setText(info);	
	}
	/**
	 * Updating the high score with a object sent from the client.
	 * @param obj 
	 */
	public void updateHighScore(Object obj) {
		taNames.setText("");
		HighScoreList list =(HighScoreList) obj;
		String output = "";
		ArrayList<UserList> aList = new ArrayList<UserList>();
		for(int i = 0; i < list.size(); i++) {
			aList.add(list.getUser(i));
		}
		for(UserList u : aList) {
			output += u.getUser() + "\t" + u.getScore() + "\n";
		}
		taNames.setText(output);
	}
	public void closeWindow() {
		frame.dispose();
	}
}
