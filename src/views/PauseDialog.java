package views;

import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JDialog;

import models.Constants;

public class PauseDialog extends JDialog{

	private static final long serialVersionUID = 1L;
	private static final int WIDTH = (int)(Constants.WINDOW_WIDTH * 0.5);
	private static final int HEIGHT = (int)(Constants.WINDOW_HEIGHT * 0.6);
	private PausedGamePanel pausedGamePanel;
	
	public PauseDialog(ActionListener listener, KeyListener keyListener) throws IOException {
		addKeyListener(keyListener);
		setSize(WIDTH, HEIGHT);
		setLocationRelativeTo(null);
		setUndecorated(true);
		pausedGamePanel = new PausedGamePanel(listener);
		add(pausedGamePanel);
	}
	
//	public static void main(String[] args) {
//		try {
//			new PauseDialog(null, null).setVisible(true);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
}
