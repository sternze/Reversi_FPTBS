package uni_klu.se2.reversi.engine.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Random;

import uni_klu.se2.reversi.data.Board;
import uni_klu.se2.reversi.data.BoardStatus;
import uni_klu.se2.reversi.data.Move;
import uni_klu.se2.reversi.engine.IPlayer;
import uni_klu.se2.reversi.engine.ReversiEngine;
import uni_klu.se2.reversi.helper.SocketHelper;

import javax.swing.SwingWorker;

public class SocketPlayer extends IPlayer
{
	public int number; 
	protected SocketHelper socketHelper;
	private class BackgroundCalculator extends SwingWorker<Boolean, Boolean>
	{
		protected Board          board;
		protected ReversiEngine  engine;

		
		public BackgroundCalculator(Board board, ReversiEngine engine) {
			super();
			this.board = board;
			this.engine = engine;
		}

		@Override
		protected Boolean doInBackground() throws Exception {
			//Send own Move to Client
			Move lastMove = board.getLastMove();
			if (lastMove != null) //If client plays first, must not send him move
			{
				socketHelper.sendMove(lastMove);
			}
			//check if game is over now
			if (board.getStatus() != BoardStatus.INPROGRESS)
				return null;
			//Receive Client Move
			Move nextMove = socketHelper.getMove();
			if (nextMove == null)
				engine.onMoveReadyCalculated(null, true);
			else
				engine.onMoveReadyCalculated(nextMove, nextMove.isPassMove());
			return null;
		}
		
	}
	
	
	public SocketPlayer(Board board, SocketHelper socketHelper) 
	{
		super(board);
		this.socketHelper = socketHelper;
	}

	@Override
	public void yourTurn() 
	{
		BackgroundCalculator bc = new BackgroundCalculator(board, engine);
		bc.execute();
	}

	@Override
	public void signalLastMove() {
		Move lastMove = board.getLastMove();
		if (lastMove != null) //If client plays first, must not send him move
		{
			socketHelper.sendMove(lastMove);
		}
	}


}
