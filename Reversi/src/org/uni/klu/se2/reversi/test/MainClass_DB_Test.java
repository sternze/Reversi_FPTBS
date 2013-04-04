package org.uni.klu.se2.reversi.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MainClass_DB_Test {

	private static Connection conn = null;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		openAndCreateDB();
		deleteAllDataAndInsertSampleValue();
		selectAllUsers();
		closeDB();
	}
	
	private static void selectAllUsers() {
		PreparedStatement prepstmt;
		try {
			prepstmt = conn.prepareStatement("SELECT * FROM USER");
		
			if(prepstmt.execute()) {
				ResultSet rs = prepstmt.getResultSet();
				
				while(rs.next()) {
					String username = rs.getString(1);
					String pass = rs.getString(2);
					System.out.println("Username: " + username + "; Password: " + pass);
				}
				rs.close();
			} else {
				System.out.println("Select was NOT successfull");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void closeDB() {
		if(conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private static void deleteAllDataAndInsertSampleValue() {
		String[] sampleValues = new String[] {
			"INSERT INTO USER VALUES('Computer', 'c1')",
			"INSERT INTO USER VALUES('NetworkPlayer', 'np1')",
			"INSERT INTO USER VALUES('User1', 'User1')",
			"INSERT INTO GAME VALUES('4DB74570-9D05-11E2-9E96-0800200C9A66', null, null, 'false')",
			"INSERT INTO PlayingGame VALUES('User1', 'Computer', '4DB74570-9D05-11E2-9E96-0800200C9A66')"
		};
		try {
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM PlayingGame; DELETE FROM GAME; DELETE FROM USER");
			
			if(!stmt.execute()) {
				System.out.println("Deleted: " + stmt.getUpdateCount() + " Entries");
			}
			
			
			
			for(int i = 0; i < sampleValues.length; i++) {
				PreparedStatement prepstmt = conn.prepareStatement(sampleValues[i]);
				
				if(!prepstmt.execute()) {
					System.out.println("Updated: " + prepstmt.getUpdateCount());
					System.out.println("Insert " + (i+1) + " was successfull");
				}
			}
			
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static void openAndCreateDB() {
		try {
			Class.forName("org.h2.Driver");
			String url = "jdbc:h2:~/reversi";
			String user = "reversi";
			String pwd = "reversi";
			
			conn = DriverManager.getConnection(url, user, pwd);
			
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
					"blackFields ARRAY," +
					"whiteFields ARRAY," +
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
			System.out.println(ex.toString());
		}
	}

}
