
import java.io.Serializable;

public class ChatMessage implements Serializable{
	private String message;
	private String from;
	private int type;
	/** Type 1= Client to Server to Client
	 * 	Type 2= Client to Server
	 * 	Type 3= Client to Server Attack
	 * 	Type 4= Server to Client Update Life
	 **/
	public ChatMessage(String from,int type,String message){
		this.message=message;
		this.from=from;
		this.type=type;
	}
	public String getMessage(){
		return message;
	}
	public String getFrom(){
		return from;
	}
	public int getType(){
		return type;
	}
}
