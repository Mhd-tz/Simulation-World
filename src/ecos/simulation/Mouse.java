package ecos.simulation;
/*
 * 
 * 								Mouse Class
 * 
 * 
 * * We use the mouse class as a subclass of AnimalChar that inherits state and behavior in the form of variables and methods from its superclass.
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
import java.util.ArrayList;
import processing.core.PVector;

public class Mouse extends AnimalChar {
	
	//Please open the block comment
	
	//Fields and Properties
	private Ellipse2D.Double lEar;
	private Ellipse2D.Double lEarIn;
	private Ellipse2D.Double rEar;
	private Ellipse2D.Double rEarIn;
	protected Ellipse2D.Double head;
	protected Ellipse2D.Double lEye;
	protected Ellipse2D.Double rEye;
	protected Ellipse2D.Double eyeDot;
	protected Ellipse2D.Double eyeDotR;
	protected Arc2D.Double nose;
	protected Ellipse2D.Double body;
	private Ellipse2D.Double tail;
	
	protected Area outline;
	protected Arc2D.Double fov;
	

	public Mouse(PVector pos, PVector vel, int w, int h, float size, Dimension paneSize) {
		super(pos, vel, w, h, size, paneSize);
		
	}
	
	@Override
	protected void setShapeAttributes() {
		body = new Ellipse2D.Double(-dim.width*1.1,-dim.height /2.5, dim.width, dim.height/1.2);
		tail = new Ellipse2D.Double(-dim.width*1.3,-dim.height /5, dim.width/2.5, dim.height/2.5);
		head = new Ellipse2D.Double(-dim.width/2,-dim.height /2, dim.width, dim.height );
		lEar = new Ellipse2D.Double(-dim.width,-dim.height /2, dim.width, dim.height/2);
		lEarIn = new Ellipse2D.Double(-dim.width/1.4,-dim.height /2.5, dim.width/1.5, dim.height/3);
		rEar = new Ellipse2D.Double(-dim.width,dim.height /9, dim.width, dim.height/2);
		rEarIn = new Ellipse2D.Double(-dim.width/1.4,dim.height /5, dim.width/1.5, dim.height/3);
		rEye = new Ellipse2D.Double(dim.width/9,-dim.height /3, dim.width/4, dim.height/4);
		eyeDot = new Ellipse2D.Double(dim.width/5,-dim.height /3.5, dim.width/7, dim.height/7);
		lEye = new Ellipse2D.Double(dim.width/9,dim.height /9, dim.width/4, dim.height/4);
		eyeDotR = new Ellipse2D.Double(dim.width/5,dim.height /7, dim.width/7, dim.height/7);
		nose = new Arc2D.Double(dim.width/3, -dim.height/7, dim.width/3, dim.height/3, -90, 180, Arc2D.PIE);
		
		float sight = dim.width * speedMag * 1.2f;
		fov = new Arc2D.Double(-sight, -sight, sight*2, sight*2, -55, 110, Arc2D.PIE);
		
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
	public void draw(Graphics2D g2) {
		AffineTransform at = g2.getTransform();
		g2.translate(pos.x, pos.y);
		g2.rotate(vel.heading());
		g2.scale(size, size);
		if (vel.x < 0) g2.scale(1, -1);
		g2.setColor(Color.black);
		g2.draw(tail);
		if (state == DYING) g2.setColor(Color.RED);
		else if(state == SICK) g2.setColor(Color.LIGHT_GRAY);
		else g2.setColor(color);
		g2.fill(body);
		g2.fill(head);
		g2.setColor(Color.black);
		g2.draw(head);	
		g2.fill(nose);
		g2.draw(lEar);
		g2.draw(rEar);
		g2.fill(rEye);
		g2.fill(lEye);
		if (state == DYING) g2.setColor(Color.RED);
		else if(state == SICK) g2.setColor(Color.LIGHT_GRAY);
		else g2.setColor(color);
		g2.fill(lEar);
		g2.fill(rEar);
		if (state == DYING) g2.setColor(Color.RED);
		else if(state == SICK) g2.setColor(Color.YELLOW);
		else g2.setColor(Color.WHITE);
		g2.fill(eyeDot);
		g2.fill(eyeDotR);
		if (state == DYING) g2.setColor(Color.RED);
		else if(state == SICK) g2.setColor(Color.LIGHT_GRAY);
		else g2.setColor(color);
		g2.fill(tail);
		g2.setColor(new Color(255, 207, 221));
		g2.fill(lEarIn);
		g2.fill(rEarIn);
		
//		g2.setColor(color.red);
//		g2.draw(outline);
//		g2.draw(fov);
		g2.setTransform(at);
	}

	@Override
	protected boolean eatable(AnimalChar food) {
		return false;
	}

	@Override
	public Food traceBestFood(ArrayList<Food> fList) {
		Food target = null;
	    if (fList.size()>0) { 
	        // set the 1st item as default target
	        target = fList.get(0);
	        float targetAttraction = this.getAttraction(target);

	        // find the closer one
	        for (Food f:fList) if (this.getAttraction(f) > targetAttraction) {
	            target = f;
	            targetAttraction = this.getAttraction(target);
	        }
	        // make animal follow this target
	        this.approach(target);
	   }
		return target; 
	}
	
	 protected float getAttraction(Food target) {
	     return (float) ((target.getScale())/(PVector.dist(pos, target.getPos())/this.vel.mag()));
	 }

	@Override
	protected boolean eatableFood(Food food) {
		return (food instanceof Food);
	}

	@Override
	protected void traceBestTarget(ArrayList<AnimalChar> fList) {
		// TODO Auto-generated method stub
		
	}
	
	public void checkCat(AnimalChar cat) {
		float diffX = pos.x - cat.getPos().x - 8;
		float diffY = pos.y - cat.getPos().y - 8;
		float distance = (float)Math.sqrt((pos.x - cat.getPos().x) * (pos.x-cat.getPos().x) + (pos.y - cat.getPos().y) * (pos.y - cat.getPos().y));
		if(distance < 125) {
			vel.x = (float)((10.0/distance) *diffX);
			vel.y = (float)((10.0/distance) *diffY);
		}
	}
	
}
