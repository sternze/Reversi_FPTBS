package uni_klu.se2.reversi.gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPaneBuilder;

public class ReversiGUIController implements Initializable {

	@FXML
	private GridPane board;
	
	private static ReversiModel model;
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

	public void initBoard() {
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
}
