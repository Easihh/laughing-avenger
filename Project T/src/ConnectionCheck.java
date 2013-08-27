import java.io.IOException;
import java.net.SocketException;
import java.sql.SQLException;

public class ConnectionCheck extends Thread{
	Database mydatabase=new Database();
	public void run(){
	long nano=1000000000L;
	int second_till_update=3;
	long prev=System.nanoTime();
	boolean running=true;
	while(running){
		if((System.nanoTime()-prev)/nano>=second_till_update){
			//System.out.println(System.nanoTime()-prev);
			prev=System.nanoTime();
			for(int i=Server.al.size()-1;i>=0;i--){
				if(Server.al.get(i).socket.isClosed()){
					disconnect(i);
					} else
						try {
							Server.al.get(i).out.writeObject(new ChatMessage("<Server>",5,"Checking Connection"));
						} catch (IOException e) {
							e.printStackTrace();
							disconnect(i);
						}
				}
			}
		}
	}
	public void disconnect(int id){
			String player=Server.al.get(id).player_Name;
			try {
				mydatabase.updateOnlineStatus(player, 0);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			Server.al.get(id).close();
			Server.al.remove(id);
			ServerGUI.broadcast("<Server>", 1, player+ " has been disconnected");
			Server.sg.textArea.setText(Server.sg.textArea.getText()+"\nPlayer:"+player+" has been disconnected");
			Server.sg.textArea.setCaretPosition(Server.sg.textArea.getText().length()-1);
		}
	}