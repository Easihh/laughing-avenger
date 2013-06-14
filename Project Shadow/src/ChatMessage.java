
import java.io.Serializable;

public class ChatMessage implements Serializable{
	private int number=0;
	private String message;
	public ChatMessage(String message){
		this.message=message;
	}
	public String getMessage(){
		return message;
	}
}
