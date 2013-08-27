
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class Server {
	static Database myDatabase;
	public static ServerGUI sg;
	private int port=4444;
	public static ArrayList<ServerClient> al;
	static int id;
	static boolean isRunning;
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
			new ConnectionCheck().start();
			System.out.println("waiting for clients...");
			while(true){
				Socket s=server.accept();
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
		Socket socket;
		String player_Name;
		ObjectInputStream in;
		ObjectOutputStream out;
		public ServerClient(Socket s){
		socket=s;
		try {
			out=new ObjectOutputStream(socket.getOutputStream());
			in=new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
				}
		}
		public void run() {
			ChatMessage cm=null;
			isRunning=true;
				while(isRunning){
						try {
							cm=(ChatMessage) in.readObject();
							if(cm.getFrom().equalsIgnoreCase(player_Name))sg.append(cm.getFrom(),cm.getMessage());
							if(cm.getType()==1)
							ServerGUI.broadcast(cm.getFrom(),cm.getType(),cm.getMessage());
							if(cm.getType()==2){
								try {
									player_Name=myDatabase.getPlayerName(cm.getFrom(), cm.getMessage());
									if(player_Name!=null){
										myDatabase.updateOnlineStatus(player_Name,1);
										sg.append(player_Name, " has connected");
										out.writeObject(new ChatMessage(player_Name,2,"OK"));
									}
									else {
										out.writeObject(new ChatMessage(player_Name,2,"NOT OK"));
										close();
										break;
									}
								} catch (SQLException e) {
									e.printStackTrace();
								}
							}
							if(cm.getType()==7){
								ArrayList<Integer> result=myDatabase.diceRoll(cm.getFrom());
								for(ServerClient client:al){
									client.out.writeObject(new ChatMessage(cm.getFrom(),8,result,sg.startingPlayer));
								}
							}
							if(cm.getType()==9){
								for(ServerClient client:al){
									client.out.writeObject(new ChatMessage(sg.startingPlayer,9,"Current Player Turn"));
								}
							}
							if(cm.getType()==10){
								Boolean valid=myDatabase.isValidBid(cm.getBid());
								if(valid){
								myDatabase.updateBid(cm.getFrom(),cm.bid);
									for(ServerClient client:al){
										client.out.writeObject(new ChatMessage(cm.getFrom(),11,cm.bid));
									}
								}//invalid bid
								else{
									JFrame MessageError=new JFrame();
									JOptionPane.showMessageDialog(MessageError, "Invalid bid","Error 2",JOptionPane.ERROR_MESSAGE);
								}
							}
							if(cm.getType()==12){
								String nextPlayer=sg.getNextTurn(cm.getFrom());
								sg.startingPlayer=nextPlayer;
								for(ServerClient client:al){
									client.out.writeObject(new ChatMessage(nextPlayer,13,"next turn"));
								}
							}
							if(cm.getType()==14){
								int DiceAmount,newDiceAmount;
								String bet_loser;
								String playerLying=myDatabase.whoLie();
								Bid maxbid=myDatabase.getBid(playerLying);
								if(maxbid.number>0 && maxbid.amount>0){
									Boolean liar=myDatabase.isLiar();
									Queue<Player> test=new LinkedList<Player>();
									if(liar)bet_loser=playerLying;//top bidder is lying
									else bet_loser=cm.getFrom();//top bidder is telling truth
									myDatabase.updateDice(bet_loser);
									int Player_remain=myDatabase.getPlayerRemaining();
									newDiceAmount=myDatabase.getDiceLeft(bet_loser);
									while(!sg.player.isEmpty()){
										Player x=sg.player.remove();
										if(x.name.equalsIgnoreCase(bet_loser)){
											DiceAmount=newDiceAmount;
											test.add(new Player(x.name,DiceAmount,0,0));
										}
										else {
											DiceAmount=x.numberof_Diceleft;
											test.add(new Player(x.name,DiceAmount,0,0));
											}
									}
									sg.player.addAll(test);
									for(ServerClient client:al){
										if(Player_remain>=2){
										client.out.writeObject(new ChatMessage(player_Name,17,"Call end Turn"));
										client.out.writeObject(new ChatMessage(playerLying,14,""+liar,test));
										client.out.writeObject(new ChatMessage(player_Name,15,"Reset play"));
										}
									if(newDiceAmount==0)
										client.out.writeObject(new ChatMessage("<Server>",1," Player "+bet_loser+" has been defeated"));
									if(Player_remain==1){
										String playername=myDatabase.getLastPlayerName();
										client.out.writeObject(new ChatMessage("<Server>",1,playername+" has won the game"));
										client.out.writeObject(new ChatMessage("<Server>",16,playername,test));
									}
								}
							}
						}
							if(cm.getType()==15){
								myDatabase.resetDice();
								for(ServerClient client:al)
									client.out.writeObject(new ChatMessage("<Server>",15,player_Name));
							}
							if(cm.getType()==16){
								String playerLying=myDatabase.whoLie();
								Bid maxbid=myDatabase.getBid(playerLying);
								ArrayList<String> newDefeatedPlayer=new ArrayList<String>();
								if(maxbid.number>0 && maxbid.amount>0){
									Boolean liar=myDatabase.isTellingTruth();
									myDatabase.updateTruth(player_Name,liar,maxbid,playerLying);
									Queue<Player>mylist=myDatabase.getPlayerList();
									int Player_remain=myDatabase.getPlayerRemaining();
									while(!sg.player.isEmpty()){
										while(!sg.player.peek().name.equalsIgnoreCase(mylist.peek().name)){
											mylist.add(mylist.remove());
										if(sg.player.peek().numberof_Diceleft>0 && mylist.peek().numberof_Diceleft==0){
													newDefeatedPlayer.add(sg.player.peek().name);
												}
										}
										sg.player.remove();
									}
									sg.player.addAll(mylist);
									for(ServerClient client:al){
										if(Player_remain>=2){
										client.out.writeObject(new ChatMessage(player_Name,17,"Call end Turn"));
										client.out.writeObject(new ChatMessage(player_Name,14,""+liar,mylist));
										client.out.writeObject(new ChatMessage(player_Name,15,"Reset play"));
										}
										for(String name:newDefeatedPlayer)
											client.out.writeObject(new ChatMessage("<Server>",1," Player "+name+" has been defeated"));
										if(Player_remain==1){
											String playername=myDatabase.getLastPlayerName();
											client.out.writeObject(new ChatMessage("<Server>",1,playername+" has won the game"));
											client.out.writeObject(new ChatMessage("<Server>",16,playername,mylist));
										}
									}
								}
							}
							if(sg.textArea.getText().length()!=0)sg.textArea.setCaretPosition(sg.textArea.getText().length()-1);
							}
						catch (IOException e) {
							close();
							break;
						}
						catch (ClassNotFoundException e) {
							e.printStackTrace();
							break;
						} catch (SQLException e) {
							e.printStackTrace();
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
