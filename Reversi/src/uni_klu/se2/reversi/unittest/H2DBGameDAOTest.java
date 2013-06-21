package uni_klu.se2.reversi.unittest;

import static org.junit.Assert.*;

import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;

import uni_klu.se2.reversi.data.Game;
import uni_klu.se2.reversi.db.factories.DAOFactory;
import uni_klu.se2.reversi.db.interfaces.GameDAO;

/**
 * @author Daniel
 *
 */
public class H2DBGameDAOTest {
	
	private static GameDAO myGame = null;

	@BeforeClass
	public static void init() {
		// create the required DAO Factory
		DAOFactory h2DBFactory = DAOFactory.getDAOFactory(DAOFactory.H2DB);

		// Create a DAO
		myGame = h2DBFactory.getGameDAO();
		
		h2DBFactory.recreateDatabase();
	}

	@Test
	public void testCRUDGame() {
		UUID GameGuid = myGame.createGame("", "", 0, 0);
		
		System.out.println("Created Game");
		Game createdGame = myGame.getGame(GameGuid);
		//Check if the created game was created according to the requirements
		assertEquals(GameGuid, createdGame.getID());
		assertNull(createdGame.getBlackFields());
		assertNull(createdGame.getWhiteFields());
		assertFalse(createdGame.isBlacksTurn());
		assertFalse(createdGame.isFinished());

		System.out.println("Game meets requirements");
		
		createdGame.setBlackFields("A1,A2,A3");
		
		myGame.updateGame(createdGame);
		System.out.println("Updated Game");
		createdGame = null;
		
		Game updatedGame = myGame.getGame(GameGuid);
		//Check if the created game was created according to the requirements
		assertEquals(GameGuid, updatedGame.getID());
		assertEquals("A1,A2,A3", updatedGame.getBlackFields());
		assertNull(updatedGame.getWhiteFields());
		assertFalse(updatedGame.isBlacksTurn());
		assertFalse(updatedGame.isFinished());

		System.out.println("Game meets requirements");
		
		myGame.setGameToFinished(GameGuid);
		updatedGame = null;
		
		System.out.println("Set game to finished");
		
		Game finishedGame = myGame.getGame(GameGuid);
		//Check if the created game was created according to the requirements
		assertEquals(GameGuid, finishedGame.getID());
		assertEquals("A1,A2,A3", finishedGame.getBlackFields());
		assertNull(finishedGame.getWhiteFields());
		assertFalse(finishedGame.isBlacksTurn());
		assertTrue(finishedGame.isFinished());
		
		System.out.println("Game meets requirements");
	}
}
