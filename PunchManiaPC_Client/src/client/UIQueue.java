package client;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class UIQueue extends JPanel implements ActionListener {
	private JTextArea taQueue = new JTextArea();
	private JTextField tfName = new JTextField();
	private JLabel lblQueue = new JLabel("Current queue");
	private JLabel lblName = new JLabel("Type name to put you in line");
	private JButton btnSend = new JButton("Put me in line");

	private String names = "";
	private int counter = 1;

	public UIQueue() {
		setLayout(new BorderLayout());
		add(panelCenter(), BorderLayout.CENTER);
		add(panelSouth(), BorderLayout.SOUTH);
		btnSend.addActionListener(this);
	}

	private JPanel panelCenter() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(lblQueue, BorderLayout.NORTH);
		panel.add(taQueue, BorderLayout.CENTER);

		taQueue.setEditable(false);
		return panel;
	}

	private JPanel panelSouth() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(lblName, BorderLayout.NORTH);
		panel.add(tfName, BorderLayout.CENTER);
		panel.add(btnSend, BorderLayout.SOUTH);
		return panel;
	}

	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnSend) {
			names += counter + ":  " + tfName.getText() + "\n";
			counter++;
			taQueue.setText(names);
			tfName.setText("");
		}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		frame = new JFrame("PUNCH MANIA");
		frame.setResizable(false);
		frame.setPreferredSize(new Dimension(500,600));
		frame.add(new UIQueue());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

