package ecos.simulation;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

import main.ForestPanel;
import processing.core.PVector;

public class MetalMouse extends AnimalChar {

	// Fields and Properties
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
	protected Line2D.Double vLine;
	protected Line2D.Double hLine;
	protected Ellipse2D.Double body;
	private Ellipse2D.Double tail;
	private Rectangle2D.Double gun;
	private Rectangle2D.Double gunDot;
	private int ySpeed;

	protected Area outline;
	protected Arc2D.Double fov;
	private Color newColor;
	private int coolingTime;
	private ForestPanel pane;

	private ArrayList<AnimalChar> animList;
	private ArrayList<Projectile> bulletList = new ArrayList<Projectile>();

	public MetalMouse(PVector pos, PVector vel, int w, int h, float size, Dimension paneSize,
			ArrayList<AnimalChar> animList, ForestPanel pane) {
		super(pos, vel, w, h, size, paneSize);
		this.animList = animList;
		// TODO Auto-generated constructor stub
		newColor = new Color(168, 189, 198);
		ySpeed = 5;
//		coolingTime = 0;
		this.pane = pane;
		health = 10;
	}

	@Override
	protected void setShapeAttributes() {
		gun = new Rectangle2D.Double(-dim.width / 10, -dim.height / 2.5, dim.width, dim.height / 3);
		gunDot = new Rectangle2D.Double(dim.width / 1.1, -dim.height / 3, dim.width / 5, dim.height / 5);
		tail = new Ellipse2D.Double(-dim.width / 1.1, -dim.height / 7, dim.width / 2.5, dim.height / 2.5);
		head = new Ellipse2D.Double(-dim.width / 2, -dim.height / 2, dim.width, dim.height);
//		lEar = new Ellipse2D.Double(-dim.width,-dim.height /2, dim.width, dim.height/2);
		lEar = new Ellipse2D.Double(-dim.width * 1.1, -dim.height / 3, dim.width, dim.height / 2);
		lEarIn = new Ellipse2D.Double(-dim.width / 1.2, -dim.height / 3.9, dim.width / 1.5, dim.height / 3);
		rEar = new Ellipse2D.Double(-dim.width * 1.1, dim.height / 20, dim.width, dim.height / 2);
		rEarIn = new Ellipse2D.Double(-dim.width / 1.2, dim.height / 8, dim.width / 1.5, dim.height / 3);
		rEye = new Ellipse2D.Double(dim.width / 9, -dim.height / 7, dim.width / 4, dim.height / 4);
		eyeDot = new Ellipse2D.Double(dim.width / 5, -dim.height / 10, dim.width / 7, dim.height / 7);
		vLine = new Line2D.Double(-dim.width / 2, -dim.height / 100, dim.width / 6, dim.height / 100);
		nose = new Arc2D.Double(dim.width / 3, -dim.height / 7, dim.width / 3, dim.height / 3, -90, 180, Arc2D.PIE);

		float sight = dim.width * speedMag * 1.2f;
		fov = new Arc2D.Double(-sight, -sight, sight * 2, sight * 2, -55, 110, Arc2D.PIE);
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
		g2.scale(size, size);
		if (vel.x < 0)
			g2.scale(1, -1);
		g2.setColor(Color.black);
		g2.draw(tail);
		g2.setColor(Color.LIGHT_GRAY);
		g2.fill(gun);
		g2.setColor(Color.black);
		g2.fill(gunDot);
		if (state == SICK)
			g2.setColor(Color.LIGHT_GRAY);
		else
			g2.setColor(newColor);
		g2.fill(head);
		g2.setColor(Color.black);
		g2.draw(head);
		g2.fill(nose);
		g2.rotate(120);
		g2.draw(lEar);
		g2.rotate(-240);
		g2.draw(rEar);
		g2.rotate(120);
		g2.fill(rEye);
		if (state == SICK)
			g2.setColor(Color.LIGHT_GRAY);
		else
			g2.setColor(newColor);
		g2.rotate(120);
		g2.fill(lEar);
		g2.rotate(-240);
		g2.fill(rEar);
		g2.rotate(120);
		if (state == SICK) {
			g2.setColor(Color.red);
			g2.fill(eyeDot);
		} else {
			g2.setColor(Color.white);
			g2.fill(eyeDot);
		}
		if (state == SICK)
			g2.setColor(Color.LIGHT_GRAY);
		else
			g2.setColor(newColor);
		g2.fill(tail);
		g2.setColor(new Color(255, 207, 221));
		g2.rotate(120);
		g2.fill(lEarIn);
		g2.rotate(-240);
		g2.fill(rEarIn);
		g2.rotate(120);
		g2.setColor(Color.black);
		g2.draw(vLine);
		g2.setTransform(at);
		for (Projectile p : bulletList)
			p.drawMe(g2);

	}

	@Override
	public void move() {
		int forestX = ForestPanel.FOREST_X;
		int forestY = ForestPanel.FOREST_Y;
		int forestMG = ForestPanel.FOREST_MG;
		int forestW = panelSize.width - (forestX + forestMG);
		Rectangle2D.Double top = new Rectangle2D.Double(forestX, forestY + 5, forestW, 10);
		Rectangle2D.Double bottom = new Rectangle2D.Double(forestX, panelSize.height - 20 - forestMG, forestW, 10);

		if (getBoundingBox().intersects(top) && ySpeed < 0) {
			ySpeed *= -1;
		}
		if (getBoundingBox().intersects(bottom) && ySpeed > 0) {
			ySpeed *= -1;
		}

		pos.add(new PVector(0, ySpeed));
	}

	public void update() {
		move();
		ArrayList<AnimalChar> fList = filterCatList(animList);
		for (int i = 0; i < bulletList.size(); i++) {
			bulletList.get(i).update(fList, pane);
			boolean isRemoved = false;
			for (int j = 0; j < fList.size(); j++) {
				AnimalChar obj = fList.get(j);
				if (bulletList.get(i).isColliding(obj)) {
					animList.remove(obj);
					pane.MAX_CAT -= 1;
					String st = String.format("%s has shooted a %s !!", animalType(), fList.get(j).animalType());
					pane.setStatus(st);
					bulletList.remove(bulletList.get(i));
					isRemoved = true;
					break;
				}
			}
			if (isRemoved)
				continue;
			if(!bulletList.get(i).isVisiable) bulletList.remove(bulletList.get(i));
		}

	}

	public void fire() {

		coolingTime++;
		// Check&Get dir if the mouseAim is in the player's direction
		if (coolingTime >= 12) {
			// create speed for the missile that goes along the same direction as the player
			PVector mSpeed = PVector.fromAngle(vel.heading()).mult(5);

			// Add Bullets
			bulletList.add(new Projectile(new PVector(pos.x, pos.y), new PVector(mSpeed.x, mSpeed.y), 5f));
			coolingTime = 0;
		}
	}
	
	public void drawInfo(Graphics2D g) {
		AffineTransform at = g.getTransform();
		g.translate(pos.x, pos.y);

//		String st1 = "Health     : " + String.format("%.2f", size);
		String st2 = "Health  : " + String.format("%.2f", health);
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
		g.drawString(this.animalType(), (float) (-metrics.stringWidth(this.animalType()) / 2.5),
				-dim.height * size * .75f - margin - (textHeight + spacing) * 4f);
		g.setColor(Color.black);
		//g.drawString(st1, -textWidth / 2, -dim.height * size * .75f - margin - (textHeight + spacing) * 2f);
		g.drawString(st2, -textWidth / 2, -dim.height * size * .75f - margin - (textHeight + spacing) * 2f);
		//if (state == SICK || state == DYING)
//			  g.setColor(Color.red);
		//g.drawString(st3, -textWidth / 2, -dim.height * size * .75f - margin);

		g.setTransform(at);
	}
	
	public void setColor(Color color) {
		this.color = color;
	}

	@Override
	protected void traceBestTarget(ArrayList<AnimalChar> fList) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Food traceBestFood(ArrayList<Food> fList) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean eatable(AnimalChar food) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean eatableFood(Food food) {
		// TODO Auto-generated method stub
		return false;
	}

}
