package org.uni.klu.se2.reversi.db.interfaces;

import java.util.List;
import java.util.UUID;

import org.uni.klu.se2.reversi.data.Game;

public interface GameDAO {
	public UUID createGame(String blackUser_Name, String whiteUser_Name);
	public boolean setGameToFinished(UUID gameID);
	public List<Game> getAllGames(boolean includeFinishedGames);
	public List<Game> getGamesOfUser(String userName, boolean includeFinishedGames);
	public Game getGame(UUID GameID);
	public boolean updateGame(Game g);
}
