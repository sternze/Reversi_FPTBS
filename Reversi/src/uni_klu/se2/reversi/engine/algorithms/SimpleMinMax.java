package uni_klu.se2.reversi.engine.algorithms;

import java.util.List;

import uni_klu.se2.reversi.data.Board;
import uni_klu.se2.reversi.data.FieldStatus;
import uni_klu.se2.reversi.data.Move;

public class SimpleMinMax 
{
	public static int getOwnScoreForMove(Move move, Board board)
	{
		Board calculateBoard = board.cloneBoard();
		calculateBoard.move(move);
		if (board.getCurrentPlayer() == FieldStatus.BLACK)
			return calculateBoard.getWhiteFieldsOnBoard();
		return calculateBoard.getBlackFieldsOnBoard();
	}
	
	public static int getOppScoreForMove(Move move, Board board)
	{
		Board calculateBoard = board.cloneBoard();
		calculateBoard.move(move);
		if (board.getCurrentPlayer() == FieldStatus.BLACK)
			return calculateBoard.getBlackFieldsOnBoard();
		return calculateBoard.getWhiteFieldsOnBoard();
	}
	
	public static int getScoreForMove(Move move, Board board)
	{
		Board calculateBoard = board.cloneBoard();
		calculateBoard.move(move);
		if (board.getCurrentPlayer() == FieldStatus.BLACK)
			return calculateBoard.getWhiteFieldsOnBoard() - calculateBoard.getBlackFieldsOnBoard();
		return calculateBoard.getBlackFieldsOnBoard() - calculateBoard.getWhiteFieldsOnBoard();
	}
	
	public static Move getMove(List<Move> moves, Board board)
	{
		
		int size = moves.size();
		if (size == 0)
			return null;
		Move move = moves.get(0);
		for (int i = 1; i < size; i++)
		{
			if (getScoreForMove(move, board) < getScoreForMove(moves.get(i), board))
				move = moves.get(i);
		}
		return move;
	}
}
