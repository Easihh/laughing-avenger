import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JPasswordField;


public class ConnectScreen extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7355877931403219143L;
	private JTextField JAccount;
	private JPasswordField JPassword;
	public ConnectScreen(){
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
				Client.client.start(JAccount.getText(),JPassword.getPassword());
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
