package org.uni.klu.se2.reversi.data;

public class Game {
	private int ID;
	private String blackFields;
	private String whiteFields;
	private boolean finished;
	
	public int getID() {
		return ID;
	}
	
	public void setID(int iD) {
		ID = iD;
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
}
