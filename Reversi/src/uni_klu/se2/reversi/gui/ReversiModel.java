package uni_klu.se2.reversi.gui;

import java.util.Iterator;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.NumberExpression;
import javafx.beans.property.SimpleIntegerProperty;
import uni_klu.se2.reversi.data.Board;
import uni_klu.se2.reversi.data.FieldStatus;
import uni_klu.se2.reversi.data.Move;
import uni_klu.se2.reversi.engine.IPlayer;
import uni_klu.se2.reversi.speech.ReversiSpeech;

public class ReversiModel extends IPlayer {
	
	private boolean myTurn;
	private boolean showPossibleMoves = true;
	private boolean recognizeSpeech = false;
		
	public ReversiModel(Board board) {
		super(board);
		this.board = board;
		myTurn = false;
	}
	
	public Board getBoard() {
		return board;
	}
	
	
	public NumberExpression getScore(FieldStatus owner) {
		NumberExpression score = new SimpleIntegerProperty();
		for (int i = 0; i < board.BOARDSIZE; i++) {
			for (int j = 0; j < board.BOARDSIZE; j++) {
				score = score.add(Bindings.when(board.getFields()[i][j].getStatus().isEqualTo(owner)).then(1).otherwise(0));
			}
		}
		return score;
	}
	
	public NumberBinding getTurnsRemaining(FieldStatus owner) {
		NumberExpression emptyCellCount = getScore(FieldStatus.EMPTY);
		
		if (board.getCurrentPlayer() == owner) {
			return emptyCellCount.add(1).divide(2);
		} else {
			return emptyCellCount.divide(2);
		}
	} 
	
	
	private void hideLegalMoves() {
		for (int i = 0; i < board.BOARDSIZE; i++) {
			for (int j = 0; j < board.BOARDSIZE; j++) {
				if (board.getFields()[i][j].getStatus().getValue() == FieldStatus.LEGAL)
					board.getFields()[i][j].getStatus().setValue(FieldStatus.EMPTY);
			}
		}
	}
	
	private void showLegalMoves() {
		System.out.println("checkLegalMoves");
		List<Move> legalMoves = board.getAvailableMoves();
		
		ReversiSpeech rs = new ReversiSpeech();
		rs.createTemporaryFile(legalMoves);
		rs = null;
		
		Iterator<Move> iterator = legalMoves.iterator();
		while(iterator.hasNext())
		{
			Move field = iterator.next();
			board.getFields()[field.getX()][field.getY()].getStatus().setValue(FieldStatus.LEGAL);
		}
	}
	
	public void play(Move move) {
		if (myTurn && board.isMoveLegal(move.getX(), move.getY())) {
			myTurn = false;
			hideLegalMoves();
			engine.onMoveReadyCalculated(move, false);
		}
	}
	
	@Override
	public void yourTurn() {
		if(showPossibleMoves) {
			showLegalMoves();
		}
		myTurn = true;
	}

	@Override
	public void signalLastMove() {
	}
	
	public boolean getMyTurn() {
		return myTurn;
	}

	public boolean isShowPossibleMoves() {
		return showPossibleMoves;
	}

	public void setShowPossibleMoves(boolean showPossibleMoves) {
		this.showPossibleMoves = showPossibleMoves;
		if(this.showPossibleMoves) {
			showLegalMoves();
		} else {
			hideLegalMoves();
		}
	}

	public boolean isRecognizeSpeech() {
		return recognizeSpeech;
	}

	public void setRecognizeSpeech(boolean recognizeSpeech) {
		this.recognizeSpeech = recognizeSpeech;
	}
	
	
	
}
