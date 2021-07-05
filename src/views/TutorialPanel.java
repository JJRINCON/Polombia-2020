package views;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.SwingWorker;

import models.Game;

public class TutorialPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private static final String PATH_TUTORIAL_IMAGES = "src/resources/tutorialImg/";
	private ArrayList<BufferedImage> tutorialImages;
	private BufferedImage actualImage;
	private boolean finishTutorial;
	
	public TutorialPanel() throws IOException {
		tutorialImages = Game.readTutorialImages(new File(PATH_TUTORIAL_IMAGES));
		actualImage = tutorialImages.get(0);
	}
	
	public void initAnimation() {
		SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
			
			@Override
			protected Void doInBackground() throws Exception {
				for (int i = 0; i < tutorialImages.size(); i++) {
					actualImage = tutorialImages.get(i);
					repaint();
					Thread.sleep(6000);
				}
				finishTutorial = true;
				return null;
			}
		};
		worker.execute();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(actualImage, 0, 0, getWidth(), getHeight(), this);
	}
	
	public boolean getFinishTutorial() {
		return finishTutorial;
	}
}
