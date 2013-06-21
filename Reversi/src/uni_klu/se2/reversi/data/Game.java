package uni_klu.se2.reversi.data;

import java.io.Serializable;
import java.util.UUID;

import javafx.beans.property.SimpleObjectProperty;

/**
 * This class is the transport class in the DAO Pattern
 * @author Daniel
 * @version 1.0
 *
 */
public class Game implements Serializable {

	private static final int BOARDSIZE = 8;
	private static final String DB_FIELD_SEPERATOR = ";";
	
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
	private boolean blacksTurn;
	private int blackAlgorithmId;
	private int whiteAlgorithmId;	
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

	public boolean isBlacksTurn() {
		return blacksTurn;
	}

	public void setBlacksTurn(boolean blacksTurn) {
		this.blacksTurn = blacksTurn;
	}
	
	public Field[][] getFieldArray() {
		Field[][] f = new Field[BOARDSIZE][BOARDSIZE];
		
		for (int i = 0; i < BOARDSIZE; i++)
			for (int j = 0; j < BOARDSIZE; j++) {
				f[i][j] = new Field(i, j);
				if(this.getBlackFields().contains(i + "_" + j + DB_FIELD_SEPERATOR)) {
					f[i][j].setStatus(new SimpleObjectProperty<FieldStatus>(FieldStatus.BLACK));
				} else if(this.getWhiteFields().contains(i + "_" + j + DB_FIELD_SEPERATOR)) {
					f[i][j].setStatus(new SimpleObjectProperty<FieldStatus>(FieldStatus.WHITE));
				} else {
					f[i][j].setStatus(new SimpleObjectProperty<FieldStatus>(FieldStatus.EMPTY));
				}
			}
		
		return f;
	}
	
	public FieldStatus getCurrentPlayer() {
		return this.isBlacksTurn() == true ? FieldStatus.BLACK : FieldStatus.WHITE;
	}

	public int getBlackAlgorithmId() {
		return blackAlgorithmId;
	}

	public void setBlackAlgorithmId(int blackAlgorithmId) {
		this.blackAlgorithmId = blackAlgorithmId;
	}

	public int getWhiteAlgorithmId() {
		return whiteAlgorithmId;
	}

	public void setWhiteAlgorithmId(int whiteAlgorithmId) {
		this.whiteAlgorithmId = whiteAlgorithmId;
	}
	
	
	
}
