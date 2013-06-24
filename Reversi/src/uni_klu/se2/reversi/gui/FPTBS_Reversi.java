package uni_klu.se2.reversi.gui;

import java.io.IOException;
import java.util.UUID;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uni_klu.se2.reversi.gui.controller.LoadGameGUIController;
import uni_klu.se2.reversi.gui.controller.NewGameGUIController;
import uni_klu.se2.reversi.gui.controller.ReversiGUIController;
import uni_klu.se2.reversi.gui.controller.StatisticsViewerGUIController;

public class FPTBS_Reversi extends Application {

	private static ReversiGUIController controller;
	private static NewGameGUIController newGameController;
	private static LoadGameGUIController loadGameController;
	private static Parent gameGUI;
	private static Stage primaryStage;
	private static Scene previousScene;
	private static FPTBS_Reversi me;
	
	public FPTBS_Reversi() {
		FPTBS_Reversi.me = this;
	}
	
	public static void main(String[] args) {
	    launch(args);
	} 
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FPTBS_Reversi.primaryStage = primaryStage;
		
		initialize();
		
		gameGUI = loadGameGUI(null, "User", "Computer", 0, 1);
		
		primaryStage.setScene(new Scene(gameGUI, 1000, 630));
		primaryStage.show();
	}
	
	private void initialize() {
		primaryStage.setTitle("Reversi");
		primaryStage.setMinHeight(675);
		primaryStage.setMinWidth(1000);
	}
	
	private static Parent loadGameGUI(UUID gameId, String blackName, String whiteName, int blackId, int whiteId) {
		FXMLLoader fxmlLoader = new FXMLLoader(FPTBS_Reversi.me.getClass().getResource("Reversi_GUI/Reversi_GUI.fxml"));
		Parent root = null;
		
		try {
			root = (Parent) fxmlLoader.load();
			FPTBS_Reversi.controller = (ReversiGUIController)fxmlLoader.getController();
			FPTBS_Reversi.controller.initGUI(gameId, blackName, whiteName, blackId, whiteId);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return root;
	}
	
	private static Parent loadGameGUI(ReversiGUIController controllerToBeSet, String blackName, String whiteName, boolean iAmBlackPlayer) {
		FXMLLoader fxmlLoader = new FXMLLoader(FPTBS_Reversi.me.getClass().getResource("Reversi_GUI/Reversi_GUI.fxml"));
		Parent root = null;
		
		try {
			root = (Parent) fxmlLoader.load();
			
			if(controllerToBeSet != null) {
				FPTBS_Reversi.controller = (ReversiGUIController)fxmlLoader.getController();
				FPTBS_Reversi.controller.initGUI(blackName, whiteName, iAmBlackPlayer);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return root;
	}
	
	private static Parent loadWaitForNetworkPlayer(String myName) {
		FXMLLoader fxmlLoader = new FXMLLoader(FPTBS_Reversi.me.getClass().getResource("NetworkProgressDialog/NetworkProgressDialog.fxml"));
		Parent root = null;
		
		try {
			root = (Parent) fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return root;
	}
	
	private static Parent loadGameStatisticsGUI() {
		FXMLLoader fxmlLoader = new FXMLLoader(FPTBS_Reversi.me.getClass().getResource("GameStatisticsDialog/GameStatisticsDialog.fxml"));
		Parent root = null;
		
		try {
			root = (Parent) fxmlLoader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return root;
	}
	
	private static Parent loadNewGame() {
		FXMLLoader fxmlLoader = new FXMLLoader(FPTBS_Reversi.me.getClass().getResource("NewGameDialog/NewGameDialog.fxml"));
		Parent root = null;
		
		try {
			root = (Parent) fxmlLoader.load();
			
			FPTBS_Reversi.newGameController = (NewGameGUIController)fxmlLoader.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return root;
	}
	
	public static void changeGameStyle(Style style) {

		gameGUI.getStylesheets().clear();
		if (style.equals(Style.LEGO)) {
			gameGUI.getStylesheets().add("file:" + FPTBS_Reversi.me.getClass().getResource("Reversi_GUI/Reversi_GUI_Lego.css").getPath());
			//gameGUI.getStylesheets().remove(0);
		} else if (style.equals(Style.METALLIC)) {
			gameGUI.getStylesheets().add("file:" + FPTBS_Reversi.me.getClass().getResource("Reversi_GUI/Reversi_GUI_Metallic.css").getPath());
			//gameGUI.getStylesheets().remove(0);
		} else if (style.equals(Style.STANDARD)) {
			gameGUI.getStylesheets().add("file:" + FPTBS_Reversi.me.getClass().getResource("Reversi_GUI/Reversi_GUI_Standard.css").getPath());
			//gameGUI.getStylesheets().remove(0);
		}
	}
	
	public static void showNewGame() {
		Parent root = loadNewGame();
		previousScene = primaryStage.getScene();
		primaryStage.setScene(new Scene(root, 1000, 630));
	}
	
	public static void closeNewGame(UUID gameId, String blackName, String whiteName, int blackId, int whiteId) {
		if(gameId != null) {
			gameGUI = loadGameGUI(gameId, blackName, whiteName, blackId, whiteId);
			previousScene = primaryStage.getScene();
			primaryStage.setScene(new Scene(gameGUI, 1000, 630));
		} else {
			primaryStage.setScene(previousScene);
		}
	}
	
	public static void closeNewGameForNetworkGame(String myName, String ip, int port, boolean isNewNetworkGame) {
		if(isNewNetworkGame) {
			Parent root = loadWaitForNetworkPlayer(myName);
			primaryStage.setScene(new Scene(root, 1000, 630));
			controller.playNetworkGame(myName, ip, port, isNewNetworkGame);
		} else {
			Parent root = loadWaitForNetworkPlayer(myName);
			primaryStage.setScene(new Scene(root, 1000, 630));
			controller.playNetworkGame(myName, ip, port, isNewNetworkGame);
		}
		
	}

	public static void showLoadGame() {
		Parent root = loadGameLoadDialog();
		previousScene = primaryStage.getScene();
		primaryStage.setScene(new Scene(root, 1000, 630));
	}

	private static Parent loadGameLoadDialog() {
		FXMLLoader fxmlLoader = new FXMLLoader(FPTBS_Reversi.me.getClass().getResource("LoadDialog/LoadDialog.fxml"));
		Parent root = null;
		
		try {
			root = (Parent) fxmlLoader.load();
			
			FPTBS_Reversi.loadGameController = (LoadGameGUIController)fxmlLoader.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return root;
	}

	public static void closeLoadGame(UUID gameId, String blackName, String whiteName) {
		if(gameId != null) {
			gameGUI = loadGameGUI(gameId, blackName, whiteName, -1, -1);
			previousScene = primaryStage.getScene();
			primaryStage.setScene(new Scene(gameGUI, 1000, 630));
		} else {
			primaryStage.setScene(previousScene);
		}
	}

	public static void showGameStatisticsDialog() {
		Parent root = loadGameStatisticsGUI();
		
		Stage stage = new Stage();
        stage.setTitle("Statistics");
        stage.setScene(new Scene(root, 803, 311));
        stage.show();
		
	}
	
	public static void showGeneratedStatistics(UUID gameId, String UserName) {
		Parent root = loadGeneratedStatisticsGUI(gameId, UserName);
		
		Stage stage = new Stage();
        stage.setTitle("Statistics");
        stage.setScene(new Scene(root, 598, 311));
        stage.show();
		
	}

	private static Parent loadGeneratedStatisticsGUI(UUID gameId, String UserName) {
		FXMLLoader fxmlLoader = new FXMLLoader(FPTBS_Reversi.me.getClass().getResource("StatisticsDialog/StatisticsDialog.fxml"));
		Parent root = null;
		
		try {
			root = (Parent) fxmlLoader.load();
			
			StatisticsViewerGUIController contr = (StatisticsViewerGUIController)fxmlLoader.getController();
			contr.initStatisticsViewer(gameId, UserName);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return root;
	}

	public static void abortWaitForNetworkPlayer() {		
		gameGUI = loadGameGUI(null, "User", "Computer", 0, 1);
		
		primaryStage.setScene(new Scene(gameGUI, 1000, 630));
		primaryStage.show();
	}

	public static void closeLoadingScreenForNetworkGame(ReversiGUIController contr, String blackPlayerName, String WhitePlayerName, boolean iAmBlackPlayer) {
		gameGUI = loadGameGUI(contr, blackPlayerName, WhitePlayerName, iAmBlackPlayer);
		
    	primaryStage.setScene(new Scene(gameGUI, 1000, 630));
		primaryStage.show();
	}
	
}
