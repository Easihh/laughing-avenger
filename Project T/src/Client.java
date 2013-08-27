
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Client extends JFrame {
	/**
	 * 
	 */
	Socket socket;
	static Client client;
	static ObjectInputStream in;
	static ObjectOutputStream out;
	static Boolean isRunning=true;
	private static final long serialVersionUID = 1L;
	private static int PORT = 4444;
	private final static String HOST = "96.21.2.128";
	private ClientGUI cg;
	
	public boolean start(String username,char[] password){
		String mypassword="";
		ClientGUI.username=username;
		for(int i=0;i<password.length;i++)
			mypassword+=password[i];
		try 
		{		
			socket= new Socket(HOST, PORT);	
		} 
		catch (Exception noServer)
		{
			JOptionPane.showMessageDialog(this, "Server is down","Error 1",JOptionPane.ERROR_MESSAGE);
			return false;
		}
		//connection accepted
		try{
			in  = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(new ChatMessage(username,2,mypassword));
		}
		catch (IOException eIO) {
			System.out.println("Exception creating new Input/output Streams: " + eIO);
			return false;
		}
		new ListenServer().start();
		return true;
	}
	public static void main(String[] args) {
		client=new Client(4444,new ClientGUI());
		//if(!client.start())
			//return;
	}
	/**
	 * Create the frame.
	 */
	public Client(int port,ClientGUI cg) {
		PORT=port;
		this.cg=cg;
	}
	class ListenServer extends Thread{
		public void run() {
			ChatMessage cm;
			isRunning=true;
					while (isRunning){
						try{
							cm=(ChatMessage)in.readObject();
							if(cm.getType()==1)Chat.textArea.setText(Chat.textArea.getText()+"\n"+"<"+cm.getFrom()+">"+" "+cm.getMessage());
							if(cm.getType()==2)cg.startGame(cm.getMessage());
							if(cm.getType()==6){
								cg.setPlayerList(cm.getPlayer());
								cg.setupGame();
								}
							if(cm.getType()==8 && cm.getFrom().equalsIgnoreCase(ClientGUI.username)){
								Game_view.dice=cm.dice_Result;
								cg.panel.checkTurn(cm.getMessage());
								cg.panel.repaint();
								}
							if(cm.getType()==9){
								cg.panel.checkTurn(cm.getMessage());
								}
							if(cm.getType()==11){
								Game_view.bid=cm.bid;
								cg.panel.updateLabel(cm.getFrom());
								cg.panel.endTurn(cm.getFrom());
								}
							if(cm.getType()==13){
								cg.panel.checkTurn(cm.getFrom());
							}
							if(cm.getType()==14){
								cg.setPlayerList(cm.getPlayer());
								cg.panel.isLiar(cm.getFrom(),cm.getMessage());
							}
							if(cm.getType()==15) cg.panel.resetPlay();
							if(cm.getType()==16){
								cg.setPlayerList(cm.getPlayer());
								cg.panel.endGame(cm.getMessage());
							}
							if(cm.getType()==17)cg.panel.endTurn(cm.getFrom());
							if(cg.theChat!=null &&Chat.textArea.getText().length()!=0)Chat.textArea.setCaretPosition(Chat.textArea.getText().length()-1);
						}
						catch(SocketException Se){
							JFrame MessageError=new JFrame();
							JOptionPane.showMessageDialog(MessageError, "Connection to Server has been lost.","Error 999",JOptionPane.ERROR_MESSAGE);
							break;
						}
						catch (IOException e) {
								e.printStackTrace();
								break;
							} 
						catch (ClassNotFoundException e) {
								e.printStackTrace();
								break;
							}
						}
				}
		}
}

