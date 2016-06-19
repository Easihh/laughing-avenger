package com.ThanatosServer.DaoImpl;

import org.apache.ibatis.session.SqlSession;

import com.ThanatosServer.Dao.UserDao;
import com.ThanatosServer.Model.Users;
import com.ThanatosServer.Utility.MyBatisUtil;


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
