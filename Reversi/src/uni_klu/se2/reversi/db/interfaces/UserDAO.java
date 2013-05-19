package uni_klu.se2.reversi.db.interfaces;

import java.util.List;

import uni_klu.se2.reversi.data.User;

/**
 * This interface is lists all methods a DAO object of the Type User must support.
 * @author Daniel
 * @version 1.0
 *
 */
public interface UserDAO {
	public boolean insertUser(User u);
	public boolean deleteUser(String userName);
	public User getUser(String userName);
	public boolean updateUser(User u);
	public List<User> getAllUsers();
}
