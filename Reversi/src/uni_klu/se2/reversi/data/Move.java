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
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public Move(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
}
