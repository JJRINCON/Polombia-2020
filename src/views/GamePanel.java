package views;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import models.Antenna;
import models.Character;
import models.Constants;
import models.Enemy;
import models.Game;
import models.HealingItem;
import models.Shoot;

public class GamePanel extends JPanel{

	private static final long serialVersionUID = 1L;
	private static final long WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
	private static final long HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
	
	private Game game;
	private BufferedImage backgroundImage;
	private BufferedImage asphaltImage;
	private BufferedImage antennaImage;
	private BufferedImage alkostoImage;
	private BufferedImage homeImage;
	private BufferedImage healthImage;
	private BufferedImage shoppingCart;
	private BufferedImage shoppingCartLeft;
	private BufferedImage actualScreenshot;
	private BufferedImage saveGameImage;
	private boolean isCapturing;
	
	
	public GamePanel(Game game, KeyListener keyListener) {
		this.addKeyListener(keyListener);
		this.setFocusable(true);
		this.game = game;
		try {
			actualScreenshot = null;
			backgroundImage = ImageIO.read(getClass().getResource("/resources/background.jpg"));
			asphaltImage = ImageIO.read(getClass().getResource("/resources/asfalto.jpg"));
			antennaImage = ImageIO.read(getClass().getResource("/resources/antena.png"));
			alkostoImage = ImageIO.read(getClass().getResource("/resources/alkostoB.png"));
			homeImage = ImageIO.read(getClass().getResource("/resources/home.png"));
			healthImage = ImageIO.read(getClass().getResource("/resources/halthBar.png"));
			shoppingCart = ImageIO.read(getClass().getResource("/resources/carrito_compras.png"));
			shoppingCartLeft = ImageIO.read(getClass().getResource("/resources/carrito_compras_left.png"));
			saveGameImage = ImageIO.read(getClass().getResource("/resources/save_game_icon.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2 = (Graphics2D) g;
		paintBackground(g2);
		g2.setColor(Color.GREEN);
		paintAsphalt(g2);
		paintAntennas(g2);
		paintWaves(g2);
		paintAlkosto(g2);
		paintHome(g2);
		paintHero(g2);
		paintEnemies(g2);
		paintSelectedProduct(g2);
		paintShoots(g2);
		paintScreenShoot(g2);
		paintSaveGame(g2);
		paintHealingItem(g2);
		Toolkit.getDefaultToolkit().sync();
	}
	
	public void paintBackground(Graphics2D g2) {
		g2.drawImage(backgroundImage, 0, 0, getWidth(), (int)(getHeight()/1.8), this);
	}
	
	public void paintAsphalt(Graphics2D g2) {
		g2.drawImage(asphaltImage,0, (int)(HEIGHT * 0.5), (int)WIDTH, (int)(HEIGHT * 0.5), this);
	}
	
	public void paintHero(Graphics2D g2) {
		g2.setColor(Color.BLACK);
		g2.drawImage(game.getCharacter().getActualImage(), game.getCharacter().getX(), game.getCharacter().getY(),
				Character.WIDTH, Character.HEIGHT, this);
		paintHeroHealthBar(g2);
	}
	
	private void paintHeroHealthBar(Graphics2D g2) {
		g2.setColor(verifyHealthBarColor(game.getCharacter().getHealth()));
		g2.drawImage(healthImage, game.getCharacter().getX() -15, game.getCharacter().getY() - 20, 110, 24, this);
		g2.fillRect(game.getCharacter().getX() - 10, game.getCharacter().getY() - 17, game.getCharacter().getHealth(), 18);
	}

	public void paintEnemies(Graphics2D g2) {
		for(Integer enemiKey : game.getEnemies().keySet()) {
			paintEnemyHealthBar(g2, game.getEnemies().get(enemiKey));
			g2.drawImage(game.getEnemies().get(enemiKey).getActualImage(),game.getEnemies().get(enemiKey).getX(), 
					game.getEnemies().get(enemiKey).getY(),Enemy.WIDTH, Enemy.HEIGHT, this);
		}
	}
	
	private void paintEnemyHealthBar(Graphics2D g2, Enemy enemy) {
		g2.setColor(verifyHealthBarColor(enemy.getHealth()));
		g2.drawImage(healthImage, enemy.getX() -15, enemy.getY() - 40, 110, 24, this);
		g2.fillRect(enemy.getX() - 10, enemy.getY() - 37, enemy.getHealth(), 18);
	}
	
	public Color verifyHealthBarColor(int health) {
		if(health >= 60) {
			return Color.GREEN;
		}else if(health >= 20 && health < 60) {
			return Color.ORANGE;
		}else if(health < 20) {
			return Color.RED;
		}
		return null;
	}
	
	private void paintShoots(Graphics2D g2) {
		if(!game.getShoots().isEmpty()) {
			for(Integer key : game.getShoots().keySet()) {
				g2.setColor(Color.CYAN);
				g2.fillRect(game.getShoots().get(key).getX(), game.getShoots().get(key).getY(),
						Shoot.WIDTH, Shoot.HEIGHT);
			}
		}
	}
	
	private void paintAntennas(Graphics2D g2) {
		g2.drawImage(antennaImage, game.getAntennas().get(1).getX(), game.getAntennas().get(1).getY(), Antenna.WITDTH,
				Antenna.HEIGHT, this);
		g2.drawImage(antennaImage, game.getAntennas().get(2).getX(), game.getAntennas().get(2).getY(), Antenna.WITDTH,
				Antenna.HEIGHT, this);
	}
	
	private void paintWaves(Graphics2D g2) {
		g2.setColor(Color.YELLOW);
		g2.setStroke(new BasicStroke(2));
		for(Integer key : game.getWaves().keySet()) {
			g2.draw(game.getWaves().get(key).getArc2d());
		}
		g2.setStroke(new BasicStroke(1));
	}
	
	public void paintAlkosto(Graphics2D g2) {
		g2.drawImage(alkostoImage, (int)(Constants.WINDOW_WIDTH * 0.8), (int)(Constants.WINDOW_HEIGHT * 0.3), 
				(int)(Constants.WINDOW_WIDTH * 0.2), (int)(Constants.WINDOW_HEIGHT * 0.3), this);
	}
	
	public void paintHome(Graphics2D g2) {
		g2.drawImage(homeImage, 0, (int)(Constants.WINDOW_HEIGHT * 0.3), (int)(Constants.WINDOW_WIDTH * 0.2),
				(int)(Constants.WINDOW_HEIGHT * 0.3), this);
	}
	
	public void paintSelectedProduct(Graphics2D g2) {
		if(game.getSelectedProduct() != null &&  game.getCharacter().getRight()) {
			paintSelectedProductRight(g2);
		}else if(game.getSelectedProduct() != null && game.getCharacter().getLeft()) {
			paintSelectedProductLeft(g2);
		}
	}
	
	private void paintSelectedProductRight(Graphics2D g2) {
		g2.drawImage(game.getImageSelectedProduct(), game.getCharacter().getX() + (int)(Character.WIDTH * 1.1) ,
				game.getCharacter().getY() + (int)(Character.HEIGHT * 0.3),(int)(Character.WIDTH * 0.9) , Character.HEIGHT / 2, this);
		g2.drawImage(shoppingCart, game.getCharacter().getX() + (int)(Character.WIDTH * 0.8), game.getCharacter().getY() + (Character.HEIGHT / 2), 
				(int)(Character.WIDTH * 1.2), Character.HEIGHT / 2, this);
	}
	
	private void paintSelectedProductLeft(Graphics2D g2) {
		g2.drawImage(game.getImageSelectedProduct(), game.getCharacter().getX() - (int)(Character.WIDTH * 0.8) ,
				game.getCharacter().getY() + (int)(Character.HEIGHT * 0.3),(int)(Character.WIDTH * 0.9) , Character.HEIGHT / 2, this);
		g2.drawImage(shoppingCartLeft, game.getCharacter().getX() - (int)(Character.WIDTH * 0.8), game.getCharacter().getY() + (Character.HEIGHT / 2), 
				(int)(Character.WIDTH * 1.2), Character.HEIGHT / 2, this);
	}
	
	private void paintScreenShoot(Graphics2D g2) {
		if(actualScreenshot != null && isCapturing) {
			g2.setColor(Color.BLACK);
			g2.drawImage(actualScreenshot,40, 20, 180, 140, this);
			g2.drawRect(40, 20, 180, 140);
		}
	}
	
	private void paintSaveGame(Graphics2D g2) {
		if(isCapturing) {
			g2.drawImage(saveGameImage, (int)(getWidth() * 0.9), 30, 60, 50, this);
		}
	}
	
	private void paintHealingItem(Graphics2D g2) {
		if(game.getHealingItem() != null) {
			g2.drawImage(game.getHealingItem().getActualImage(), game.getHealingItem().getX(), game.getHealingItem().getY() ,
					HealingItem.SIZE, HealingItem.SIZE, this);
		}
	}
	
	public void setIsCapturing(boolean isCapturing) {
		this.isCapturing = isCapturing;
	}
	
	public void setActualScreenshot(BufferedImage actualScreenshot) {
		this.actualScreenshot = actualScreenshot;
	}
	
	public boolean getIsCapturing() {
		return isCapturing;
	}
	
	public void update(Game game) {
		this.game = game;
		repaint();
	}
}
