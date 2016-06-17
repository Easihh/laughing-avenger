package com.ThanatosServer.Utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class Util {
	
	public static byte[] toByte(Object object) throws IOException{
		try(ByteArrayOutputStream bos=new ByteArrayOutputStream();
			ObjectOutput out=new ObjectOutputStream(bos)){	
				out.writeObject(object);
				return bos.toByteArray();
			}
	}
	public static Object convertFromBytes(byte[] bytes) throws IOException{
	    try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
	         ObjectInput in = new ObjectInputStream(bis)) {
	        return in.readObject();
	    } 
	    catch(ClassNotFoundException nfe){
	    	System.out.println(nfe.getMessage());
	    }
	    return null;
	}
}
