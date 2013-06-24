package uni_klu.se2.reversi.gui.controller;

import java.net.URL;
import java.util.ResourceBundle;

import uni_klu.se2.reversi.gui.FPTBS_Reversi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class NetworkProgressGUIController implements Initializable {

	
	public void initialize(URL arg0, ResourceBundle arg1) {
		
	}
	
	@FXML
	public void bCancel_Clicked(ActionEvent event) {
		FPTBS_Reversi.abortWaitForNetworkPlayer();
	}
	
}
