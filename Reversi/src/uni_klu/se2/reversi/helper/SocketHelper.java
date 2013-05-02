package uni_klu.se2.reversi.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.SwingWorker;

import uni_klu.se2.reversi.data.Move;
import uni_klu.se2.reversi.gui.IReversiGUI;

public class SocketHelper {
	protected ServerSocket   server;
	protected Socket         client;
	protected PrintWriter    out;
	protected BufferedReader in;
	protected IReversiGUI    gui;
	protected boolean        isHostFirstPlayer;
	protected int            port;
	protected String         url;
	
	public SocketHelper(IReversiGUI gui)
	{
		server   = null;
		client   = null;
		in       = null;
		out      = null;
		url      = null;
		this.gui = gui;
		port     = 8600;
		isHostFirstPlayer = true;

	}
	
	public void sendMove(Move move)
	{
		out.println(move.toNetString());
	}
	
	public Move getMove()
	{
		Move m = null;
		try {
			String input = in.readLine();
			m = new Move(input);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return m;
	}
	
	public void closeConnection()
	{
		try {
			out.close();
			in.close();
			client.close();
			if (server != null)
				server.close();
			out    = null;
			in     = null;
			client = null;
			server = null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public void createGameAndWaitForClient(int port, boolean isHostFirstPlayer)
	{
		this.port              = port;
		this.isHostFirstPlayer = isHostFirstPlayer;
		BackgroundHost bg      = new BackgroundHost();
		bg.execute();
	}
	
	public void connectToHost(String url, int port)
	{
		this.url  = url;
		this.port = port;
		BackgroundClient gb = new BackgroundClient();
		gb.execute();
	}
	

	private class BackgroundHost extends SwingWorker<Boolean, Boolean>
	{
		public BackgroundHost() { }
		@Override
		protected Boolean doInBackground() throws Exception 
		{
			try {
				gui.socketHelperNotification(SocketHelperNotification.WAITING);
			    server = new ServerSocket(port);
			} 
			catch (IOException e) {
			    System.out.println("Could not listen on port: " + port);
			    gui.socketHelperNotification(SocketHelperNotification.ERROR);
			}
			try {
			    client = server.accept();
			    
			} 
			catch (IOException e) {
			    System.out.println("Accept failed");
			    System.exit(-1);
			}
			out = new PrintWriter(client.getOutputStream(), true);
			in =  new BufferedReader(new InputStreamReader(client.getInputStream()));
			if (isHostFirstPlayer)
			{
				out.println("first");
				gui.socketHelperNotification(SocketHelperNotification.CONNECTED_AS_SECOND);
			}
			else
			{
				out.println("second");
				gui.socketHelperNotification(SocketHelperNotification.CONNECTED_AS_FIRST);
			}
			return true;
		}
		
	}
	
	private class BackgroundClient extends SwingWorker<Boolean, Boolean>
	{
		public BackgroundClient() { }
		@Override
		protected Boolean doInBackground() throws Exception 
		{
			client = new Socket(url, port);
			out = new PrintWriter(client.getOutputStream(), true);
			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
			gui.socketHelperNotification(SocketHelperNotification.WAITING);
			String fromHost = in.readLine();
			if (fromHost.equals("first"))
			{
				gui.socketHelperNotification(SocketHelperNotification.CONNECTED_AS_FIRST);
			} else
			{
				gui.socketHelperNotification(SocketHelperNotification.CONNECTED_AS_SECOND);
			}
			return true;
		}
		
	}

}
