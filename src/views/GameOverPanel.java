package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JPanel;

import models.Constants;
import presenters.Commands;

public class GameOverPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private static final String PATH_GAME_OVER_IMG = "/resources/game over.png";
	private BufferedImage gameOverImage;
	
	public GameOverPanel(ActionListener listener) throws IOException {
		setLayout(new BorderLayout());
		gameOverImage = ImageIO.read(getClass().getResource(PATH_GAME_OVER_IMG));
		addBtns(listener);
	}
	
	private void addBtns(ActionListener listener) {
		JPanel panel = new JPanel(new GridLayout(1,3,10,10));
		panel.setBackground(Color.BLACK);
		panel.add(createBtn(Constants.TXT_RESTART_BTN, Color.RED, Color.WHITE, listener));
		panel.add(createBtn(Constants.TXT_START_PANEL_BTN, Color.BLUE, Color.WHITE, listener));
		panel.add(createBtn(Constants.TXT_EXIT_GAME_BTN, Color.YELLOW, Color.BLACK, listener));
		panel.add(createBtn(Constants.TXT_SHOW_SCRERNSHOT_BTN, Color.WHITE, Color.BLACK, listener));
		add(panel, BorderLayout.SOUTH);
	}
	
	private JButton createBtn(String txt, Color color, Color foreground, ActionListener listener) {
		JButton generalBtn = new JButton(txt);
		generalBtn.setBackground(color);
		generalBtn.setForeground(foreground);
		generalBtn.setActionCommand(Commands.GAME_OVER_OPTION.toString());
		generalBtn.addActionListener(listener);
		generalBtn.setName(txt);
		return generalBtn;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(gameOverImage, 0, 0, getWidth(), getHeight(), this);
	}
}
