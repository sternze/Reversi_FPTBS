package uni_klu.se2.reversi.engine.player;

import java.util.List;
import java.util.Random;

import uni_klu.se2.reversi.data.Board;
import uni_klu.se2.reversi.data.Move;
import uni_klu.se2.reversi.engine.IPlayer;
import uni_klu.se2.reversi.engine.ReversiEngine;

import javax.swing.SwingWorker;

public class RandomComputerPlayer extends IPlayer
{
	public int number;
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
			Thread.sleep(100);
			List<Move> moves = board.getAvailableMoves();
			int size = moves.size();
			if (size > 0)
			{
				Random r = new Random();
				int num = r.nextInt(size);
				Move nextMove = moves.get(num);
				//System.out.println("Random Player " + number + ": " + nextMove.getX() + " " + nextMove.getY());
				engine.onMoveReadyCalculated(nextMove, false);
			} else 
			{
				engine.onMoveReadyCalculated(null, true);
				System.out.println("Random Player: Pass!");
			}
			return null;
		}
		
	}
	
	
	public RandomComputerPlayer(Board board) 
	{
		super(board);
	}

	@Override
	public void yourTurn() 
	{
		System.out.println("RandomComputerPlayer's turn");
		BackgroundCalculator bc = new BackgroundCalculator(board, engine);
		bc.execute();
	}

	@Override
	public void signalLastMove() {
		// TODO Auto-generated method stub
		
	}


}
