package ecos.simulation;
/*
 * 
 * 			Food Section
 * 
 * This is the Food Class.
 * The food will respawn randomly in the forest.
 * 
 * */

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import processing.core.PVector;

public class Food {
//	private ArrayList<Food> foods;
//	public Food[] foods;
	private Ellipse2D.Double firstDot;
	private Ellipse2D.Double secDot;
	private Ellipse2D.Double thiDot;
	private Rectangle2D.Double cShape;
	private Area dBox;
	
	private PVector pos;
	private int bodyW, bodyH;
	private double scale;
	private Color c;
	
	public Food(PVector pos, int w, int h) {
		this.pos = pos;
		this.bodyH = h;
		this.bodyW = w;
		this.scale = 1;
		this.c = Color.orange;
		
//		firstDot = new Ellipse2D.Double();
//		secDot = new Ellipse2D.Double();
//		thiDot = new Ellipse2D.Double();
//		cShape = new Rectangle2D.Double();
		setCheeseAttributes();
	}
	
	public void setCheeseAttributes() {
		cShape = new Rectangle2D.Double(-bodyW/2, -bodyH, bodyW, bodyH);
		firstDot = new Ellipse2D.Double((-bodyW/3), (-bodyH/1.5), bodyW/3, bodyH/3);
		secDot = new Ellipse2D.Double((bodyW/6),(-bodyH/1.5), bodyW/5, bodyH/5);
		thiDot = new Ellipse2D.Double((bodyW/7),(-bodyH/2.8), bodyW/3, bodyH/3);
		dBox = new Area(cShape);
	}
	public void render(Graphics2D g2) {
		AffineTransform af = g2.getTransform();
		g2.translate(pos.x, pos.y);
		g2.scale(scale, scale);
		g2.setColor(c);
		g2.fill(cShape);
		g2.setColor(new Color(150,64,0));
		g2.fill(firstDot);
		g2.fill(secDot);
		g2.fill(thiDot);
		g2.setTransform(af);
	}
	
//	public boolean getHit(Rabbit other) {
//		return (Math.abs(pos.x-other.getPosX()) < (bodyW/2 * scale+other.getBodyW()/2) && Math.abs(other.getPosY() - pos.y) < (bodyH/2 *scale +other.getBodyH()/2));
//	}
	
	protected Shape getOutline() {
		AffineTransform at = new AffineTransform();		
		at.translate(pos.x, pos.y);
		at.scale(scale, scale);
		return at.createTransformedShape(dBox);
	}
	
	public Shape getBoundary() {
		AffineTransform at = new AffineTransform();		
		at.translate(pos.x, pos.y);
		at.scale(scale, scale);
		return at.createTransformedShape(dBox);
	}
	
	public int getBodyH() {
		return bodyH;
	}
	public int getBodyW() {
		return bodyW;
	}
	
	public PVector getPos() {
		return pos;
	}
	public float getPosX() {
		return pos.x;
	}
	public float getPosY() {
		return pos.y;
	}
	public double getScale() {
		return scale;
	}
	public void setScale(double sc) {
		scale = sc;
	}
	
	public void setColor(Color color) {
		this.c = color;
	}
	
	public boolean checkHit(MouseEvent e) {
		return getBoundary().contains(e.getX(), e.getY());
	}

	public Rectangle2D getBoundingBox() {
		return getOutline().getBounds2D();
	}
	
	
}
