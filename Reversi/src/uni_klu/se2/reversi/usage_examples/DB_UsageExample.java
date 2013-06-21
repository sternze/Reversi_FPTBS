package uni_klu.se2.reversi.usage_examples;

import java.util.List;
import java.util.UUID;

import javafx.beans.property.SimpleObjectProperty;


import uni_klu.se2.reversi.data.Field;
import uni_klu.se2.reversi.data.FieldStatus;
import uni_klu.se2.reversi.data.Game;
import uni_klu.se2.reversi.data.User;
import uni_klu.se2.reversi.db.factories.DAOFactory;
import uni_klu.se2.reversi.db.interfaces.GameDAO;
import uni_klu.se2.reversi.db.interfaces.UserDAO;

public class DB_UsageExample {

	private static final int BOARDSIZE = 8;
	private static final String DB_FIELD_SEPERATOR = ";";

	/**
	 * @param args
	 
	public static void dbExample(String[] args) {
		// create the required DAO Factory
		DAOFactory h2DBFactory = DAOFactory.getDAOFactory(DAOFactory.H2DB);

		// Create a DAO
		UserDAO userDAO = h2DBFactory.getUserDAO();

		// get all users from DB
		List<User> allUsers = userDAO.getAllUsers();
		
		for(User u : allUsers) {
			System.out.println("Username: " + u.getUserName() + "; Password: " + u.getPassWord());
		}
	}*/
	
	public static UUID createNewGame(String blackUserName, String whiteUserName) {
		ensureUserExists(new String[] {blackUserName, whiteUserName});
		
		// create the required DAO Factory
		DAOFactory h2DBFactory = DAOFactory.getDAOFactory(DAOFactory.H2DB);

		// Create a DAO
		GameDAO gameDAO = h2DBFactory.getGameDAO();
		
		UUID gameID = gameDAO.createGame(blackUserName, whiteUserName, 0, 0);
		
		return gameID;
	}

	private static void ensureUserExists(String[] strings) {
		// create the required DAO Factory
		DAOFactory h2DBFactory = DAOFactory.getDAOFactory(DAOFactory.H2DB);

		// Create a DAO
		UserDAO userDAO = h2DBFactory.getUserDAO();

		User black = userDAO.getUser(strings[0]);

		if(black == null) {
			black = new User();
			black.setUserName(strings[0]);
			black.setPassWord(strings[0]);
			
			userDAO.insertUser(black);
		}
		
		User white = userDAO.getUser(strings[0]);

		if(white == null) {
			white = new User();
			white.setUserName(strings[1]);
			white.setPassWord(strings[1]);
			
			userDAO.insertUser(white);
		}		
	}
	
	public static void loadGame(UUID gameID) {
		// create the required DAO Factory
		DAOFactory h2DBFactory = DAOFactory.getDAOFactory(DAOFactory.H2DB);

		// Create a DAO
		GameDAO gameDAO = h2DBFactory.getGameDAO();
		
		Game g = gameDAO.getGame(gameID);
		
		Field[][] fields = getFieldArrayFromGame(g);
		
		FieldStatus currentPlayer = g.isBlacksTurn() == true ? FieldStatus.BLACK : FieldStatus.WHITE;

	}

	private static Field[][] getFieldArrayFromGame(Game g) {
		Field[][] f = new Field[BOARDSIZE][BOARDSIZE];
		
		
		for (int i = 0; i < BOARDSIZE; i++)
			for (int j = 0; j < BOARDSIZE; j++) {
				f[i][j] = new Field(i, j);
				if(g.getBlackFields().contains(i + "_" + j + DB_FIELD_SEPERATOR)) {
					f[i][j].setStatus(new SimpleObjectProperty<FieldStatus>(FieldStatus.BLACK));
				} else if(g.getWhiteFields().contains(i + "_" + j + DB_FIELD_SEPERATOR)) {
					f[i][j].setStatus(new SimpleObjectProperty<FieldStatus>(FieldStatus.WHITE));
				} else {
					f[i][j].setStatus(new SimpleObjectProperty<FieldStatus>(FieldStatus.EMPTY));
				}
			}
		
		return f;
	}
}
