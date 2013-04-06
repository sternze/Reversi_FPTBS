package uni_klu.se2.reversi.data;

import java.io.Serializable;
import java.util.UUID;

/**
 * This class is the transport class in the DAO Pattern
 * @author Daniel
 * @version 1.0
 *
 */
public class Game implements Serializable {
	
	/**
	 * Beim Serialisieren eines Objektes wird auch die serialVersionUID der zugehörigen
	 * Klasse mit in die Ausgabedatei geschrieben. Soll das Objekt später deserialisiert
	 * werden, so wird die in der Datei gespeicherte serialVersionUID mit der aktuellen
	 * serialVersionUID des geladenen .class-Files verglichen. Stimmen beide nicht
	 * überein, so gibt es eine Ausnahme des Typs InvalidClassException, und der
	 * Deserialisierungsvorgang bricht ab.
	 */
	private static final long serialVersionUID = 1258424447180509476L;
	
	
	private UUID ID;
	private String blackFields;
	private String whiteFields;
	private boolean finished;
	private User blackPlayer;
	private User whitePlayer;
	
	public UUID getID() {
		return ID;
	}
	
	public void setID(UUID iD) {
		ID = iD;
	}
	
	public void setID(String iD) {
		ID = UUID.fromString(iD);
	}
	
	public String getBlackFields() {
		return blackFields;
	}
	
	public void setBlackFields(String blackFields) {
		this.blackFields = blackFields;
	}
	
	public String getWhiteFields() {
		return whiteFields;
	}
	
	public void setWhiteFields(String whiteFields) {
		this.whiteFields = whiteFields;
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public User getBlackPlayer() {
		return blackPlayer;
	}

	public void setBlackPlayer(User blackPlayer) {
		this.blackPlayer = blackPlayer;
	}

	public User getWhitePlayer() {
		return whitePlayer;
	}

	public void setWhitePlayer(User whitePlayer) {
		this.whitePlayer = whitePlayer;
	}
	
	
}
