package views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import models.Constants;
import presenters.Commands;

public class PausedGamePanel extends MyGridPanel{

	private static final String PATH_BACKGROUND_IMAGE = "/resources/pausa.png";
	private static final long serialVersionUID = 1L;
	private static final String PATH_SILENCE_ICON = "/resources/silence.png";
	private static final String PATH_START_ICON = "/resources/start.png";
	private BufferedImage backgroundImage;
	private BufferedImage silenceIcon;
	private BufferedImage startIcon;
	private JButton silenceBtn;
	private JButton playBtn;
	
	public PausedGamePanel(ActionListener listener) throws IOException {
		backgroundImage = ImageIO.read(getClass().getResource(PATH_BACKGROUND_IMAGE));
		silenceIcon = ImageIO.read(getClass().getResource(PATH_SILENCE_ICON));
		startIcon = ImageIO.read(getClass().getResource(PATH_START_ICON));
		addComponent(new JLabel(), 1, 1, 11, 0.3);
		addComponent(createBtn(Constants.TXT_START_BTN, Color.BLUE, Color.WHITE, listener), 2, 2, 4, 0.1);
		addComponent(createBtn(Constants.TXT_RESTART_BTN_PAUSE_PANEL, Color.YELLOW, Color.BLACK, listener), 7, 2, 4, 0.1);
		addComponent(new JLabel(), 1, 5, 11, 0.05);
		addComponent(createBtn(Constants.TXT_LOAD_BTN_PAUSE_PANEL, Color.RED, Color.WHITE, listener), 2, 6, 4, 0.1);
		addComponent(createBtn(Constants.TXT_CONTINUE_BTN_PAUSE_PANEL, Color.BLUE, Color.BLACK, listener), 7, 6, 4, 0.1);
		addComponent(new JLabel(), 1, 7, 11, 0.05);
		
		silenceBtn = createBtn(Constants.TXT_SILENCE_SOUND_BTN, Color.WHITE, Color.WHITE, listener);
		silenceBtn.setText("");
		silenceBtn.setIcon(new ImageIcon(silenceIcon));
		silenceBtn.setPreferredSize(new Dimension(50, 50));
		addComponent(silenceBtn, 0,8, 1, 0.01);
		
		playBtn = createBtn(Constants.TXT_PLAY_SOUND_BTN, Color.WHITE, Color.WHITE, listener);
		playBtn.setText("");
		playBtn.setIcon(new ImageIcon(startIcon));
		playBtn.setPreferredSize(new Dimension(50, 50));
		addComponent(playBtn, 11,8, 1, 0.01);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
	}
	
	private JButton createBtn(String txt, Color color, Color foreground, ActionListener listener) {
		JButton generalBtn = new JButton(txt);
		generalBtn.setBackground(color);
		generalBtn.setForeground(foreground);
		generalBtn.setActionCommand(Commands.PAUSED_OPTION.toString());
		generalBtn.addActionListener(listener);
		generalBtn.setName(txt);
		return generalBtn;
	}
}
