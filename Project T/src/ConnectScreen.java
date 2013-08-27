import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.BorderLayout;


public class ConnectScreen extends JPanel {
	private JTextField JAccount;
	private ConnectScreen frame;
	private JPasswordField JPassword;
	public ConnectScreen(){
		frame=this;
		//setDefaultCloseOperation(EXIT_ON_CLOSE);
		//setSize(500,500);
		//getContentPane().setLayout(null);
		setVisible(true);
		setLayout(null);
		JAccount = new JTextField();
		JAccount.setBounds(189, 183, 150, 20);
		add(JAccount);
		JAccount.setColumns(10);
		JLabel Accountlbl = new JLabel("Account");
		Accountlbl.setBounds(119, 186, 60, 14);
		add(Accountlbl);
		
		JLabel Passwordlbl = new JLabel("Password");
		Passwordlbl.setBounds(119, 215, 60, 14);
		add(Passwordlbl);
		JButton bntLogin = new JButton("Login");
		bntLogin.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent ev) {
				//Client.cg=new ClientGUI(JAccount.getText(),JPassword.getPassword());
				Client.client.start(JAccount.getText(),JPassword.getPassword());
					//frame.setVisible(false);
					//Client.cg.startGame();
			}
		});
		bntLogin.setBounds(215, 255, 89, 23);
		add(bntLogin);
		
		JPassword = new JPasswordField();
		JPassword.setEchoChar('*');
		JPassword.setBounds(189, 215, 150, 20);
		add(JPassword);
		repaint();
	}
}
