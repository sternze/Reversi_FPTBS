package uni_klu.se2.reversi.gui;

import uni_klu.se2.reversi.data.Move;
import uni_klu.se2.reversi.helper.SocketHelperNotification;

public interface IReversiGUI 
{
	public void paintBoard();
	public void socketHelperNotification(SocketHelperNotification notfication);
}
