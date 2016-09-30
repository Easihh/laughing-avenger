package com.thanatos.server.dao;


public interface UserDao {
	
	public boolean getUserByUsernamePassword(String username,String password);
	
}
