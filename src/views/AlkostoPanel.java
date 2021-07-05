package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

import models.Constants;

public class AlkostoPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private static final String PATH_IMG_ALKOSTO = "/resources/logo alkosto.png";
	private static final String TXT_LB_INFO = "Seleccione el producto que quiera comprar";
	private static int WIDTH = (int)(Constants.WINDOW_WIDTH * 0.3);
	private static int HEIGHT = (int)(Constants.WINDOW_WIDTH * 0.3);
	private BufferedImage alkostoImage;
	
	public AlkostoPanel(ConcurrentHashMap<String, BufferedImage> images, KeyListener keyListener) throws IOException {
		addKeyListener(keyListener);
		setBorder(new LineBorder(Color.decode("#07009B"), 4 , true));
		setBackground(Color.WHITE);
		setLayout(new BorderLayout());
		alkostoImage = ImageIO.read(getClass().getResource(PATH_IMG_ALKOSTO));
		JLabel infoLb = new JLabel(TXT_LB_INFO);
		infoLb.setFont(new Font("Arial", Font.BOLD, 18));
		add(infoLb, BorderLayout.PAGE_END);
		add(new JLabel(new ImageIcon(new ImageIcon(alkostoImage).getImage().getScaledInstance((int)(WIDTH * 0.8), (int)(WIDTH * 0.2), 
				Image.SCALE_SMOOTH))),BorderLayout.NORTH);
		addProducts(images);
	}
	
	private void addProducts(ConcurrentHashMap<String, BufferedImage> images) {
		int number = 1;
		JPanel labelsPanel = new JPanel(new GridLayout(2,4, 10, 10));
		labelsPanel.setBackground(Color.WHITE);
		for(String key : images.keySet()) {
			labelsPanel.add(createLabel(key, images.get(key), number));
			number++;
		}
		add(labelsPanel, BorderLayout.CENTER);
	}
	
	private JLabel createLabel(String name, BufferedImage image, int number) {
		JLabel generalLb = new JLabel((new ImageIcon(new ImageIcon(image).getImage().getScaledInstance((int)(WIDTH * 0.28),
				(int)(HEIGHT * 0.19), Image.SCALE_SMOOTH))));
		generalLb.setBackground(Color.WHITE);
		generalLb.setFont(new Font("Arial", Font.BOLD, 20));
		generalLb.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.DARK_GRAY, 2, true), String.valueOf(number)));
		return generalLb;
	}
}
