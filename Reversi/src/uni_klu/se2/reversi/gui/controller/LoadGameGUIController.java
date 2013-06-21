package uni_klu.se2.reversi.gui.controller;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import uni_klu.se2.reversi.data.Game;
import uni_klu.se2.reversi.db.factories.DAOFactory;
import uni_klu.se2.reversi.db.interfaces.GameDAO;
import uni_klu.se2.reversi.gui.FPTBS_Reversi;

public class LoadGameGUIController implements Initializable {

	@FXML
	public ListView<String> lvLoadableGames;
	private List<Game> games; 
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		// check init of Load for( --> load saved games in ListView)
		loadSavedGames();
	}

	private void loadSavedGames() {
		// create the required DAO Factory
		DAOFactory h2DBFactory = DAOFactory.getDAOFactory(DAOFactory.H2DB);

		// Create a DAO
		GameDAO gameDAO = h2DBFactory.getGameDAO();
		
		games = gameDAO.getAllGames(false);
		
		List<String> gameArray = new ArrayList<String>();
		
		for(int i = 0; i < games.size(); i++) {
			Game g = games.get(i);
			if(g.getBlackPlayer() != null && g.getWhitePlayer() != null && g.getBlackFields() != null && g.getWhiteFields() != null) {
				gameArray.add(g.getBlackPlayer().getUserName() + " vs. " + g.getWhitePlayer().getUserName() +
						" -- Points: " + g.getBlackFields().split(";").length + ":" + g.getWhiteFields().split(";").length +
						" -- " + g.getID()); 
			}
		}
		
		ObservableList<String> items = FXCollections.observableList(gameArray);
		
		lvLoadableGames.setItems(items);
	}

	
	
	@FXML
	public void loadGame(ActionEvent event) {
		String selectedItem = lvLoadableGames.getSelectionModel().getSelectedItem();
		UUID gameId = UUID.fromString(selectedItem.split(" -- ")[selectedItem.split(" -- ").length-1]);
		
		String blackUserName = selectedItem.split(" vs. ")[0];
		String whiteUserName = selectedItem.split(" vs. ")[1].split(" -- ")[0];

		FPTBS_Reversi.closeLoadGame(gameId, blackUserName, whiteUserName);
	}
	
	@FXML
	public void cancelLoadGame(ActionEvent event) {
		FPTBS_Reversi.closeLoadGame(null, "Black", "White");
	}
	
}
