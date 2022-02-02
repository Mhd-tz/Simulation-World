package ecos.simulation;
/*
 * 
 * 					CAT Class
 * 
 * 
 * * We use the cat class as a subclass of AnimalChar that inherits state and behavior in the form of variables and methods from its superclass.
 * * The subclass can just use the items inherited from its superclass as is, or the subclass can modify or override it.
 * * This will provide a project with high level of data hiding from user.
 * * It also allow us to add more functionality on top of the superClass methods.
 * */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import processing.core.PVector;
import random.Random;

public class Cat extends AnimalChar {
	
	//Fields and Properties
	
	private int rand;
	
	private Arc2D.Double lEar;
	private Arc2D.Double lEarIn;
	private Arc2D.Double rEar;
	private Arc2D.Double rEarIn;
	private Ellipse2D.Double head;
	private Ellipse2D.Double lEye;
	private Ellipse2D.Double rEye;
	private Ellipse2D.Double eyeDot;
	private Ellipse2D.Double eyeDotR;
	private Arc2D.Double nose;
	private Ellipse2D.Double body;
	private Rectangle2D.Double tail;
	private Ellipse2D.Double dot1;
	private Ellipse2D.Double dot2;
	private Ellipse2D.Double dot3;
	
	protected Area outline;
	protected Arc2D.Double fov;

	public Cat(PVector pos, PVector vel, int w, int h, float size, Dimension paneSize) {
		super(pos, vel, w, h, size, paneSize);
		rand = (int) Random.random(100);
	}

	@Override
	public void draw(Graphics2D g2) {
		AffineTransform at = g2.getTransform();
		g2.translate(pos.x, pos.y);
		g2.rotate(vel.heading());
		g2.scale(size, size);
		if (vel.x < 0) g2.scale(1, -1);
		g2.setColor(Color.black);
		g2.draw(tail);
		g2.draw(body);
		g2.setColor(Color.DARK_GRAY);
		g2.fill(body);
		g2.fill(tail);
		g2.fill(head);
		g2.setColor(Color.black);
		g2.draw(head);
		g2.fill(nose);
		g2.draw(lEar);
		g2.draw(rEar);
		g2.fill(rEye);
		g2.fill(lEye);
		g2.setColor(Color.DARK_GRAY);
		g2.fill(lEar);
		g2.fill(rEar);
		if (state == DYING) g2.setColor(Color.RED);
		else if(state == SICK) g2.setColor(Color.YELLOW);
		else g2.setColor(Color.white);
		g2.fill(eyeDot);
		g2.fill(eyeDotR);
		if(rand > 60) {
			g2.fill(dot1);	
		}
		if(rand < 50 || rand>60) {
			g2.fill(dot2);	
		}
		if(rand < 20 || rand > 60) {
			g2.fill(dot3);	
		}
		g2.setColor(new Color(255, 207, 221));
		g2.fill(lEarIn);
		g2.fill(rEarIn);
		g2.setTransform(at);
	}

	@Override
	protected void traceBestTarget(ArrayList<AnimalChar> fList) {
		if (fList.size()>0) {	
			// find 1st target
			AnimalChar target = fList.get(0);
			float distToTarget = PVector.dist(pos, target.getPos());
			
			// find the closer one
			for (AnimalChar f:fList) if (PVector.dist(pos, f.getPos()) < distToTarget) {
				target = f;
				distToTarget = PVector.dist(pos, target.getPos());
			}
			
			// make animal follow this target
			this.approach(target);
		}
	}
	
	
	public void attackHunter(MetalMouse h) {
		PVector path = PVector.sub(h.getPos(), pos);
		vel = path.limit(7);
	}

	@Override
	protected Food traceBestFood(ArrayList<Food> fList) {
		// No need for this
		return null;
	}

	@Override
	protected boolean eatable(AnimalChar food) {
		return (food instanceof Mouse);
	}

	@Override
	protected void setShapeAttributes() {
		head = new Ellipse2D.Double(-dim.width/2,-dim.height /2, dim.width, dim.height );
		lEar = new Arc2D.Double(-dim.width/4,-dim.height/2.5, dim.width/2, dim.height/3, 90, 180, Arc2D.PIE);
		lEarIn = new Arc2D.Double(-dim.width/6,-dim.height/3, dim.width/3, dim.height/5, 90, 180, Arc2D.PIE);
		rEar = new Arc2D.Double(-dim.width/4,-dim.height/8, dim.width/2, dim.height/3, 90, 180, Arc2D.PIE);
		rEarIn = new Arc2D.Double(-dim.width/6,-dim.height/9, dim.width/3, dim.height/5, 90, 180, Arc2D.PIE);
		rEye = new Ellipse2D.Double(dim.width/9,-dim.height /3, dim.width/4, dim.height/4);
		eyeDot = new Ellipse2D.Double(dim.width/5,-dim.height /3.5, dim.width/7, dim.height/7);
		lEye = new Ellipse2D.Double(dim.width/9,dim.height /9, dim.width/4, dim.height/4);
		eyeDotR = new Ellipse2D.Double(dim.width/5,dim.height /7, dim.width/7, dim.height/7);
		nose = new Arc2D.Double(dim.width/3, -dim.height/7, dim.width/3, dim.height/3, -90, 180, Arc2D.PIE);
		body = new Ellipse2D.Double(-dim.width*1.1,-dim.height /2.5, dim.width, dim.height/1.2);
		tail = new Rectangle2D.Double(-dim.width*1.3,-dim.height /7, dim.width/2, dim.height/5);
		dot1 = new Ellipse2D.Double(-dim.width/1.1, -dim.height/3.5, dim.width/5, dim.height/5);
		dot2 = new Ellipse2D.Double(-dim.width/1.2, dim.height/9, dim.width/5, dim.height/5);
		dot3 = new Ellipse2D.Double(-dim.width/1.5, -dim.height/7, dim.width/5, dim.height/5);
		
		float sight = dim.width * speedMag * 1f;
		fov = new Arc2D.Double(-sight, -sight, sight*2, sight*2, -55, 110, Arc2D.PIE);
	}

	@Override
	protected void setOutline() {
		outline = new Area(head);
		outline.add(new Area(nose));
		outline.add(new Area(body));
		outline.add(new Area(tail));
		outline.add(new Area(rEar));
		outline.add(new Area(lEar));
	}

	@Override
	protected Shape getOutline() {
		AffineTransform at = new AffineTransform();		
		at.translate(pos.x, pos.y);
		at.rotate(vel.heading());
		at.scale(size, size);
		return at.createTransformedShape(outline);
	}

	@Override
	protected Shape getFOV() {
	      AffineTransform at = new AffineTransform();
	      at.translate(pos.x, pos.y);
	      at.rotate(vel.heading());
	      at.scale(size, size);
	      return at.createTransformedShape(fov);
	}

	@Override
	protected boolean eatableFood(Food food) {
		return false;
	}

}
