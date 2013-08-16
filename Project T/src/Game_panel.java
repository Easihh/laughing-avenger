import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JLabel;


public class Game_panel extends JPanel {
	private Chat_panel thechat;
	private int enemylife;
	public Game_panel() {
		enemylife=99;
		setPreferredSize(new Dimension(500,500));
		setLayout(new BorderLayout(0, 0));	
		JPanel panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(null);
		
		JButton btnAttack = new JButton("Attack");
		btnAttack.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent ev) {
				try {
					Client.out.writeObject(new ChatMessage(ClientGUI.username,3,"Attack"));
				} catch (IOException e) {
					e.printStackTrace();
				}	
			}
		});
		btnAttack.setBounds(200, 300, 89, 23);
		panel.add(btnAttack);
		
		JLabel lbl_Hp = new JLabel("HP:"+ClientGUI.life);
		lbl_Hp.setBounds(200, 347, 89, 14);
		panel.add(lbl_Hp);
		
		JLabel lblEnemy_Hp = new JLabel("Enemy HP:"+enemylife);
		lblEnemy_Hp.setBounds(200, 150, 89, 14);
		panel.add(lblEnemy_Hp);
		thechat=new Chat_panel(ClientGUI.username);
		add(thechat,BorderLayout.SOUTH);
	}

}
