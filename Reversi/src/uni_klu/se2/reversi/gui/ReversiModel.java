package uni_klu.se2.reversi.gui;

import java.security.acl.Owner;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.NumberBinding;
import javafx.beans.binding.NumberExpression;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import uni_klu.se2.reversi.data.Board;
import uni_klu.se2.reversi.data.BoardStatus;
import uni_klu.se2.reversi.data.FieldStatus;
import uni_klu.se2.reversi.data.Move;
import uni_klu.se2.reversi.engine.IPlayer;
import uni_klu.se2.reversi.engine.ReversiEngine;

public class ReversiModel extends IPlayer {
	public static int BOARD_SIZE = 8;
	
	public ObjectProperty<FieldStatus> turn = new SimpleObjectProperty<FieldStatus>(FieldStatus.BLACK);
  
	public ObjectProperty<FieldStatus>[][] gameBoard = new ObjectProperty[BOARD_SIZE][BOARD_SIZE];
	
	public ReversiModel(Board myBoard) {
		super(myBoard);
		board = myBoard;
		
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				gameBoard[i][j] = new SimpleObjectProperty<FieldStatus>(board.getFields()[i][j].getStatus());
			}
		}
		
		matchBoards();
	}
	
	public NumberExpression getScore(FieldStatus owner) {
		NumberExpression score = new SimpleIntegerProperty();
		for (int i = 0; i < BOARD_SIZE; i++) {
			for (int j = 0; j < BOARD_SIZE; j++) {
				score = score.add(Bindings.when(gameBoard[i][j].isEqualTo(owner)).then(1).otherwise(0));
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
	
	public BooleanBinding legalMove(final int x, final int y) {
		return new BooleanBinding() {
			{
				bind(gameBoard[x][y]); 
		    }
			
			@Override
			protected boolean computeValue() {
				return board.isMoveLegal(x, y);
			}
		};
		/*
		  return gameBoard[x][y].isEqualTo(FieldStatus.EMPTY).and(
		    canFlip(x, y, 0, -1, turn).or(
		    canFlip(x, y, -1, -1, turn).or(
		    canFlip(x, y, -1, 0, turn).or(
		    canFlip(x, y, -1, 1, turn).or(
		    canFlip(x, y, 0, 1, turn).or(
		    canFlip(x, y, 1, 1, turn).or(
		    canFlip(x, y, 1, 0, turn).or(
		    canFlip(x, y, 1, -1, turn))))))))
		  );*/
	}
	
	public void matchBoards() {
		int i, j = 0;
		for (i = 0; i < BOARD_SIZE; i++) {
			for (j = 0; j < BOARD_SIZE; j++) {
				if (gameBoard[i][j].getValue() != board.getFields()[i][j].getStatus()) {
					gameBoard[i][j].setValue(board.getFields()[i][j].getStatus());
					System.out.println("matched(" + i + "," + j + ")");
				}
			}
		}
		
		if (i == 0 && j == 0)
			System.out.println("nothing to match");
	}
	
	public void play(Move move) {
		System.out.println("play(" + move.getX() + "," + move.getY() + "): " + board.isMoveLegal(move.getX(), move.getY()));
		
		int cellX = move.getX();
		int cellY = move.getY();
		
		if (board.isMoveLegal(move.getX(), move.getY())) {
			engine.onMoveReadyCalculated(move, false);
			
			gameBoard[cellX][cellY].setValue(turn.get());
			/*
			flip(cellX, cellY, 0, -1, turn);
		    flip(cellX, cellY, -1, -1, turn);
		    flip(cellX, cellY, -1, 0, turn);
		    flip(cellX, cellY, -1, 1, turn);
		    flip(cellX, cellY, 0, 1, turn);
		    flip(cellX, cellY, 1, 1, turn);
		    flip(cellX, cellY, 1, 0, turn);
		    flip(cellX, cellY, 1, -1, turn);
			turn.setValue(turn.getValue() == FieldStatus.BLACK ? FieldStatus.WHITE : FieldStatus.BLACK);
			*/
		}
		
		
	}
	/*
	public void flip(int cellX, int cellY, int directionX, int directionY, ObjectProperty<FieldStatus> turn) {
	  if (canFlip(cellX, cellY, directionX, directionY, turn).get()) {
	    int x = cellX + directionX;
	    int y = cellY + directionY;
	    while (x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE && gameBoard[x][y].get() != turn.get()) {
	      gameBoard[x][y].setValue(turn.get());
	      x += directionX;
	      y += directionY;
	    }
	  }
	} 
	
	public BooleanBinding canFlip(final int cellX, final int cellY, final int directionX, final int directionY, final ObjectProperty<FieldStatus> turn) {
			  return new BooleanBinding() { 
			    {
			      bind(turn);
			      int x = cellX + directionX;
			      int y = cellY + directionY;
			      while (x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE) {
			          bind(gameBoard[x][y]);
			          x += directionX;
			          y += directionY;
			        }
			    }
			    @Override
			    protected boolean computeValue() {
			    	FieldStatus turnVal = turn.get();
			        int x = cellX + directionX;
			        int y = cellY + directionY;
			        boolean first = true;
			        while (x >= 0 && x < BOARD_SIZE && y >= 0 && y < BOARD_SIZE && gameBoard[x][y].get() != FieldStatus.EMPTY) {
			          if (gameBoard[x][y].get() == turnVal) {
			            return !first;
			          }
			          first = false;
			          x += directionX;
			          y += directionY;
			        }
			        return false;
			      }
			    };
			  }
*/
	@Override
	public void yourTurn() {
		matchBoards();
		
	}

	@Override
	public void signalLastMove() {
		// TODO Auto-generated method stub
		
	}
}
