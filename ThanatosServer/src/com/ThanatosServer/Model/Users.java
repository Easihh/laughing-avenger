package com.ThanatosServer.Model;

public class Users {
	
	private int id;
	
	private String username;

    private String password;
    
    public Users(){}
    
    public Users(String name,String pass){
	     username=name;
	     password=pass;
    }
    
    public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
}