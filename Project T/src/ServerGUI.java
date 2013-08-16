
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
import java.net.Socket;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ServerGUI extends JFrame {

	private JPanel contentPane;
	public JTextField textField;
	public JTextArea textArea;
	private String message;
	private Socket socket;
	public ObjectOutputStream out;
	private JPanel control_panel;
	private JButton btnStartGame;
	/**
	 * Launch the application.
	 */
	//public static void main(String[] args) {
	//}
	public void append(String From,String message){
		textArea.setText(textArea.getText()+From+":"+message+"\n");
	}
	public String getMessage(){
		return message;
	}
	public void setMessage(String msg){
		message=msg;
	}
	/**
	 * Create the frame.
	 */
	public ServerGUI() {
		setTitle("Server Client");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
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
		textArea.setColumns(20);
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
					message=textField.getText();
					textArea.setText(textArea.getText()+"Server:"+textField.getText()+"\n");
					textField.setText("");
					broadcast("Server",1,message);
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
				for(int i=0;i<Server.al.size();i++){
					if(!Server.al.get(i).socket.isClosed())
					try{
						Server.al.get(i).out.writeObject(new ChatMessage("Server",1,"Start"));
					}catch (IOException e){
						e.printStackTrace();
					}
				}
			}
		});
		control_panel.add(btnStartGame);
		pack();
	}
	public void writeMsg(ObjectOutputStream out) {
		this.out=out;
	}
	static void broadcast(String from,int type,String theMessage){
		for(int i=Server.al.size()-1;i>=0;i--){
			//if(Server.al.get(i).socket.isClosed())
				//disconnect(i);
			//else//if(!Server.al.get(i).socket.isClosed())
		try {
		Server.al.get(i).out.writeObject(new ChatMessage(from,type,theMessage));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	}
	public void disconnect(int id){
		Server.al.remove(id);
	}
}
