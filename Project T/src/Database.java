//package game;
import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Scanner;
import java.util.Vector;

import javax.xml.bind.annotation.XmlElementRef.DEFAULT;
public class Database {
	Statement stmt=null;
	ResultSet rs=null;
	Connection connection;
	String server_ip="96.21.2.128";
	public Database(){
		connection=getConnection();
	}
	public Connection getConnection(){
	   	 try
		   {
		    Class.forName("com.mysql.jdbc.Driver").newInstance();
		    String Url = "jdbc:mysql://"+server_ip+":3306/info";
		    return DriverManager.getConnection(Url,"root","root");
		  }catch (Exception ex){
			  ex.printStackTrace();
			  return null;
		  }
	}
    public static void main(String[] args) {
    	Statement stmt=null;
    	ResultSet rs=null;
    	int test=34;
    	String playername="Sakura";
    	int sessionID=13;
    	 try
    	   {
    	    Class.forName("com.mysql.jdbc.Driver").newInstance();
    	    String Url = "jdbc:mysql://localhost:3306/info";
    	    Connection connection=DriverManager.getConnection(Url,"root","root");
        	int amount=0;
        	String name="Sakura";
        	String query="Select Dice_remains From testing WHERE Name='"+name+"'";
        	stmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
        	rs=stmt.executeQuery(query);
        	rs.first();
        	amount=rs.getInt(1);
        	System.out.println(amount);

    	    }catch(Exception ex){
    	   ex.printStackTrace();
    	  }
    	
    }
    public int getPlayer_Hp(String playername,int sessionID) throws SQLException{
    	int hp=-1;
    	String query="Select Hitpoint From testing WHERE Name='"+playername+"' AND Session_id='"+sessionID+"'";
    	//System.out.println(query);
    	stmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
    	rs=stmt.executeQuery(query);
    	rs.first();
    	hp=rs.getInt(1);
    	rs.close();
    	return hp;
    }
    public void setPlayer_Hp(String playername,int sessionID,int value) throws SQLException{
    	 String update="UPDATE testing SET Hitpoint='"+value+"' WHERE Name='"+playername+"' AND Session_id='"+sessionID+"'";
    	stmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
    	 stmt.executeUpdate(update);
    }
    public String getPlayerName(String playername,String password) throws SQLException{
    	String name=null;
    	String query="Select Name From testing WHERE Name='"+playername+"' AND Password='"+password+"'";
    	stmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
    	rs=stmt.executeQuery(query);
    	if(rs.next()){
    		name=rs.getString(1);
    	}
    	rs.close();
    	return name;
    }
    public Queue<Player> getPlayerList() throws SQLException{
    	Queue<Player> name=new LinkedList<Player>();
    	String query="Select Name,Dice_remains,NumberBet,Amount_bet From testing WHERE OnlineStatus='1' ";//ORDER BY RAND()
    	stmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
    	rs=stmt.executeQuery(query);
    	while(rs.next()){
    		name.add(new Player(rs.getString(1),rs.getInt(2),rs.getInt(3),rs.getInt(4)));
    	}
    	rs.close();
    	return name;
    }
    public void setGame() throws SQLException{
   	 String update="UPDATE testing SET Dice_remains='1',NumberBet='0',Amount_bet='0',Dice_1='0',Dice_2='0',Dice_3='0',Dice_4='0',Dice_5='0'";
   	 stmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
   	 stmt.executeUpdate(update);
    }
    public ArrayList<Integer> diceRoll(String name) throws SQLException{
    	Random myrandom=new Random();
    	ArrayList<Integer> roll=new ArrayList<Integer>();
    	int dice_amount=getDiceLeft(name);
    	String update="";
    	//int[] roll=new int[dice_amount];
    	for(int i=0;i<dice_amount;i++){
    		roll.add(myrandom.nextInt(6)+1);
    	}
    	update=getRoll(dice_amount,update,roll,name);
    	return roll;
    }
    public String getRoll(int dice_amount,String update,ArrayList roll,String name) throws SQLException{
    	switch(dice_amount){
		case 1: update="UPDATE testing SET Dice_1='"+roll.get(0)+"'WHERE Name='"+name+"'";
				break;
		case 2:	update="UPDATE testing SET Dice_1='"+roll.get(0)+"',Dice_2='"+roll.get(1)+"'WHERE Name='"+name+"'";
				break;
		case 3:	update="UPDATE testing SET Dice_1='"+roll.get(0)+"',Dice_2='"+roll.get(1)+"',Dice_3='"+roll.get(2)+"'WHERE Name='"+name+"'";
				break;
		case 4:	update="UPDATE testing SET Dice_1='"+roll.get(0)+"',Dice_2='"+roll.get(1)+"',Dice_3='"+roll.get(2)+"',Dice_4='"+roll.get(3)+"'WHERE Name='"+name+"'";
				break;
		case 5:	update="UPDATE testing SET Dice_1='"+roll.get(0)+"',Dice_2='"+roll.get(1)+"',Dice_3='"+roll.get(2)+"',Dice_4='"+roll.get(3)+"',Dice_5='"
				+roll.get(4)+"'WHERE Name='"+name+"'";
				break;
    	}
    	stmt.executeUpdate(update);
    	return update;
    }
    public int getDiceLeft(String name) throws SQLException{
    	int amount=0;
    	String query="Select Dice_remains From testing WHERE Name='"+name+"'";
    	stmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
    	rs=stmt.executeQuery(query);
    	rs.first();
    	amount=rs.getInt(1);
    	rs.close();
    	return amount;
    }
    public void updateBid(String name,Bid thebid) throws SQLException{
    	String update="UPDATE testing SET NumberBet='"+thebid.number+"',Amount_bet='"+thebid.amount+"'WHERE Name='"+name+"'";
    	stmt.executeUpdate(update);
    }
	public Boolean isValidBid(Bid thebid) throws SQLException {
		String query="Select NumberBet,Amount_bet From testing ORDER BY NumberBet DESC,Amount_bet DESC LIMIT 1";
		stmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		rs=stmt.executeQuery(query);
		rs.first();
		int number_bet=rs.getInt(1);
		int amount=rs.getInt(2);
		rs.close();
		if(thebid.number>number_bet)return true;
		if(thebid.number==number_bet && thebid.amount>amount)return true;
		return false;
	}
	public Boolean isLiar() throws SQLException{
		int maxbid_Number=0;
		int maxbid_Amount=0;
		int total_Amount;
		String query="Select NumberBet,Amount_bet From testing ORDER BY NumberBet DESC,Amount_bet DESC LIMIT 1";
		stmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		rs=stmt.executeQuery(query);
		rs.first();
		maxbid_Number=rs.getInt(1);
		maxbid_Amount=rs.getInt(2);
		rs.close();
		total_Amount=countMax_Bid(maxbid_Number);
		return(total_Amount<maxbid_Amount);
	}
	public Boolean isTellingTruth() throws SQLException{
		int maxbid_Number=0;
		int maxbid_Amount=0;
		int total_Amount;
		String query="Select NumberBet,Amount_bet From testing ORDER BY NumberBet DESC,Amount_bet DESC LIMIT 1";
		stmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		rs=stmt.executeQuery(query);
		rs.first();
		maxbid_Number=rs.getInt(1);
		maxbid_Amount=rs.getInt(2);
		total_Amount=countMax_Bid(maxbid_Number);
		rs.close();
		return(total_Amount==maxbid_Amount);
	}
	public int countMax_Bid(int Number) throws SQLException{
		int total=0;
		String query="Select Dice_1,Dice_2,Dice_3,Dice_4,Dice_5 From testing ";
		stmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		rs=stmt.executeQuery(query);
		while(rs.next()){
			if(rs.getInt(1)==Number)total++;
			if(rs.getInt(2)==Number)total++;
			if(rs.getInt(3)==Number)total++;
			if(rs.getInt(4)==Number)total++;
			if(rs.getInt(5)==Number)total++;
		}
		rs.close();
		return total;
	}
	public String whoLie() throws SQLException{
		String query="Select Name,NumberBet,Amount_bet From testing ORDER BY NumberBet DESC,Amount_bet DESC LIMIT 1";
		stmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		rs=stmt.executeQuery(query);
		rs.first();
		return(rs.getString(1));
	}
	public void updateDice(String name) throws SQLException{
		int amount=getDiceLeft(name);
		String query=getUpdateString(name, amount);
		amount--;
	   	 String update="UPDATE testing SET Dice_remains='"+amount+"'WHERE Name='"+name+"'";
	   	 stmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
	   	 stmt.executeUpdate(update);
	   	 setDiceZero(query);
	}
	public void setDiceZero(String query) throws SQLException{
	   	 String update=query;
	   	 stmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
	   	 stmt.executeUpdate(update);
	}
	public String getUpdateString(String name,int amount){
		String update="";
		switch(amount){
		case 1:update="UPDATE testing SET Dice_1='0' WHERE Name='"+name+"'";
				break;
		case 2:update="UPDATE testing SET Dice_2='0' WHERE Name='"+name+"'";
				break;
		case 3:update="UPDATE testing SET Dice_3='0' WHERE Name='"+name+"'";
				break;
		case 4:update="UPDATE testing SET Dice_4='0' WHERE Name='"+name+"'";
				break;
		case 5:update="UPDATE testing SET Dice_5='0' WHERE Name='"+name+"'";
				break;
		}
		return update;
	}
	public Bid getBid(String from) throws SQLException {
		int number,amount;
		String query="Select NumberBet,Amount_bet From testing WHERE Name='"+from+"'";
		stmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		rs=stmt.executeQuery(query);
		rs.first();
		number=rs.getInt(1);
		amount=rs.getInt(2);
		rs.close();
		return(new Bid(number,amount));
	}
	public void resetDice() throws SQLException{
	   	 String update="UPDATE testing SET NumberBet='0',Amount_bet='0',Dice_1='0',Dice_2='0',Dice_3='0',Dice_4='0',Dice_5='0'";
	   	 stmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
	   	 stmt.executeUpdate(update);
	}
	public int getPlayerRemaining() throws SQLException {
		String query="Select count(*) from testing where Dice_remains !='0' AND OnlineStatus='1' ";
		stmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		rs=stmt.executeQuery(query);
		rs.first();
		int playerleft=rs.getInt(1);
		rs.close();
		return playerleft;
	}
	public String getLastPlayerName() throws SQLException {
		String query="Select Name from testing where Dice_remains !='0' ";
		stmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
		rs=stmt.executeQuery(query);
		rs.first();
		String name=rs.getString(1);
		rs.close();
		return name;
	}
	public void updateOnlineStatus(String player_Name,int status) throws SQLException {
	   	 String update="UPDATE testing SET OnlineStatus='"+status+"' WHERE Name='"+player_Name+"'";
	   	 stmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
	   	 stmt.executeUpdate(update);
	}
	public void updateTruth(String caller, Boolean liar,Bid maxbid,String theliar) throws SQLException {
		String query="";
		ArrayList<String> player=new ArrayList<String>();
		if(liar){//remove a dice from everyone who bided the same Number as the highest bidder
			query="Select Name,Dice_1,Dice_2,Dice_3,Dice_4,Dice_5 from testing WHERE Name!='"+caller+"' AND Name!='"+theliar+"'";
			stmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
			rs=stmt.executeQuery(query);
			while(rs.next()){
				if(rs.getInt(2)==maxbid.number || rs.getInt(3)==maxbid.number || rs.getInt(4)==maxbid.number ||
						rs.getInt(5)==maxbid.number || rs.getInt(6)==maxbid.number)
				player.add(rs.getString(1));//get list of player who bidded the same Number as highest
			}
			player.add(theliar);
			rs.close();			
			for(String name:player){
				updateDice(name);
			}
		}
		else{//remove dice from caller 
			player.add(caller);
			updateDice(caller);
		}
	}
}
