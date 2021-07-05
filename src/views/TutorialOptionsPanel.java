package views;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import models.Constants;
import presenters.Commands;

public class TutorialOptionsPanel extends JPanel{

	private static final long serialVersionUID = 1L;
	
	public TutorialOptionsPanel(ActionListener listener) {
		setLayout(new GridLayout(3,5));
		setBackground(Color.WHITE);
		add(new JLabel());
		add(new JLabel());
		add(new JLabel());
		add(new JLabel());
		add(new JLabel());
		add(new JLabel());
		add(createButton("/resources/continue.png",Constants.TXT_CONTINUE_BTN_TUTORIAL,Color.RED, Color.WHITE, listener));
		add(new JLabel());
		add(createButton("/resources/tutorial.png",Constants.TXT_TUTORIAL_BTN, Color.YELLOW, Color.BLACK, listener));
		add(new JLabel());
		add(new JLabel());
		add(createLb(Constants.TXT_CONTINUE_BTN_TUTORIAL));
		add(new JLabel());
		add(createLb(Constants.TXT_TUTORIAL_BTN));
	}

	private JButton createButton(String iconPath,String txt, Color color, Color foreground ,ActionListener listener) {
		JButton generalBtn = new JButton(new ImageIcon(getClass().getResource(iconPath)));
		generalBtn.setName(txt);
		generalBtn.setHorizontalTextPosition( SwingConstants.CENTER );
		generalBtn.setVerticalTextPosition( SwingConstants.BOTTOM );
		generalBtn.addActionListener(listener);
		generalBtn.setActionCommand(Commands.TUTORIAL_OPTIONS.toString());
		generalBtn.setBackground(color);
		generalBtn.setForeground(foreground);
		return generalBtn;
	}
	
	private JLabel createLb(String txt) {
		JLabel generalLb = new JLabel(txt);
		generalLb.setHorizontalAlignment(SwingConstants.CENTER);
		generalLb.setFont(new Font("Arial", Font.BOLD, 30));
		generalLb.setVerticalAlignment(SwingConstants.TOP);
		return generalLb;
	}
}
