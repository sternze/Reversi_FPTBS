package uni_klu.se2.reversi.unittest;

import static org.junit.Assert.*;

import org.junit.Test;
import uni_klu.se2.reversi.data.*;

public class DataMoveTest {

	@Test
	public void test() {
		Move testMove = new Move();
		assertEquals(testMove.getX(), 0);
		assertEquals(testMove.getY(), 0);
		assertTrue(testMove.isPassMove());
		testMove.setPassMove(false);
		assertEquals(testMove.isPassMove(), false);
		testMove.setX(4);
		testMove.setY(4);
		assertEquals(testMove.getX(), 4);
		assertEquals(testMove.getY(), 4);
		testMove = new Move(2, 2);
		assertEquals(testMove.getX(), 2);
		assertEquals(testMove.getY(), 2);
		assertEquals(testMove.isPassMove(), false);
		testMove = new Move("pass");
		assertEquals(testMove.getX(), 0);
		assertEquals(testMove.getY(), 0);
		assertEquals(testMove.isPassMove(), true);
		testMove = new Move("3;3");
		assertEquals(testMove.getX(), 3);
		assertEquals(testMove.getY(), 3);
		assertEquals(testMove.isPassMove(), false);
	}

}
