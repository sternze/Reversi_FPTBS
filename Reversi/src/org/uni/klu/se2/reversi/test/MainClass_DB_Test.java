package org.uni.klu.se2.reversi.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.uni.klu.se2.reversi.data.User;
import org.uni.klu.se2.reversi.db.factories.DAOFactory;
import org.uni.klu.se2.reversi.db.interfaces.UserDAO;

public class MainClass_DB_Test {

	private static Connection conn = null;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
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
