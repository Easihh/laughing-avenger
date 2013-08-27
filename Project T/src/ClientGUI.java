

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.Queue;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import java.awt.Font;
import javax.swing.JMenuItem;
public class ClientGUI extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 26255196293192271L;
	public Game_view panel;
	public Chat theChat;
	private JPanel contentPane;
	public JPanel game_view;
	static int numberof_Dice;
	static int number;
	static ArrayList<Player> player_list;
	static String username;
	static String password="";
	public ClientGUI() {
		super("Project T:Client");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(600,600));
		
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		JMenu mnNewMenu = new JMenu("Option");
		mnNewMenu.setFont(new Font("Segoe UI", Font.PLAIN, 11));
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Option1");
		mnNewMenu.add(mntmNewMenuItem);
		contentPane = new ConnectScreen();	
		setContentPane(contentPane);
		contentPane.setLayout(null);
		pack();
	}
	public void startGame(String status) {
		if(status.equalsIgnoreCase("OK")){
			
		contentPane = new JPanel();
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		theChat = new Chat();
		contentPane.add(theChat,BorderLayout.SOUTH);
		pack();
		}
		else JOptionPane.showMessageDialog(this, "Login failed.","ERROR 3",JOptionPane.ERROR_MESSAGE);
	}
	public void setTextArea(String message){
		Chat.textArea.setText(Chat.textArea.getText()+message+"\n");	
		Chat.textArea.setCaretPosition(Chat.textArea.getText().length()-1);
	}
	public void updateHp(String from,int value){
		if(from.equalsIgnoreCase(username)){
		}
	}
	public void setupGame(){
		panel = new Game_view();
		contentPane.add(panel, BorderLayout.CENTER);
		pack();
	}
	public void setPlayerList(Queue<Player> playerlist){
		player_list=new ArrayList<Player>();
		player_list.addAll(playerlist);
	}
}
