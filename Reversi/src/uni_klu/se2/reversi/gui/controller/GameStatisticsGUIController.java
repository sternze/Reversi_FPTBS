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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import jfx.messagebox.MessageBox;
import uni_klu.se2.reversi.data.Game;
import uni_klu.se2.reversi.data.User;
import uni_klu.se2.reversi.db.factories.DAOFactory;
import uni_klu.se2.reversi.db.interfaces.GameDAO;
import uni_klu.se2.reversi.db.interfaces.UserDAO;
import uni_klu.se2.reversi.gui.FPTBS_Reversi;

public class GameStatisticsGUIController implements Initializable {

	@FXML
	public ListView<String> lvUsers;
	@FXML
	public ListView<String> lvGames;
	@FXML
	private static Stage primaryStage;
	
	private List<Game> games;
	private int lastSelectedIndex = Integer.MIN_VALUE;
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		// check init of Load for( --> load saved games in ListView)
		loadUsers();
	}

	private void loadUsers() {
		// create the required DAO Factory
		DAOFactory h2DBFactory = DAOFactory.getDAOFactory(DAOFactory.H2DB);

		// Create a DAO
		UserDAO userDAO = h2DBFactory.getUserDAO();
		
		List<String> users = new ArrayList<String>();
		for(User u : userDAO.getAllUsers()) {
			users.add(u.getUserName());
		}
		
		ObservableList<String> items = FXCollections.observableList(users);
		
		lvUsers.setItems(items);
	}

	

	@FXML
	public void generateGameStatistics(ActionEvent event) {
		String userName = lvUsers.getSelectionModel().getSelectedItem();
		if(lvGames.getSelectionModel().getSelectedItem() != null && lvUsers.getSelectionModel().getSelectedItem() != null) {
			if(lvGames.getSelectionModel().getSelectedIndex() == 0) {
				FPTBS_Reversi.showGeneratedStatistics(null, userName);
			} else {
				String gameIdString = lvGames.getSelectionModel().getSelectedItem().split(" -- ")[lvGames.getSelectionModel().getSelectedItem().split(" -- ").length-1];
				UUID gameId = UUID.fromString(gameIdString);
				FPTBS_Reversi.showGeneratedStatistics(gameId, userName);
			}
		} else {
			MessageBox.show(primaryStage, "Please choose a user/game option!",
					"Not valid", 
					MessageBox.ICON_INFORMATION | MessageBox.OK);
		}
	}
	
	@FXML
	public void lvUserClicked(MouseEvent event) {
		int currentIndex = lvUsers.getSelectionModel().getSelectedIndex(); 
		if(currentIndex != lastSelectedIndex) {
			loadGamesToUser(lvUsers.getSelectionModel().getSelectedItem());
		}
		
		lastSelectedIndex = lvUsers.getSelectionModel().getSelectedIndex();
	}
	
	private void loadGamesToUser(String userName) {
		// create the required DAO Factory
		DAOFactory h2DBFactory = DAOFactory.getDAOFactory(DAOFactory.H2DB);

		// Create a DAO		
		GameDAO gameDAO = h2DBFactory.getGameDAO();
		
		games = gameDAO.getGamesOfUser(userName, true);
		
		List<String> gameArray = new ArrayList<String>();
		gameArray.add("(All Games of User)");
		
		for(int i = 0; i < games.size(); i++) {
			Game g = games.get(i);
			if(g.getBlackPlayer() != null && g.getWhitePlayer() != null && g.getBlackFields() != null && g.getWhiteFields() != null) {
				gameArray.add(g.getBlackPlayer().getUserName() + " vs. " + g.getWhitePlayer().getUserName() +
						" -- Points: " + g.getBlackFields().split(";").length + ":" + g.getWhiteFields().split(";").length +
						" -- " + (g.isFinished() ? "finished" : "running") +
						" -- " + g.getID()); 
			}
		}
		
		ObservableList<String> items = FXCollections.observableList(gameArray);
		
		lvGames.setItems(items);
		
	}

	@FXML
	public void closeDialog(ActionEvent event) {
		Stage g = (Stage)lvUsers.getScene().getWindow();
		g.close();
	}
	
}
