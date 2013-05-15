package uni_klu.se2.reversi.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides easy access for the Reversi Remote Server. It is intended
 * to be used for A.I. vs A.I. games accross different implementations of
 * reversi. The server component decides which moves are valid and which are
 * not, therefore same rules apply for all implementations.
 * 
 * @author Peter Spiess-Knafl <peter.knafl@edu.aau.at>
 */
public class RemoteReversiStub {
	private String host;
	private int gameid;
	private String token;
	private String color;

	/**
	 * This server address is the public one, which you should use.
	 */
	public static final String SERVER_PUBLIC = "http://vm233-isys.aau.at/seii13/index.php/reversi";
	
	/**
	 * This server address was only for testing purposes on the localhost. You will not need this.
	 */
	public static final String SERVER_LOCAL = "http://localhost:8888/index.php/reversi";
	
	/**
	 * String constant for color white.
	 */
	public static final String COLOR_WHITE = "white";
	
	/**
	 * String constant for color black.
	 */
	public static final String COLOR_BLACK = "black";
	
	/**
	 * 
	 * @param host - the address off the reversi server. You can use the provided constant SERVER_PUBLIC.
	 */
	public RemoteReversiStub(String host) {
		this.host = host;
		this.gameid = -1;
		this.token = null;
		this.color = null;
	}

	/**
	 * 
	 * @return the current host address.
	 */
	public String getCurrentHost() {
		return host;
	}

	/**
	 * 
	 * @return the current gameid which is set automatically after calling joinGame() method.
	 */
	public int getCurrentGameId() {
		return gameid;
	}

	/**
	 * 
	 * @return the current token which was assigned to you by the server. It is set automatically after calling joinGame() method.
	 */
	public String getCurrentToken() {
		return token;
	}

	/**
	 * 
	 * @return the current color which was assigned to you by the server. It is set automatically after calling joinGame() method.
	 */
	public String getCurrentColor() {
		return this.color;
	}
	
	/**
	 * 
	 * @return This method gets all available games at the server. The key of the map is the game id, the value is the gamename.
	 */
	public Map<Integer,String> getAvailableGames() {
		HashMap<Integer,String> result = new HashMap<Integer,String>();
		String response = this.doGetRequest("listgames");
		String[] lines = response.split("\\n");
		for(int i=0; i < lines.length-1; i++) {
			String[] parts = lines[i].split(";");
			result.put(Integer.parseInt(parts[0]), parts[1]);
		}
		return result;
	}

	/**
	 * This method starts a new game on the server with a specific name.
	 * @param gamename - the name of the game.
	 * @return the gameid which can be used to join a specific game.
	 */
	public int startGame(String gamename) {
		String result = this.doGetRequest("startgame/" + gamename);
		this.gameid = Integer.parseInt(result);
		return this.gameid;
	}

	/**
	 * This method allows you to join a specific game (provided via gameid, which can be obtained using the return value of startGame() or getAvailableGames()). 
	 * @param gameid the id of the game you want to join.
	 * @return true on success, false otherwise (e.g. already two players joined, or the game is not available).
	 */
	public boolean joinGame(int gameid) {
		if (this.token == null) {
			String result = this.doGetRequest("joingame/" + gameid);
			if (result.contains("false")) {
				return false;
			} else {
				String[] parts = result.split(";");
				this.token = parts[0];
				this.color = parts[1];
				this.gameid = gameid;
				return true;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * This method gets the current field encoded in CSV values. e stands for empty field, w for a white brick and b for a black brick.
	 * @return the gamefield or null, if no game has been joined yet.
	 */
	public String[][] getField() {
		if(this.token == null) {
			return null;
		} else {
			String response = this.doGetRequest("getfield/"+this.token);
			if(response.contains("false")) {
				return null;
			} else {
				String[] rows = response.split("\\n");
				String[][] result = new String[rows.length-1][rows[0].split(";").length];
				for(int i=0; i< rows.length-1; i++) {
					String[] columns = rows[i].split(";");
					for(int j=0; j < columns.length; j++) {
						if(columns[j].equals("e")) {
							result[i][j] = " ";
						} else {
							result[i][j] = columns[j];
						}	
					}
				}
				return result;
			}
		}
	}
	
	/**
	 * This method should always be called, after you set a brick. It is blocking (or not) until you are allowed to set the next brick or pass.
	 * @param block if set to true, this method blocks until it is your turn, or the game as been canceled.
	 * @return true if it is your turn, false if it is not your turn or the game has been canceled.
	 */
	public boolean isMyTurn(boolean block) {
		String result = null;
		do {
			if(result != null) {
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {}
			}
			result = this.doGetRequest("getstatus/"+this.token);
			
		}
		while (!result.equals(this.color) && !result.equals("error") && block); 
		
		if(result.equals(this.color))
			return true;
		else
			return false;
	}
	
	/**
	 * This method quits the current game you have joined. isMyTurn() on your opponents side will return false.
	 * @return true if the game could be quit, false otherwise (e.g. you have not been assigned to a valid game).
	 */
	public boolean closeGame() {
		String response = this.doGetRequest("closegame/"+this.token);
		if(response.contains("true")) {
			return true;
		}
		return false;
	}
	
	/**
	 * this method can be used to set a brick on the playfield if it is your turn (check that using isMyTurn() method).
	 * @param row the row where you want to set your brick.
	 * @param col the column wher you want to set your brick.
	 * @return true if the brick has been set (you can check that using getField()) or false if it wasn't your turn, or the move was not valid.
	 */
	public boolean setBrick(int row, int col) {
		if(this.doGetRequest("setbrick/"+this.token+"/"+row + "/" + col).equals("true")) {
			return true;
		}
		return false;
	}
	
	/**
	 * If you are not able to set any bricks, you can call this method.
	 * @return true if pass was allowed, false otherwise.
	 */
	public boolean passRound() {
		if(this.doGetRequest("pass/"+this.token).equals("true")) {
			return true;
		}
		return false;
	}
	
	/**
	 * This method is only for debugging purposes, it prints the current playfield on stdout.
	 */
	public void printField() {
		System.out.println(this.doGetRequest("getfield/"+this.token));
	}
	
	private String doGetRequest(String url) {
		String result = null;
		try {
			String[] urlElements = url.split("/");
			String res = "";
			for (String s : urlElements) {
				res += URLEncoder.encode(s, "UTF-8").replaceAll("\\+", "%20")
						.replaceAll("%3A", ":")
						+ "/";
			}
			URLConnection conn = new URL(this.host + "/" + res)
					.openConnection();
			BufferedReader rd = new BufferedReader(new InputStreamReader(
					conn.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String line;
			while ((line = rd.readLine()) != null) {
				sb.append(line + "\n");
			}
			result = sb.toString().substring(0, sb.toString().length()-1);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}

	/**
	 * represents the current state of the stub as a string.
	 */
	@Override
	public String toString() {
		return "RemoteReversiStub [host=" + host + ", gameid=" + gameid
				+ ", token=" + token + ", color=" + color + "]";
	}
	
	/**
	 * This main is only provided as an example how to use the stub.
	 * @param args
	 */
	public static void main(String[] args) {
		
		//Create a connection object
		RemoteReversiStub stub = new RemoteReversiStub(RemoteReversiStub.SERVER_PUBLIC);
		
		//Create a new game
		int gameid = stub.startGame("My new game");
		
		//Then join it
		stub.joinGame(gameid);
		
		//See if everything worked -> token id != null
		System.out.println(stub.toString());
		
		//This method blocks until it is our turn
		boolean myturn = stub.isMyTurn(true);
		
		if(myturn) {
			if(stub.setBrick(3, 2)) {
				System.out.println("Brick set successful");
			} else {
				System.out.println("Brick cannot be placed at 2/3");
			}
		}
		
		
		//play along ....
		stub.closeGame();
	}
	
}
