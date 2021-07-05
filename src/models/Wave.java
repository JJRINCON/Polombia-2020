package models;

import java.awt.geom.Arc2D;

public class Wave{
	
	public static final int WIDTH = 150;
	public static final int HEIGHT = 150;
	private volatile int x;
	private volatile int y;
	private int start;
	private int extent;
	private Arc2D arc2d;
	private boolean location;
	
	
	public Wave(int x, int y, int start, int extent, boolean location) {
		this.x = x;
		this.y = y;
		this.start = start;
		this.extent = extent;
		this.location = location;
		arc2d = new Arc2D.Double(x, y, WIDTH, HEIGHT, start, extent, Arc2D.OPEN);
	}
	
	public Wave(Long x, Long y, Long start, Long extent, boolean location) {
		this.x = x.intValue();
		this.y = y.intValue();
		this.start = start.intValue();
		this.extent = extent.intValue();
		this.location = location;
		arc2d = new Arc2D.Double(x, y, WIDTH, HEIGHT, start, extent, Arc2D.OPEN);
	}
	
	//direction left:falase,  right:true
	public void move() {
		if(location) {
			moveLeft();
			arc2d.setFrame(x,y,WIDTH, HEIGHT);
		}else {
			moveRight();
			arc2d.setFrame(x,y,WIDTH, HEIGHT);
		}
	}
	
	private void moveRight() {
		x += 3;
		y += 3;
	}
	
	private void moveLeft() {
		x -= 3;
		y += 3;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getStart() {
		return start;
	}
	
	public int getExtent() {
		return extent;
	}

	public Arc2D getArc2d() {
		return arc2d;
	}
	
	public boolean getLocation() {
		return location;
	}
}
