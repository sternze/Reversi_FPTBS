package org.uni.klu.se2.reversi.db.interfaces;

import java.util.List;

import org.uni.klu.se2.reversi.data.User;


public interface UserDAO {
	public boolean insertUser(User u);
	public boolean deleteUser(String userName);
	public User findUser(String userName);
	public boolean updateUser(User u);
	public List<User> getAllUsers();
}
