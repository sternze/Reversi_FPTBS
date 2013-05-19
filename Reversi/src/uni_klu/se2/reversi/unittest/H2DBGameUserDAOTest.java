package uni_klu.se2.reversi.unittest;

import static org.junit.Assert.*;

import java.util.List;
import java.util.UUID;

import org.junit.BeforeClass;
import org.junit.Test;

import uni_klu.se2.reversi.data.Game;
import uni_klu.se2.reversi.data.User;
import uni_klu.se2.reversi.db.factories.DAOFactory;
import uni_klu.se2.reversi.db.interfaces.GameDAO;
import uni_klu.se2.reversi.db.interfaces.UserDAO;

public class H2DBGameUserDAOTest {

	private static GameDAO myGame = null;
	private static UserDAO myUser = null;

	@BeforeClass
	public static void init() {
		// create the required DAO Factory
		DAOFactory h2DBFactory = DAOFactory.getDAOFactory(DAOFactory.H2DB);

		// Create a DAO
		myGame = h2DBFactory.getGameDAO();
		myUser = h2DBFactory.getUserDAO();
		
		h2DBFactory.recreateDatabase();
	}

	@Test
	public void testUserGameDAO() {
		User userPeppi = new User();
		userPeppi.setUserName("Peppi");
		userPeppi.setPassWord("Peppi");

		User userHorst = new User();
		userHorst.setUserName("Horst");
		userHorst.setPassWord("Horst");

		User userGudrun = new User();
		userGudrun.setUserName("Gudrun");
		userGudrun.setPassWord("Gudrun");

		myUser.insertUser(userPeppi);
		myUser.insertUser(userHorst);
		myUser.insertUser(userGudrun);
		
		System.out.println("Users created");

		UUID gameID = myGame.createGame("Peppi", "Horst");
		System.out.println("Game Peppi - Horst created");
		myGame.createGame("Peppi", "Gudrun");
		System.out.println("Game Peppi - Gudrun created");

		List<Game> allGames = myGame.getAllGames(true);
		
		assertEquals(allGames.size(), 2);
		System.out.println(allGames.size() + " games found");
		
		System.out.println("Getting games of Peppi");
		List<Game> peppisGames = myGame.getGamesOfUser(userPeppi.getUserName(), false);
		assertEquals(peppisGames.size(), 2);
		System.out.println("Peppi plays " + peppisGames.size() + " games");
		

		System.out.println("Getting games of Gudrun");
		List<Game> gudrunsGames = myGame.getGamesOfUser(userGudrun.getUserName(), false);
		assertEquals(gudrunsGames.size(), 1);
		System.out.println("Gudrund plays " + gudrunsGames.size() + " game");
		
		System.out.println("Finishing one game of peppi");
		
		myGame.setGameToFinished(gameID);
		
		System.out.println("Getting games of Peppi, including finished ones");
		List<Game> peppisGames2 = myGame.getGamesOfUser(userPeppi.getUserName(), true);
		assertEquals(peppisGames2.size(), 2);
		System.out.println("Peppi played " + peppisGames2.size() + " games");
		
		
		System.out.println("Getting games of Peppi, NOT including finished ones");
		List<Game> peppisGames3 = myGame.getGamesOfUser(userPeppi.getUserName(), false);
		assertEquals(peppisGames3.size(), 1);
		System.out.println("Peppi plays " + peppisGames3.size() + " game");
		
	}

}
