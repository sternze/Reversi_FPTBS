package uni_klu.se2.reversi.unittest;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import uni_klu.se2.reversi.data.User;
import uni_klu.se2.reversi.db.factories.DAOFactory;
import uni_klu.se2.reversi.db.interfaces.UserDAO;

public class H2DBUserDAOTest {

	private static UserDAO myUser = null;

	@Before
	public void init() {
		// create the required DAO Factory
		DAOFactory h2DBFactory = DAOFactory.getDAOFactory(DAOFactory.H2DB);

		// Create a DAO
		myUser = h2DBFactory.getUserDAO();
		
		h2DBFactory.recreateDatabase();
	}
	
	@Test
	public void testCRUDUser() {
		
		User userPeppi = new User();
		userPeppi.setUserName("Peppi");
		userPeppi.setPassWord("Peppi");

		myUser.insertUser(userPeppi);
		
		System.out.println("Created User");
		
		User dbPeppi = myUser.getUser("Peppi");

		assertEquals(dbPeppi.getUserName(), userPeppi.getUserName());
		assertEquals(dbPeppi.getPassWord(), userPeppi.getPassWord());

		System.out.println("Peppi meets requirements");
		
		userPeppi.setPassWord("abc");

		myUser.updateUser(userPeppi);
		System.out.println("Changed Password and Updated User Peppi");
		dbPeppi = null;
		
		dbPeppi = myUser.getUser("Peppi");

		assertEquals(dbPeppi.getUserName(), userPeppi.getUserName());
		assertEquals(dbPeppi.getPassWord(), userPeppi.getPassWord());

		System.out.println("Peppi meets requirements");
		
		System.out.println("Deleting Peppi now");
		dbPeppi = null;
		myUser.deleteUser("Peppi");
		
		assertNull(myUser.getUser("Peppi"));
		System.out.println("Peppi was verified to be deleted");
	}
	
	@Test
	public void testGetAllUsers() {
		User userPeppi = new User();
		userPeppi.setUserName("Peppi");
		userPeppi.setPassWord("Peppi");
		
		User userHorst = new User();
		userHorst.setUserName("Horst");
		userHorst.setPassWord("Horst");

		myUser.insertUser(userPeppi);
		myUser.insertUser(userHorst);

		System.out.println("Getting all Users from DB");
		List<User> users = myUser.getAllUsers();

		assertEquals(users.size(), 2);
		System.out.println("There are exactly two users in the DB");
		for(User u : users) {
			if(u.getUserName().equals("Peppi")) {
				assertEquals(u.getUserName(), userPeppi.getUserName());
				assertEquals(u.getPassWord(), userPeppi.getPassWord());
			} else {
				assertEquals(u.getUserName(), userHorst.getUserName());
				assertEquals(u.getPassWord(), userHorst.getPassWord());
			}
		}
		
		
	}

}
