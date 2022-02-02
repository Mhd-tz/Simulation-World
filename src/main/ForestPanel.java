package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.Timer;

import ecos.simulation.AnimalChar;
import ecos.simulation.Cat;
import ecos.simulation.Environment;
import ecos.simulation.Food;
import ecos.simulation.MetalMouse;
import ecos.simulation.Mouse;
import processing.core.PVector;
import random.Random;

public class ForestPanel extends JPanel implements ActionListener {

	// Fields and Properties

	private static final long serialVersionUID = 1L;
	// List of Animals and Food
	private ArrayList<Food> foods;
	private ArrayList<AnimalChar> animalList;
	private MetalMouse mMouse;

	// Forest Position and size
	public final static int FOREST_MG = 50;
	public final static int FOREST_X = 50;
	public final static int FOREST_Y = 50;
	public final static int FOREST_H = 800;
	public final static int FOREST_W = 1200;
	private int MAX_FOOD = 11;
	public int MAX_MOUSE = 14;
	public int MAX_CAT = 8;

	// Reference the classes
	private Environment forest;

	// Timer for respawning.
	private int t = 0;
	private int mT = 0;

	private Dimension paneSize;
	private Timer timer;

	private boolean fire;
	private boolean showInfo = true;
	private String status = "Status of the environment here...";

	public ForestPanel() {

		// Setup the Panel
		super();
		this.setBackground(Color.white);
		paneSize = new Dimension(1350, 800);
		this.setPreferredSize(paneSize);

		// Get the forest
		forest = new Environment(FOREST_X, FOREST_Y, FOREST_W, FOREST_H, paneSize);

		// Get the Animals
		this.animalList = new ArrayList<>();
		for (int i = 0; i < MAX_MOUSE; i++) {

			// Mouse
			float mScale = Random.random(1f, 1.4f);
			float mLocX = (float) Random.random(100, paneSize.width - 100);
			float mLocY = (float) Random.random(100, paneSize.height - 100);
			float mSpdX = Random.random(-20.0f, 20.0f);
			float mSpdY = Random.random(-20.0f, 20.0f);
			float mBdyWH = 25f;
			this.animalList.add(new Mouse(new PVector(mLocX, mLocY), new PVector(mSpdX, mSpdY), (int) mBdyWH,
					(int) mBdyWH, mScale, paneSize));
		}
//		respawnNewMouse();
//		float mScale = Random.random(1f, 1.4f);
		float mBdyWH = 30f;
		mMouse = new MetalMouse(new PVector(150, 100), new PVector(0, 0), (int) mBdyWH, (int) mBdyWH, (float) 1.5,
				paneSize, animalList, this);

//		for(int j =0; j< 2; j++) {
//			
//			// Cat
//			float cScale = Random.random(1.3f, 1.5f);
//			float cSpdX = Random.random(-6.0f, 6.0f);
//			float cSpdY = Random.random(-6.0f, 6.0f);
//			float cLocX = (float) Random.random(100, paneSize.width - 100);
//			float cLocY = (float) Random.random(100, paneSize.height - 100);
//			float cBdyWH = Random.random(30f, 40f);
//			this.animalList.add(new MetalMouse(new PVector(mLocX,mLocY), new PVector(mSpdX,mSpdY), (int)mBdyWH,(int)mBdyWH, mScale, paneSize));
//		}

		for (int j = 0; j < MAX_CAT; j++) {

			// Cat
			float cScale = Random.random(1.3f, 1.5f);
			float cSpdX = Random.random(-6.0f, 6.0f);
			float cSpdY = Random.random(-6.0f, 6.0f);
			float cLocX = (float) Random.random(100, paneSize.width - 100);
			float cLocY = (float) Random.random(100, paneSize.height - 100);
			float cBdyWH = Random.random(30f, 40f);
			this.animalList.add(new Cat(new PVector(cLocX, cLocY), new PVector(cSpdX, cSpdY), (int) cBdyWH,
					(int) cBdyWH, cScale, paneSize));
		}

		foods = new ArrayList<>();
		for (int i = 0; i < MAX_FOOD; i++) {
			float fLocX = (float) Random.random(100, paneSize.width - 100);
			float fLocY = (float) Random.random(100, paneSize.height - 100);
			float fBdyWH = Random.random(15f, 30f);

			// Food
			foods.add(new Food(new PVector(fLocX, fLocY), (int) fBdyWH, (int) fBdyWH));
		}
		// Start the Timer
		timer = new Timer(33, this);
		timer.start();

		// MouseActions
		addMouseListener(new MyMouseListener());
		addKeyListener(new MyKeyAdapter());
		setFocusable(true);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		forest.render(g2);
		for (AnimalChar a : animalList) {
			a.draw(g2);
			if (showInfo) {
				a.drawInfo(g2);
			}
		}
		for (Food f : foods)
			f.render(g2);
		forest.respawnFeatures(g2);

		drawStatusBar(g2);

		if (mMouse != null && MAX_MOUSE < 6)
			mMouse.draw(g2);
		if (mMouse != null && MAX_MOUSE < 6) {
			if(showInfo) mMouse.drawInfo(g2);
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		for (int i = 0; i < animalList.size(); i++) {
			AnimalChar animali = animalList.get(i);
			for (int j = 0; (i != j) && j < animalList.size(); j++) {
				AnimalChar animalj = animalList.get(j);
				if (animali.detectCollision(animalj)) {
					animali.resolveCollision(animalj);
				}
				if (animali.detectCollision(mMouse)) {
					animali.resolveCollision(mMouse);
				}
				if (animalj.detectCollision(mMouse)) {
					animalj.resolveCollision(mMouse);
				}
				if (animali instanceof Cat && animalj instanceof Mouse) {
					Mouse animaljM = (Mouse) animalj;
					Cat animaliC = (Cat) animali;
					animaljM.checkCat(animaliC);
					if(MAX_CAT < 5) {
						animaliC.attackHunter(mMouse);
						if(animaliC.detectCollision(mMouse)) {
							mMouse.health -= 1;
						}
					}
					Food jFood = animaljM.traceBestFood(foods);
					if (jFood != null) {
						jFood.setColor(animaljM.getColor());
					}
				} else if (animali instanceof Mouse && animalj instanceof Cat) {
					Mouse animaliM = (Mouse) animali;
					Cat animaljC = (Cat) animalj;
					animaliM.checkCat(animaljC);
					if(MAX_CAT < 5) {
						animaljC.attackHunter(mMouse);
						if(animaljC.detectCollision(mMouse)) {
							mMouse.health -= 1;
						}
					}
					Food iFood = animaliM.traceBestFood(foods);
					if (iFood != null) {
						iFood.setColor(animaliM.getColor());
					}
				}
			}
			if (foods.size() < MAX_FOOD) {
				respawnNewFood();
			}
			animalList.get(i).update(animalList, foods, this);
		}
		if (mMouse != null && MAX_MOUSE < 6) {
			mMouse.update();
			if (fire)
				mMouse.fire();
		}
		if (MAX_CAT == 0) {
//			MAX_MOUSE = 12;
			respawnNewMouse();
		}
		repaint();
	}

	void respawnNewFood() {
		float fLocX = (float) Random.random(100, paneSize.width - 100);
		float fLocY = (float) Random.random(100, paneSize.height - 100);
		float fBdyWH = Random.random(15f, 30f);
		if (t < 60) {
			t++;
		}
		if (t >= 60) {
			foods.add(new Food(new PVector(fLocX, fLocY), (int) fBdyWH, (int) fBdyWH));
			t = 0;
		}
	}

	void respawnRobot() {
		float mBdyWH = 30f;

		if (MAX_MOUSE <= 6) {
			mMouse = new MetalMouse(new PVector(150, 100), new PVector(0, 0), (int) mBdyWH, (int) mBdyWH, (float) 1.5,
					paneSize, animalList, this);
		}
	}

	void respawnNewMouse() {
		float mScale = Random.random(1.1f, 1.3f);
		float mLocX = (float) Random.random(100, paneSize.width - 100);
		float mLocY = (float) Random.random(100, paneSize.height - 100);
		float mSpdX = Random.random(-10.0f, 10.0f);
		float mSpdY = Random.random(-10.0f, 10.0f);
		float mBdyWH = Random.random(20f, 27f);
		float cScale = Random.random(1.3f, 1.5f);
		float cSpdX = Random.random(-6.0f, 6.0f);
		float cSpdY = Random.random(-6.0f, 6.0f);
		float cLocX = (float) Random.random(100, paneSize.width - 100);
		float cLocY = (float) Random.random(100, paneSize.height - 100);
		float cBdyWH = Random.random(30f, 40f);

		if (mT < 50) {
			mT++;
		}
		if (mT >= 50 && MAX_CAT <= 0) {
			for (int i = 0; i < 12; i++) {
				animalList.add(new Mouse(new PVector(mLocX, mLocY), new PVector(mSpdX, mSpdY), (int) mBdyWH,
						(int) mBdyWH, mScale, paneSize));
				MAX_MOUSE = 12;
			}
			for(int i = 0; i < 6; i++) {
				animalList.add(new Cat(new PVector(cLocX,cLocY), new PVector(cSpdX,cSpdY), (int)cBdyWH,(int)cBdyWH, cScale, paneSize));
				MAX_CAT = 6 ;
			}
			mT = 0;
		}
	}

	private void drawStatusBar(Graphics2D g) {
		Font f = new Font("Arial", Font.BOLD, 12);
		g.setFont(f);
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, getSize().height - 24, getSize().width, 24);
		g.setColor(Color.BLACK);
		g.drawString(status, 12, getSize().height - 8);
	}

	public void setStatus(String st) {
		this.status = st;
	}

	private class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_SPACE)
				fire = true;

			if (e.getKeyCode() == KeyEvent.VK_D) {
				System.out.println(showInfo);
				if (showInfo)
					showInfo = false;
				else
					showInfo = true;
			}
		}

		public void keyReleased(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_SPACE)
				fire = false;
		}
	}

	private class MyMouseListener extends MouseAdapter {

		/*
		 * First we get all the elements of the food's ArrayList. Check the mouse if it
		 * hits a specified element using a public method in food class that can detect
		 * if the mouse has hit the element it is hitting. (checkHit()) checkHit method
		 * has a parameter MouseEvent that passes all mouse actions. Get the element
		 * scale and pass it to a double variable. Check if the scale is less than the
		 * creature then add some to the scale. Set the scale variable to the element
		 * scale.
		 */
		public void mousePressed(MouseEvent e) {
			for (int i = 0; i < foods.size(); i++) {
				if (foods.get(i).checkHit(e)) {
					double scale = foods.get(i).getScale();
					if (scale <= 2.6) {
						scale += 0.2;
						foods.get(i).setScale(scale);
					}
				}
			}
		}

		public void mouseClicked(MouseEvent e) {
			float fBdyWH = Random.random(15f, 30f);
			float mScale = Random.random(1f, 1.4f);
			float mSpdX = Random.random(-20.0f, 20.0f);
			float mSpdY = Random.random(-20.0f, 20.0f);
			float mBdyWH = 25f;
			if (e.getClickCount() == 2) {
				foods.add(new Food(new PVector(e.getX(), e.getY()), (int) fBdyWH, (int) fBdyWH));
			}
			if (e.getClickCount() == 2 && e.isAltDown()) {
				animalList.add(new Mouse(new PVector(e.getX(), e.getY()), new PVector(mSpdX, mSpdY), (int) mBdyWH,
						(int) mBdyWH, mScale, paneSize));
			}

			for (int i = 0; i < foods.size(); i++) {
				if (foods.get(i).checkHit(e)) {
					if (e.isControlDown())
						foods.remove(i);
				}
			}
			for (int j = 0; j < animalList.size(); j++) {
				if (animalList.get(j).checkHit(e)) {
					if (e.isControlDown()) {
						animalList.remove(j);
					}
				}
			}
		}

	}
}
