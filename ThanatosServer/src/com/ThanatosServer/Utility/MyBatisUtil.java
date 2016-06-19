package com.ThanatosServer.Utility;

import java.io.InputStream;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

public class MyBatisUtil {
	private static SqlSessionFactory sqlSessionFactory;
	static{
	    String resource = "mybatis-config.xml";
	    InputStream inputStream;
	    try{
		    inputStream = Resources.getResourceAsStream(resource);
		    sqlSessionFactory = new SqlSessionFactoryBuilder()
		            .build(inputStream);	    
	    	}
	    catch(Exception e){
	    	e.printStackTrace();
	    }
	}
	public static SqlSessionFactory getSqlSessionFactory(){
		return sqlSessionFactory;
	}
}

