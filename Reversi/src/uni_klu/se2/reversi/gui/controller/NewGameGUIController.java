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
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import uni_klu.se2.reversi.data.User;
import uni_klu.se2.reversi.db.factories.DAOFactory;
import uni_klu.se2.reversi.db.interfaces.GameDAO;
import uni_klu.se2.reversi.db.interfaces.UserDAO;
import uni_klu.se2.reversi.engine.player.DeepMinMaxComputerPlayer;
import uni_klu.se2.reversi.engine.player.NaivDiskSquareComputerPlayer;
import uni_klu.se2.reversi.engine.player.SimpleMinMaxComputerPlayer;
import uni_klu.se2.reversi.gui.FPTBS_Reversi;

public class NewGameGUIController implements Initializable {

	@FXML
	public TextField playerOne;
	@FXML
	public TextField playerTwo;

	@FXML
	public ComboBox<String> cbPlayer2;
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		initCombobox();
	}

		
	
	private void initCombobox() {
		List<String> cbItems = new ArrayList<String>();

		cbItems.add("Human Player");
		cbItems.add("Random Computer Player");
		cbItems.add("Simple Min Max Computer Player");
		cbItems.add("Naiv Disk Square Computer Player (3)");
		cbItems.add("Naiv Disk Square Computer Player (4)");
		cbItems.add("Naiv Disk Square Computer Player (5)");
		cbItems.add("Naiv Disk Square Computer Player (6)");
		cbItems.add("Naiv Disk Square Computer Player (7)");
		cbItems.add("Deep Min Max Computer Player (3)");
		cbItems.add("Deep Min Max Computer Player (4)");
		cbItems.add("Deep Min Max Computer Player (5)");
		cbItems.add("Deep Min Max Computer Player (6)");
		cbItems.add("Deep Min Max Computer Player (7)");
 
		ObservableList<String> items = FXCollections.observableList(cbItems);
		cbPlayer2.setItems(items);
	}
	
	@FXML
	public void startGame(ActionEvent event) {
		String blackUserName = playerOne.getText() != "" ? playerOne.getText() : "Player1";
		String whiteUserName = "Player2";
		int whiteId = 0;
		int blackId = 0;
		
		if(cbPlayer2.getValue().equals("Human Player")) {
			whiteUserName = playerTwo.getText() != "" ? playerTwo.getText() : "Player2";
		} else {
			whiteUserName = cbPlayer2.getValue();
			whiteId = cbPlayer2.getSelectionModel().getSelectedIndex();
		}
		
		UUID gameId = null;
		
		if(Math.random() > 0.5) {
			String tmp = String.copyValueOf(whiteUserName.toCharArray()); 
			whiteUserName = String.copyValueOf(blackUserName.toCharArray());
			blackUserName = String.copyValueOf(tmp.toCharArray());
			
			int tmpid = whiteId;
			whiteId = blackId;
			blackId = tmpid;
		}
		
		gameId = createNewGame(blackUserName, whiteUserName, blackId, whiteId);
		
		FPTBS_Reversi.closeNewGame(gameId, blackUserName, whiteUserName, blackId, whiteId);
	}
	
	@FXML
	public void cbPlayer2Changed(ActionEvent event) {
		if(cbPlayer2.getValue().equals("Human Player")) {
			playerTwo.setDisable(false);
		}
	}
	
	@FXML
	public void cancelNewGame(ActionEvent event) {
		FPTBS_Reversi.closeNewGame(null, "Black", "White", 0, 0);
	}
	
	public UUID createNewGame(String blackUserName, String whiteUserName, int blackAlgId, int whiteAlgId) {
		ensureUserExists(new String[] {blackUserName, whiteUserName});
		
		// create the required DAO Factory
		DAOFactory h2DBFactory = DAOFactory.getDAOFactory(DAOFactory.H2DB);

		// Create a DAO
		GameDAO gameDAO = h2DBFactory.getGameDAO();
		
		UUID gameID = gameDAO.createGame(blackUserName, whiteUserName, blackAlgId, whiteAlgId);
		
		return gameID;
	}
	
	
	private void ensureUserExists(String[] strings) {
		// create the required DAO Factory
		DAOFactory h2DBFactory = DAOFactory.getDAOFactory(DAOFactory.H2DB);

		// Create a DAO
		UserDAO userDAO = h2DBFactory.getUserDAO();

		User black = userDAO.getUser(strings[0]);

		if(black == null) {
			black = new User();
			black.setUserName(strings[0]);
			black.setPassWord(strings[0]);
			
			userDAO.insertUser(black);
		}
		
		User white = userDAO.getUser(strings[1]);

		if(white == null) {
			white = new User();
			white.setUserName(strings[1]);
			white.setPassWord(strings[1]);
			
			userDAO.insertUser(white);
		}		
	}
}
