package uni_klu.se2.reversi.engine.player;

import java.util.List;

import uni_klu.se2.reversi.data.Board;
import uni_klu.se2.reversi.data.Move;
import uni_klu.se2.reversi.engine.IPlayer;
import uni_klu.se2.reversi.engine.ReversiEngine;
import uni_klu.se2.reversi.engine.algorithms.SimpleMinMax;

import javax.swing.SwingWorker;


public class SimpleMinMaxComputerPlayer extends IPlayer
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
			List<Move> moves = board.getAvailableMoves();
			Thread.sleep(250);
			int size = moves.size();
			if (size > 0)
			{
				engine.onMoveReadyCalculated(SimpleMinMax.getMove(moves, board), false);
			} else 
			{
				engine.onMoveReadyCalculated(null, true);
				System.out.println("AITrail Player : Pass!");
			}
			return null;
		}
		
	}
	
	
	public SimpleMinMaxComputerPlayer(Board board) 
	{
		super(board);
	}

	@Override
	public void yourTurn() 
	{
		BackgroundCalculator bc = new BackgroundCalculator(board, engine);
		bc.execute();
	}


}
