
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;

import javax.swing.JDialog;
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
	private static final long serialVersionUID = 1L;
	private static int PORT = 4444;
	private final static String HOST = "96.21.2.128";
	private ClientGUI cg;
	/**
	 * Launch the application.
	 */
	public boolean start(){
		try 
		{		
			socket= new Socket(HOST, PORT);	
			cg.setTextArea("You connected to " + HOST);
			//new ChatMessage(cg.username,1," has connected from "+socket.getInetAddress().getHostAddress());
			//cg.setTextArea("You connected from " + socket.getInetAddress().getHostAddress());		
		} 
		catch (Exception noServer)
		{
			cg.setTextArea("The server might not be up at this time.");
			cg.setTextArea("Please try again later.");
			//System.out.println("The server might not be up at this time.");
			//System.out.println("Please try again later.");
			return false;
		}
		//cg.setTextArea("Connection Accepted");
		try{
			in  = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
			out.writeObject(new ChatMessage(cg.username,2,cg.password));
			//cg.setTextArea("Stream Sucess");
		}
		catch (IOException eIO) {
			cg.setTextArea("Exception creating new Input/output Streams: " + eIO);
			//System.out.println("Exception creating new Input/output Streams: " + eIO);
			return false;
		}
		new ListenServer().start();
		//cg.setupWrite(out);
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
					while (true){
						try{
							//System.out.println((String)in.readObject());
							//if(in.available()>0){
							cm=(ChatMessage)in.readObject();
							//if(cm.getMessage()!=null){//cg.setTextArea(cm.getMessage());
							if(cm.getMessage().equalsIgnoreCase("Start"))
								cg.setupGame();
							if(cm.getType()==1)cg.setTextArea(cm.getFrom()+":"+cm.getMessage());
							if(cm.getType()==4)cg.updateHp(cm.getFrom(),999);
							if(cm.getType()==5)cg.setTextArea(cm.getFrom()+":"+cm.getMessage());
							}
						catch(SocketException Se){
							JFrame MessageError=new JFrame();
							JOptionPane errMessage=new JOptionPane();
							errMessage.showMessageDialog(MessageError, "Connection to Server has been lost.","Error 999",errMessage.ERROR_MESSAGE);
							break;
						}
						catch (IOException e) {
								e.printStackTrace();
							} 
						catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
						}
				}
		}
}

