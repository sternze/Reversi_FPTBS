package uni_klu.se2.reversi.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import uni_klu.se2.reversi.helper.SocketHelperNotification;

public class FPTBS_Reversi extends Application implements IReversiGUI {

	private ReversiGUIController controller;
	
	public void paintBoard() {
		// TODO Auto-generated method stub
		
	}

	public void socketHelperNotification(SocketHelperNotification notfication) {
		// TODO Auto-generated method stub
		
	}
	
	public static void main(String[] args) {
	    launch(args);
	} 
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Reversi_GUI.fxml"));
		
		Parent root = (Parent) fxmlLoader.load();
		
		controller = (ReversiGUIController)fxmlLoader.getController();
		controller.initGUI();
		
		primaryStage.setTitle("Reversi");
		primaryStage.setMinHeight(675);
		primaryStage.setMinWidth(1000);
		primaryStage.setScene(new Scene(root, 1000, 630));
		primaryStage.show();
	}

}
