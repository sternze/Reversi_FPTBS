package uni_klu.se2.reversi.engine.player;

import java.util.List;

import uni_klu.se2.reversi.data.Board;
import uni_klu.se2.reversi.data.Move;
import uni_klu.se2.reversi.engine.IPlayer;
import uni_klu.se2.reversi.engine.ReversiEngine;
import uni_klu.se2.reversi.engine.algorithms.DeepMinMax;
import uni_klu.se2.reversi.engine.algorithms.NaivDiskSquare;

import javafx.application.Platform;

import javax.swing.SwingWorker;


public class NaivDiskSquareComputerPlayer extends IPlayer
{
	public int number;
	private int           depth;
	private class BackgroundCalculator extends SwingWorker<Boolean, Boolean>
	{
		protected Board         board;
		protected ReversiEngine engine;
		
		
		
		public BackgroundCalculator(Board board, ReversiEngine engine) {
			super();
			this.board = board;
			this.engine = engine;
		}

		@Override
		protected Boolean doInBackground() throws Exception {
			List<Move> moves = board.getAvailableMoves();
			int size = moves.size();
			if (size > 0)
			{
				Platform.runLater(new Runnable() {
	                @Override public void run() {
	                	engine.onMoveReadyCalculated(NaivDiskSquare.getMove(board, depth), false);
	                }
	            });
				//engine.onMoveReadyCalculated(NaivDiskSquare.getMove(board, depth), false);
			} else 
			{
				Platform.runLater(new Runnable() {
	                @Override public void run() {
	                	engine.onMoveReadyCalculated(null, true);
	                }
	            });
	//			engine.onMoveReadyCalculated(null, true);
				System.out.println("NaivDisk Player: Pass!");
			}
			return null;
		}
		
	}
	
	
	public NaivDiskSquareComputerPlayer(Board board, int depth) 
	{
		super(board);
		this.depth = depth;
	}

	@Override
	public void yourTurn() 
	{
		BackgroundCalculator bc = new BackgroundCalculator(board, engine);
		bc.execute();
	}

	@Override
	public void signalLastMove() {
		// TODO Auto-generated method stub
		
	}


}
