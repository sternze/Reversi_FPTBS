package org.uni.klu.se2.reversi.data;

import java.security.MessageDigest;

public class User {
	private String userName;
	private String passWord;
	
	
	public String getUserName() {
		return userName;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getPassWord() {
		return passWord;
	}
	
	// I know MD5 is not very secure, but at least not clear text.. :D
	public void setPassWord(String passWord) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.reset();
			md.update(passWord.getBytes());
			
			this.passWord = md.digest().toString();
		} catch(Exception ex) {
			this.passWord = "";
		}
	}
	
	
}
