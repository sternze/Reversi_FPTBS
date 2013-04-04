package org.uni.klu.se2.reversi.data;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.List;

public class User implements Serializable {
	private String userName;
	private String passWord;
	private List<Game> games;
	
	
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

	public List<Game> getGames() {
		return games;
	}

	public void setGames(List<Game> games) {
		this.games = games;
	}
	
	public void addGame(Game game) {
		this.games.add(game);
	}

	public void setPassWordHash(String pwHash) {
		this.passWord = pwHash;
	}
	
}
