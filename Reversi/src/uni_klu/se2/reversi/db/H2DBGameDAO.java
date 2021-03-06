package uni_klu.se2.reversi.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


import uni_klu.se2.reversi.data.Game;
import uni_klu.se2.reversi.data.User;
import uni_klu.se2.reversi.db.factories.H2DBDAOFactory;
import uni_klu.se2.reversi.db.interfaces.GameDAO;

/**
 * This class is the DAO class in the DAO Pattern
 * @author Daniel
 * @version 1.0
 *
 */
public class H2DBGameDAO implements GameDAO {

	public H2DBGameDAO() { }
	
	/**
	 * This methods creates a game in the DB and if the user exist, it creates also an entry
	 * in the playingGame Table
	 * @param blackUser_Name The username of the black user
	 * @param whiteUser_Name The username of the white user
	 * @return the newly assigned UUID of the Game
	 */
	public UUID createGame(String blackUser_Name, String whiteUser_Name, int blackAlgId, int whiteAlgId) {
		UUID gameID = UUID.randomUUID();
		Connection conn = H2DBDAOFactory.createConnection();
		boolean blackPlayerExists = false;
		boolean whitePlayerExists = false;
		
		if(conn != null) {
			try {
				PreparedStatement stmt = conn.prepareStatement("INSERT INTO GAME VALUES(" +
						"?, ?, ?, ?, ?, ?, ?)");
				
				stmt.setString(1, gameID.toString());
				stmt.setNull(2, java.sql.Types.NVARCHAR);
				stmt.setNull(3, java.sql.Types.NVARCHAR);
				stmt.setBoolean(4, false);
				stmt.setBoolean(5, false);
				stmt.setInt(6, blackAlgId);
				stmt.setInt(7, whiteAlgId);
				
				
				stmt.executeUpdate();
				
				stmt.close();
				
				if(blackUser_Name != null && !blackUser_Name.trim().isEmpty() &&
						whiteUser_Name != null && !whiteUser_Name.trim().isEmpty()) {
					
					PreparedStatement blackUserQuery = conn.prepareStatement(
							"SELECT * FROM User WHERE UserName = ?");
					
					blackUserQuery.setString(1, blackUser_Name);
					
					ResultSet rs = blackUserQuery.executeQuery();

					if(rs.next()) {
						blackPlayerExists = true;
					}
					
					rs.close();
					blackUserQuery.close();
					
					PreparedStatement whiteUserQuery = conn.prepareStatement(
							"SELECT * FROM User WHERE UserName = ?");
					
					whiteUserQuery.setString(1, whiteUser_Name);
					
					ResultSet rsw = whiteUserQuery.executeQuery();

					if(rsw.next()) {
						whitePlayerExists = true;
					}
					
					rsw.close();
					whiteUserQuery.close();

					if(blackPlayerExists && whitePlayerExists) {
						PreparedStatement mappingInsert = conn.prepareStatement(
								"INSERT INTO playingGame VALUES(?,?,?)");
						
						mappingInsert.setString(1, blackUser_Name);
						mappingInsert.setString(2, whiteUser_Name);
						mappingInsert.setString(3, gameID.toString());
						
						mappingInsert.executeUpdate();
						
						mappingInsert.close();
					}
				}
			}
			catch(SQLTimeoutException sqlTimeEx)
			{
				System.out.println("Timeout happend!");
				System.out.println(sqlTimeEx.toString());
				gameID = null;
			}
			catch(SQLException sqlEx)
			{
				System.out.println("DB-Access Error");
				System.out.println(sqlEx.toString());
				gameID = null;
			}
			finally
			{
				closeDBConnection(conn);
			}
		}
		
		return gameID;
	}
	
	/**
	 * This methods closes the DB Connection
	 */
	private void closeDBConnection(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * This methods sets a created Game to finished
	 * @param gameID The ID of the Game in the DB
	 * @return true if the Update was successful, otherwise false (e.g. the Game is not present)
	 */
	
	public boolean setGameToFinished(UUID gameID) {
		boolean successful = false;
		Connection conn = H2DBDAOFactory.createConnection();
		
		if(conn != null) {
			try {
				PreparedStatement stmt = conn.prepareStatement("UPDATE GAME SET " +
						"finished = ? WHERE ID = ?");
				
				stmt.setBoolean(1, true);
				stmt.setString(2, gameID.toString());
				
				if(stmt.executeUpdate() > 0) {
					//System.out.println("Deleted: " + stmt.getUpdateCount() + " Entries");
					successful = true;
				}
				
				stmt.close();
			}
			catch(SQLTimeoutException sqlTimeEx)
			{
				System.out.println("Timeout happend while deleting!");
				System.out.println(sqlTimeEx.toString());
			}
			catch(SQLException sqlEx)
			{
				System.out.println("DB-Access Error while deleting");
				System.out.println(sqlEx.toString());
			} finally {
				closeDBConnection(conn);
			}
		}
		
		return successful;
	}

	/**
	 * This methods gets all Games from the DB
	 * @param includeFinishedGames Indicator if all games should be selected, or just running games
	 * @return a List of Game objects. If no Game is in the DB the List is empty; never null
	 */
	
	public List<Game> getAllGames(boolean includeFinishedGames) {
		List<Game> games = new ArrayList<Game>();
		
		Connection conn = H2DBDAOFactory.createConnection();
		
		if(conn != null) {
			try {
				String selectString = "SELECT ID, blackFields, whiteFields, finished, blacksTurn, blackAlgorithmId, whiteAlgorithmId FROM GAME";
				if(!includeFinishedGames) {
					selectString += " WHERE finished = ?";
				}

				PreparedStatement stmt = conn.prepareStatement(selectString);
				
				if(!includeFinishedGames) {
					stmt.setBoolean(1, false);
				}
				
				
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {
					Game g = new Game();
					g.setID(rs.getString(1));
					g.setBlackFields(rs.getString(2));
					g.setWhiteFields(rs.getString(3));
					g.setFinished(rs.getBoolean(4));
					g.setBlacksTurn(rs.getBoolean(5));
					g.setBlackAlgorithmId(rs.getInt(6));
					g.setWhiteAlgorithmId(rs.getInt(7));
					
					games.add(g);
				}
				rs.close();
				stmt.close();
				
				for(Game g : games) {
					PreparedStatement players = conn.prepareStatement("SELECT pg.BlackPlayer, pg.WhitePlayer FROM Game g, PlayingGame pg WHERE g.ID = pg.GameID AND g.ID = ?");
					
					players.setString(1, g.getID().toString());
					
					ResultSet playerRs = players.executeQuery();
					while(playerRs.next()) {
						User black = new User();
						black.setUserName(playerRs.getString(1));
						g.setBlackPlayer(black);

						User white = new User();
						white.setUserName(playerRs.getString(2));
						g.setWhitePlayer(white);
					}
				}
				
			}
			catch(SQLTimeoutException sqlTimeEx)
			{
				System.out.println("Timeout happend while deleting!");
				System.out.println(sqlTimeEx.toString());
			}
			catch(SQLException sqlEx)
			{
				System.out.println("DB-Access Error while deleting");
				System.out.println(sqlEx.toString());
			} finally {
				closeDBConnection(conn);
			}
		}
		
		return games;
	}

	/**
	 * This methods gets all Games of a User from the DB
	 * @param userName The name of the User, whose games are searched
	 * @param includeFinishedGames Indicator if all games should be selected, or just running games
	 * @return a List of Game objects. If no Game is in the DB the List is empty; never null
	 */
	
	public List<Game> getGamesOfUser(String userName, boolean includeFinishedGames) {
		List<Game> games = new ArrayList<Game>();
		
		Connection conn = H2DBDAOFactory.createConnection();
		
		if(conn != null) {
			try {
				String selectString = "SELECT game.ID, game.blackFields, " +
						"game.whiteFields, game.finished, game.blacksTurn, " +
						"game.blackAlgorithmId, game.whiteAlgorithmId FROM GAME game, PlayingGame playing " +
						"WHERE (playing.BlackPlayer = ? OR playing.WhitePlayer = ?) AND " + 
						"game.ID = playing.GameID";
				if(!includeFinishedGames) {
					selectString += " AND game.finished = ?";
				}

				PreparedStatement stmt = conn.prepareStatement(selectString);
				
				stmt.setString(1, userName);
				stmt.setString(2, userName);
				
				if(!includeFinishedGames) {
					stmt.setBoolean(3, false);
				}
				
				
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {
					Game g = new Game();
					g.setID(rs.getString(1));
					g.setBlackFields(rs.getString(2));
					g.setWhiteFields(rs.getString(3));
					g.setFinished(rs.getBoolean(4));
					g.setBlacksTurn(rs.getBoolean(5));
					g.setBlackAlgorithmId(rs.getInt(6));
					g.setWhiteAlgorithmId(rs.getInt(7));
					
					games.add(g);
				}
				rs.close();
				stmt.close();
				

				for(Game g : games) {
					PreparedStatement players = conn.prepareStatement("SELECT pg.BlackPlayer, pg.WhitePlayer FROM Game g, PlayingGame pg WHERE g.ID = pg.GameID AND g.ID = ?");
					
					players.setString(1, g.getID().toString());
					
					ResultSet playerRs = players.executeQuery();
					while(playerRs.next()) {
						User black = new User();
						black.setUserName(playerRs.getString(1));
						g.setBlackPlayer(black);

						User white = new User();
						white.setUserName(playerRs.getString(2));
						g.setWhitePlayer(white);
					}
				}
			}
			catch(SQLTimeoutException sqlTimeEx)
			{
				System.out.println("Timeout happend while deleting!");
				System.out.println(sqlTimeEx.toString());
			}
			catch(SQLException sqlEx)
			{
				System.out.println("DB-Access Error while deleting");
				System.out.println(sqlEx.toString());
			} finally {
				closeDBConnection(conn);
			}
		}
		
		return games;
	}

	/**
	 * This methods gets a specific Game from the DB
	 * @param GameID ID of the Game
	 * @return a Game object if this query was successful, null otherwise (e.g. Game not present in DB)
	 */
	
	public Game getGame(UUID GameID) {
		Game game = null;
		
		Connection conn = H2DBDAOFactory.createConnection();
		
		if(conn != null) {
			try {
				String selectString = "SELECT ID, blackFields, whiteFields, finished, blacksTurn, " +
						"blackAlgorithmId, whiteAlgorithmId FROM GAME" +
						" WHERE ID = ?";
				

				PreparedStatement stmt = conn.prepareStatement(selectString);
				
				stmt.setString(1, GameID.toString());
				
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {
					game = new Game();
					game.setID(rs.getString(1));
					game.setBlackFields(rs.getString(2));
					game.setWhiteFields(rs.getString(3));
					game.setFinished(rs.getBoolean(4));
					game.setBlacksTurn(rs.getBoolean(5));
					game.setBlackAlgorithmId(rs.getInt(6));
					game.setWhiteAlgorithmId(rs.getInt(7));
				}
				rs.close();
				stmt.close();
				
				PreparedStatement players = conn.prepareStatement("SELECT pg.BlackPlayer, pg.WhitePlayer FROM Game g, PlayingGame pg WHERE g.ID = pg.GameID AND g.ID = ?");
				
				players.setString(1, game.getID().toString());
				
				ResultSet playerRs = players.executeQuery();
				while(playerRs.next()) {
					User black = new User();
					black.setUserName(playerRs.getString(1));
					game.setBlackPlayer(black);

					User white = new User();
					white.setUserName(playerRs.getString(2));
					game.setWhitePlayer(white);
				}
				
			}
			catch(SQLTimeoutException sqlTimeEx)
			{
				System.out.println("Timeout happend while deleting!");
				System.out.println(sqlTimeEx.toString());
				game = null;
			}
			catch(SQLException sqlEx)
			{
				System.out.println("DB-Access Error while deleting");
				System.out.println(sqlEx.toString());
				game = null;
			} finally {
				closeDBConnection(conn);
			}
		}
		
		return game;
	}

	/**
	 * This method updates a game in the DB
	 * @param g The new (already altered) Game-object
	 * @return true if the Update was successful, false otherwise
	 */
	
	public boolean updateGame(Game g) {
		boolean successful = false;
		Connection conn = H2DBDAOFactory.createConnection();
		
		if(conn != null) { 
			try {
				PreparedStatement stmt = conn.prepareStatement("UPDATE GAME SET " +
						"blackFields = ?, whiteFields = ?, finished = ?, blacksTurn = ? " +
						", blackAlgorithmId = ?, whiteAlgorithmId = ? WHERE ID = ?");
				
				stmt.setString(1, g.getBlackFields());
				stmt.setString(2, g.getWhiteFields());
				stmt.setBoolean(3, g.isFinished());
				stmt.setBoolean(4, g.isBlacksTurn());
				stmt.setInt(5, g.getBlackAlgorithmId());
				stmt.setInt(6, g.getWhiteAlgorithmId());
				stmt.setString(7, g.getID().toString());
				
				if(stmt.executeUpdate() > 0) {
					//System.out.println("Deleted: " + stmt.getUpdateCount() + " Entries");
					successful = true;
				}
				stmt.close();
			}
			catch(SQLTimeoutException sqlTimeEx)
			{
				System.out.println("Timeout happend while deleting!");
				System.out.println(sqlTimeEx.toString());
			}
			catch(SQLException sqlEx)
			{
				System.out.println("DB-Access Error while deleting");
				System.out.println(sqlEx.toString());
			} finally {
				closeDBConnection(conn);
			}
		}
		
		return successful;
	}

}
