package com.thanatos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class BaseController {
	private final String ip="74.58.85.141:1433";
	private final String databaseName="TEST";
	private String db_connect="jdbc:sqlserver://"+ip+";databaseName="+databaseName;
	private final String user="sa";
	private final String password="Polimkar1";
	public Connection getDBConnection(){
		Connection con=null;
		try {
			Class.forName( "com.microsoft.sqlserver.jdbc.SQLServerDriver" );
			con=DriverManager.getConnection(db_connect,user,password);}
		catch (ClassNotFoundException e) {
			e.printStackTrace();}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}
}
