package views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;

import models.Constants;
import presenters.Commands;

public class initialPanel extends MyGridPanel{

	private static final String FONT_NAME = "Arial";
	private static final long serialVersionUID = 1L;
	private static final String PATH_COLOMBIA_IMG = "/resources/colombia.png";
	private static final String PATH_SILENCE_ICON = "/resources/silence.png";
	private static final String PATH_START_ICON = "/resources/start.png";
	private BufferedImage image;
	private BufferedImage silenceIcon;
	private BufferedImage startIcon;
	private JButton startBtn;
	private JButton loadBtn;
	private JButton exitBtn;
	private JButton silenceBtn;
	private JButton playBtn;
	
	public initialPanel(ActionListener listener) throws IOException {
		image = ImageIO.read(getClass().getResource(PATH_COLOMBIA_IMG));
		silenceIcon = ImageIO.read(getClass().getResource(PATH_SILENCE_ICON));
		startIcon = ImageIO.read(getClass().getResource(PATH_START_ICON));
		
		addComponent(new JLabel(), 3, 1, 6, 0.4);
		startBtn = createBtn(Constants.TXT_START_BTN, Color.RED, Color.WHITE, listener);
		addComponent(startBtn, 4, 2, 4, 0.1);
		addComponent(new JLabel(), 3, 3, 6, 0.02);
		loadBtn = createBtn(Constants.TXT_LOAD_GAME_BTN, Color.BLUE, Color.WHITE, listener);
		addComponent(loadBtn, 4, 4, 4, 0.1);
		addComponent(new JLabel(), 3, 5, 6, 0.02);
		exitBtn = createBtn(Constants.TXT_EXIT_BTN, Color.YELLOW, Color.BLACK, listener);
		addComponent(exitBtn, 4, 6, 4, 0.1);
		addComponent(new JLabel(), 3, 7, 6, 0.02);
		
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
	
	private JButton createBtn(String txt, Color color, Color foreground, ActionListener listener) {
		JButton generalBtn = new JButton(txt);
		generalBtn.setName(txt);
		generalBtn.setBackground(color);
		generalBtn.setForeground(foreground);
		generalBtn.setBorder(new LineBorder(Color.BLACK, 3, true));
		generalBtn.setFont(new Font(FONT_NAME, Font.BOLD, 20));
		generalBtn.setActionCommand(Commands.START.toString());
		generalBtn.addActionListener(listener);
		return generalBtn;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.drawImage(image, 0, 0, getWidth(), getHeight(), this);
	}
}
