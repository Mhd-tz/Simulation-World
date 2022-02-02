package ecos.simulation;
/*
 * 
 * 					SuperClass AnimalChar
 * 
 * 
 * * We use AnimalChar as a superClass. All its methods and fields except the private fields will be accessible through all subclass that is convenient for code reusing.
 * * This will provide a project with high level of data hiding from user.
 * * It also allow us to add more functionality on top of the superClass methods.
 * */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import main.ForestPanel;
import processing.core.PVector;
import random.Random;

public abstract class AnimalChar {
	protected PVector pos, vel;
	protected float speedMag; // speed limit
	protected Dimension dim;

	protected float size;
	protected Color color;

	protected Area outline;
	protected Arc2D.Double fov;
	protected Dimension panelSize; // field to reference panel's dimension
	
	//Energy properties 
	protected float energy; // energy
	protected final float FULL_ENERGY = 1000;
	protected float engGainRatio = 100; // Energy gained per food size unit
	protected float engLossRatio;
	protected float sizeGrowRatio = 0.0001f; // size growth ratio per extra energy unit
	
	// FSM states
	protected int state;
	protected final int HUNGRY = 0;
	protected final int HALF_FULL = 1;
	protected final int FULL = 2;
	protected final int OVER_FULL = 3;
	protected final int SICK = 4;
	protected final int DYING = 5;
	protected final int DEATH = -1;
	private ForestPanel panel;
	public float health;

	public AnimalChar(PVector pos, PVector vel, int w, int h, float size, Dimension paneSize) {
		this.pos = pos;
		this.vel = vel;
		speedMag = (float) Random.random(5, 7);
		;
		vel = Random.randomPVector(speedMag);
		this.dim = new Dimension(w, h);
		this.size = size;
		this.panelSize = paneSize;
		this.color = Random.randomColor();
		setShapeAttributes();
		setOutline();
		state = FULL;
		engLossRatio = FULL_ENERGY / (30 * 20 *size); // Energy loss per frame
	}

	protected void move() {
		edgeDetection();
		vel.normalize().mult(speedMag);
		if(state == DYING || state == SICK) vel.normalize().mult(speedMag/2);
		pos.add(vel);
		energy -= engLossRatio;
	}

	protected void approach(Food target) {
		float coef = .3f; // coefficient of acceleration relative to maxSpeed
		PVector direction = PVector.sub(target.getPos(), pos).normalize();
		PVector accel = PVector.mult(direction, speedMag * coef);
		vel.add(accel);
	}

	protected void approach(AnimalChar target) {
		float coef = .3f; // coefficient of acceleration relative to maxSpeed
		PVector direction = PVector.sub(target.getPos(), pos).normalize();
		PVector accel = PVector.mult(direction, speedMag * coef);
		vel.add(accel);
	}

	protected boolean isColliding(Food other) {
		return (getOutline().intersects(other.getBoundingBox()) && other.getOutline().intersects(getBoundingBox()));
	}

	protected boolean isColliding(AnimalChar other) {
		return (getOutline().intersects(other.getBoundingBox()) && other.getOutline().intersects(getBoundingBox()));
	}

	private void edgeDetection() {

		int forestX = ForestPanel.FOREST_X;
		int forestY = ForestPanel.FOREST_Y;
		int forestMG = ForestPanel.FOREST_MG;
		int forestW = panelSize.width - (forestX + forestMG);
		int forestH = panelSize.height - (forestY + forestMG);
		Rectangle2D.Double top = new Rectangle2D.Double(forestX, forestY - 10, forestW, 10);
		Rectangle2D.Double bottom = new Rectangle2D.Double(forestX, panelSize.height - forestMG, forestW, 10);
		Rectangle2D.Double left = new Rectangle2D.Double(forestX - 10, forestY, 10, forestH);
		Rectangle2D.Double right = new Rectangle2D.Double(panelSize.width - forestMG, forestY, 10, forestH);

		float coef = .1f;
		PVector accel = new PVector();

		if (getFOV().intersects(left))
			accel.add(1, 0);
		else if (getFOV().intersects(right))
			accel.add(-1, 0);
		else if (getFOV().intersects(top))
			accel.add(0, 1);
		else if (getFOV().intersects(bottom))
			accel.add(0, -1);

		accel.mult(coef * speedMag);
		vel.add(accel);

		if (getBoundingBox().intersects(left) && vel.x < 0) {
			vel.x *= -1;
		}

		if (getBoundingBox().intersects(right) && vel.x > 0) {
			vel.x *= -1;
		}
		if (getBoundingBox().intersects(top) && vel.y < 0) {
			vel.y *= -1;
		}
		if (getBoundingBox().intersects(bottom) && vel.y > 0) {
			vel.y *= -1;
		}
	}

	public void update(ArrayList<AnimalChar> objList, ArrayList<Food> food, ForestPanel panel) {
		ArrayList<Food> fList = filterFoodList(food);
		ArrayList<AnimalChar> aList = filterTargetList(objList);
		ArrayList<AnimalChar> cList = filterCatList(objList);
		traceBestFood(fList);
		traceBestTarget(aList);
		
		for (int j = 0; j < aList.size(); j++) {
			for (int k = 0; k < cList.size(); k++) {
				if (cList.get(k).isColliding(aList.get(j))) {
					float foodSize = (float) aList.get(j).getSize();
					cList.get(k).energy += foodSize * cList.get(k).engGainRatio * 2;
					String st = String.format("%s gains energy by %.2f units to %.2f", cList.get(k).animalType(), foodSize * 100,
							cList.get(k).energy);
					if (cList.get(k).state == OVER_FULL) {
						float extra = energy - FULL_ENERGY;
						cList.get(k).energy = FULL_ENERGY;
						cList.get(k).size += extra * cList.get(k).sizeGrowRatio * cList.get(k).size;
						st = String.format("%s grows by %.1f%% to %.2f%n", cList.get(k).animalType(), cList.get(k).energy * .01, cList.get(k).size);
						panel.setStatus(st);
					}
					objList.remove(aList.get(j));
					panel.MAX_MOUSE -= 1;
					System.out.println(panel.MAX_MOUSE);
				}
			}
		}
		for (int i = 0; i < fList.size(); i++) {
			if (isColliding(fList.get(i))) {
				float foodSize = (float) fList.get(i).getScale();
				energy += foodSize * engGainRatio * 2;
				String st = String.format("%s gains energy by %.2f units to %.2f", animalType(), foodSize * 100,
						energy);

				if (state == OVER_FULL) {
					float extra = energy - FULL_ENERGY;
					energy = FULL_ENERGY;
					size += extra * sizeGrowRatio * size;
					st = String.format("%s grows by %.1f%% to %.2f%n", animalType(), energy * .01, size);
					panel.setStatus(st);
				}
				food.remove(fList.get(i));
			}
		}
		
		if (energy > FULL_ENERGY)
			state = OVER_FULL;
		else if (energy == FULL_ENERGY)
			state = FULL;
		else if (energy > FULL_ENERGY / 2)
			state = HALF_FULL;
		else if (energy > FULL_ENERGY / 3)
			 state = HUNGRY;
		else if (energy > -.2 * FULL_ENERGY)
			 state = SICK;
		else if (energy > -.3 * FULL_ENERGY)
			 state = DYING;
		else
			 state = DEATH;
		
		if (state == DEATH) {
			if(this instanceof Cat) {
				panel.MAX_CAT -= 1;
				System.out.println("#Cats"+panel.MAX_CAT);
			}
			else if(this instanceof Mouse) {
				panel.MAX_MOUSE -= 1;
			}
			  objList.remove(this);
			  return;
			}
		move();
	}

//	protected void checkCat(AnimalChar cat) {
//		float diffX = pos.x - cat.getPos().x - 8;
//		float diffY = pos.y - cat.getPos().y - 8;
//		float distance = (float) Math.sqrt((pos.x - cat.getPos().x) * (pos.x - cat.getPos().x)
//				+ (pos.y - cat.getPos().y) * (pos.y - cat.getPos().y));
//		if (distance < 125) {
//			vel.x = (float) ((10.0 / distance) * diffX);
//			vel.y = (float) ((10.0 / distance) * diffY);
//		}
//
//	}

	public boolean detectCollision(AnimalChar otherBug) {
		boolean hit = false;

		if (getOutline().getBounds2D().intersects(otherBug.getOutline().getBounds2D()))
			hit = true;

		return hit;
	}

	public void resolveCollision(AnimalChar otherBug){
		float angle = (float) Math.atan2(pos.y - otherBug.pos.y, pos.x - otherBug.pos.x);
		
		//if current bug smaller, turn it away by the angle
		if(size < otherBug.size) {	
			vel = PVector.fromAngle(angle);
			vel.mult(speedMag);
		}
		else {
			//Otherwise send the otherBug away in the opposite direction: angle+PI
			otherBug.vel = PVector.fromAngle(angle-(float)Math.PI);
			otherBug.vel.mult(speedMag);
		}
		
	}
	
	public void attackHunter(MetalMouse mMouse) {
		PVector path = PVector.sub(mMouse.getPos(), pos);
		vel = path.limit(7);
		
	}
	
	protected String animalType() {
		String type = "unknown animal";
		if (this instanceof Cat)
			type = "Cat (Predater)";
		else if (this instanceof Mouse)
			type = "Mouse (Prey)";
		else if(this instanceof MetalMouse)
			type = "MetalMouse (Hunter)";
		return type;
	}
	
	public void drawInfo(Graphics2D g) {
		AffineTransform at = g.getTransform();
		g.translate(pos.x, pos.y);

		String st1 = "Size     : " + String.format("%.2f", size);
		String st2 = "Speed  : " + String.format("%.2f", vel.mag());
		String st3 = "Energy : " + String.format("%.2f", energy);

		Font f = new Font("Courier", Font.PLAIN, 12);
		FontMetrics metrics = g.getFontMetrics(f);

		float textWidth = metrics.stringWidth(st3);
		float textHeight = metrics.getHeight();
		float margin = 12, spacing = 6;

		g.setColor(new Color(255, 255, 255, 60));
		g.fillRect((int) (-textWidth / 2 - margin),
				(int) (-dim.height * size * .75f - textHeight * 5f - spacing * 4f - margin * 2f),
				(int) (textWidth + margin * 2f), (int) (textHeight * 5f + spacing * 4f + margin * 2f));

		g.setColor(Color.blue.darker());
		g.drawString(this.animalType(), -metrics.stringWidth(this.animalType()) / 2,
				-dim.height * size * .75f - margin - (textHeight + spacing) * 4f);
		g.drawString(st1, -textWidth / 2, -dim.height * size * .75f - margin - (textHeight + spacing) * 2f);
		g.drawString(st2, -textWidth / 2, -dim.height * size * .75f - margin - (textHeight + spacing) * 1f);
		if (state == DYING) g.setColor(Color.RED);
		else if(state == SICK) g.setColor(Color.YELLOW);
		else g.setColor(Color.black);
		g.drawString(st3, -textWidth / 2, -dim.height * size * .75f - margin);

		g.setTransform(at);
	}

	protected ArrayList<AnimalChar> filterTargetList(ArrayList<AnimalChar> aList) {
		ArrayList<AnimalChar> list = new ArrayList<>();
		for (AnimalChar f : aList)
			if (eatable(f))
				list.add(f);
		return list;
	}

	protected ArrayList<Food> filterFoodList(ArrayList<Food> pList) {
		ArrayList<Food> list = new ArrayList<>();
		for (Food f : pList)
			if (eatableFood(f))
				list.add(f);
		return list;
	}

	protected ArrayList<AnimalChar> filterCatList(ArrayList<AnimalChar> aList) {
		ArrayList<AnimalChar> list = new ArrayList<>();
		for (AnimalChar f : aList)
			if (hitable(f))
				list.add(f);
		return list;
	}

	protected boolean hitable(AnimalChar food) {
		return (food instanceof Cat);
	}

	public abstract void draw(Graphics2D g2);

	protected abstract void traceBestTarget(ArrayList<AnimalChar> fList);

	protected abstract Food traceBestFood(ArrayList<Food> fList);

	protected abstract boolean eatable(AnimalChar food);

	protected abstract boolean eatableFood(Food food);

	protected abstract void setShapeAttributes();

	protected abstract void setOutline();

	protected abstract Shape getOutline();

	protected abstract Shape getFOV();
	
	public Rectangle2D getBoundingBox() {
		return getOutline().getBounds2D();
	}

	public float getSize() {
		return size;
	}

	public PVector getPos() {
		return pos;
	}

	public Color getColor() {
		return color;
	}

	public boolean checkHit(MouseEvent e) {
		return getBoundingBox().contains(e.getX(), e.getY());
	}

}
