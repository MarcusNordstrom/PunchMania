package client;

import java.awt.*;
import javax.swing.*;

public class UIHighScore extends JPanel {
	private JLabel lblHighScore = new JLabel("High Score");
	private JTextArea taNames = new JTextArea("test \ntest \ntest \ntest ");

	public UIHighScore() {
		setLayout(new BorderLayout());
		add(lblHighScore, BorderLayout.NORTH);
		add(taNames, BorderLayout.CENTER);
		taNames.setEditable(false);

	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame = new JFrame("PUNCH MANIA - HIGH SCORE");
		frame.setResizable(false);
		frame.setPreferredSize(new Dimension(500,600));
		frame.add(new UIHighScore());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
