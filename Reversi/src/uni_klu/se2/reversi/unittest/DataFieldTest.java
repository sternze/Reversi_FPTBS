/**
 * 
 */
package uni_klu.se2.reversi.unittest;

import static org.junit.Assert.*;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

import org.junit.Test;

import uni_klu.se2.reversi.data.Field;
import uni_klu.se2.reversi.data.FieldStatus;

/**
 * @author Armin
 *
 */
public class DataFieldTest {

	@Test
	public void test() {
		int x = 0;
		int y = 0;
		ObjectProperty<FieldStatus> status  = new SimpleObjectProperty<FieldStatus>(FieldStatus.EMPTY);
		Field testField = new Field(x, y);
		assertEquals(testField.getX(), x);
		assertEquals(testField.getY(), y);
		assertEquals(testField.getStatus().get(), status.get());
		testField.setBlack();
		status.setValue(FieldStatus.BLACK);
		assertEquals(testField.getStatus().get(), status.get());
		testField.flip();
		status.setValue(FieldStatus.WHITE);
		assertEquals(testField.getStatus().get(), status.get());
		testField.flip();
		status.setValue(FieldStatus.BLACK);
		assertEquals(testField.getStatus().get(), status.get());
		testField.setWhite();
		status.setValue(FieldStatus.WHITE);
		assertEquals(testField.getStatus().get(), status.get());
		status.setValue(FieldStatus.BLACK);
		testField.setStatus(status);
		assertEquals(testField.getStatus().get(), status.get());
		
	}

}
