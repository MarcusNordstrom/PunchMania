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

public class ServerUI extends JPanel{
	
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
		
		add(taCmdArea,BorderLayout.CENTER);
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
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					readCmd();
				}
				
			}

			private void readCmd() {
				// TODO Auto-generated method stub
				
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
		frame.setPreferredSize(new Dimension(500,600));
		frame.add(new ServerUI());
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
