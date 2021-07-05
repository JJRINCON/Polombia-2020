package models;

public class Antenna {
	
	public static final int WITDTH = (int)(Constants.WINDOW_WIDTH * 0.1);
	public static final int HEIGHT = (int)(Constants.WINDOW_HEIGHT * 0.3);
	private volatile int x;
	private volatile int Y;
	
	public Antenna(int x, int y) {
		this.x = x;
		this.Y = y;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return Y;
	}
}
