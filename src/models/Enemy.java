package models;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Enemy {
	
	private static final String PATH_ENEMY_HURT_LEFT_IMG = "src/resources/enemyImg/hurtLeft/";
	private static final String PATH_ENEMY_HURT_RIGHT_IMG = "src/resources/enemyImg/hurt/";
	private static final String PATH_ENEMY_WALK_LEFT_IMG = "src/resources/enemyImg/walkLeft/";
	private static final String PATH_ENEMY_WALK_RIGHT_IMG = "src/resources/enemyImg/walk/";
	private volatile boolean right;
	private volatile boolean left;
	private volatile boolean hurt;
	private volatile int x;
	private volatile int y;
	private int speedX;
	private volatile int health;
	public static final int WIDTH = (int)(Constants.WINDOW_WIDTH * 0.04);
	public static final int HEIGHT = (int)(Constants.WINDOW_HEIGHT * 0.08);
	private ArrayList<BufferedImage> imagesWalkRight;
	private ArrayList<BufferedImage> imagesWalkLeft;
	private ArrayList<BufferedImage> imagesHurtRight;
	private ArrayList<BufferedImage> imagesHurtLeft;
	private volatile BufferedImage actualImage;
	
	public Enemy(int x, int y) throws IOException {
		this.x = x;
		this.y = y;
		speedX = 8;
		health = 100;
		right = true;		
		imagesWalkRight = new ArrayList<BufferedImage>();
		loadImagesWalkRight(new File(PATH_ENEMY_WALK_RIGHT_IMG));
		imagesWalkLeft = new ArrayList<BufferedImage>();
		loadImagesWalkLeft(new File(PATH_ENEMY_WALK_LEFT_IMG));
		imagesHurtRight = new ArrayList<BufferedImage>();
		loadImagesHurtRight( new File(PATH_ENEMY_HURT_RIGHT_IMG));
		imagesHurtLeft = new ArrayList<BufferedImage>();
		loadImagesHurtLeft(new File(PATH_ENEMY_HURT_LEFT_IMG));
		actualImage = imagesWalkRight.get(0);
	}
	
	public Enemy(Long x, Long y, Long health, boolean right, boolean left) throws IOException {
		this.x = x.intValue();
		this.y = y.intValue();
		speedX = 8;
		this.health = health.intValue();
		this.right = right;
		this.left = left;
		imagesWalkRight = new ArrayList<BufferedImage>();
		loadImagesWalkRight(new File(PATH_ENEMY_WALK_RIGHT_IMG));
		imagesWalkLeft = new ArrayList<BufferedImage>();
		loadImagesWalkLeft(new File(PATH_ENEMY_WALK_LEFT_IMG));
		imagesHurtRight = new ArrayList<BufferedImage>();
		loadImagesHurtRight(new File(PATH_ENEMY_HURT_RIGHT_IMG));
		imagesHurtLeft = new ArrayList<BufferedImage>();
		loadImagesHurtLeft(new File(PATH_ENEMY_HURT_LEFT_IMG));
		actualImage = imagesWalkRight.get(0);
	}
	
	public void loadImagesWalkRight(File directory) throws IOException {
		for (File file : directory.listFiles()) {
			BufferedImage image = ImageIO.read(file);
			imagesWalkRight.add(image);
		}
	}
	
	public void loadImagesWalkLeft(File directory) throws IOException {
		for (File file : directory.listFiles()) {
			BufferedImage image = ImageIO.read(file);
			imagesWalkLeft.add(image);
		}
	}
	
	public void loadImagesHurtRight(File directory) throws IOException {
		for (File file : directory.listFiles()) {
			BufferedImage image = ImageIO.read(file);
			imagesHurtRight.add(image);
		}
	}
	
	public void loadImagesHurtLeft(File directory) throws IOException {
		for (File file : directory.listFiles()) {
			BufferedImage image = ImageIO.read(file);
			imagesHurtLeft.add(image);
		}
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getHealth() {
		return health;
	}
	
	public void walk() {
		if(right) {
			walkRight();
		}else {
			walkLeft();
		}
	}
	
	public void walkRight() {
		right = true;
		left = false;
		if(!hurt) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					for (BufferedImage image : imagesWalkRight) {
						actualImage = image;
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					verifyXRight();
				}
			}).start();
		}
	}
	
	public void verifyXRight() {
		if(x < Constants.WINDOW_WIDTH - WIDTH) {
			 if((x >= Constants.WINDOW_WIDTH * 0.8) && y <= Constants.WINDOW_HEIGHT * 0.6){
				x = (int)(Constants.WINDOW_WIDTH * 0.8 - 2);
				setLeft();
			}else {
				x += speedX;
			}
		}else {
			x = (int)(Constants.WINDOW_WIDTH - WIDTH) - 2;
			setLeft();
		}
	}
	
	public void walkLeft() {
		left = true;
		right = false;
		if(!hurt) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					for (BufferedImage image : imagesWalkLeft) {
						actualImage = image;
						try {
							Thread.sleep(20);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					verifyXLeft();
				}
			}).start();
		}
	}
	
	public void verifyXLeft() {
		if(x >= 0) {
			 if((x <= Constants.WINDOW_WIDTH * 0.2) && y <= Constants.WINDOW_HEIGHT * 0.6){
				 x = (int)(Constants.WINDOW_WIDTH * 0.2) + 2;
				 setRight();
			 }else {
				 x -= speedX;
			 }
		}else {
			x = 2;
			setRight();
		}
	}
	
	public boolean getRight() {
		return  right;
	}
	
	public boolean getLeft() {
		return  left;
	}
	
	public void setRight() {
		right = true;
		left = false;
	}
	
	public void setLeft() {
		left = true;
		right = false;
	}
	
	public void hurt() {
		if(right) {
			hurtRight();
		}else {
			hurtLeft();
		}
	}
	
	public void hurtRight() {
		if(hurt) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(BufferedImage image : imagesHurtRight) {
					actualImage = image;
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				health -= 10;
				hurt = false;
			}
		}).start();
		}
	}
	
	public void hurtLeft() {
		if(hurt) {
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					for(BufferedImage image : imagesHurtLeft) {
						actualImage = image;
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					health -= 10;
					hurt = false;
				}
			}).start();
		}
	}
	
	public BufferedImage getActualImage() {
		return actualImage;
	}
	
	public void setHurt() {
		hurt = true;
	}
}
