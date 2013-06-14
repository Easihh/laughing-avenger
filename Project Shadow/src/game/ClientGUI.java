package game;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class ClientGUI extends JFrame {
	private JTextField textField;
	private JTextArea textArea;
	private String message;
	private JPanel contentPane;
	private ObjectOutputStream out;
	public ClientGUI() {
		super("Client");
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		//contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setRows(10);
		textArea.setColumns(15);
		scrollPane.add(textArea);
		scrollPane.setViewportView(textArea);
		contentPane.add(scrollPane, BorderLayout.NORTH);
		//contentPane.add(textArea, BorderLayout.SOUTH);
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		
		textField = new JTextField();
		textField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ev) {
				int key=ev.getKeyCode();
				if(key==KeyEvent.VK_ENTER){
				message=textField.getText();
				textArea.setText(textArea.getText()+"You:"+textField.getText()+"\n");
				textField.setText("");
				try {
					out.writeObject(new ChatMessage("Client:"+message));
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
	public void setTextArea(String message){
		textArea.setText(textArea.getText()+message+"\n");		
	}
	public void setupWrite(ObjectOutputStream out) {
		this.out=out;
	}
}
