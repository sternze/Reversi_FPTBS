package uni_klu.se2.reversi.engine.algorithms;

import java.util.List;

import uni_klu.se2.reversi.data.Board;
import uni_klu.se2.reversi.data.BoardStatus;
import uni_klu.se2.reversi.data.FieldStatus;
import uni_klu.se2.reversi.data.Move;

public class NaivDiskSquare 
{
	/*
	public static int[][] fieldscores = {{ 50, -20, 10, 5, 5, 10, -20,  50},
										 {-20, -30,  1, 1, 1,  1, -30, -20},
										 { 10,   1,  1, 1, 1,  1,   1,  10},
										 {  5,   1,  1, 1, 1,  1,   1,   5},
										 {  5,   1,  1, 1, 1,  1,   1,   5},
										 { 10,   1,  1, 1, 1,  1,   1,  10},
										 {-20, -30,  1, 1, 1,  1, -30, -20},
										 { 50, -20, 10, 5, 5, 10, -20,  50}};
	*/
	public static int[][] fieldscores = {{ 500, -240, 85, 69, 69, 85, -240,  500},
										 {-240, -130, 49, 23, 23, 49, -130, -240},
										 {  85,   49,  1,  9,  9,  1,   49,   85},
										 {  69,   23,  9, 32, 32,  9,   23,   69},
										 {  69,   23,  9, 32, 32,  9,   23,   69},
										 {  85,   49,  1,  9,  9,  1,   49,   85},
										 {-240, -130, 49, 23, 23, 49, -130, -240},
										 { 500, -240, 85, 69, 69, 85, -240,  500}};
	public static Move getMoveSimple(List<Move> moves)
	{
		int size = moves.size();
		if (size == 0)
			return null;
		Move move = moves.get(0);
		for (int i = 1; i < size; i++)
		{
			if (fieldscores[move.getX()][move.getY()] < fieldscores[moves.get(i).getX()][moves.get(i).getY()])
			{
				move = moves.get(i);
			}
		}
		return move;
	}
	
	public static Move getMove(Board board, int depth)
	{
		
		List<Move> moves = board.getAvailableMoves();
		int size = moves.size();
		if (size == 0)
			return null;
		Move move = moves.get(0);
		int maxScore = getScore(move, board, depth, board.getCurrentPlayer());
		//System.out.println("Move: " + move.getX() + "/" + move.getY() + " Score: " + maxScore);
		for (int i = 1; i < size; i++)
		{
			int current = getScore(moves.get(i), board, depth, board.getCurrentPlayer());
			//System.out.println("Move: " + moves.get(i).getX() + "/" + moves.get(i).getY() + " Score: " + current);
			if (maxScore < current)
			{
				move = moves.get(i);
				maxScore = current;
			}
		}
		return move;
	}
	
	public static int getScore(Move move, Board board, int depth, FieldStatus player)
	{
		int score = 0;
		int multiplier = -1;
		if (player == board.getCurrentPlayer())
			multiplier *= -1;
		if (move != null)
		{
			score = multiplier * fieldscores[move.getX()][move.getY()];
			//System.out.println("getScore(" + move.getX() + "/" + move.getY() + " Score: " + score);
		}
		if (depth == 0)
			return score;
		depth--;
		Board calcBoard = board.cloneBoard();
		if (move == null)
			calcBoard.pass();
		else
			calcBoard.move(move);
		if (calcBoard.getStatus() != BoardStatus.INPROGRESS)
			return getEndOfGameScore(player, calcBoard.getStatus());
		
		List<Move> moves = calcBoard.getAvailableMoves();
		int size = moves.size();
		if (size == 0)
			return score + getScore(null, calcBoard, depth, player);
		
		int maxScore = getScore(moves.get(0), calcBoard, depth, player);
		for (int i = 1; i < size; i++)
		{
			int current = getScore(moves.get(i), calcBoard, depth, player);
			if (maxScore < current)
				maxScore = current;
		}
		return score + maxScore;
	}
	
	public static int getEndOfGameScore(FieldStatus player, BoardStatus status)
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
