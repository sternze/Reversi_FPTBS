package uni_klu.se2.reversi.unittest;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.SimpleObjectProperty;

import org.junit.Test;

import uni_klu.se2.reversi.data.Field;
import uni_klu.se2.reversi.data.FieldStatus;
import uni_klu.se2.reversi.data.Game;
import uni_klu.se2.reversi.data.User;

public class DataGameTest {

	@Test
	public void test() {
		Game testGame = new Game();
		testGame.setFinished(false);
		List<Game> games =  new ArrayList<Game>();
		games.add(testGame);
		User user1 = new User();
		user1.setUserName("test");
		assertEquals(user1.getUserName(), "test");
		user1.setGames(games);
		assertEquals(user1.getGames().get(0), testGame);
		user1.addGame(testGame);
		assertEquals(user1.getGames().get(1), testGame);
		user1.setPassWord("test");
		assertEquals(user1.getPassWord().isEmpty(), false);
		user1.setPassWordHash("test");
		testGame.setBlackPlayer(user1);
		assertEquals(testGame.getBlackPlayer().getUserName(), "test");
		testGame.setWhitePlayer(user1);
		assertEquals(testGame.getWhitePlayer().getUserName(), "test");
		testGame.setBlacksTurn(true);
		assertEquals(testGame.getCurrentPlayer(), FieldStatus.BLACK);
		testGame.setBlacksTurn(false);
		assertEquals(testGame.getCurrentPlayer(), FieldStatus.WHITE);
		testGame.setBlackAlgorithmId(0);
		assertEquals(testGame.getBlackAlgorithmId(), 0);
		testGame.setWhiteAlgorithmId(0);
		assertEquals(testGame.getWhiteAlgorithmId(), 0);
		testGame.setBlackFields("0_0;1_1;");
		testGame.setWhiteFields("2_2;3_3;");
		Field[][] f = testGame.getFieldArray();
		SimpleObjectProperty<FieldStatus> fsBlack = new SimpleObjectProperty<FieldStatus>(FieldStatus.BLACK);
		SimpleObjectProperty<FieldStatus> fsWhite = new SimpleObjectProperty<FieldStatus>(FieldStatus.WHITE);
		SimpleObjectProperty<FieldStatus> fsEmpty = new SimpleObjectProperty<FieldStatus>(FieldStatus.EMPTY);
		assertEquals(f[0][0].getStatus().get(), fsBlack.get());
		assertEquals(f[1][1].getStatus().get(), fsBlack.get());
		assertEquals(f[2][2].getStatus().get(), fsWhite.get());
		assertEquals(f[3][3].getStatus().get(), fsWhite.get());
		assertEquals(f[1][2].getStatus().get(), fsEmpty.get());

	}

}
