package uni_klu.se2.reversi.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;

import uni_klu.se2.reversi.data.*;
import uni_klu.se2.reversi.engine.IPlayer;
import uni_klu.se2.reversi.engine.ReversiEngine;
import uni_klu.se2.reversi.engine.player.*;
import uni_klu.se2.reversi.helper.RemoteReversiStub;
import uni_klu.se2.reversi.helper.SocketHelper;
import uni_klu.se2.reversi.helper.SocketHelperNotification;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;

import javax.swing.JComboBox;

public class test extends IPlayer implements IReversiGUI {

	private JFrame frame;
	private static Board  board;
	private static ReversiEngine engine;
	private static RemoteReversiStub stub;
	private Color black;
	private Color white;
	private JButton[][] buttons;
	private JLabel labelWhite;
	private JLabel labelBlack;
	private JLabel lblStatus;
	private JLabel labelStatus;
	private boolean myTurn;
	private JLabel lblBlackTotal;
	private JLabel lblWhiteTotal;
	private JLabel lblDrawTotal;
	private int blackT;
	private int whiteT;
	private int drawT;
	private JComboBox<String> comboBoxWhite;
	private JComboBox<String> comboBoxBlack;
	private JButton btnCreateGame;
	private JButton btnConnectToGame;
	private IPlayer Pwhite;
	private IPlayer Pblack;
	private SocketHelper socketHelper;
	private boolean selectedIndexChanged;
	private boolean isNetworkGameFirst;
	private boolean isNetworkGameSecond;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				try {
					board = new Board();
					stub = new RemoteReversiStub(RemoteReversiStub.SERVER_PUBLIC);
					test window = new test(board);
					RandomComputerPlayer cp = new RandomComputerPlayer(board);
					//SimpleMinMaxComputerPlayer cp2 = new SimpleMinMaxComputerPlayer(board);
					NaivDiskSquareComputerPlayer cp2 = new NaivDiskSquareComputerPlayer(board, 5);
					cp.number = 2;
					cp2.number = 1;
					engine = new ReversiEngine(board, window, cp2);
					cp.setEngine(engine);
					cp2.setEngine(engine);
					engine.watcher = window;
					window.setEngine(engine);
					engine.start();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public test(Board board) {
		super(board);
		myTurn = false;
		socketHelper = new SocketHelper(this);
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		blackT = 0;
		whiteT = 0;
		drawT = 0;
		frame = new JFrame();
		isNetworkGameFirst = false;
		isNetworkGameSecond = false;
		selectedIndexChanged = false;
		frame.setBounds(100, 100, 1013, 718);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
	
		
		JLabel lblWhite = new JLabel("White:");
		lblWhite.setBounds(30, 0, 46, 14);
		frame.getContentPane().add(lblWhite);
		
		JLabel lblBlack = new JLabel("Black:");
		lblBlack.setBounds(340, 0, 46, 14);
		frame.getContentPane().add(lblBlack);
		
		labelWhite = new JLabel("0");
		labelWhite.setBounds(94, 0, 46, 14);
		frame.getContentPane().add(labelWhite);
		
		labelBlack = new JLabel("0");
		labelBlack.setBounds(396, 0, 46, 14);
		frame.getContentPane().add(labelBlack);
		
		lblStatus = new JLabel("Status:");
		lblStatus.setBounds(553, 0, 46, 14);
		frame.getContentPane().add(lblStatus);
		
		labelStatus = new JLabel(" ");
		labelStatus.setBounds(596, 0, 103, 14);
		frame.getContentPane().add(labelStatus);
		
		JLabel lblBlack_1 = new JLabel("Black:");
		lblBlack_1.setBounds(798, 36, 46, 14);
		frame.getContentPane().add(lblBlack_1);
		
		JLabel lblStatistics = new JLabel("Statistics:");
		lblStatistics.setBounds(798, 11, 77, 14);
		frame.getContentPane().add(lblStatistics);
		
		JLabel lblWhite_1 = new JLabel("White: ");
		lblWhite_1.setBounds(798, 61, 46, 14);
		frame.getContentPane().add(lblWhite_1);
		
		JLabel lblDraw = new JLabel("Draw: ");
		lblDraw.setBounds(798, 86, 46, 14);
		frame.getContentPane().add(lblDraw);
		
		lblBlackTotal = new JLabel("0");
		lblBlackTotal.setBounds(901, 36, 46, 14);
		frame.getContentPane().add(lblBlackTotal);
		
		lblWhiteTotal = new JLabel("0");
		lblWhiteTotal.setBounds(901, 61, 46, 14);
		frame.getContentPane().add(lblWhiteTotal);
		
		lblDrawTotal = new JLabel("0");
		lblDrawTotal.setBounds(901, 86, 46, 14);
		frame.getContentPane().add(lblDrawTotal);
		
		JLabel lblPlayerWhite = new JLabel("Player White:");
		lblPlayerWhite.setBounds(798, 135, 82, 14);
		frame.getContentPane().add(lblPlayerWhite);
		
		JLabel lblPlayerBlack = new JLabel("Player Black:");
		lblPlayerBlack.setBounds(798, 204, 77, 14);
		frame.getContentPane().add(lblPlayerBlack);
		
		String[] str = { "Human Player", "Random Computer Player", "Simple MinMax Player", "NaivDiskSquare(d=3)", "NaivDiskSquare(d=4)", "NaivDiskSquare(d=5)", "NaivDiskSquare(d=6)", "NaivDiskSquare(d=7)", "DeepMinMax(d=3)", "DeepMinMax(d=4)", "DeepMinMax(d=5)", "DeepMinMax(d=6)", "DeepMinMax(d=7)" };

		comboBoxWhite = new JComboBox<String>(str);
		comboBoxWhite.setBounds(798, 229, 189, 20);
		comboBoxWhite.setSelectedIndex(0);
		frame.getContentPane().add(comboBoxWhite);
		
		comboBoxBlack = new JComboBox<String>(str);
		comboBoxBlack.setBounds(798, 160, 189, 20);
		frame.getContentPane().add(comboBoxBlack);
		comboBoxWhite.setSelectedIndex(5);
		
		JButton btnNewButton_1 = new JButton("New Game");
		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 selectedIndexChanged = true;
				 newGame();
			}
		});
		btnNewButton_1.setBounds(798, 276, 189, 60);
		frame.getContentPane().add(btnNewButton_1);
		
		btnCreateGame = new JButton("Create Socket");
		btnCreateGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				socketHelper.createGameAndWaitForClient(8200, true);
			}
		});
		btnCreateGame.setBounds(798, 346, 189, 60);
		frame.getContentPane().add(btnCreateGame);
		
		btnConnectToGame = new JButton("Connect Socket");
		btnConnectToGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				 socketHelper.connectToHost("localhost", 8200);
			}
		});
		btnConnectToGame.setBounds(798, 416, 189, 60);
		frame.getContentPane().add(btnConnectToGame);
		
		JList list = new JList(stub.getAvailableGames().values().toArray()); //data has type Object[]
		list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		list.setVisibleRowCount(-1);
		
		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(250, 100));
		listScroller.setBounds(700, 500, 250, 100);
		
		
		frame.getContentPane().add(listScroller);

		white = new Color(242, 161, 33);
		black = new Color(33, 45, 78);
		board = new Board();
		buttons = new JButton[8][8];
		Field[][] fields = board.getFields();
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
			{
				JButton btnNewButton = new JButton(i + " " + j);
				btnNewButton.setOpaque(true);
				btnNewButton.setBounds(35 + i * 80, 35 + j * 80, 75, 75);
				btnNewButton.setEnabled(false);
				if (fields[i][j].getStatus() == FieldStatus.BLACK)
				{
					btnNewButton.setBackground(black);
				}
				if (fields[i][j].getStatus() == FieldStatus.WHITE)
				{
					btnNewButton.setBackground(white);
				}
				btnNewButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						if (!myTurn)
							return;
						JButton btn = (JButton)arg0.getSource();
						int x = (btn.getX() - 35) / 80;
						int y = (btn.getY() - 35) / 80;
						System.out.println(x + " " + y);
						engine.onMoveReadyCalculated(new Move(x, y), false);
						Field[][] fieldss = board.getFields();
						for (int i = 0; i < 8; i++)
							for (int j = 0; j < 8; j++)
							{
								JButton current = buttons[i][j];
								current.setEnabled(false);
								current.setBackground(Color.GRAY);
								if (fieldss[i][j].getStatus() == FieldStatus.BLACK)
								{
									current.setBackground(black);
								}
								if (fieldss[i][j].getStatus() == FieldStatus.WHITE)
								{
									current.setBackground(white);
								}
							}
						List<Move> legalMoves = board.getAvailableMoves();
						Iterator<Move> iterator = legalMoves.iterator();
						while(iterator.hasNext())
						{
							Move field = iterator.next();
							//buttons[field.getX()][field.getY()].setEnabled(true);
						}
						
						labelBlack.setText("" + board.getBlackFieldsOnBoard());
						labelWhite.setText("" + board.getWhiteFieldsOnBoard());
						
						if (board.getStatus() == BoardStatus.INPROGRESS)
						{
							labelStatus.setText("InProgress");
						} else if (board.getStatus() == BoardStatus.BLACKWON)
						{
							labelStatus.setText("Black wins!");
						} else if (board.getStatus() == BoardStatus.WHITEWON)
						{
							labelStatus.setText("White wins!");
						} else if (board.getStatus() == BoardStatus.DRAW)
						{
							labelStatus.setText("Draw!");
						}
					}
				});
				frame.getContentPane().add(btnNewButton);
				buttons[i][j] = btnNewButton;
			}
		
		List<Move> legalMoves = board.getAvailableMoves();
		Iterator<Move> iterator = legalMoves.iterator();
		while(iterator.hasNext())
		{
			Move field = iterator.next();
			buttons[field.getX()][field.getY()].setEnabled(true);
		}
		

	}

	@Override
	public void yourTurn() {
		this.myTurn = true;
		Field[][] fieldss = board.getFields();
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
			{
				JButton current = buttons[i][j];
				current.setEnabled(false);
				current.setBackground(Color.GRAY);
				if (fieldss[i][j].getStatus() == FieldStatus.BLACK)
				{
					current.setBackground(black);
				}
				if (fieldss[i][j].getStatus() == FieldStatus.WHITE)
				{
					current.setBackground(white);
				}
			}
		List<Move> legalMoves = board.getAvailableMoves();
		Iterator<Move> iterator = legalMoves.iterator();
		while(iterator.hasNext())
		{
			Move field = iterator.next();
			buttons[field.getX()][field.getY()].setEnabled(true);
		}
		
		labelBlack.setText("" + board.getBlackFieldsOnBoard());
		labelWhite.setText("" + board.getWhiteFieldsOnBoard());
		
		if (board.getStatus() == BoardStatus.INPROGRESS)
		{
			labelStatus.setText("InProgress");
		} else if (board.getStatus() == BoardStatus.BLACKWON)
		{
			labelStatus.setText("Black wins!");
		} else if (board.getStatus() == BoardStatus.WHITEWON)
		{
			labelStatus.setText("White wins!");
		} else if (board.getStatus() == BoardStatus.DRAW)
		{
			labelStatus.setText("Draw!");
		}
	}
	
	public void paintBoard()
	{
		Field[][] fieldss = board.getFields();
		for (int i = 0; i < 8; i++)
			for (int j = 0; j < 8; j++)
			{
				JButton current = buttons[i][j];
				current.setEnabled(false);
				current.setBackground(Color.GRAY);
				if (fieldss[i][j].getStatus() == FieldStatus.BLACK)
				{
					current.setBackground(black);
				}
				if (fieldss[i][j].getStatus() == FieldStatus.WHITE)
				{
					current.setBackground(white);
				}
			}
		List<Move> legalMoves = board.getAvailableMoves();
		Iterator<Move> iterator = legalMoves.iterator();
		while(iterator.hasNext())
		{
			Move field = iterator.next();
			//buttons[field.getX()][field.getY()].setEnabled(true);
		}
		
		labelBlack.setText("" + board.getBlackFieldsOnBoard());
		labelWhite.setText("" + board.getWhiteFieldsOnBoard());
		
		if (board.getStatus() == BoardStatus.INPROGRESS)
		{
			labelStatus.setText("InProgress");
		} else if (board.getStatus() == BoardStatus.BLACKWON)
		{	
			labelStatus.setText("Black wins!");
			blackT++;
			lblBlackTotal.setText(blackT + "");
			newGame();
		} else if (board.getStatus() == BoardStatus.WHITEWON)
		{
			labelStatus.setText("White wins!");
			whiteT++;
			lblWhiteTotal.setText(whiteT + "");
			newGame();
		} else if (board.getStatus() == BoardStatus.DRAW)
		{
			labelStatus.setText("Draw!");
			drawT++;
			lblDrawTotal.setText(drawT + "");
			newGame();
		}
	}
	
	
	private void newGame() 
	{
		if (isNetworkGameFirst)
		{
			socketHelper.closeConnection();
			isNetworkGameFirst = false;
			return;
		} else if (isNetworkGameSecond)
		{
			socketHelper.closeConnection();
			isNetworkGameSecond = false;
			return;
		}
		System.out.println("new game!");
		Pwhite = null;
		Pblack = null;
		board = null;
		engine = null;
		board = new Board();
		paintBoard();
		switch(comboBoxWhite.getSelectedIndex())
		{
			case 0:
				Pwhite = this; break;
			case 1:
				Pwhite = new RandomComputerPlayer(board); break;
			case 2:
				Pwhite = new SimpleMinMaxComputerPlayer(board); break;
			case 3:
				Pwhite = new NaivDiskSquareComputerPlayer(board, 3); break;
			case 4:
				Pwhite = new NaivDiskSquareComputerPlayer(board, 4); break;
			case 5:
				Pwhite = new NaivDiskSquareComputerPlayer(board, 5); break;
			case 6:
				Pwhite = new NaivDiskSquareComputerPlayer(board, 6); break;
			case 7:
				Pwhite = new NaivDiskSquareComputerPlayer(board, 7); break;
			case 8:
				Pwhite = new DeepMinMaxComputerPlayer(board, 3); break;
			case 9:
				Pwhite = new DeepMinMaxComputerPlayer(board, 4); break;
			case 10:
				Pwhite = new DeepMinMaxComputerPlayer(board, 5); break;
			case 11:
				Pwhite = new DeepMinMaxComputerPlayer(board, 6); break;
			case 12:
				Pwhite = new DeepMinMaxComputerPlayer(board, 7); break;
			default:
				Pwhite = this;		 break;
		}
		switch(comboBoxBlack.getSelectedIndex())
		{
			case 0:
				Pblack = this; break;
			case 1:
				Pblack = new RandomComputerPlayer(board); break;
			case 2:
				Pblack = new SimpleMinMaxComputerPlayer(board); break;
			case 3:
				Pblack = new NaivDiskSquareComputerPlayer(board, 3); break;
			case 4:
				Pblack = new NaivDiskSquareComputerPlayer(board, 4); break;
			case 5:
				Pblack = new NaivDiskSquareComputerPlayer(board, 5); break;
			case 6:
				Pblack = new NaivDiskSquareComputerPlayer(board, 6); break;
			case 7:
				Pblack = new NaivDiskSquareComputerPlayer(board, 7); break;
			case 8:
				Pblack = new DeepMinMaxComputerPlayer(board, 3); break;
			case 9:
				Pblack = new DeepMinMaxComputerPlayer(board, 4); break;
			case 10:
				Pblack = new DeepMinMaxComputerPlayer(board, 5); break;
			case 11:
				Pblack = new DeepMinMaxComputerPlayer(board, 6); break;
			case 12:
				Pblack = new DeepMinMaxComputerPlayer(board, 7); break;
			default:
				Pblack = this;		 break;
		}
		engine = new ReversiEngine(board, Pblack, Pwhite);
		Pwhite.setEngine(engine);
		Pblack.setEngine(engine);
		engine.watcher = this;
		this.setEngine(engine);
		engine.start();
	}

	public void socketHelperNotification(SocketHelperNotification notfication) {
		switch(notfication)
		{
		case INITIALISED: System.out.println("SocketHelperNotification: Initialized"); break;
		case WAITING: System.out.println("SocketHelperNotification: Waiting"); break;
		case CONNECTED_AS_FIRST: System.out.println("SocketHelperNotification: Connected as First"); 
			System.out.println("new game!");
			isNetworkGameFirst = true;
			isNetworkGameSecond = false;
			board = new Board();
			paintBoard();
			Pwhite = new SocketPlayer(board, socketHelper);
			Pblack = new RandomComputerPlayer(board);
			engine = new ReversiEngine(board, Pwhite, Pblack);
			Pwhite.setEngine(engine);
			Pblack.setEngine(engine);
			engine.watcher = this;
			this.setEngine(engine);
			engine.start();

		break;
		case CONNECTED_AS_SECOND: System.out.println("SocketHelperNotification: Connected as Second"); 
			System.out.println("new game!");
			isNetworkGameFirst = false;
			isNetworkGameSecond = true;
			board = new Board();
			paintBoard();
			Pblack = new SocketPlayer(board, socketHelper);
			Pwhite = new RandomComputerPlayer(board);
			engine = new ReversiEngine(board, Pwhite, Pblack);
			Pwhite.setEngine(engine);
			Pblack.setEngine(engine);
			engine.watcher = this;
			this.setEngine(engine);
			engine.start();
		break;
		case DISCONNECTED: System.out.println("SocketHelperNotification: Disconnected"); break;
		case RECONNECTING: System.out.println("SocketHelperNotification: Reconnecting"); break;
		case ERROR: System.out.println("SocketHelperNotification: Error"); break;
		default: break;
		}
		
	}

	@Override
	public void signalLastMove() {
		// TODO Auto-generated method stub
		
	}
}
