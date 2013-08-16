import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class ConnectScreen extends JPanel {
	private JTextField JAccount;
	private JTextField JPassword;
	public ConnectScreen(){
		setPreferredSize(new Dimension(500,500));
		setAlignmentY(Component.TOP_ALIGNMENT);
		setAlignmentX(Component.LEFT_ALIGNMENT);
		setLayout(null);
		JAccount = new JTextField();
		JAccount.setBounds(189, 183, 150, 20);
		add(JAccount);
		JAccount.setColumns(10);
		
		JPassword = new JTextField();
		JPassword.setBounds(189, 212, 150, 20);
		add(JPassword);
		JPassword.setColumns(10);
		JLabel Accountlbl = new JLabel("Account");
		Accountlbl.setBounds(119, 186, 60, 14);
		add(Accountlbl);
		
		JLabel Passwordlbl = new JLabel("Password");
		Passwordlbl.setBounds(119, 215, 60, 14);
		add(Passwordlbl);
		JButton bntLogin = new JButton("Login");
		bntLogin.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent ev) {
				ClientGUI.username=JAccount.getText();
				ClientGUI.password=JPassword.getText();
				setVisible(false);
				ClientGUI.chat=new Chat_panel(JAccount.getText());
				//connect();
				Client.client.start();
				
			}
		});
		bntLogin.setBounds(215, 255, 89, 23);
		add(bntLogin);
	}
}
