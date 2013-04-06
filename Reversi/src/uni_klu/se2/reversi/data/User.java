package uni_klu.se2.reversi.data;

import java.io.Serializable;
import java.security.MessageDigest;
import java.util.List;

/**
 * This class is the transport class in the DAO Pattern
 * @author Daniel
 * @version 1.0
 *
 */
public class User implements Serializable {
	
	/**
	 * Beim Serialisieren eines Objektes wird auch die serialVersionUID der zugehörigen
	 * Klasse mit in die Ausgabedatei geschrieben. Soll das Objekt später deserialisiert
	 * werden, so wird die in der Datei gespeicherte serialVersionUID mit der aktuellen
	 * serialVersionUID des geladenen .class-Files verglichen. Stimmen beide nicht
	 * überein, so gibt es eine Ausnahme des Typs InvalidClassException, und der
	 * Deserialisierungsvorgang bricht ab.
	 */
	private static final long serialVersionUID = -3957221281248748435L;
	
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
