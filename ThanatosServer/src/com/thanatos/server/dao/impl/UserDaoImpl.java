package com.thanatos.server.dao.impl;

import org.apache.ibatis.session.SqlSession;

import com.thanatos.model.Users;
import com.thanatos.server.dao.UserDao;
import com.thanatos.server.utility.MyBatisUtil;


public class UserDaoImpl  implements UserDao{

	@Override
	public boolean getUserByUsernamePassword(String username, String password) {
		boolean retVal=false;
		try(SqlSession session=MyBatisUtil.getSqlSessionFactory().openSession(true)){
			Users myUser=new Users(username,password);
			Users user=session.selectOne("selectUser",myUser);
			if(user!=null)retVal=true;
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		return retVal;
	}

}
