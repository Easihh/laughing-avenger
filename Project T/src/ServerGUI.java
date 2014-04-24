import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Queue;
import javax.swing.JButton;

public class ServerGUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2001610855181071715L;
	private JPanel contentPane;
	public JTextField textField;
	public JTextArea textArea;
	public ObjectOutputStream out;
	private JPanel control_panel;
	private JButton btnStartGame;
	private Database theDatabase;
	String startingPlayer="";
	Queue<Player> player=new LinkedList<Player>();
	/**
	 * Launch the application.
	 */
	public void append(String From,String message){
		textArea.setText(textArea.getText()+"\n"+From+":"+message);
	}
	/**
	 * Create the frame.
	 */
	public ServerGUI() {
		setTitle("Server Client");
		setVisible(true);
		theDatabase=new Database();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.NORTH);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setRows(8);
		textArea.setColumns(25);
		scrollPane.add(textArea);
		scrollPane.setViewportView(textArea);
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public  void keyPressed(KeyEvent ev) {
				int key= ev.getKeyCode();
				if(key==KeyEvent.VK_ENTER){
					textArea.setText(textArea.getText()+"Server:"+textField.getText()+"\n");
					broadcast("Server",1,textField.getText());
					textField.setText("");
				}
			}
		});
		panel.add(textField);
		textField.setColumns(10);
		
		control_panel = new JPanel();
		contentPane.add(control_panel, BorderLayout.CENTER);
		
		btnStartGame = new JButton("Start Game");
		btnStartGame.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent ev) {
				try {
					theDatabase.setGame();
					player=theDatabase.getPlayerList();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
				startingPlayer=player.peek().name;
				for(int i=0;i<Server.al.size();i++){
					if(!Server.al.get(i).socket.isClosed())
					try{
						Server.al.get(i).out.writeObject(new ChatMessage("<Server>",6,player));
						Server.al.get(i).out.writeObject(new ChatMessage("<Server>",9,startingPlayer));
					}catch (IOException e){
						e.printStackTrace();
					}
				}
			}
		});
		control_panel.add(btnStartGame);
		pack();
	}
	static void broadcast(String from,int type,String theMessage){
		for(int i=Server.al.size()-1;i>=0;i--){
			if(!Server.al.get(i).socket.isClosed())
				try {Server.al.get(i).out.writeObject(new ChatMessage(from,type,theMessage));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

	public String getNextTurn(String lastPlayer){
		String nextPlayer="";
		player.add(player.remove());
		nextPlayer=player.peek().name;
		return nextPlayer;
	}
}
