import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class Chat  extends JPanel{
	private static final long serialVersionUID = 3727597429246173997L;
	public JTextField textField;
	public static JTextArea textArea;
	public Chat(){
		setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);
		
		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setRows(6);
		textArea.setColumns(15);
		scrollPane.add(textArea);
		scrollPane.setViewportView(textArea);
	
		textField = new JTextField();
		add(textField, BorderLayout.SOUTH);
		textField.setColumns(10);
		textField.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent ev) {
				if(ev.getKeyCode()==KeyEvent.VK_ENTER){
					try {
						Client.out.writeObject(new ChatMessage(ClientGUI.username,1,textField.getText()));
					} catch (IOException e) {
						e.printStackTrace();
					}
					textField.setText("");
				}
			}
		});
		}
	}
