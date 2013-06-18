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
	
	private void initGame() {
		board = new Board();
		model = new ReversiModel(board);
		for (int i = 0; i < board.BOARDSIZE; i++) {
		    for (int j = 0; j < board.BOARDSIZE; j++) {
		    	ReversiSquare square = new ReversiSquare(i, j, model);
		    	ReversiPiece piece = new ReversiPiece();
		    	piece.ownerProperty().bind(board.getFields()[i][j].getStatus());
		    	gameBoard.add(StackPaneBuilder.create().children(square, piece).build(), i, j);
		    }
		}
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
	
	public void newGame(ActionEvent event) {
		System.out.println("new game!");
		
		clearGUI();
		initGame();
		
		IPlayer Pwhite = getPlayer(0);
		IPlayer Pblack = getPlayer(0);	
		
		engine = new ReversiEngine(board, Pwhite, Pblack);
		Pwhite.setEngine(engine);
		Pblack.setEngine(engine);
		engine.watcher = this;
		engine.start();
	}
	
	private IPlayer getPlayer(int index) {
		IPlayer player = null;
		
		switch(index)
		{
			case 0:
				player = model; 
				System.out.println("player: HumanPlayer");
				break;
			case 1:
				player = new RandomComputerPlayer(board); 
				System.out.println("player: RandomComputerPlayer");
				break;
			case 2:
				player = new SimpleMinMaxComputerPlayer(board); 
				System.out.println("player: SimpleMinMaxComputerPlayer");
				break;
			case 3:
				player = new NaivDiskSquareComputerPlayer(board, 3); 
				System.out.println("player: NaivDiskSquareComputerPlayer(3)");
				break;
			case 4:
				player = new NaivDiskSquareComputerPlayer(board, 4); 
				System.out.println("player: NaivDiskSquareComputerPlayer(4)");
				break;
			case 5:
				player = new NaivDiskSquareComputerPlayer(board, 5); 
				System.out.println("player: NaivDiskSquareComputerPlayer(5)");
				break;
			case 6:
				player = new NaivDiskSquareComputerPlayer(board, 6); 
				System.out.println("player: NaivDiskSquareComputerPlayer(6)");
				break;
			case 7:
				player = new NaivDiskSquareComputerPlayer(board, 7); 
				System.out.println("player: NaivDiskSquareComputerPlayer(7)");
				break;
			case 8:
				player = new DeepMinMaxComputerPlayer(board, 3); 
				System.out.println("player: DeepMinMaxComputerPlayer(3)");
				break;
			case 9:
				player = new DeepMinMaxComputerPlayer(board, 4); 
				System.out.println("player: DeepMinMaxComputerPlayer(4)");
				break;
			case 10:
				player = new DeepMinMaxComputerPlayer(board, 5); 
				System.out.println("player: DeepMinMaxComputerPlayer(5)");
				break;
			case 11:
				player = new DeepMinMaxComputerPlayer(board, 6); 
				System.out.println("player: DeepMinMaxComputerPlayer(6)");
				break;
			case 12:
				player = new DeepMinMaxComputerPlayer(board, 7); 
				System.out.println("player: DeepMinMaxComputerPlayer(7)");
				break;
			default:
				player = model;		 
				System.out.println("player: HumanPlayer");
				break;
		}
		
		return player;
	}
	
	public void loadGame(ActionEvent event) {
		System.out.println("loadGame");
		FPTBS_Reversi.showNewGame();
	}
	
	public void setMetallicStyle(ActionEvent event) {
		FPTBS_Reversi.changeGameStyle(Style.METALLIC);
	}
	
	public void setLegoStyle(ActionEvent event) {
		FPTBS_Reversi.changeGameStyle(Style.LEGO);
	}
	
	public void startGame(ActionEvent event) {
		
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
		}
	}
	
	public void socketHelperNotification(SocketHelperNotification notfication) {
		// TODO Auto-generated method stub
		
	}
}
