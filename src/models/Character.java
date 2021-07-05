package models;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class Character {
	
	private static final String PATH_WALK_DEAD_IMG = "src/resources/heroImg/dead/";
	private static final String PATH_WALK_LEFT_IMG = "src/resources/heroImg/walkLeft/";
	private static final String PATH_WALK_RIGHT_IMG = "src/resources/heroImg/walk/";
	public static final int WIDTH = 70;
	public static final int HEIGHT = 110;
	private volatile int x;
	private volatile int  y;
	private int speedX;
	private int speedY;
	private boolean right;
	private boolean left;
	private boolean up;
	private boolean down;
	private boolean alive;
	private volatile int health;
	private ArrayList<BufferedImage> walkRightImages;
	private ArrayList<BufferedImage> walkLeftImages;
	private ArrayList<BufferedImage> deadImages;
	private volatile BufferedImage actualImage;
	private Rectangle2D rectangle2d;
	
	public Character(int x, int y) throws IOException {
		right = true;
		alive = true;
		health = 100;
		speedX = 7;
		speedY = 6;
		this.x = x;
		this.y = y;
		rectangle2d = new Rectangle2D.Double(x, y, WIDTH, HEIGHT);
		readWalkRightImages( new File(PATH_WALK_RIGHT_IMG));
		readWalkLeftImages(new File(PATH_WALK_LEFT_IMG));
		readDeadImages(new File(PATH_WALK_DEAD_IMG));
		actualImage = walkRightImages.get(0);
	}
	
	public Character(Long x, Long y, Long health, Long speedX, Long speedY,boolean right, boolean left, 
			boolean up, boolean down) throws IOException {
		alive = true;
		this.x = x.intValue();
		this.y = y.intValue();
		this.health = health.intValue();
		System.out.println(this.health);
		this.speedX = speedX.intValue();
		this.speedY = speedY.intValue();
		this.right = right;
		this.left = left;
		this.up = up;
		this.down = down;
		rectangle2d = new Rectangle2D.Double(x, y, WIDTH, HEIGHT);
		readWalkRightImages(new File(PATH_WALK_RIGHT_IMG));
		readWalkLeftImages(new File(PATH_WALK_LEFT_IMG));
		readDeadImages(new File(PATH_WALK_DEAD_IMG));
		actualImage = walkRightImages.get(0);
	}
	
	public void readWalkRightImages(File directory) throws IOException {
		walkRightImages = new ArrayList<BufferedImage>();
		for (File file : directory.listFiles()) {
			BufferedImage image = ImageIO.read(file);
			walkRightImages.add(image);
		}
	}
	
	public void readWalkLeftImages(File directory) throws IOException {
		walkLeftImages = new ArrayList<BufferedImage>();
		for (File file : directory.listFiles()) {
			BufferedImage image = ImageIO.read(file);
			walkLeftImages.add(image);
		}
	}
	
	public void readDeadImages(File directory) throws IOException {
		deadImages = new ArrayList<BufferedImage>();
		for(File file : directory.listFiles()) {
			BufferedImage image = ImageIO.read(file);
			deadImages.add(image);
		}
	}
	
	public void walkRight() {
		if(alive) {
			right = true;
			left = false;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					for (BufferedImage image : walkRightImages) {
						actualImage = image;
						try {
							Thread.sleep(12);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					verifyXLimits();
				}
			}).start();
		}
	}
	
	public void walkLeft() {
		if(alive) {
			left = true;
			right = false;
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					for (BufferedImage image : walkLeftImages) {
						actualImage = image;
						try {
							Thread.sleep(12);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					verifyXLimits();
				}
			}).start();
		}
	}
	
	public void walkUp(){
		if(alive) {
			up = true;
			down = false;
			if(right) {
				walkUpRight();
			}else if(left) {
				walkUpLeft();
			}
		}
	}
	
	public void walkUpRight() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for (BufferedImage image : walkRightImages) {
					actualImage = image;
					try {
						Thread.sleep(12);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				verifyYLimits();
			}
		}).start();
	}
	
	public void walkUpLeft() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for (BufferedImage image : walkLeftImages) {
					actualImage = image;
					try {
						Thread.sleep(12);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				verifyYLimits();
			}
		}).start();
	}
	
	public void walkDown() {
		if(alive) {
			down = true;
			up = false;
			if(right) {
				walkDownRight();
			}else if(left) {
				walkDownLeft();
			}
		}
	}
	
	public void walkDownRight() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for (BufferedImage image : walkRightImages) {
					actualImage = image;
					try {
						Thread.sleep(12);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				verifyYLimits();
			}
		}).start();
	}

	public void walkDownLeft() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for (BufferedImage image : walkLeftImages) {
					actualImage = image;
					try {
						Thread.sleep(12);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				verifyYLimits();
			}
		}).start();
	}
	
	public void verifyYLimits() {
		if(y > Constants.WINDOW_HEIGHT - HEIGHT) {
			y = (int)(Constants.WINDOW_HEIGHT - HEIGHT) - 2;
		}else if(y < (Constants.WINDOW_HEIGHT * 0.52) - HEIGHT) {
			y = (int)(Constants.WINDOW_HEIGHT * 0.52 - HEIGHT) + 2;
		}else {
			if(up) {
				y -= speedY;
			}else {
				y += speedY;
			}
		}
	}
	
	public void verifyXLimits() {
		if(x < 0) {
			x = 2;
		}else if(x > Constants.WINDOW_WIDTH - WIDTH) {
			x = (int) ((Constants.WINDOW_WIDTH - WIDTH) - 2);
		}else {
			if(right) {
				x += speedX;
			}else {
				x -= speedX;
			}
		}
	}
	
	public void dead() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				for(BufferedImage image : deadImages) {
					actualImage = image;
					try {
						Thread.sleep(20);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				alive = false;
			}
		}).start();
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
	
	public BufferedImage getActualImage() {
		return actualImage;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public Rectangle2D getRectangle2d() {
		return rectangle2d;
	}
	
	public void hurt(boolean right) {
		health -= 3;
		if(right) {
			x += speedX;
		}else{
			x -= speedX;
		}
	}
	
	public void waveHurt() {
		health -= 10;
	}
	
	public boolean getRight(){
		return right;
	}
	
	public boolean getLeft(){
		return left;
	}
	
	public void setRight() {
		this.right = true;
	}
	
	public void setLeft() {
		this.left = false;
	}
	
	public boolean getUp(){
		return up;
	}
	
	public boolean getDown() {
		return down;
	}
	
	public void setDead() {
		this.alive = false;
	}
	
	public boolean getAlive() {
		return alive;
	}
	
	public int getSpeedX() {
		return speedX;
	}
	
	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}
	
	public int getSpeedY() {
		return speedY;
	}
	
	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}
}
