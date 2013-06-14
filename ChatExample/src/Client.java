
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import javax.swing.JFrame;

public class Client extends JFrame {
	/**
	 * 
	 */
	Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private static final long serialVersionUID = 1L;
	private static int PORT = 4444;
	private final static String HOST = "localhost";
	private ClientGUI cg;
	/**
	 * Launch the application.
	 */
	public boolean start(){
		try 
		{		
			socket= new Socket(HOST, PORT);	
			System.out.println("You connected to " + HOST);		
		} 
		catch (Exception noServer)
		{
			System.out.println("The server might not be up at this time.");
			System.out.println("Please try again later.");
			return false;
		}
		cg.setTextArea("Connection Accepted");
		try{
			in  = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
			cg.setTextArea("Stream Sucess");
		}
		catch (IOException eIO) {
			System.out.println("Exception creating new Input/output Streams: " + eIO);
			return false;
		}
		new ListenServer().start();
		cg.setupWrite(out);
		return true;
	}
	public static void main(String[] args) {
		Client client=new Client(4444,new ClientGUI());
		if(!client.start())
			return;
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
							cg.setTextArea(cm.getMessage());
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

