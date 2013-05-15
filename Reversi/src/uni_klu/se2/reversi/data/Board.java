package uni_klu.se2.reversi.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**This class encapsulates the Logic for controlling the whole Board.
 * It will hold the state of the game and take moves from players.
 * For every new game the board must be resetted.
 * 
 * @version 1.0
 * @author Armin
 */
public class Board 
{
	private Field[][]   fields;
	private FieldStatus currentPlayer;    

	private BoardStatus status;
	private boolean     statusCalculated;
	private List<Move>  currentLegalMoves;
	private boolean     currentLegalMovesCalculated;
	private int         whiteFieldsOnBoard;
	private int         blackFieldsOnBoard;
	private boolean     fieldCountsUpdated;
	private final int   BOARDSIZE = 8;
	private Move        lastMove;

	
	public Move getLastMove() {
		return lastMove;
	}
	public             Board                () 
	{
		super();
		this.fields = new Field[BOARDSIZE][BOARDSIZE];
		for (int i = 0; i < BOARDSIZE; i++)
			for (int j = 0; j < BOARDSIZE; j++)
				this.fields[i][j] = new Field(i, j);
		
		setInitialFieldValues();
		currentLegalMovesCalculated = false;
		fieldCountsUpdated          = false;
		statusCalculated            = false;
		lastMove                    = null;
	}	
	public             Board                (Field[][] fields, FieldStatus currentPlayer) 
	{
		super();
		this.fields = new Field[BOARDSIZE][BOARDSIZE];
		for (int i = 0; i < BOARDSIZE; i++)
			for (int j = 0; j < BOARDSIZE; j++)
			{
				this.fields[i][j] = new Field(i, j);
				this.fields[i][j].setStatus(fields[i][j].getStatus());
			}
		this.currentPlayer          = currentPlayer;
		currentLegalMovesCalculated = false;
		fieldCountsUpdated          = false;
		statusCalculated            = false;
		lastMove                     = null;
	}		
	public Board       cloneBoard           () 
	{
		return new Board(this.fields, this.currentPlayer);
	}
	public BoardStatus getStatus            () 
	{
		if (statusCalculated)
			return this.status;	
		statusCalculated = true;
		this.status = BoardStatus.INPROGRESS;
		currentLegalMoves = getAvailableMoves();
		if (currentLegalMoves.isEmpty())
		{
			//Check if opponent also has no moves, then game is over
			FieldStatus opponent        = FieldStatus.BLACK;
			boolean     legalFieldFound = false;
			if (currentPlayer == FieldStatus.BLACK)
				opponent = FieldStatus.WHITE;
			int i = 0;
			int j = 0;
			while (!legalFieldFound && (i < BOARDSIZE))
			{
				while (!legalFieldFound && (j < BOARDSIZE))
				{
					if (isFieldLegal(this.fields[i][j], opponent))
						legalFieldFound = true;
					j++;
				}
				i++;
			}
			//Actually no move for both players possible, so game is over
			if (!legalFieldFound)
			{
				//Check who has won
				updateFieldCounts();
				if (blackFieldsOnBoard > whiteFieldsOnBoard)
					this.status = BoardStatus.BLACKWON;
				else if (blackFieldsOnBoard < whiteFieldsOnBoard)
					this.status = BoardStatus.WHITEWON;
				else
					this.status = BoardStatus.DRAW;
			}
		}
		return this.status;	
	}
	public Field[][]   getFields            () 
	{
		return fields;
	}	
	public boolean     move                 (Move move)
	{
		if (getStatus() != BoardStatus.INPROGRESS)
			return false;
		int x = move.getX();
		int y = move.getY();
		if (!isMoveLegal(x, y))
			return false;
		lastMove                    = move;
		currentLegalMovesCalculated = false;
		fieldCountsUpdated          = false;
		statusCalculated            = false;
		flipFieldsAfterMove(x, y);
		if (currentPlayer == FieldStatus.WHITE)
		{
			fields[x][y].setWhite();
			currentPlayer = FieldStatus.BLACK;
		} else if (currentPlayer == FieldStatus.BLACK)
		{
			fields[x][y].setBlack();
			currentPlayer = FieldStatus.WHITE;
		}
		return true;
	}
	public boolean     pass                 ()
	{
		if (getStatus() != BoardStatus.INPROGRESS)
			return false;
		currentLegalMoves = getAvailableMoves();
		if (!currentLegalMoves.isEmpty())
			return false;
		
		currentLegalMovesCalculated = false;
		fieldCountsUpdated          = false;
		statusCalculated            = false;
		if (currentPlayer == FieldStatus.WHITE)
		{
			currentPlayer = FieldStatus.BLACK;
		} else if (currentPlayer == FieldStatus.BLACK)
		{
			currentPlayer = FieldStatus.WHITE;
		}
		return true;
	}
	public int         getWhiteFieldsOnBoard() 
	{
		updateFieldCounts();
		return whiteFieldsOnBoard;
	}
	public int         getBlackFieldsOnBoard() 
	{
		updateFieldCounts();
		return blackFieldsOnBoard;
	}
	public List<Move>  getAvailableMoves    ()
	{
		if (currentLegalMovesCalculated)
			return currentLegalMoves;
		currentLegalMovesCalculated = true;
		currentLegalMoves = new ArrayList<Move>();
		for (int i = 0; i < BOARDSIZE; i++)
			for (int j = 0; j < BOARDSIZE; j++)
				if (isFieldLegal(this.fields[i][j], currentPlayer))
				{
					currentLegalMoves.add(new Move(this.fields[i][j].getX(), this.fields[i][j].getY()));
				}
		return currentLegalMoves;
	}
	public FieldStatus getCurrentPlayer     () 
	{
		return currentPlayer;
	}
	
	private void    setInitialFieldValues()
	{
		this.fields[3][3].setWhite();
		this.fields[4][3].setWhite();
		this.fields[3][4].setBlack();
		this.fields[4][4].setBlack();
		
		currentPlayer = FieldStatus.WHITE;
	}	
	private boolean isFieldLegal         (Field field, FieldStatus playerOnTheMove)
	{
		if (field.getStatus() != FieldStatus.EMPTY)
			return false;
		
		int         posX      = field.getX();
		int         posY      = field.getY();
		//check all directions
		for (int x = -1; x <= 1; x++)
			for (int y = -1; y <= 1; y++)
			{
				if (!((x == 0) && (y == 0)))
				{
					boolean abortSearch = false;
					int     depth       = 1;
					//check along this direction
					while (!abortSearch)
					{
						int searchX = posX + (x * depth);
						int searchY = posY + (y * depth);
						
						// if out of bonds abort
						if ((searchX < 0) || (searchX+1 > BOARDSIZE) || (searchY < 0) || (searchY+1 > BOARDSIZE))
						{
							abortSearch = true;
							break;
						}
						//if field is empty abort
						if (fields[searchX][searchY].getStatus() == FieldStatus.EMPTY)
						{
							abortSearch = true;
							break;
						}
						//if field is next and has same color
						if ((depth == 1) && (fields[searchX][searchY].getStatus() == playerOnTheMove))
						{
							abortSearch = true;
							break;
						}
						//if field is further than next and has same color: success!
						if ((depth > 1) && (fields[searchX][searchY].getStatus() == playerOnTheMove))
						{
							return true;
						}
						depth++;
					}
				}
			}
		return false;
	}	
	private boolean isMoveLegal          (int x, int y)
	{
		if ((x < 0) || (x+1 > BOARDSIZE) || (y < 0) || (y+1 > BOARDSIZE))
			return false;
		
		if (!currentLegalMovesCalculated)
			currentLegalMoves = getAvailableMoves();
		
		Iterator<Move> iterator = this.currentLegalMoves.iterator();
		while(iterator.hasNext())
		{
			Move field = iterator.next();
			if ((field.getX() == x) && (field.getY() == y))
				return true;
		}
		return false;
	}
	private void    flipFieldsAfterMove  (int posX, int posY)
	{
		//check all directions
		for (int x = -1; x <= 1; x++)
			for (int y = -1; y <= 1; y++)
			{
				if (!((x == 0) && (y == 0)))
				{
					boolean abortSearch = false;
					boolean flipFields  = false;
					List<Field> fieldsToFlip = new ArrayList<Field>();
					int     depth       = 1;
					//check along this direction
					while (!abortSearch)
					{
						int searchX = posX + (x * depth);
						int searchY = posY + (y * depth);
						
						// if out of bonds abort
						if ((searchX < 0) || (searchX+1 > BOARDSIZE) || (searchY < 0) || (searchY+1 > BOARDSIZE))
						{
							abortSearch = true;
							break;
						}
						//if field is empty abort
						if (fields[searchX][searchY].getStatus() == FieldStatus.EMPTY)
						{
							abortSearch = true;
							break;
						}
						//if field is next and has same color
						if ((depth == 1) && (fields[searchX][searchY].getStatus() == currentPlayer))
						{
							abortSearch = true;
							break;
						}
						//if field is further than next and has same color: success!
						if ((depth > 1) && (fields[searchX][searchY].getStatus() == currentPlayer))
						{
							abortSearch = true;
							flipFields  = true;
							break;
						}
						fieldsToFlip.add(fields[searchX][searchY]);
						depth++;
					}
					if (flipFields)
					{
						Iterator<Field> iterator = fieldsToFlip.iterator();
						while(iterator.hasNext())
						{
							Field field = iterator.next();
							field.flip();
						}
					}
				}
			}
	}
	private void    updateFieldCounts    ()
	{
		if (fieldCountsUpdated)
			return;
		fieldCountsUpdated = true;
		this.blackFieldsOnBoard = 0;
		this.whiteFieldsOnBoard = 0;
		for (int i = 0; i < BOARDSIZE; i++)
			for (int j = 0; j < BOARDSIZE; j++)
				if (this.fields[i][j].getStatus() == FieldStatus.BLACK)
					this.blackFieldsOnBoard++;
				else if (this.fields[i][j].getStatus() == FieldStatus.WHITE)
					this.whiteFieldsOnBoard++;
	}
}
