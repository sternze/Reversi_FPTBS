package uni_klu.se2.reversi.gui.controller;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPaneBuilder;
import javafx.stage.Stage;
import jfx.messagebox.MessageBox;
import uni_klu.se2.reversi.data.Board;
import uni_klu.se2.reversi.data.BoardStatus;
import uni_klu.se2.reversi.data.FieldStatus;
import uni_klu.se2.reversi.data.Game;
import uni_klu.se2.reversi.data.Move;
import uni_klu.se2.reversi.db.factories.DAOFactory;
import uni_klu.se2.reversi.db.interfaces.GameDAO;
import uni_klu.se2.reversi.engine.IPlayer;
import uni_klu.se2.reversi.engine.ReversiEngine;
import uni_klu.se2.reversi.engine.player.DeepMinMaxComputerPlayer;
import uni_klu.se2.reversi.engine.player.NaivDiskSquareComputerPlayer;
import uni_klu.se2.reversi.engine.player.RandomComputerPlayer;
import uni_klu.se2.reversi.engine.player.SimpleMinMaxComputerPlayer;
import uni_klu.se2.reversi.engine.player.SocketPlayer;
import uni_klu.se2.reversi.gui.FPTBS_Reversi;
import uni_klu.se2.reversi.gui.IReversiGUI;
import uni_klu.se2.reversi.gui.ReversiModel;
import uni_klu.se2.reversi.gui.ReversiPiece;
import uni_klu.se2.reversi.gui.ReversiSquare;
import uni_klu.se2.reversi.gui.Style;
import uni_klu.se2.reversi.helper.SocketHelper;
import uni_klu.se2.reversi.helper.SocketHelperNotification;
import uni_klu.se2.reversi.speech.ReversiSpeech;

public class ReversiGUIController implements Initializable, IReversiGUI {

	@FXML
	public GridPane gameBoard;
	@FXML
	public Label ScoreBlack;
	@FXML
	public Label ScoreWhite;
	@FXML
	public Label lMyIP;
	@FXML
	public Label lMyPort;
	@FXML
	public Label BlackPlayerName;
	@FXML
	public Label WhitePlayerName;
	@FXML
	public Menu mbNewGame;
	@FXML
	public CheckMenuItem cmiShowPossibleMoves;
	@FXML
	public CheckMenuItem cmiSpeechRecognition;
	@FXML
	private static Stage primaryStage;
	@FXML
	public Button bRecognizeSpeech;
	@FXML
	public HBox hboxNetworkInfo;
	
	private int blackPlayerID = 1;
	private int whitePlayerID = 0;
	private String myName = "";

	private static ReversiModel model;
	private static ReversiEngine engine;
	private static Board board;
	private static SocketHelper socketHelper;
	private static String myIP;
	private static int myPort;
	private static boolean iWasNetworkGameCreator;
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub

		cmiShowPossibleMoves.setSelected(true);
	}
	
	public void initGUI(UUID gameId, String blackPlayerName, String whitePlayerName, int blackPlayerId, int whitePlayerId) {
		this.blackPlayerID = blackPlayerId;
		this.whitePlayerID = whitePlayerId;
		
		hboxNetworkInfo.setVisible(false);
		
		resetGame(new ActionEvent(gameId, null));
				
		if(blackPlayerName != null && whitePlayerName != null) {
			BlackPlayerName.setText(blackPlayerName);
			WhitePlayerName.setText(whitePlayerName);
		}
	}
	
	public void initGUI(String blackPlayerName, String whitePlayerName, boolean iAmBlackPlayer) {
		if(iWasNetworkGameCreator) {
			lMyIP.setText("My IP: " + myIP);
			lMyPort.setText("My Port: " + myPort);
		} else {
			lMyIP.setText("Connected IP: " + myIP);
			lMyPort.setText("Connected Port: " + myPort);
		}
		hboxNetworkInfo.setVisible(true);
		resetGame(new ActionEvent(new Boolean(iAmBlackPlayer), null));
				
		if(blackPlayerName != null && whitePlayerName != null) {
			BlackPlayerName.setText(blackPlayerName);
			WhitePlayerName.setText(whitePlayerName);
		}
	}
	
	private void clearGUI() {
		gameBoard.getChildren().clear();
	}
	
	private Game loadGameFromDB(UUID gameID) {
		// create the required DAO Factory
		DAOFactory h2DBFactory = DAOFactory.getDAOFactory(DAOFactory.H2DB);

		// Create a DAO
		GameDAO gameDAO = h2DBFactory.getGameDAO();
		
		return gameDAO.getGame(gameID);
	}
	
	private Game initGame(UUID gameId) {
		Game g = null;
		if(gameId != null) {
			g = loadGameFromDB(gameId);
			if(g != null && g.getBlackFields() != null && g.getWhiteFields() != null) {
				board = new Board(gameId, g.getFieldArray(), g.getCurrentPlayer());
			} else {
				board = new Board(gameId);
			}
		} else {
			board = new Board();
		}
		createNewModel();
		
		return g;
	}

	private void createNewModel() {
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
	
	private void initScore() {
		ScoreWhite.textProperty().bind(model.getScore(FieldStatus.WHITE).asString());
		ScoreBlack.textProperty().bind(model.getScore(FieldStatus.BLACK).asString());
		
	}


	public void newGame(ActionEvent event) {
		System.out.println("newGame");
		FPTBS_Reversi.showNewGame();		
	}
	
	
	public void playNetworkGame(String myName, String ip, int port, boolean isGameCreation) {
		this.myName = myName;
		myIP = ip;
		myPort = port;
		
		socketHelper = new SocketHelper(this);
		if(isGameCreation) {
			iWasNetworkGameCreator = true;
			socketHelper.createGameAndWaitForClient(port, true);
		} else {
			socketHelper.connectToHost(ip, port);
		}		
	}
	
	public void resetGame(ActionEvent event) {
		System.out.println("reset game!");

		IPlayer Pwhite;
		IPlayer Pblack;
		
		clearGUI();
		Object id = event.getSource();
		Game g = null;
		if(id instanceof UUID) {
			g = initGame((UUID)id);
		}else {
			g = initGame(null);
			if(!(id instanceof Boolean)) {
				 MessageBox.show(primaryStage,
				    "The following initial game is a dummy\ngame versus a random computer.\n\nIf you want to save the Game, please\ncreate a new one via the 'Game' menu.",
				"Information dialog",
				MessageBox.ICON_INFORMATION | MessageBox.OK);
			}
		}
		
		initScore();

		if(!(id instanceof Boolean)) {
			if(g != null) {
				Pwhite = getPlayer(g.getWhiteAlgorithmId());
				Pblack = getPlayer(g.getBlackAlgorithmId());
			} else {
				Pwhite = getPlayer(this.whitePlayerID);
				Pblack = getPlayer(this.blackPlayerID);
			}
		} else {
			// boolean == true --> I am black player
			if(((Boolean)id).booleanValue()) {
				Pwhite = getPlayer(13);
				Pblack = getPlayer(0);
			} else {
				Pwhite = getPlayer(0);
				Pblack = getPlayer(13);
			}
		}
		
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
			case 13:
				player = new SocketPlayer(board, socketHelper);
				System.out.println("player: SocketPlayer");
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
		FPTBS_Reversi.showLoadGame();		
	}
	
	@FXML
	public void showPossibleMovesChanged(ActionEvent event) {
		model.setShowPossibleMoves(!model.isShowPossibleMoves());
		
		cmiShowPossibleMoves.setSelected(model.isShowPossibleMoves());		
	}
	
	@FXML
	public void recognizeSpeechChanged(ActionEvent event) {
		model.setRecognizeSpeech(!model.isRecognizeSpeech());
		
		cmiSpeechRecognition.setSelected(model.isRecognizeSpeech());
		bRecognizeSpeech.setVisible(model.isRecognizeSpeech());
		bRecognizeSpeech.setDisable(!model.isRecognizeSpeech());
	}

	@FXML
	public void deleteAllSavedData(ActionEvent event) {
		int retVal = MessageBox.show(primaryStage,
		        "Are you sure you want to delete all data?",
		        "Warning about deletion!",
		        MessageBox.ICON_WARNING | MessageBox.YES | MessageBox.NO);
		
		if(retVal == MessageBox.YES) {
			System.out.println("Delete all saved Data");
			DAOFactory h2DBFactory = DAOFactory.getDAOFactory(DAOFactory.H2DB);
			h2DBFactory.recreateDatabase();
		}
		
	}
	
	@FXML
	public void showGameStatisticsClicked(ActionEvent event) {
		System.out.println("Game statistics clicked.");
		FPTBS_Reversi.showGameStatisticsDialog();
		
	}
	
	public void setMetallicStyle(ActionEvent event) {
		FPTBS_Reversi.changeGameStyle(Style.METALLIC);
	}
	
	public void setLegoStyle(ActionEvent event) {
		FPTBS_Reversi.changeGameStyle(Style.LEGO);
	}
	
	public void setStandardStyle(ActionEvent event) {
		FPTBS_Reversi.changeGameStyle(Style.STANDARD);
	}
	
	public void bRecognizeSpeechClicked(ActionEvent event) {
		if(model.isRecognizeSpeech()) {
			List<Move> legalMoves = board.getAvailableMoves();
			ReversiSpeech rs = new ReversiSpeech(legalMoves);
			
			Move m = rs.recognizeNow(legalMoves, primaryStage);
			if(m != null) {
				model.play(m);
			}
			rs = null;
		}
	}
	
	public void startGame(ActionEvent event) {
		
	}
	
	public void paintBoard() {
		System.out.println("paint board");
		if (board.getStatus() == BoardStatus.BLACKWON) {
			MessageBox.show(primaryStage, "Black won.", "The winner is...", MessageBox.ICON_INFORMATION | MessageBox.OK);
			System.out.println("black won");
			resetGame(new ActionEvent());
		} else if (board.getStatus() == BoardStatus.WHITEWON) {
			MessageBox.show(primaryStage, "White won.", "The winner is...", MessageBox.ICON_INFORMATION | MessageBox.OK);
			System.out.println("white won");
			resetGame(new ActionEvent());
		} else if (board.getStatus() == BoardStatus.DRAW) {
			MessageBox.show(primaryStage, "This was a draw..", "The winner is...", MessageBox.ICON_INFORMATION | MessageBox.OK);
			System.out.println("draw");
			resetGame(new ActionEvent());
		}
	}
	
	public void socketHelperNotification(SocketHelperNotification notfication) {

		IPlayer Pwhite;
		IPlayer Pblack;
		switch (notfication) {
		case INITIALISED:
			System.out.println("SocketHelperNotification: Initialized");
			break;
		case WAITING:
			System.out.println("SocketHelperNotification: Waiting");
			break;
		case CONNECTED_AS_FIRST:

			System.out.println("SocketHelperNotification: Connected as First");
			System.out.println("new game!");
			//initGUI(myName, "Network Player", true);
			
			
//			clearGUI();
//			initGame(null);
//			initScore();
//			Pwhite = new SocketPlayer(board, socketHelper);
//			Pblack = model;
//			engine = new ReversiEngine(board, Pwhite, Pblack);
//			Pwhite.setEngine(engine);
//			Pblack.setEngine(engine);
//			engine.watcher = this;
//			engine.start();
//			System.out.println("Loading game screen");
			FPTBS_Reversi.closeLoadingScreenForNetworkGame(this, myName, "Network Player", true);
			break;
		case CONNECTED_AS_SECOND:
        	System.out.println("SocketHelperNotification: Connected as Second");
			System.out.println("new game!");
			
			//initGUI("Network Player", myName, false);
			
//			clearGUI();
//			initGame(null);
//			initScore();
//			Pblack = new SocketPlayer(board, socketHelper);
//			Pwhite = model;
//			engine = new ReversiEngine(board, Pwhite, Pblack);
//			Pwhite.setEngine(engine);
//			Pblack.setEngine(engine);
//			engine.watcher = this;
//			engine.start();
//			System.out.println("Loading game screen");
			FPTBS_Reversi.closeLoadingScreenForNetworkGame(this, "Network Player", myName, false);
			break;
		case DISCONNECTED:
			System.out.println("SocketHelperNotification: Disconnected");
			break;
		case RECONNECTING:
			System.out.println("SocketHelperNotification: Reconnecting");
			break;
		case ERROR:
			System.out.println("SocketHelperNotification: Error");
			break;
		default:
			break;
		}

	}

}
