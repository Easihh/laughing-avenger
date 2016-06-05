package com.thanatos.DaoImpl;

import java.util.List;

import org.apache.ibatis.session.SqlSession;

import com.thanatos.Dao.UsersDao;
import com.thanatos.model.Users;
import com.thanatos.utility.MyBatisUtil;

public class UsersDaoImpl  implements UsersDao{



	@Override
	public List<Users> getUsers() {
		List<Users> retVal=null;
		try(SqlSession session=MyBatisUtil.getSqlSessionFactory().openSession(true)){
			retVal=session.selectList("getAllUsers");
		}
		catch(Exception e){
			System.out.println(e.getMessage());
		}
		return retVal;
	}

}
