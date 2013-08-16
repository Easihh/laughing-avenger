import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JButton;


public class Chat_panel extends JPanel {
	public JTextArea textArea;
	private JTextField textField;
	private JScrollPane scrollPane;
	private String message;
	private String username;
	private JPanel panel;

	/**
	 * Create the panel.
	 */
	public Chat_panel(String user) {
		username=user;
		ClientGUI.chat=this;
		setLayout(new BorderLayout(0, 0));	
		panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		panel.setLayout(new BorderLayout(0, 0));
		JScrollPane scrollPane_1 = new JScrollPane();
		panel.add(scrollPane_1, BorderLayout.CENTER);
		scrollPane_1.setAlignmentX(Component.LEFT_ALIGNMENT);
		scrollPane_1.setAlignmentY(Component.TOP_ALIGNMENT);
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		textArea.setRows(5);
		textArea.setColumns(15);
		scrollPane_1.add(textArea);
		scrollPane_1.setViewportView(textArea);
		textField = new JTextField();
		add(textField, BorderLayout.SOUTH);
		textField.setColumns(10);
		textField.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent ev) {
				int key=ev.getKeyCode();
				if(key==KeyEvent.VK_ENTER){
				message=textField.getText();
				//textArea.setText(textArea.getText()+username+":"+textField.getText()+"\n");
				textField.setText("");
				try {
					Client.out.writeObject(new ChatMessage(username,1,message));
				} catch (IOException e) {
					e.printStackTrace();
				}
				}
			}
		});
		ClientGUI.contentPane.add(this);
	}

}
