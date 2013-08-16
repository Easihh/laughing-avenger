import java.io.IOException;

public class ConnectionCheck extends Thread{
	public void run(){
	long nano=1000000000L;
	int second_till_update=5;
	long prev=System.nanoTime();
	long time_passed=0;
	boolean running=true;
	while(running){
		//time_passed=prev;
		if((System.nanoTime()-prev)/nano>=second_till_update){
			System.out.println(System.nanoTime()-prev);
			prev=System.nanoTime();
			for(int i=Server.al.size()-1;i>=0;i--){
				if(Server.al.get(i).socket.isClosed()){
					disconnect(i);
					} else
						try {
							Server.al.get(i).out.writeObject(new ChatMessage("Server",5,"Checking Connection"));
						} catch (IOException e) {
							e.printStackTrace();
					//running=false;
						}
					}
			//System.out.println(time_passed);
			//prev=System.nanoTime();
		}
	}
	}
	public void disconnect(int id){
			Server.al.remove(id);
			ServerGUI.broadcast("Server", 1, "Player:"+id+ " has been disconnected");
		}
	}