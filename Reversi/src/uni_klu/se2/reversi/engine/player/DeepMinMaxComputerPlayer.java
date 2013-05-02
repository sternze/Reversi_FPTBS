package uni_klu.se2.reversi.engine.player;

import java.util.List;

import uni_klu.se2.reversi.data.Board;
import uni_klu.se2.reversi.data.Move;
import uni_klu.se2.reversi.engine.IPlayer;
import uni_klu.se2.reversi.engine.ReversiEngine;
import uni_klu.se2.reversi.engine.algorithms.DeepMinMax;

import javax.swing.SwingWorker;


public class DeepMinMaxComputerPlayer extends IPlayer
{
	public int number;
	private int depth;
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
				engine.onMoveReadyCalculated(DeepMinMax.getMove(board, depth), false);
			} else 
			{
				engine.onMoveReadyCalculated(null, true);
				System.out.println("DeepMinMax Player: Pass!");
			}
			return null;
		}
		
	}
	
	
	public DeepMinMaxComputerPlayer(Board board, int depth) 
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


}
