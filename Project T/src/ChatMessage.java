import java.io.Serializable;
import java.util.ArrayList;
import java.util.Queue;

public class ChatMessage implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8060724970436876123L;
	private String message;
	private String from;
	private int type;
	private Queue<Player> player_list;
	ArrayList<Integer> dice_Result;
	public Bid bid;
	public int dice_amount;
	public ChatMessage(String from,int type,String message){
		this.message=message;
		this.from=from;
		this.type=type;
	}
	public ChatMessage(String from,int type,Queue<Player> list){
		this.from=from;
		this.type=type;
		player_list=list;
	}
	public ChatMessage(String from,int type,ArrayList<Integer> list,String message){
		this.from=from;
		this.type=type;
		this.message=message;
		dice_Result=list;
	}
	public ChatMessage(String from,int type,Bid thebid){
		this.from=from;
		this.type=type;
		bid=thebid;
		message="Biding";
	}
	public ChatMessage(String from,int type,String message,Queue<Player> playerlist){
		this.from=from;
		this.type=type;
		this.message=message;
		player_list=playerlist;
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
	public Bid getBid(){
		return bid;
	}
	public Queue<Player> getPlayer(){
		return player_list;
	}
}
