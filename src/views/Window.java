package views;

import java.awt.AWTException;
import java.awt.BorderLayout;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import models.Constants;
import models.Game;

public class Window extends JFrame{

	private static final long serialVersionUID = 1L;
	private static final String APP_TITLE = "Polombia 2020";
	private static final String APP_ICON_PATH = "/resources/icono.png";
	private GamePanel gamePanel;
	private BufferedImage screenShot;
	
	public Window(Game game, KeyListener keyListener) throws AWTException, IOException {
		BufferedImage appIcon = ImageIO.read(getClass().getResource(APP_ICON_PATH));
		setIconImage(appIcon);
		setLayout(new BorderLayout());
		setTitle(APP_TITLE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setExtendedState(MAXIMIZED_BOTH);
		setUndecorated(true);
	}
	
	public void showGamePanel(Game game, KeyListener keyListener) {
		gamePanel = new GamePanel(game, keyListener);
		add(gamePanel);
	}
	
	public void hideGamePanel() {
		gamePanel.setVisible(false);
	}
	
	public BufferedImage screenShot() {
		screenShot = new BufferedImage((int)(Constants.WINDOW_WIDTH), (int)(Constants.WINDOW_HEIGHT),
				BufferedImage.TYPE_INT_RGB);
		gamePanel.paint(screenShot.getGraphics());
		gamePanel.setActualScreenshot(screenShot);
		return screenShot;
	}
	
	public void setIsCapturing(boolean isCapturing) {
		gamePanel.setIsCapturing(isCapturing);
	}
	
	public boolean getIsCapturing() {
		return gamePanel.getIsCapturing();
	}
	
	public void update(Game game) {
		gamePanel.update(game);
	}
}
