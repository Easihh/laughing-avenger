

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.FlowLayout;
import javax.swing.BoxLayout;
import javax.swing.JTextPane;
import javax.swing.JEditorPane;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;

public class ClientGUI extends JFrame {
	private JTextField textField;
	private JTextArea textArea;
	private String message;
	static JPanel contentPane;
	//static ObjectOutputStream out;
	private ConnectScreen connect_panel;
	static Chat_panel chat;
	static int life;
	//private JTextField JAccount;
	//private JTextField JPassword;
	static String username;
	static String password;
	public ClientGUI() {
		super("Client");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setBounds(100, 100, 450, 300);
		setSize(500,500);
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		
		connect_panel = new ConnectScreen();
		//game_panel.setAlignmentY(Component.TOP_ALIGNMENT);
		//game_panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		//game_panel.setPreferredSize(new Dimension(500,500));
		contentPane.add(connect_panel);
		//game_panel.setLayout(null);
		
		//JAccount = new JTextField();
		//JAccount.setBounds(189, 183, 150, 20);
		//game_panel.add(JAccount);
		//JAccount.setColumns(10);
		
		//JPassword = new JTextField();
		//JPassword.setBounds(189, 212, 150, 20);
		//game_panel.add(JPassword);
		//JPassword.setColumns(10);
		
		//JLabel Accountlbl = new JLabel("Account");
		//Accountlbl.setBounds(119, 186, 60, 14);
		//game_panel.add(Accountlbl);
		
		//JLabel Passwordlbl = new JLabel("Password");
		//Passwordlbl.setBounds(119, 215, 60, 14);
		//game_panel.add(Passwordlbl);
		
		//JButton bntLogin = new JButton("Login");
		pack();
		//bntLogin.addMouseListener(new MouseAdapter() {
			//public void mousePressed(MouseEvent ev) {
				//username=JAccount.getText();
				//connect();
				//Client.client.start();
				
			//}
		//});
		//bntLogin.setBounds(215, 255, 89, 23);
		//game_panel.add(bntLogin);

		//contentPane.add(textArea, BorderLayout.SOUTH);
	}
	public void setTextArea(String message){
		chat.textArea.setText(chat.textArea.getText()+message+"\n");	
		chat.textArea.setCaretPosition(chat.textArea.getText().length()-1);
	}
	public void updateHp(String from,int value){
		if(from.equalsIgnoreCase(username)){
			
		}
	}
	//public void setupWrite(ObjectOutputStream out) {
		//this.out=out;
		//System.out.println(out);
	//}
	/*public void connect(){	
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setAlignmentX(Component.LEFT_ALIGNMENT);
		scrollPane.setAlignmentY(Component.TOP_ALIGNMENT);
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setRows(5);
		textArea.setColumns(15);
		scrollPane.add(textArea);
		scrollPane.setViewportView(textArea);
		contentPane.add(scrollPane);
		
		JPanel panel = new JPanel();
		panel.setAlignmentX(Component.LEFT_ALIGNMENT);
		panel.setAlignmentY(Component.TOP_ALIGNMENT);
		contentPane.add(panel);
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ev) {
				int key=ev.getKeyCode();
				if(key==KeyEvent.VK_ENTER){
				message=textField.getText();
				//textArea.setText(textArea.getText()+"You:"+textField.getText()+"\n");
				textField.setText("");
				try {
					out.writeObject(new ChatMessage(username,1,message));
				} catch (IOException e) {
					e.printStackTrace();
				}
				}
			}
		});
		panel.setLayout(new BorderLayout(0, 0));
		panel.add(textField);
		textField.setColumns(25);
		pack();
	}*/
	public void setupGame(){
		life=99;
		JButton Attack=new JButton("Attack");
		JLabel hp=new JLabel("HP:"+life);
		hp.setBounds(200,200,100,100);
		//contentPane.add(Attack);
		setContentPane(new Game_panel());
		//contentPane.add(hp);
		//connect_panel.add(hp);
		//connect_panel.add(Attack);
		//chat.textArea.setText(chat.textArea.getText()+"Setup initialized\n");
		repaint();
		pack();
	}
	//public Testingshit getChat(){
		//return chat;
	//}
}
