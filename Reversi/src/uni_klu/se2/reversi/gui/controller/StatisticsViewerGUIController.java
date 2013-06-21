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
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import uni_klu.se2.reversi.data.Game;
import uni_klu.se2.reversi.db.factories.DAOFactory;
import uni_klu.se2.reversi.db.interfaces.GameDAO;

public class StatisticsViewerGUIController implements Initializable {

	@FXML
	public Label lGamesWon;
	@FXML
	public Label lGamesLost;
	@FXML
	public Label lGamesPlayed;
	@FXML
	public Label lGamesOngoing;
	@FXML
	public Label lUsersColor;
	@FXML
	public Label lGamesDraw;
	@FXML
	public PieChart pieChart;
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}

	public void initStatisticsViewer(UUID gameId, String userName) {
		List<Game> allGamesOfUser = loadAllGamesOfUser(userName, true);
		List<Game> ongoingGamesOfUser = loadAllGamesOfUser(userName, false);
		List<Game> finishedGamesOfUser = new ArrayList<Game>();
		
		for(Game aG : allGamesOfUser) {
			if(aG.isFinished()) {
				finishedGamesOfUser.add(aG);
			}
		}
		
		
		if(gameId != null) {			
			Game g = loadGameFromDB(gameId);
			if(g != null) {
				ObservableList<PieChart.Data> pieChartData =
		                FXCollections.observableArrayList(
		                new PieChart.Data("Black", g.getBlackFields().split(";").length),
		                new PieChart.Data("White", g.getWhiteFields().split(";").length));
				if((64-g.getBlackFields().split(";").length-g.getWhiteFields().split(";").length) != 0) {
		                pieChartData.add(new PieChart.Data("None", (64-g.getBlackFields().split(";").length-g.getWhiteFields().split(";").length)));
				}
		        pieChart.setTitle("Game statistics.");
		        
		        pieChart.setData(pieChartData);
			} else {
				lGamesWon.setText("Error");
			}
		}
		
		lGamesPlayed.setText("" + allGamesOfUser.size());
		lGamesOngoing.setText("" + ongoingGamesOfUser.size());
		
		int gamesWon = 0;
		int gamesLost = 0;
		int gamesDraw = 0;
		for(int i = 0; i < finishedGamesOfUser.size(); i++) {
			Game g = finishedGamesOfUser.get(i);
			if(g.getBlackPlayer().getUserName().equals(userName)) {
				lUsersColor.setText("Black");
				if(g.getBlackFields().split(";").length > g.getWhiteFields().split(";").length) {
					gamesWon++;
				} else if(g.getBlackFields().split(";").length < g.getWhiteFields().split(";").length) {
					gamesLost++;
				} else {
					gamesDraw++;
				}
			} else {
				lUsersColor.setText("White");
				if(g.getBlackFields().split(";").length < g.getWhiteFields().split(";").length) {
					gamesWon++;
				} else if(g.getBlackFields().split(";").length > g.getWhiteFields().split(";").length) {
					gamesLost++;
				} else {
					gamesDraw++;
				}
			}
		}
		
		lGamesDraw.setText("" + gamesDraw);
		lGamesWon.setText("" + gamesWon);
		lGamesLost.setText("" + gamesLost);
		
		if(gameId == null) {
			List<PieChart.Data> data = new ArrayList<PieChart.Data>();
			
			if(gamesWon > 0) {
				data.add(new PieChart.Data("Won", gamesWon / finishedGamesOfUser.size() * 100));
			}
			
			if(gamesLost > 0) {
				data.add(new PieChart.Data("Lost", gamesLost / finishedGamesOfUser.size() * 100));
			}
			
			if(gamesDraw > 0) {
				data.add(new PieChart.Data("Draw", gamesDraw / finishedGamesOfUser.size() * 100));
			}
			
			ObservableList<PieChart.Data> pieChartData =
	                FXCollections.observableArrayList(data);
	        pieChart.setTitle("All games of User");
	        
	        pieChart.setData(pieChartData);
		}
	}
	
	private List<Game> loadAllGamesOfUser(String userName, boolean includeFinishedGames) {
		DAOFactory h2DBFactory = DAOFactory.getDAOFactory(DAOFactory.H2DB);

		// Create a DAO		
		GameDAO gameDAO = h2DBFactory.getGameDAO();
		
		return gameDAO.getGamesOfUser(userName, includeFinishedGames);
	}

	private Game loadGameFromDB(UUID gameId) {
		DAOFactory h2DBFactory = DAOFactory.getDAOFactory(DAOFactory.H2DB);

		// Create a DAO		
		GameDAO gameDAO = h2DBFactory.getGameDAO();
		
		return gameDAO.getGame(gameId);
	}

	@FXML
	public void closeDialog(ActionEvent event) {
		Stage g = (Stage)lGamesWon.getScene().getWindow();
		g.close();
	}
	
}
