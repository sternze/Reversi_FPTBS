package uni_klu.se2.reversi.engine;

import uni_klu.se2.reversi.gui.IReversiGUI;
import uni_klu.se2.reversi.data.Board;
import uni_klu.se2.reversi.data.BoardStatus;
import uni_klu.se2.reversi.data.Move;

/**This class controls the Board and manages both players
 * 
 * @version 1.0
 * @author Armin
 */
public class ReversiEngine 
{
	private Board board;
	private IPlayer player1;
	private IPlayer player2;
	private int     turn;
	public IReversiGUI watcher;
	
	
	public ReversiEngine(Board board, IPlayer player1, IPlayer player2) {
		super();
		this.board = board;
		this.player1 = player1;
		this.player2 = player2;
		turn = 0;
	}

	public void onMoveReadyCalculated(Move move, boolean pass)
	{
		if (pass)
			board.pass();
		else
			board.move(move);
		turn++;

		if (board.getStatus() != BoardStatus.INPROGRESS)
		{
			
			if (turn%2 == 0)
				player1.signalLastMove();
			else
				player2.signalLastMove();
		}
		
		watcher.paintBoard();
	
		if (board.getStatus() != BoardStatus.INPROGRESS)
			return;
		
		if (turn%2 == 0)
			player1.yourTurn();
		else
			player2.yourTurn();
		
	}
	
	public void start()
	{
		player1.yourTurn();
	}
}
