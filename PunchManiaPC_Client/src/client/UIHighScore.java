package client;

import java.awt.*;


import javax.swing.*;

public class UIHighScore extends JPanel {
	private JLabel lblHighScore = new JLabel("High Score");
	private JTextArea taNames = new JTextArea();

	/**
	 * Creating panel
	 */
	public UIHighScore() {
		setLayout(new BorderLayout());
		add(lblHighScore, BorderLayout.NORTH);
		add(taNames, BorderLayout.CENTER);
		taNames.setEditable(false);
		
		JFrame frame = new JFrame();
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
	
}
