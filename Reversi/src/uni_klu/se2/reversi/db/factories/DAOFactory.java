package uni_klu.se2.reversi.db.factories;

import uni_klu.se2.reversi.db.interfaces.GameDAO;
import uni_klu.se2.reversi.db.interfaces.UserDAO;

/**
 * This class is the abstract Factory class for the factory-DAO-Pattern combination
 * @author Daniel
 * @version 1.0
 *
 */
public abstract class DAOFactory {
	
	// List of DAO types supported by the factory
	public static final int H2DB = 1;
	//public static final int ORACLE = 2;
	
	// There will be a method for each DAO that can be 
	// created. The concrete factories will have to 
	// implement these methods.
	public abstract GameDAO getGameDAO();
	public abstract UserDAO getUserDAO();
	public abstract boolean createDatabase();
	public abstract boolean recreateDatabase();
	
	public static DAOFactory getDAOFactory(
		      int whichFactory) {
		  
		    switch (whichFactory) {
		      case H2DB: 
		          return new H2DBDAOFactory();
		      /*case ORACLE: 
		          return new OracleDAOFactory();*/
		      default: 
		          return null;
		    }
		  }
}
