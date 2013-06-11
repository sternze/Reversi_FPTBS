package uni_klu.se2.reversi.gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPaneBuilder;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import uni_klu.se2.reversi.data.Board;
import uni_klu.se2.reversi.data.BoardStatus;
import uni_klu.se2.reversi.data.FieldStatus;
import uni_klu.se2.reversi.engine.IPlayer;
import uni_klu.se2.reversi.engine.ReversiEngine;
import uni_klu.se2.reversi.engine.player.DeepMinMaxComputerPlayer;
import uni_klu.se2.reversi.engine.player.NaivDiskSquareComputerPlayer;
import uni_klu.se2.reversi.engine.player.RandomComputerPlayer;
import uni_klu.se2.reversi.engine.player.SimpleMinMaxComputerPlayer;
import uni_klu.se2.reversi.helper.SocketHelperNotification;

public class ReversiGUIController implements Initializable, IReversiGUI {

	@FXML
	public GridPane gameBoard;
	@FXML
	public AnchorPane BlackIcon;
	@FXML
	public AnchorPane WhiteIcon;
	@FXML
	public Label ScoreBlack;
	@FXML
	public Label ScoreWhite;
	@FXML
	public Menu mbNewGame;
	
	private Stage newGameDialog;
	
	private final ObservableList<String> WHITE_ENGINE_MODE = FXCollections.observableArrayList("Human Player", "Random Computer Player", "Simple MinMax Player", "NaivDiskSquare(d=3)", "NaivDiskSquare(d=4)", "NaivDiskSquare(d=5)", "NaivDiskSquare(d=6)", "NaivDiskSquare(d=7)", "DeepMinMax(d=3)", "DeepMinMax(d=4)", "DeepMinMax(d=5)", "DeepMinMax(d=6)", "DeepMinMax(d=7)");
	private final ObservableList<String> BLACK_ENGINE_MODE = FXCollections.observableArrayList("Human Player", "Random Computer Player", "Simple MinMax Player", "NaivDiskSquare(d=3)", "NaivDiskSquare(d=4)", "NaivDiskSquare(d=5)", "NaivDiskSquare(d=6)", "NaivDiskSquare(d=7)", "DeepMinMax(d=3)", "DeepMinMax(d=4)", "DeepMinMax(d=5)", "DeepMinMax(d=6)", "DeepMinMax(d=7)");
	
	private static ReversiModel model;
	private static ReversiEngine engine;
	private static Board board;
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
	}

	public void initGUI() {
		newGame(null);
	}
	
	private void clearGUI() {
		gameBoard.getChildren().clear();
		BlackIcon.getChildren().clear();
		WhiteIcon.getChildren().clear();
	}
	
	private void initBoard() {
		gameBoard.getChildren().clear();
		board = new Board();
		model = new ReversiModel(board);
		for (int i = 0; i < ReversiModel.BOARD_SIZE; i++) {
		    for (int j = 0; j < ReversiModel.BOARD_SIZE; j++) {
		    	ReversiSquare square = new ReversiSquare(i, j, model);
		    	ReversiPiece piece = new ReversiPiece();
		    	piece.ownerProperty().bind(board.getFields()[i][j].getStatus());
		    	gameBoard.add(StackPaneBuilder.create().children(square, piece).build(), i, j);
		    }
		}
	}
	
	private void initScore() {
		/*
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
		*/
	}
	
	public void newGame(ActionEvent event) {
		System.out.println("new game!");
		
		clearGUI();
		initBoard();
		//initScore();
		
		IPlayer Pwhite = null;
		IPlayer Pblack = null;
		engine = null;
		
		switch(0)
		{
			case 0:
				Pwhite = model; 
				System.out.println("white: HumanPlayer");
				break;
			case 1:
				Pwhite = new RandomComputerPlayer(board); 
				System.out.println("white: RandomComputerPlayer");
				break;
			case 2:
				Pwhite = new SimpleMinMaxComputerPlayer(board); 
				System.out.println("white: SimpleMinMaxComputerPlayer");
				break;
			case 3:
				Pwhite = new NaivDiskSquareComputerPlayer(board, 3); 
				System.out.println("white: NaivDiskSquareComputerPlayer(3)");
				break;
			case 4:
				Pwhite = new NaivDiskSquareComputerPlayer(board, 4); 
				System.out.println("white: NaivDiskSquareComputerPlayer(4)");
				break;
			case 5:
				Pwhite = new NaivDiskSquareComputerPlayer(board, 5); 
				System.out.println("white: NaivDiskSquareComputerPlayer(5)");
				break;
			case 6:
				Pwhite = new NaivDiskSquareComputerPlayer(board, 6); 
				System.out.println("white: NaivDiskSquareComputerPlayer(6)");
				break;
			case 7:
				Pwhite = new NaivDiskSquareComputerPlayer(board, 7); 
				System.out.println("white: NaivDiskSquareComputerPlayer(7)");
				break;
			case 8:
				Pwhite = new DeepMinMaxComputerPlayer(board, 3); 
				System.out.println("white: DeepMinMaxComputerPlayer(3)");
				break;
			case 9:
				Pwhite = new DeepMinMaxComputerPlayer(board, 4); 
				System.out.println("white: DeepMinMaxComputerPlayer(4)");
				break;
			case 10:
				Pwhite = new DeepMinMaxComputerPlayer(board, 5); 
				System.out.println("white: DeepMinMaxComputerPlayer(5)");
				break;
			case 11:
				Pwhite = new DeepMinMaxComputerPlayer(board, 6); 
				System.out.println("white: DeepMinMaxComputerPlayer(6)");
				break;
			case 12:
				Pwhite = new DeepMinMaxComputerPlayer(board, 7); 
				System.out.println("white: DeepMinMaxComputerPlayer(7)");
				break;
			default:
				Pwhite = model;		 
				System.out.println("white: HumanPlayer");
				break;
		}
		
		switch(0)
		{
			case 0:
				Pblack = model; 
				System.out.println("black: HumanPlayer");
				break;
			case 1:
				Pblack = new RandomComputerPlayer(board); 
				System.out.println("black: RandomComputerPlayer");
				break;
			case 2:
				Pblack = new SimpleMinMaxComputerPlayer(board);
				System.out.println("black: SimpleMinMaxComputerPlayer");
				break;
			case 3:
				Pblack = new NaivDiskSquareComputerPlayer(board, 3);
				System.out.println("black: NaivDiskSquareComputerPlayer(3)");
				break;
			case 4:
				Pblack = new NaivDiskSquareComputerPlayer(board, 4); 
				System.out.println("black: NaivDiskSquareComputerPlayer(4)");
				break;
			case 5:
				Pblack = new NaivDiskSquareComputerPlayer(board, 5);
				System.out.println("black: NaivDiskSquareComputerPlayer(5)");
				break;
			case 6:
				Pblack = new NaivDiskSquareComputerPlayer(board, 6);
				System.out.println("black: NaivDiskSquareComputerPlayer(6)");
				break;
			case 7:
				Pblack = new NaivDiskSquareComputerPlayer(board, 7); 
				System.out.println("black: NaivDiskSquareComputerPlayer(7)");
				break;
			case 8:
				Pblack = new DeepMinMaxComputerPlayer(board, 3); 
				System.out.println("black: DeepMinMaxComputerPlayer(3)");
				break;
			case 9:
				Pblack = new DeepMinMaxComputerPlayer(board, 4); 
				System.out.println("black: DeepMinMaxComputerPlayer(4)");
				break;
			case 10:
				Pblack = new DeepMinMaxComputerPlayer(board, 5); 
				System.out.println("black: DeepMinMaxComputerPlayer(5)");
				break;
			case 11:
				Pblack = new DeepMinMaxComputerPlayer(board, 6); 
				System.out.println("black: DeepMinMaxComputerPlayer(6)");
				break;
			case 12:
				Pblack = new DeepMinMaxComputerPlayer(board, 7); 
				System.out.println("black: DeepMinMaxComputerPlayer(7)");
				break;
			default:
				Pblack = model;		 
				System.out.println("black: HumanPlayer");
				break;
		}
		
		//initBoard();
		engine = new ReversiEngine(board, Pwhite, Pblack);
		Pwhite.setEngine(engine);
		Pblack.setEngine(engine);
		engine.watcher = this;
		engine.start();
		
	}

	public void startGame(ActionEvent event) {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("NewGameDialog/NewGameDialog.fxml"));
		Parent root;
		
		try {
			root = (Parent) fxmlLoader.load();
			
			newGameDialog = new Stage();
			newGameDialog.initStyle(StageStyle.UTILITY);
			Scene scene = new Scene(root);
			newGameDialog.setScene(scene);
			newGameDialog.show();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void loadGame(ActionEvent event) {
		System.out.println("loadGame");
		
	}
	
	public void paintBoard() {
		System.out.println("paint board");
		if (board.getStatus() == BoardStatus.BLACKWON) {
			System.out.println("black won");
			newGame(new ActionEvent());
		} else if (board.getStatus() == BoardStatus.WHITEWON) {
			System.out.println("white won");
			newGame(new ActionEvent());
		} else if (board.getStatus() == BoardStatus.DRAW) {
			System.out.println("draw");
			newGame(new ActionEvent());
		} else {
			model.checkLegalMoves();
		}
	}
	
	public void socketHelperNotification(SocketHelperNotification notfication) {
		// TODO Auto-generated method stub
		
	}
}
