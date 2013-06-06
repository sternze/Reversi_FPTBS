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
import uni_klu.se2.reversi.data.FieldStatus;
import uni_klu.se2.reversi.data.Move;
import uni_klu.se2.reversi.engine.IPlayer;

public class ReversiModel extends IPlayer {
	public static int BOARD_SIZE = 8;
	
	public ObjectProperty<FieldStatus> turn = new SimpleObjectProperty<FieldStatus>(FieldStatus.BLACK);
	
	public ReversiModel(Board myBoard) {
		super(myBoard);
		board = myBoard;
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
		List<Move> legalMoves = board.getAvailableMoves();
		Iterator<Move> iterator = legalMoves.iterator();
		while(iterator.hasNext())
		{
			Move field = iterator.next();
			board.getFields()[field.getX()][field.getY()].getStatus().setValue(FieldStatus.LEGAL);
		}
	}
	
	public void matchBoards() {
		//checkLegalMoves();
	}
	
	public void play(Move move) {
		System.out.println("play (" + move.getX() + ", " + move.getY() + "): " + board.isMoveLegal(move.getX(), move.getY()));
		engine.onMoveReadyCalculated(move, false);
	}
	
	@Override
	public void yourTurn() {
		checkLegalMoves();
	}

	@Override
	public void signalLastMove() {
		// TODO Auto-generated method stub
		
	}
}
