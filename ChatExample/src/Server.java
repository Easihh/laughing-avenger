
import java.awt.TextArea;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.ArrayList;

public class Server {
	static Database myDatabase;
	private static ServerGUI sg;
	private int port=4444;
	public static ArrayList<ServerClient> al;
	static int id;
	public Server(int portnumber){
		sg=new ServerGUI();
		this.port=portnumber;
		 al=new ArrayList<ServerClient>();
	}
	public static void main(String[] args) throws IOException{
		myDatabase=new Database();
		new Server(4444).start();;
	}
	public void start(){
		try{
			ServerSocket server= new ServerSocket(port);
			//Serverconnection serconnect= new Serverconnection(s);
			new ConnectionCheck().start();
			System.out.println("waiting for clients...");
			while(true){
				Socket s=server.accept();
				System.out.println("Client connected from "+ s.getLocalAddress().getHostName());
				ServerClient sc=new ServerClient(s);
				al.add(sc);
				sc.start();
			}
		}
		catch(BindException be){
			System.exit(1);
		}
		catch(Exception e){
			System.out.println("an error has occured");
			e.printStackTrace();
		}
	}
	
	static class ServerClient extends Thread{
		//private int client_id;
		Socket socket;
		String player_Name;
		ObjectInputStream in;
		ObjectOutputStream out;
		//private boolean keepGoing=true;
		public ServerClient(Socket s){
		//id++;
		//client_id=id;
		socket=s;
		try {
			out=new ObjectOutputStream(socket.getOutputStream());
			in=new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
				}
		sg.writeMsg(out);
		}
		public void run() {
			ChatMessage cm;
			String msg;
				while(true){
						try {
							//sg.out.writeObject(new ChatMessage("Server",5,"checkConnection"));
							cm=(ChatMessage) in.readObject();
							sg.append(cm.getFrom(),cm.getMessage());
							if(cm.getType()==1)
							sg.broadcast(cm.getFrom(),cm.getType(),cm.getMessage());
							if(cm.getType()==2){
								try {
									player_Name=myDatabase.getPlayerName(cm.getFrom(), cm.getMessage());
									sg.append(player_Name, " has connected");
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}
							if(cm.getType()==3){
								//int playerHp=myDatabase.getPlayer_Hp(cm.getFrom(), 13);
								//myDatabase.setPlayer_Hp(cm.getFrom(), 13, 2);
								if(myDatabase==null)System.out.println("null");
								sg.broadcast(cm.getFrom(), 4, "Update Life");
							}
							sg.textArea.setCaretPosition(sg.textArea.getText().length()-1);
							}
						catch (IOException e) {
							//e.printStackTrace();
							close();
							break;
						}
						catch (ClassNotFoundException e) {
							e.printStackTrace();
							break;
						}
					}
				}
	public void close(){
		 try {
			 if(out != null) out.close();
			 }
			catch(Exception e) {}
		try {
			 if(in != null) in.close();
			}
			catch(Exception e) {};
		try{
			if(socket != null) socket.close();
			}
			catch(Exception e) {};
	}
	}
}
