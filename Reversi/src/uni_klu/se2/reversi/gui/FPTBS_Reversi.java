package uni_klu.se2.reversi.gui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class FPTBS_Reversi extends Application {

	private static ReversiGUIController controller;
	private static NewGameGUIController newGameController;
	private static Parent gameGUI;
	private static Stage primaryStage;
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
		
		gameGUI = loadGameGUI();
		
		primaryStage.setScene(new Scene(gameGUI, 1000, 630));
		primaryStage.show();
	}
	
	private void initialize() {
		primaryStage.setTitle("Reversi");
		primaryStage.setMinHeight(675);
		primaryStage.setMinWidth(1000);
	}
	
	private Parent loadGameGUI() {
		FXMLLoader fxmlLoader = new FXMLLoader(FPTBS_Reversi.me.getClass().getResource("Reversi_GUI/Reversi_GUI.fxml"));
		Parent root = null;
		
		try {
			root = (Parent) fxmlLoader.load();
			
			FPTBS_Reversi.controller = (ReversiGUIController)fxmlLoader.getController();
			FPTBS_Reversi.controller.initGUI();
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
	
	public static void changeGameStyle() {
		System.out.println(gameGUI.getStylesheets().get(0));
		gameGUI.getStylesheets().add("file:" + FPTBS_Reversi.me.getClass().getResource("Reversi_GUI/Reversi_GUI_Lego.css").getPath());
		gameGUI.getStylesheets().remove(0);
	}
	
	public static void showNewGame() {
		Parent root = loadNewGame();
		primaryStage.setScene(new Scene(root, 1000, 630));
	}
}
