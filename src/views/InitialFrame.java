package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;


public class InitialFrame extends JFrame{

	private static final long serialVersionUID = 1L;
	private static final String APP_ICON_PATH = "/resources/icono.png";
	private initialPanel initialPanel;
	private HistoryPanel historyPanel;
	private TutorialPanel tutorialPanel;
	private TutorialOptionsPanel optionsPanel;
	
	public InitialFrame(ActionListener listener) throws IOException {
		BufferedImage appIcon = ImageIO.read(getClass().getResource(APP_ICON_PATH));
		setIconImage(appIcon);
		setUndecorated(true);
		setBackground(Color.DARK_GRAY);
		setLayout(new BorderLayout());
		setExtendedState(MAXIMIZED_BOTH);
		initialPanel = new initialPanel(listener);
		historyPanel = new HistoryPanel();
		tutorialPanel = new TutorialPanel();
		add(tutorialPanel);
		optionsPanel = new TutorialOptionsPanel(listener);
		
	}
	
	public void initInitialPanel() {
		initialPanel.setVisible(false);
		historyPanel.setVisible(false);
		optionsPanel.setVisible(false);
		tutorialPanel.setVisible(false);
	    add(initialPanel);
		initialPanel.setVisible(true);
	}
	
	public void initPanelHistory(String[] history) {
		initialPanel.setVisible(false);
		add(historyPanel);
		historyPanel.initAnimation(history);
		historyPanel.setVisible(true);
	}
	
	public void initTutorialPanel() {
		initialPanel.setVisible(false);
		historyPanel.setVisible(false);
		optionsPanel.setVisible(false);
		add(tutorialPanel);
		tutorialPanel.setVisible(true);
		tutorialPanel.initAnimation();
	}
	
	public void initOptionsPanel() {
		initialPanel.setVisible(false);
		historyPanel.setVisible(false);
		add(optionsPanel);
		optionsPanel.setVisible(true);
	}
	
	public boolean getFinish() {
		return historyPanel.getFinish();
	}
	
	public boolean getFinishTutorial() {
		return tutorialPanel.getFinishTutorial();
	}
}
