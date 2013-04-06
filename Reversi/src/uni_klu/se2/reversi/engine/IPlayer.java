package uni_klu.se2.reversi.engine;

import uni_klu.se2.reversi.data.Board;
import uni_klu.se2.reversi.data.Move;

public abstract class IPlayer
{
	protected Board         board;
	protected ReversiEngine engine;
	protected Move          nextMove;
	
	public IPlayer(Board board) 
	{
		super();
		this.board = board;
	}
	
	public void setEngine(ReversiEngine engine) 
	{
		this.engine = engine;
	}


	public abstract void yourTurn();
	
	public void signalEngine(boolean pass)
	{
		engine.onMoveReadyCalculated(nextMove, pass);
	}
}
