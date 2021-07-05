package models;

public class Shoot {
	
	private int x;
	private int y;
	public static final int WIDTH = 30; 
	public static final int HEIGHT = 10;
	private boolean inpacted;
	private boolean right;
	private boolean left;
	
	public Shoot(int x,int y) {
		this.x = x;
		this.y = y;
	}
	
	public Shoot(Long x, Long y) {
		this.x = x.intValue();
		this.y = y.intValue();
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void moveRight() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(!inpacted) {	
					x++;
					try {
						Thread.sleep(15);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	public void moveLeft() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(!inpacted) {
					x--;
					try {
						Thread.sleep(15);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	
	public void setInpact() {
		inpacted = true;
	}
	
	public void setRight() {
		right = true;
	}
	
	public boolean getRight() {
		return right;
	}
	
	public void setLeft() {
		left = true;
	}
	
	public boolean getLeft() {
		return left;
	}
}
