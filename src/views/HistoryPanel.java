package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.border.LineBorder;

import models.Game;

public class HistoryPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private static final String PATH_HISTORY_IMAGES = "src/resources/historyImg/";
	private JLabel lbHistory;
	private ArrayList<BufferedImage> historyImages;
	private BufferedImage actualImage;
	private SwingWorker<Void, Void> worker;
	private boolean finish;
	
	public HistoryPanel() throws IOException {
		historyImages = Game.readHistoryImages(new File(PATH_HISTORY_IMAGES));
		actualImage = historyImages.get(0);
		setLayout(new BorderLayout());
		addHistoryLb();
	}
	
	private void addHistoryLb() {
		JPanel panel = new JPanel();
		lbHistory = new JLabel();
		lbHistory.setForeground(Color.WHITE);
		lbHistory.setFont(new Font("Arial", Font.ITALIC, 30));
		lbHistory.setHorizontalAlignment(SwingConstants.CENTER);
		lbHistory.setHorizontalTextPosition(SwingConstants.CENTER);
		panel.add(lbHistory);
		panel.setBackground(Color.DARK_GRAY);
		panel.setForeground(Color.WHITE);
		panel.setBorder(new LineBorder(Color.YELLOW, 3, true));
		add(panel, BorderLayout.SOUTH);
	}
	
	public void initAnimation(String[] history) {
		worker = new SwingWorker<Void, Void>() {
			@Override
			protected Void doInBackground() throws Exception {
				for (int i = 0; i < history.length; i++) {
					actualImage = historyImages.get(i);
					repaint();
					lbHistory.setText(history[i]);
					Thread.sleep(2500);
				}
				finish = true;
				return null;
			}
		};
		worker.execute();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g;
		g2.drawImage(actualImage, 0, 0, getWidth(), getHeight(), this);
	}
	
	public boolean getFinish(){
		return finish;
	}
}
