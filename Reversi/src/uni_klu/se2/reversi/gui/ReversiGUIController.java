package uni_klu.se2.reversi.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPaneBuilder;
import uni_klu.se2.reversi.gui.ReversiModel.Owner;

public class ReversiGUIController implements Initializable {

	@FXML
	private GridPane board;
	@FXML
	private AnchorPane BlackIcon;
	@FXML
	private AnchorPane WhiteIcon;
	@FXML
	private Label ScoreBlack;
	@FXML
	private Label ScoreWhite;
	@FXML
	private ComboBox<String> EngineBlack;
	@FXML
	private ComboBox<String> EngineWhite;
	
	private final ObservableList<String> WHITE_ENGINE_MODE = FXCollections.observableArrayList("Human Player", "Random Computer Player", "Simple MinMax Player", "NaivDiskSquare(d=3)", "NaivDiskSquare(d=4)", "NaivDiskSquare(d=5)", "NaivDiskSquare(d=6)", "NaivDiskSquare(d=7)", "DeepMinMax(d=3)", "DeepMinMax(d=4)", "DeepMinMax(d=5)", "DeepMinMax(d=6)", "DeepMinMax(d=7)");
	private final ObservableList<String> BLACK_ENGINE_MODE = FXCollections.observableArrayList("Human Player", "Random Computer Player", "Simple MinMax Player", "NaivDiskSquare(d=3)", "NaivDiskSquare(d=4)", "NaivDiskSquare(d=5)", "NaivDiskSquare(d=6)", "NaivDiskSquare(d=7)", "DeepMinMax(d=3)", "DeepMinMax(d=4)", "DeepMinMax(d=5)", "DeepMinMax(d=6)", "DeepMinMax(d=7)");
	
	private static ReversiModel model;
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

	public void initGUI() {
		initBoard();
		initScore();
		initSettings();
	}
	
	private void initBoard() {
		model = ReversiModel.getInstance();
		for (int i = 0; i < ReversiModel.BOARD_SIZE; i++) {
		    for (int j = 0; j < ReversiModel.BOARD_SIZE; j++) {
		      ReversiSquare square = new ReversiSquare(i, j);
		      ReversiPiece piece = new ReversiPiece();
		      piece.ownerProperty().bind(model.board[i][j]);
		      board.add(StackPaneBuilder.create().children(
		        square,
		        piece
		      ).build(), i, j);
		    }
		  }
	}
	
	private void initScore() {
		ReversiPiece white = new ReversiPiece(Owner.WHITE);
		ReversiPiece black = new ReversiPiece(Owner.BLACK);
		
		white.setPrefWidth(WhiteIcon.getPrefWidth());
		white.setPrefHeight(WhiteIcon.getPrefWidth());
		black.setPrefWidth(BlackIcon.getPrefWidth());
		black.setPrefHeight(BlackIcon.getPrefWidth());		
		
		WhiteIcon.getChildren().add(white);
		BlackIcon.getChildren().add(black);
		
		ScoreWhite.textProperty().bind(model.getScore(Owner.WHITE).asString());
		ScoreBlack.textProperty().bind(model.getScore(Owner.BLACK).asString());
	}
	
	private void initSettings() {
		EngineWhite.setItems(WHITE_ENGINE_MODE);
		EngineBlack.setItems(BLACK_ENGINE_MODE);
	}
}
