package uni_klu.se2.reversi.db.interfaces;

import java.util.List;
import java.util.UUID;

import uni_klu.se2.reversi.data.Game;

/**
 * This interface is lists all methods a DAO object of the Type Game must support.
 * @author Daniel
 * @version 1.0
 *
 */
public interface GameDAO {
	public UUID createGame(String blackUser_Name, String whiteUser_Name, int blackAlgId, int whiteAlgId);
	public boolean setGameToFinished(UUID gameID);
	public List<Game> getAllGames(boolean includeFinishedGames);
	public List<Game> getGamesOfUser(String userName, boolean includeFinishedGames);
	public Game getGame(UUID GameID);
	public boolean updateGame(Game g);
}
