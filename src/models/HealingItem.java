package models;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class HealingItem {
	
	private static final String IMAGES_PATH = "src/resources/healingItemImg/";
	public static final int SIZE = 60;
	private int x;
	private int y;
	private BufferedImage actualImage;
	private ArrayList<BufferedImage> itemImages;
	private Rectangle2D rectangle;
	private MyThread thread;
	
	public HealingItem(int x, int y) throws IOException {
		this.x = x;
		this.y = y;
		rectangle = new Rectangle2D.Double(x, y, SIZE, SIZE);
		readItemImages(new File(IMAGES_PATH));
		actualImage = itemImages.get(0);
		moveItem();
	}
	
	private void readItemImages(File directory) throws IOException {
		itemImages = new ArrayList<BufferedImage>();
		for (File file : directory.listFiles()) {
			BufferedImage image = ImageIO.read(file);
			itemImages.add(image);
		}
	}
	
	private void moveItem() {
		thread = new MyThread(500) {
			
			@Override
			void executeTask() {
				for(BufferedImage image : itemImages) {
					actualImage = image;
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};
		thread.start();
	}
	
	public void pauseThread() {
		thread.pause();
	}
	
	public void stopThread() {
		thread.stop();
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public Rectangle2D getRectangle() {
		return rectangle;
	}
	
	public BufferedImage getActualImage() {
		return actualImage;
	}
}
