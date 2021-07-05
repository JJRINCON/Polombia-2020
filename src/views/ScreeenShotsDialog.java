package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;

import models.Constants;
import presenters.Commands;

public class ScreeenShotsDialog extends JDialog{

	private static final long serialVersionUID = 1L;
	
	public ScreeenShotsDialog(ArrayList<BufferedImage> screenShots, ActionListener listener) {
		setLayout(new BorderLayout());
		setUndecorated(true);
		setSize(600,600);
		setLocationRelativeTo(null);
		initImagesPanel(screenShots);
		add(createBtn(listener, Constants.TXT_EXIT_SCREENSHOT_DIALOG, Color.BLUE, Color.WHITE), BorderLayout.SOUTH);
	}
	
	private void initImagesPanel(ArrayList<BufferedImage> screenShots) {
		JPanel imagesPanel = new JPanel(new GridLayout(3, screenShots.size() / 3));
		imagesPanel.setBackground(Color.decode("#323131"));
		for(BufferedImage image : screenShots) {
			JLabel imageLb = new JLabel(new ImageIcon(new ImageIcon(image).getImage().getScaledInstance(190, 190, Image.SCALE_SMOOTH)));
			imageLb.setBorder(new LineBorder(Color.WHITE, 2, true));
			imagesPanel.add(imageLb);
		}
		add(new JScrollPane(imagesPanel), BorderLayout.CENTER);
	}
	
	private JButton createBtn(ActionListener listener, String txt, Color color, Color foreground) {
		JButton generalBtn = new JButton(txt);
		generalBtn.setBackground(color);
		generalBtn.setForeground(foreground);
		generalBtn.addActionListener(listener);
		generalBtn.setActionCommand(Commands.SCREENSHOTS_OPTION.toString());
		return generalBtn;
	}
}
