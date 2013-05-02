package uni_klu.se2.reversi.engine.algorithms;

import java.util.List;

import uni_klu.se2.reversi.data.Board;
import uni_klu.se2.reversi.data.BoardStatus;
import uni_klu.se2.reversi.data.FieldStatus;
import uni_klu.se2.reversi.data.Move;

public class DeepMinMax 
{
	public static Move getMove(Board board, int depth)
	{
		
		List<Move> moves = board.getAvailableMoves();
		int size = moves.size();
		if (size == 0)
			return null;
		Move move = moves.get(0);
		int maxScore = getScore(move, board, depth, board.getCurrentPlayer());
		//System.out.println("Sum Move: " + move.getX() + "/" + move.getY() + " Score: " + maxScore);
		for (int i = 1; i < size; i++)
		{
			int current = getScore(moves.get(i), board, depth, board.getCurrentPlayer());
			//System.out.println("Sum Move: " + moves.get(i).getX() + "/" + moves.get(i).getY() + " Score: " + current);
			if (maxScore < current)
			{
				move = moves.get(i);
				maxScore = current;
			}
		}
		System.out.println("Best Move: " + move.getX() + "/" + move.getY() + " Score: " + maxScore);
		return move;
	}

	private static int getScore(Move move, Board board, int depth, FieldStatus player) 
	{
		Board calcBoard = board.cloneBoard();
		if (move == null)
			calcBoard.pass();
		else
			calcBoard.move(move);
		if (calcBoard.getStatus() != BoardStatus.INPROGRESS)
		{
			
			int ret = getEndOfGameScore(player, calcBoard.getStatus());
			//System.out.println("Move: " + move.getX() + "/" + move.getY() + " Score: " + ret + " Depth: " + depth);
			return ret;
		}
		if (depth <= 0)
		{
			int ret = getScoreForPlayer(player, calcBoard);
			//System.out.println("Move: " + move.getX() + "/" + move.getY() + " Score: " + ret + " Depth: " + depth);
			return ret;
		}
		List<Move> moves = board.getAvailableMoves();
		int size = moves.size();
		Move nextMove = null;
		if (size != 0)
			nextMove = moves.get(0);
		int maxScore = -1 * getScore(nextMove, calcBoard, depth - 1, player);
		for (int i = 1; i < size; i++)
		{
			int current = -1 * getScore(moves.get(i), calcBoard, depth - 1, player);
			if (maxScore < current)
			{
				maxScore = current;
			}
		}
		//System.out.println("Move: " + move.getX() + "/" + move.getY() + " Score: " + maxScore + " Depth: " + depth);
		return maxScore;
	}
	
	private static int getScoreForPlayer(FieldStatus player, Board board)
	{
		if (player == FieldStatus.BLACK)
			return board.getWhiteFieldsOnBoard();
		return board.getBlackFieldsOnBoard();
	}
	
	private static int getEndOfGameScore(FieldStatus player, BoardStatus status)
	{
		if (status == BoardStatus.BLACKWON)
			if (player == FieldStatus.BLACK)
				return 10000;
			else
				return -10000;
		if (status == BoardStatus.WHITEWON)
			if (player == FieldStatus.WHITE)
				return 10000;
			else
				return -10000;
		return 0;
	}
	
}
