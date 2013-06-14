
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	private static ServerGUI sg;
	private int port=4444;
	private ArrayList<ServerClient> al;
	public Server(int portnumber){
		sg=new ServerGUI();
		this.port=portnumber;
		 al=new ArrayList<ServerClient>();
	}
	public static void main(String[] args) throws IOException{
		new Server(4444).start();
	}
	public void start(){
		try{
			ServerSocket server= new ServerSocket(port);
			System.out.println("waiting for clients...");
			while(true){
				Socket s=server.accept();
				System.out.println("Client connected from "+ s.getLocalAddress().getHostName());
				ServerClient sc=new ServerClient(s);
				al.add(sc);
				sc.start();
			}
		}
		catch(Exception e){
			System.out.println("an error has occured");
			e.printStackTrace();
		}
	}
	static class ServerClient extends Thread{
		private Socket socket;
		ObjectInputStream in;
		ObjectOutputStream out;
		private boolean keepGoing=true;
		public ServerClient(Socket s){
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
							cm=(ChatMessage) in.readObject();
							sg.append(cm.getMessage());
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

