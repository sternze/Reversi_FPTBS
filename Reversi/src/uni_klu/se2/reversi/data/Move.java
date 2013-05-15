package uni_klu.se2.reversi.data;
/**This encapsulates the inforamtion needed for a move
 * 
 * @version 1.0
 * @author Armin
 */
public class Move 
{
	private int x;
	private int y;
	private boolean isPassMove;
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public boolean isPassMove() {
		return isPassMove;
	}
	public void setPassMove(boolean isPassMove) {
		this.isPassMove = isPassMove;
	}
	public void setY(int y) {
		this.y = y;
	}
	public String toNetString()
	{
		return x + ";" + y;
	}
	public Move(int x, int y) {
		super();
		this.x = x;
		this.y = y;
		this.isPassMove = false;
	}
	public Move(String netString) {
		super();
		if (netString != "pass")
		{
			String[] coords = netString.split(";");
			this.x = Integer.parseInt(coords[0]);
			this.y = Integer.parseInt(coords[1]);
		} else
		{
			this.x = 0;
			this.y = 0;
			this.isPassMove = true;
		}
	}
	public Move() {
		super();
		this.x = 0;
		this.y = 0;
		this.isPassMove = true;
	}
}
