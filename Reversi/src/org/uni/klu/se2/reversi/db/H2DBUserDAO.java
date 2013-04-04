package org.uni.klu.se2.reversi.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.uni.klu.se2.reversi.data.User;
import org.uni.klu.se2.reversi.db.factories.H2DBDAOFactory;
import org.uni.klu.se2.reversi.db.interfaces.UserDAO;

public class H2DBUserDAO implements UserDAO {

	public H2DBUserDAO() { }
	
	
	/**
	 * This methods inserts an user into the DB
	 * @param u The User to save to the DB
	 * @return true if the insert was successful, false otherwise
	 */
	@Override
	public boolean insertUser(User u) {
		boolean successful = false;
		Connection conn = H2DBDAOFactory.createConnection();
		
		if(conn != null) {
			try {
				PreparedStatement stmt = conn.prepareStatement("INSERT INTO USER VALUES(" +
						"?, ?)");
				
				stmt.setString(1, u.getUserName());
				stmt.setString(2, u.getPassWord());
				
				if(stmt.executeUpdate() > 0) {
					//System.out.println("Deleted: " + stmt.getUpdateCount() + " Entries");
					successful = true;
				}
			}
			catch(SQLTimeoutException sqlTimeEx)
			{
				System.out.println("Timeout happend!");
				System.out.println(sqlTimeEx.toString());
			}
			catch(SQLException sqlEx)
			{
				System.out.println("DB-Access Error");
				System.out.println(sqlEx.toString());
			}
			finally
			{
				closeDBConnection(conn);
			}
		}
		
		return successful;
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
	 * This methods deletes an user from the DB
	 * @param userName The Name of the User to delete from the DB
	 * @return true if the delete was successful, false otherwise
	 */
	@Override
	public boolean deleteUser(String userName) {
		boolean successful = false;
		Connection conn = H2DBDAOFactory.createConnection();
		
		if(conn != null) {
			try {
				PreparedStatement stmt = conn.prepareStatement("DELETE FROM USER WHERE " +
						"UserName = ?");
				
				stmt.setString(1, userName);
				
				if(stmt.executeUpdate() > 0) {
					//System.out.println("Deleted: " + stmt.getUpdateCount() + " Entries");
					successful = true;
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
		
		return successful;
	}


	/**
	 * This method gets an User from the DB
	 * @param userName The Name of the User to search in DB
	 * @return a User object if the User is present in DB, null otherwise
	 */
	@Override
	public User findUser(String userName) {
		User u = null;
		
		Connection conn = H2DBDAOFactory.createConnection();
		
		if(conn != null) {
			try {
				PreparedStatement stmt = conn.prepareStatement("SELECT UserName, Password FROM USER WHERE " +
						"UserName = ?");
				
				stmt.setString(1, userName);
				
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {
					u = new User();
					u.setUserName(rs.getString(1));
					u.setPassWordHash(rs.getString(2));
				}
				rs.close();
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
		
		return u;
	}

	/**
	 * This method updates an User in the DB
	 * @param u The new (already altered) User-object
	 * @return true if the Update was successful, false otherwise
	 */
	@Override
	public boolean updateUser(User u) {
		boolean successful = false;
		Connection conn = H2DBDAOFactory.createConnection();
		
		if(conn != null) {
			try {
				PreparedStatement stmt = conn.prepareStatement("UPDATE USER SET " +
						"Password = ? WHERE UserName = ?");
				
				stmt.setString(1, u.getPassWord());
				stmt.setString(2, u.getUserName());
				
				if(stmt.executeUpdate() > 0) {
					//System.out.println("Deleted: " + stmt.getUpdateCount() + " Entries");
					successful = true;
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
		
		return successful;
	}

	/**
	 * This method retrieves all Users from the DB
	 * @return a List of User objects. If no user is in the DB the List is empty; never null
	 */
	@Override
	public List<User> getAllUsers() {
		List<User> users = new ArrayList<User>();
		
		Connection conn = H2DBDAOFactory.createConnection();
		
		if(conn != null) {
			try {
				PreparedStatement stmt = conn.prepareStatement("SELECT UserName, Password FROM USER");
				
				ResultSet rs = stmt.executeQuery();
				while(rs.next()) {
					User u = new User();
					u.setUserName(rs.getString(1));
					u.setPassWordHash(rs.getString(2));
					users.add(u);
				}
				rs.close();
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
		
		return users;
	}
}
