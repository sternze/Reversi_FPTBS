package uni_klu.se2.reversi.usage_examples;

import java.util.List;


import uni_klu.se2.reversi.data.User;
import uni_klu.se2.reversi.db.factories.DAOFactory;
import uni_klu.se2.reversi.db.interfaces.UserDAO;

public class DB_UsageExample {

	/**
	 * @param args
	 */
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
	}
}
