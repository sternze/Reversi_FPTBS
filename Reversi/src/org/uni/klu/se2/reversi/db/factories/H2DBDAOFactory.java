package org.uni.klu.se2.reversi.db.factories;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.uni.klu.se2.reversi.db.H2DBGameDAO;
import org.uni.klu.se2.reversi.db.H2DBUserDAO;

public class H2DBDAOFactory extends DAOFactory {
	public static final String DRIVER="org.h2.Driver";
	public static final String DBURL="jdbc:h2:~/reversi";

	/**
	 * This methods creates a connection to the H2DB
	 * @return a connection object if the connection was successful, otherwise null
	 */
	public static Connection createConnection() {
		Connection conn = null;
		try {
			Class.forName(DRIVER);
			String user = "reversi";
			String pwd = "reversi";
			
			conn = DriverManager.getConnection(DBURL, user, pwd);
		} catch(ClassNotFoundException cnfEx) {
			System.out.println("Class '" + DRIVER + "' was not found");
			System.out.println(cnfEx.toString());
		} catch(SQLException sqlEx) {
			System.out.println("There was an DB-Access error");
			System.out.println(sqlEx.toString());
		}
		
		return conn;
	}	
	
	@Override
	public H2DBGameDAO getGameDAO() {
		return new H2DBGameDAO();
	}

	@Override
	public H2DBUserDAO getUserDAO() {
		return new H2DBUserDAO();
	}

	@Override
	public boolean createDatabase() {
		boolean created = true;
		try {
			Connection conn = createConnection();
			PreparedStatement prepstmt = conn.prepareStatement("CREATE TABLE IF NOT EXISTS USER(" +
					"UserName varchar(255) primary key not null, " + 
					"Password varchar(255) not null)");
			
			if(!prepstmt.execute()) {
				if(prepstmt.getUpdateCount() > 0) {
					System.out.println("Table USER created");
				} else {
					System.out.println("Table USER already exists");
				}
			}
			
			prepstmt = conn.prepareStatement("CREATE TABLE IF NOT EXISTS GAME(" +
					"ID UUID primary key not null," +
					"blackFields varchar(" + Integer.MAX_VALUE + ")," +
					"whiteFields varchar(" + Integer.MAX_VALUE + ")," +
					"finished BOOLEAN)");
			
			if(!prepstmt.execute()) {
				if(prepstmt.getUpdateCount() > 0) {
					System.out.println("Table GAME created");
				} else {
					System.out.println("Table GAME already exists");
				}
			}
			
			prepstmt = conn.prepareStatement("CREATE TABLE IF NOT EXISTS PlayingGame(" +
					"BlackPlayer varchar(255) not null," + 
					"WhitePlayer varchar(255) not null," + 
					"GameID UUID not null," + 
					"PRIMARY KEY(BlackPlayer, WhitePlayer, GameID)," +
					"FOREIGN KEY(BlackPlayer) REFERENCES USER(UserName)," +
					"FOREIGN KEY(WhitePlayer) REFERENCES USER(UserName)," +
					"FOREIGN KEY(GameID) REFERENCES GAME(ID))");
			
			if(!prepstmt.execute()) {
				if(prepstmt.getUpdateCount() > 0) {
					System.out.println("Table PlayingGame created");
				} else {
					System.out.println("Table PlayingGame already exists");
				}
			}
			
		} catch(Exception ex) {
			created = false;
			System.out.println(ex.toString());
		}
		return created;
	}

}
