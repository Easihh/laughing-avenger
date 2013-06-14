
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ServerGUI extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextArea textArea;
	private String message;
	private Socket socket;
	private ObjectOutputStream out;
	/**
	 * Launch the application.
	 */
	//public static void main(String[] args) {
	//}
	public void append(String message){
		textArea.setText(textArea.getText()+message+"\n");
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
			public void keyPressed(KeyEvent ev) {
				int key= ev.getKeyCode();
				if(key==KeyEvent.VK_ENTER){
					message=textField.getText();
					textArea.setText(textArea.getText()+"You:"+textField.getText()+"\n");
					textField.setText("");
					try {
						out.writeObject(new ChatMessage("Server:"+message));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		panel.add(textField);
		textField.setColumns(10);
		pack();
	}
	public void writeMsg(ObjectOutputStream out) {
		this.out=out;
	}

}
