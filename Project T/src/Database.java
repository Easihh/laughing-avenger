//package game;
import java.sql.*;
import java.util.Scanner;

import javax.xml.bind.annotation.XmlElementRef.DEFAULT;
public class Database {
	Statement stmt=null;
	ResultSet rs=null;
	Connection connection;
	public Database(){
		connection=getConnection();
	}
	public Connection getConnection(){
	   	 try
		   {
		    Class.forName("com.mysql.jdbc.Driver").newInstance();
		    String Url = "jdbc:mysql://localhost:3306/info";
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
    	    String update="Select Hitpoint From testing WHERE Name='"+playername+"' AND Session_id='"+sessionID+"'";
    	    System.out.println(update);
    	    stmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
    	    rs=stmt.executeQuery(update);
    	   // System.out.println(count);
    	   //rs=stmt.getGeneratedKeys();
    	    rs.first();
    	    int health=rs.getInt(1);
    	    System.out.println(health);
    	    //while(rs.next())
    	    //{
    	    	//System.out.println("TESTID"+rs.getInt(1));
    	    //}
    	    rs.close();
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
    	String name;
    	String query="Select Name From testing WHERE Name='"+playername+"' AND Password='"+password+"'";
    	stmt=connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_UPDATABLE);
    	rs=stmt.executeQuery(query);
    	rs.first();
    	name=rs.getString(1);
    	rs.close();
    	return name;
    }
}
