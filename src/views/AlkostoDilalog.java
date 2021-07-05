package views;

import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.JDialog;

import models.Constants;

public class AlkostoDilalog extends JDialog{
	
	private static final long serialVersionUID = 1L;
	
	public AlkostoDilalog(ConcurrentHashMap<String, BufferedImage> images,KeyListener keyListener) throws IOException {
		setFocusable(true);
		addKeyListener(keyListener);
		setUndecorated(true);
		setSize((int)(Constants.WINDOW_WIDTH * 0.3), (int)(Constants.WINDOW_HEIGHT * 0.4));
		setLocation((int)(Constants.WINDOW_WIDTH * 0.7), (int)(Constants.WINDOW_HEIGHT * 0.07));
		add(new AlkostoPanel(images, keyListener));
	}
}
