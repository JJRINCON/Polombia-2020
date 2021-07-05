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

public class GameWinPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private static final String PATH_GAME_OVER_IMG = "/resources/imagen_ganar.png";
	private BufferedImage gameWinImage;
	
	public GameWinPanel(ActionListener listener) throws IOException {
		setLayout(new BorderLayout());
		gameWinImage = ImageIO.read(getClass().getResource(PATH_GAME_OVER_IMG));
		addBtns(listener);
	}
	
	private void addBtns(ActionListener listener) {
		JPanel panel = new JPanel(new GridLayout(1,3,10,10));
		panel.setBackground(Color.DARK_GRAY);
		panel.add(createBtn(Constants.TXT_PLAY_AGAIN_BTN, Color.RED, Color.WHITE, listener));
		panel.add(createBtn(Constants.TXT_START_PANEL_BTN, Color.BLUE, Color.WHITE, listener));
		panel.add(createBtn(Constants.TXT_EXIT_GAME_BTN, Color.YELLOW, Color.BLACK, listener));
		panel.add(createBtn(Constants.TXT_SHOW_SCRERNSHOT_BTN, Color.WHITE, Color.BLACK, listener));
		add(panel, BorderLayout.SOUTH);
	}
	
	private JButton createBtn(String txt, Color color, Color foreground, ActionListener listener) {
		JButton generalBtn = new JButton(txt);
		generalBtn.setBackground(color);
		generalBtn.setForeground(foreground);
		generalBtn.setActionCommand(Commands.GAME_WIN_OPTION.toString());
		generalBtn.addActionListener(listener);
		generalBtn.setName(txt);
		return generalBtn;
	}
	
	@Override
	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(gameWinImage, 0, 0, getWidth(), getHeight(), this);
	}
}
