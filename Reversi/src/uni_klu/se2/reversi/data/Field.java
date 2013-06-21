package uni_klu.se2.reversi.data;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

/**This class encapsulates the Logic for controlling a single Field
 * 
 * @version 1.0
 * @author Armin
 */
public class Field 
{
	private ObjectProperty<FieldStatus> status;
	//private FieldStatus status;
	private int         x;
	private int         y;

	public Field(int x, int y)
	{
		super();
		this.x = x;
		this.y = y;
		this.status = new SimpleObjectProperty<FieldStatus>(FieldStatus.EMPTY);
	}

	public ObjectProperty<FieldStatus> getStatus()
	{
		return status;
	}
	

	public void setStatus(ObjectProperty<FieldStatus> status) {
		this.status = status;
	}

	public void setBlack()
	{
		this.status.setValue(FieldStatus.BLACK);
	}
	
	public void setWhite()
	{
		this.status.setValue(FieldStatus.WHITE);
	}
	
	public void flip()
	{
		try {
			if (this.status.getValue() == FieldStatus.BLACK)
				this.status.setValue(FieldStatus.WHITE);
				//this.setStatus(new SimpleObjectProperty<FieldStatus>(FieldStatus.WHITE));
			else
				this.status.setValue(FieldStatus.BLACK);
				//this.setStatus(new SimpleObjectProperty<FieldStatus>(FieldStatus.BLACK));
		} catch (Exception ex) {
			System.out.println("Flip failed. " + ex.toString());
		}
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}	
}
