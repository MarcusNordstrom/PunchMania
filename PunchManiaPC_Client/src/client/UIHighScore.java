package client;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class UIHighScore extends JPanel implements ActionListener {
	private JLabel lblHighScore = new JLabel("High Score");
	private JTextArea taNames = new JTextArea();
	private JButton btn = new JButton("Update");

	private String text = "apa";

	public UIHighScore() {
		setLayout(new BorderLayout());
		add(lblHighScore, BorderLayout.NORTH);
		add(taNames, BorderLayout.CENTER);
		add(btn, BorderLayout.SOUTH);
		taNames.setEditable(false);
		btn.addActionListener(this);

	}

	public void updateHighScore(String info) {
		text = info; 							// HUR KOMMER JAG ÅT "INFO" FRÅN EN ANNAN METOD?? 
		taNames.setText(info);

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

	public static void main(String[] args) {
		//		JFrame frame = new JFrame();
		//		frame = new JFrame("PUNCH MANIA - HIGH SCORE");
		//		frame.setResizable(false);
		//		frame.setPreferredSize(new Dimension(500,600));
		//		frame.add(new UIHighScore());
		//		frame.pack();
		//		frame.setLocationRelativeTo(null);
		//		frame.setVisible(true);
		//		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}


	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btn) {
			taNames.setText(text);
			System.out.println(text);
		}

	}
}
