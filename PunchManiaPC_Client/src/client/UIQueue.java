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
	private Client client;


	/**
	 * Creating panel
	 */
	public UIQueue(Client client) {
		this.client = client;
		
		JFrame frame = new JFrame();
		frame = new JFrame("PUNCH MANIA");
		frame.setResizable(false);
		frame.setPreferredSize(new Dimension(500,600));
		frame.add(this);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.add(panelCenter(), BorderLayout.CENTER);
		frame.add(panelSouth(), BorderLayout.SOUTH);
		
		btnSend.addActionListener(this);
	}
	
	
	/**
	 * 1 of 2 parts of the panel
	 * @return panel in center
	 */
	private JPanel panelCenter() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(lblQueue, BorderLayout.NORTH);
		panel.add(taQueue, BorderLayout.CENTER);

		taQueue.setEditable(false);
		return panel;
	}


	/**
	 * 1 of 2 parts of the panel
	 * @return panel in bottom
	 */
	private JPanel panelSouth() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(lblName, BorderLayout.NORTH);
		panel.add(tfName, BorderLayout.CENTER);
		panel.add(btnSend, BorderLayout.SOUTH);
		return panel;
	}


	/**
	 * When pushing the button a new string will be written on a new line in the queue list.
	 * The string is taken from the text field. 
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnSend) {
			names = tfName.getText();
			client.sendUser(names); 							
			
			tfName.setText("");
		}
	}


	/**
	 * Updates the queue list from the client. 
	 * @param name  : a string from client adding to queue list. 
	 */
	public void updateQueue(String name) {
		taQueue.setText(name);

	}
	
	
	
}