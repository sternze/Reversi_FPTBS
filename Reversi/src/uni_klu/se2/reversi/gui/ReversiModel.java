package uni_klu.se2.reversi.gui;

import java.util.Iterator;
import java.util.List;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.NumberExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import uni_klu.se2.reversi.data.Board;
import uni_klu.se2.reversi.data.Field;
import uni_klu.se2.reversi.data.FieldStatus;
import uni_klu.se2.reversi.data.Move;
import uni_klu.se2.reversi.engine.IPlayer;

public class ReversiModel extends IPlayer {
	public static int BOARD_SIZE = 8;
	
	public ObjectProperty<FieldStatus> turn = new SimpleObjectProperty<FieldStatus>(FieldStatus.BLACK);
	
	private boolean myTurn = false;
	
	public ReversiModel(Board myBoard) {
		super(myBoard);
		board = myBoard;
		checkLegalMoves();
	}
	
	public Board getBoard() {
		return board;
	}
	
	public NumberExpression getScore(FieldStatus owner) {
		NumberExpression score = new SimpleIntegerProperty();
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				score = score.add(Bindings.when(board.getFields()[i][j].getStatus().isEqualTo(owner)).then(1).otherwise(0));
			}
		}
		return score;
	}
	  
	public NumberBinding getTurnsRemaining(FieldStatus owner) {
		NumberExpression emptyCellCount = getScore(FieldStatus.EMPTY);
		return Bindings.when(turn.isEqualTo(owner))
						.then(emptyCellCount.add(1).divide(2))
						.otherwise(emptyCellCount.divide(2));
	} 
	
	public void checkLegalMoves() {
		System.out.println("checklegalmoves");
		List<Move> legalMoves = board.getAvailableMoves();
		Iterator<Move> iterator = legalMoves.iterator();
		while(iterator.hasNext())
		{
			Move field = iterator.next();
			board.getFields()[field.getX()][field.getY()].getStatus().setValue(FieldStatus.LEGAL);
		}
	}
	
	public void play(Move move) {
		System.out.println("play (" + move.getX() + ", " + move.getY() + "): " + board.isMoveLegal(move.getX(), move.getY()));
		if (board.isMoveLegal(move.getX(), move.getY())) {
			myTurn = false;
			engine.onMoveReadyCalculated(move, false);
		}
	}
	
	@Override
	public void yourTurn() {
		myTurn = true;
	}

	@Override
	public void signalLastMove() {
	}
	
	public boolean getMyTurn() {
		return this.myTurn;
	}
	
	public void setMyTurn(boolean myTurn) {
		this.myTurn = myTurn;
	}
}
