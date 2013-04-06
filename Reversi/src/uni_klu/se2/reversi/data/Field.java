package uni_klu.se2.reversi.data;

/**This class encapsulates the Logic for controlling a single Field
 * 
 * @version 1.0
 * @author Armin
 */
public class Field 
{
	private FieldStatus status;
	private int         x;
	private int         y;

	public Field(int x, int y)
	{
		super();
		this.x = x;
		this.y = y;
		this.status = FieldStatus.EMPTY;
	}

	public FieldStatus getStatus()
	{
		return status;
	}
	

	public void setStatus(FieldStatus status) {
		this.status = status;
	}

	public void setBlack()
	{
		this.status = FieldStatus.BLACK;
	}
	
	public void setWhite()
	{
		this.status = FieldStatus.WHITE;
	}
	
	public void flip()
	{
		if (this.status == FieldStatus.BLACK)
			this.status = FieldStatus.WHITE;
		else
			this.status = FieldStatus.BLACK;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}	
}
