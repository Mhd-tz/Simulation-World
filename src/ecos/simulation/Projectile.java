package ecos.simulation;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import main.ForestPanel;
import processing.core.PVector;

public class Projectile {
	// Bullet Fields
	private PVector vel; // Velocity
	private PVector pos; // Poition
	boolean isVisiable; // Can player see it?
	private float size;
	private Ellipse2D bullet;
	private Area outline;

	// Bullet Const
	Projectile(PVector pos, PVector vel, float size) {
		this.pos = pos;
		this.vel = vel;
		this.size = size;
		isVisiable = true;
		setShapeAttributes();
	}
	
	public void setShapeAttributes() {
		bullet = new Ellipse2D.Double(-size / 2, -size / 2, size, size);
		outline = new Area(bullet);
	}

	public Shape getOutline() {
		AffineTransform at = new AffineTransform();
		at.translate(pos.x, pos.y);
		at.scale(size, size);
		at.rotate(vel.heading());
		return at.createTransformedShape(outline);
	}

	// Bullet Movement
	private void move() {
		pos.add(vel);
	}

	// Check if the bullet is out or not
	private void checkWalls() {
		if (Math.abs(pos.x - ForestPanel.FOREST_W / 1.85) > ForestPanel.FOREST_W / 1.85 || Math.abs(pos.y - ForestPanel.FOREST_H / 1.85) > ForestPanel.FOREST_H / 1.85) {
			isVisiable = false;
		}
	}

	public void update(ArrayList<AnimalChar> animList, ForestPanel panel) {
		//Rectangle2D env = new Rectangle2D.Double(0, 0, panel.PAN_SIZE.width, panel.PAN_SIZE.height);
		ArrayList<AnimalChar> fList = filterTargetList(animList);
		traceBestFood(fList);
		move();
		checkWalls();
//		for(int i=0; i<fList.size(); i++) {
//			AnimalChar obj = animList.get(i);
//			if (isColliding(obj)) {
//				// someone got shot
//				animList.remove(obj);
//				System.out.println("Shooted");
//			}
//		}
	}
	
	boolean isColliding(AnimalChar other) {
		return (getOutline().intersects(other.getBoundingBox()) &&
				other.getOutline().intersects(getBoundingBox()) );
	}

	private Rectangle2D getBoundingBox() {
		return getOutline().getBounds2D();
	}

	// Render Bullet
	public void drawMe(Graphics2D g) {
		AffineTransform at = g.getTransform();
		g.translate(pos.x, pos.y);
		g.scale(size, size);
		g.rotate(vel.heading());
		g.setColor(Color.BLACK);
		g.fill(bullet);
		g.setTransform(at);
	}
	
	public void approach(AnimalChar obj) {
		PVector path = PVector.sub(obj.getPos(), pos);
		vel = path.limit(6);
	}
	
	protected void traceBestFood(ArrayList<AnimalChar> fList) {
		if (fList.size() > 0) {
			// find 1st target
			AnimalChar target = fList.get(0);

			// find the biggest
			for (int i = 0; i<fList.size(); i++) {
				for(int j = 0; (i != j) && j<fList.size(); j++) {
					if (fList.get(i).getSize() > fList.get(j).getSize()) {
						target = fList.get(i);
					} else {
						target = fList.get(j);
					}
				}
			}

			// make animal follow this target
			this.approach(target);
		}
	}
	
	protected ArrayList<AnimalChar> filterTargetList(ArrayList<AnimalChar> fList) {
		ArrayList<AnimalChar> list = new ArrayList<>();
		for (AnimalChar f : fList)
			if (tough(f))
				list.add(f);
		return list;
	}
	
	
	protected boolean tough(AnimalChar food) {
		return (food instanceof Cat);
	}

}
