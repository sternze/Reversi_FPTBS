package uni_klu.se2.reversi.unittest;

import static org.junit.Assert.*;

import org.junit.Test;

import uni_klu.se2.reversi.data.Board;
import uni_klu.se2.reversi.data.BoardStatus;
import uni_klu.se2.reversi.data.FieldStatus;
import uni_klu.se2.reversi.data.Move;

public class DataBoardTest {

	@Test
	public void test() {
		Board testBoard = new Board();
		assertEquals(testBoard.getBlackFieldsOnBoard(), 2);
		assertEquals(testBoard.getWhiteFieldsOnBoard(), 2);
		assertEquals(testBoard.getLastMove(), null);
		assertEquals(testBoard.getAvailableMoves().size(), 4);
		assertEquals(testBoard.getCurrentPlayer(), FieldStatus.WHITE);
		assertEquals(testBoard.getStatus(), BoardStatus.INPROGRESS);
		assertEquals(testBoard.pass(), false);
		assertEquals(testBoard.isMoveLegal(0, 0), false);
		assertEquals(testBoard.isMoveLegal(5, 5), true);
		assertEquals(testBoard.move(new Move(0, 0)), false);
		
		assertEquals(testBoard.move(new Move(5, 5)), true);
		assertEquals(testBoard.getBlackFieldsOnBoard(), 1);
		assertEquals(testBoard.getWhiteFieldsOnBoard(), 4);
		assertEquals(testBoard.getLastMove().getX(), 5);
		assertEquals(testBoard.getLastMove().getY(), 5);
		assertEquals(testBoard.getAvailableMoves().size(), 3);
		assertEquals(testBoard.getCurrentPlayer(), FieldStatus.BLACK);
		assertEquals(testBoard.getStatus(), BoardStatus.INPROGRESS);
		assertEquals(testBoard.pass(), false);
		assertEquals(testBoard.isMoveLegal(0, 0), false);
		assertEquals(testBoard.isMoveLegal(3, 2), true);
		assertEquals(testBoard.move(new Move(0, 0)), false);
		
		assertEquals(testBoard.move(new Move(3, 2)), true);
		assertEquals(testBoard.getBlackFieldsOnBoard(), 3);
		assertEquals(testBoard.getWhiteFieldsOnBoard(), 3);
		assertEquals(testBoard.getLastMove().getX(), 3);
		assertEquals(testBoard.getLastMove().getY(), 2);
		assertEquals(testBoard.getAvailableMoves().size(), 5);
		assertEquals(testBoard.getCurrentPlayer(), FieldStatus.WHITE);
		assertEquals(testBoard.getStatus(), BoardStatus.INPROGRESS);
		assertEquals(testBoard.pass(), false);
		assertEquals(testBoard.isMoveLegal(0, 0), false);
		assertEquals(testBoard.isMoveLegal(2, 1), true);
		assertEquals(testBoard.move(new Move(0, 0)), false);
		
		Board cloneBoard = testBoard.cloneBoard();
		
		assertEquals(cloneBoard.getBlackFieldsOnBoard(), 3);
		assertEquals(cloneBoard.getWhiteFieldsOnBoard(), 3);
		assertEquals(cloneBoard.getLastMove().getX(), 3);
		assertEquals(cloneBoard.getLastMove().getY(), 2);
		assertEquals(cloneBoard.getAvailableMoves().size(), 5);
		assertEquals(cloneBoard.getCurrentPlayer(), FieldStatus.WHITE);
		assertEquals(cloneBoard.getStatus(), BoardStatus.INPROGRESS);
		assertEquals(cloneBoard.pass(), false);
		assertEquals(cloneBoard.isMoveLegal(0, 0), false);
		assertEquals(cloneBoard.isMoveLegal(2, 1), true);
		assertEquals(cloneBoard.move(new Move(0, 0)), false);
	}

}
