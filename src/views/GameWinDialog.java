package views;

import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JDialog;

import models.Constants;

public class GameWinDialog extends JDialog{

	private static final long serialVersionUID = 1L;
	private static final int WIDTH = (int)(Constants.WINDOW_WIDTH * 0.5);
	private static final int HEIGHT = (int)(Constants.WINDOW_HEIGHT * 0.6);
	
	public GameWinDialog(ActionListener listener) throws IOException {
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setUndecorated(true);
		add(new GameWinPanel(listener));
	}
}
