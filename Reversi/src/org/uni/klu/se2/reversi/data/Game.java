package org.uni.klu.se2.reversi.data;

import java.io.Serializable;
import java.util.UUID;

public class Game implements Serializable {
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
