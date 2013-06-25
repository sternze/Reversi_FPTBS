package uni_klu.se2.reversi.gui.controller;

import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jfx.messagebox.MessageBox;
import uni_klu.se2.reversi.data.User;
import uni_klu.se2.reversi.db.factories.DAOFactory;
import uni_klu.se2.reversi.db.interfaces.GameDAO;
import uni_klu.se2.reversi.db.interfaces.UserDAO;
import uni_klu.se2.reversi.gui.FPTBS_Reversi;

public class NewGameGUIController implements Initializable {

	@FXML
	public TextField playerOne;
	@FXML
	public TextField playerTwo;
	@FXML
	public HBox hboxPlayer2;
	@FXML
	public VBox vboxNetworkGame;
	@FXML
	public TextField tbIpOfOpponent;
	@FXML
	public TextField tbPortOfOpponent;
	@FXML
	private static Stage primaryStage;
	@FXML
	private CheckBox cbCreateNewNetworkGame;
	@FXML
	private Label ipTextLabel;
	@FXML
	private Label portTextLabel;
	
	
	@FXML
	public ComboBox<String> cbPlayer2;
	
	public void initialize(URL arg0, ResourceBundle arg1) {
		initCombobox();
	}

		
	
	private void initCombobox() {
		List<String> cbItems = new ArrayList<String>();

		cbItems.add("Human Player");
		cbItems.add("Random Computer Player");
		cbItems.add("Simple Min Max Computer Player");
		cbItems.add("Naiv Disk Square Computer Player (3)");
		cbItems.add("Naiv Disk Square Computer Player (4)");
		cbItems.add("Naiv Disk Square Computer Player (5)");
		cbItems.add("Naiv Disk Square Computer Player (6)");
		cbItems.add("Naiv Disk Square Computer Player (7)");
		cbItems.add("Deep Min Max Computer Player (3)");
		cbItems.add("Deep Min Max Computer Player (4)");
		cbItems.add("Deep Min Max Computer Player (5)");
		cbItems.add("Deep Min Max Computer Player (6)");
		cbItems.add("Deep Min Max Computer Player (7)");
		cbItems.add("Network Player");
 
		ObservableList<String> items = FXCollections.observableList(cbItems);
		cbPlayer2.setItems(items);
	}
	
	@FXML
	public void cbCreateNewNetworkGame_Checked(ActionEvent event) {
		if(cbCreateNewNetworkGame.isSelected()) {
			ipTextLabel.setText("Your IP: ");
			portTextLabel.setText("Your Port: ");
			tbIpOfOpponent.setText(getMyIP());			
			tbPortOfOpponent.setText("" + getMyPort());
			tbIpOfOpponent.setDisable(true);
			tbPortOfOpponent.setDisable(true);
		} else {
			ipTextLabel.setText("IP of Opponent: ");
			portTextLabel.setText("Port of Opponent: ");
			tbIpOfOpponent.setText("");
			tbPortOfOpponent.setText("");
			tbIpOfOpponent.setDisable(false);
			tbPortOfOpponent.setDisable(false);			
		}
	}
	
	private int getMyPort() {
		int port = 0;
		for (port = 8000; port <= 1000 && !IsPortAvailable(port); port++);
		
		return port;
	}



	/**
	 * Checks to see if a specific port is available. (implementation from the Apache "camel" project)
	 *
	 * @param port the port to check for availability
	 */
	public static boolean IsPortAvailable(int port) {
	    if (port < 8000 || port > 10000) {
	        throw new IllegalArgumentException("Invalid start port: " + port);
	    }

	    ServerSocket ss = null;
	    DatagramSocket ds = null;
	    try {
	        ss = new ServerSocket(port);
	        ss.setReuseAddress(true);
	        ds = new DatagramSocket(port);
	        ds.setReuseAddress(true);
	        return true;
	    } catch (IOException e) {
	    } finally {
	        if (ds != null) {
	            ds.close();
	        }

	        if (ss != null) {
	            try {
	                ss.close();
	            } catch (IOException e) {
	                /* should not be thrown */
	            }
	        }
	    }

	    return false;
	}
	
	private String getMyIP() {
		//asdf
		String IP = null;
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()){
			    NetworkInterface current = interfaces.nextElement();
			    System.out.println(current);
			    if (!current.isUp() || current.isLoopback() || current.isVirtual()) continue;
			    Enumeration<InetAddress> addresses = current.getInetAddresses();
			    while (addresses.hasMoreElements()){
			        InetAddress current_addr = addresses.nextElement();
			        if (current_addr.isLoopbackAddress() || !(current_addr instanceof Inet4Address))
			        	continue;
			        IP = current_addr.getHostAddress();
			        return IP;
			    }
			}
		} catch(Exception ex) { }
		
		return IP;
	}
	
	@FXML
	public void startGame(ActionEvent event) {
		String blackUserName = playerOne.getText() != "" ? playerOne.getText() : "Player1";
		String whiteUserName = "Player2";
		String ipToConnect = tbIpOfOpponent.getText() != "" ? tbIpOfOpponent.getText() : "";
		int portToConnect = 0;
		try {
			if(cbPlayer2.getValue().equals("Network Player")) {
				portToConnect = Integer.parseInt(tbPortOfOpponent.getText());
				if(portToConnect < 0 || ipToConnect.equals("")) {
					throw new Exception("Error");
				}
			}			
		} catch(Exception e) {
			MessageBox.show(primaryStage, "Please fill in appropriate values", "Error", MessageBox.ICON_WARNING | MessageBox.OK);
			return;
		}
		
		int whiteId = 0;
		int blackId = 0;
		
		if(cbPlayer2.getValue().equals("Human Player")) {
			whiteUserName = playerTwo.getText() != "" ? playerTwo.getText() : "Player2";
		} else {
			whiteUserName = cbPlayer2.getValue();
			whiteId = cbPlayer2.getSelectionModel().getSelectedIndex();
		}
		
		UUID gameId = null;
		
		if(Math.random() > 0.5) {
			String tmp = String.copyValueOf(whiteUserName.toCharArray()); 
			whiteUserName = String.copyValueOf(blackUserName.toCharArray());
			blackUserName = String.copyValueOf(tmp.toCharArray());
			
			int tmpid = whiteId;
			whiteId = blackId;
			blackId = tmpid;
		}
		
		if(!cbPlayer2.getValue().equals("Network Player")) {
			gameId = createNewGame(blackUserName, whiteUserName, blackId, whiteId);
			FPTBS_Reversi.closeNewGame(gameId, blackUserName, whiteUserName, blackId, whiteId);
		} else {
			String myName = playerOne.getText() != "" ? playerOne.getText() : "Player1";
			FPTBS_Reversi.closeNewGameForNetworkGame(myName, ipToConnect, portToConnect, cbCreateNewNetworkGame.isSelected());
		}
		
	}
	
	@FXML
	public void cbPlayer2Changed(ActionEvent event) {
		if(cbPlayer2.getValue().equals("Human Player")) {
			hboxPlayer2.setVisible(true);
		} else {
			hboxPlayer2.setVisible(false);
		}
		
		if(cbPlayer2.getValue().equals("Network Player")) {
			MessageBox.show(primaryStage, "Please be aware that you can't save network games.", "Information", MessageBox.ICON_INFORMATION | MessageBox.OK);
			vboxNetworkGame.setVisible(true);
		} else {
			vboxNetworkGame.setVisible(false);
		}
	}
	
	@FXML
	public void cancelNewGame(ActionEvent event) {
		FPTBS_Reversi.closeNewGame(null, "Black", "White", 0, 0);
	}
	
	public UUID createNewGame(String blackUserName, String whiteUserName, int blackAlgId, int whiteAlgId) {
		ensureUserExists(new String[] {blackUserName, whiteUserName});
		
		// create the required DAO Factory
		DAOFactory h2DBFactory = DAOFactory.getDAOFactory(DAOFactory.H2DB);

		// Create a DAO
		GameDAO gameDAO = h2DBFactory.getGameDAO();
		
		UUID gameID = gameDAO.createGame(blackUserName, whiteUserName, blackAlgId, whiteAlgId);
		
		return gameID;
	}
	
	
	private void ensureUserExists(String[] strings) {
		// create the required DAO Factory
		DAOFactory h2DBFactory = DAOFactory.getDAOFactory(DAOFactory.H2DB);

		// Create a DAO
		UserDAO userDAO = h2DBFactory.getUserDAO();

		User black = userDAO.getUser(strings[0]);

		if(black == null) {
			black = new User();
			black.setUserName(strings[0]);
			black.setPassWord(strings[0]);
			
			userDAO.insertUser(black);
		}
		
		User white = userDAO.getUser(strings[1]);

		if(white == null) {
			white = new User();
			white.setUserName(strings[1]);
			white.setPassWord(strings[1]);
			
			userDAO.insertUser(white);
		}		
	}
}
