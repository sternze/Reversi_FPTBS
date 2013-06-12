package uni_klu.se2.reversi.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPaneBuilder;
import uni_klu.se2.reversi.data.Board;
import uni_klu.se2.reversi.data.BoardStatus;
import uni_klu.se2.reversi.engine.IPlayer;
import uni_klu.se2.reversi.engine.ReversiEngine;
import uni_klu.se2.reversi.engine.player.DeepMinMaxComputerPlayer;
import uni_klu.se2.reversi.engine.player.NaivDiskSquareComputerPlayer;
import uni_klu.se2.reversi.engine.player.RandomComputerPlayer;
import uni_klu.se2.reversi.engine.player.SimpleMinMaxComputerPlayer;
import uni_klu.se2.reversi.helper.SocketHelperNotification;

public class NewGameGUIController implements Initializable {

	
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}

	
	
	
	
	
	
	/*
	private void initScore() {
		
		ReversiPiece white = new ReversiPiece(FieldStatus.WHITE);
		ReversiPiece black = new ReversiPiece(FieldStatus.BLACK);
	
		white.setPrefWidth(WhiteIcon.getPrefWidth());
		white.setPrefHeight(WhiteIcon.getPrefWidth());
		black.setPrefWidth(BlackIcon.getPrefWidth());
		black.setPrefHeight(BlackIcon.getPrefWidth());		
		
		WhiteIcon.getChildren().add(white);
		BlackIcon.getChildren().add(black);
		
		ScoreWhite.textProperty().bind(model.getScore(FieldStatus.WHITE).asString());
		ScoreBlack.textProperty().bind(model.getScore(FieldStatus.BLACK).asString());
		
	}
	*/
	
	public void startGame(ActionEvent event) {
		
	}
}
