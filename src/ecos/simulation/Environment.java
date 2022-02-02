package ecos.simulation;
/*
 *
 * 			Environment Section
 * 
 * This is the Environment Class with all its fields.
 * The features in the environment will respawn randomly with random speed.
 * 
 * */
import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

import main.ForestPanel;
import processing.core.PVector;
import random.Random;

public class Environment {
	
	private Rectangle2D.Double forest;
	private Ellipse2D.Double treeRLeaves;
	private Ellipse2D.Double treeLLeaves;
	private Ellipse2D.Double treeTLeaves;
	private Ellipse2D.Double treeBLeaves;
	private Ellipse2D.Double cloudTop;
	private Ellipse2D.Double cloudRight;
	private Ellipse2D.Double cloudLeft;
	private Ellipse2D.Double cloudTopTop;
	private Ellipse2D.Double cloudTopRight;
	private Ellipse2D.Double cloudTopLeft;
	private Ellipse2D.Double cloudMidTop;
	private Ellipse2D.Double cloudMidRight;
	private Ellipse2D.Double cloudMidLeft;
	private Rectangle2D.Double birdBody;
	private Ellipse2D.Double birdWing;
	private Dimension panelSize;	//field to reference panel's dimension
	
	private int forestX;
	private int forestY;
	private int forestH;
	private int forestW;
	private double scale;
	private float x,y;
	private int rand1;
	private int rand2;
	private int rand2Y;
	private int rand3;
	private int rand3Y;
	private int rand4;
	private int rand4Y;
	private int bSpeed= (int)Random.random(1, 2);
	private int cSpeed = (int)Random.random(3, 5);
	private int xPos;
	private int yPos = (int)Random.random(50, 630);
	private int cXPos;
	private int cYPos = (int)Random.random(500, 630);
	
	public Environment(int x, int y, int w, int h, Dimension paneSize) {
		this.forestX = x;
		this.forestY = y;
		this.forestW = w;
		this.forestH = h;
		this.scale = 1;
		forest = new Rectangle2D.Double();
		treeRLeaves = new Ellipse2D.Double();
		treeTLeaves = new Ellipse2D.Double();
		treeLLeaves = new Ellipse2D.Double();
		treeBLeaves = new Ellipse2D.Double();
		cloudTop = new Ellipse2D.Double();
		cloudRight = new Ellipse2D.Double();
		cloudLeft = new Ellipse2D.Double();
		cloudTopTop = new Ellipse2D.Double();
		cloudTopRight = new Ellipse2D.Double();
		cloudTopLeft = new Ellipse2D.Double();
		cloudMidTop = new Ellipse2D.Double();
		cloudMidRight = new Ellipse2D.Double();
		cloudMidLeft = new Ellipse2D.Double();
		birdBody = new Rectangle2D.Double();
		birdWing = new Ellipse2D.Double();
		rand1 = forestX+(int)Random.random(10, 30);
		rand2 = forestX+(int)Random.random(90, 120);
		rand2Y = forestY+(int)Random.random(550, 580);
		rand3 = forestX+(int)Random.random(110, 150);
		rand3Y = forestY+(int)Random.random(15, 20);
		rand4 = forestX+(int)Random.random(510, 550);
		rand4Y = forestY+(int)Random.random(540, 590);
		panelSize = paneSize;
		
	}
	
	public void setEnvironmentAttributes() {
		forest.setFrame(forestX, forestY, panelSize.width - (ForestPanel.FOREST_X+ForestPanel.FOREST_MG) , panelSize.height - (ForestPanel.FOREST_Y+ForestPanel.FOREST_MG));
	}
	
	public void setTreeAttributes() {
		treeRLeaves.setFrame(rand1,rand2, 50,50);
	}
	public void setTree2Attributes() {
		treeTLeaves.setFrame(rand2, rand2Y, 50, 50);
	}
	public void setTree3Attributes() {
		treeLLeaves.setFrame(rand3, rand3Y, 50, 50);
	}
	public void setTree4Attributes() {
		treeBLeaves.setFrame(rand4,rand4Y, 50, 50);
	}
	
	public void setCloudBottomAttributes() {
		cXPos += cSpeed;
		if(cXPos>ForestPanel.FOREST_W+20) {
			cXPos = -10;
		}
		cloudTop.setFrame(cXPos-2, cYPos -350 , 20, 20);
		cloudRight.setFrame(cXPos+15, cYPos -350, 20, 20);
		cloudLeft.setFrame(cXPos+5, cYPos-360, 20, 20);
	}
	
	public void setCloudTopAttributes() {
		cXPos += cSpeed;
		if(cXPos>ForestPanel.FOREST_W+100) {
			cXPos = -10;
		}
		cloudTopTop.setFrame(cXPos+38, cYPos -100 , 20, 20);
		cloudTopRight.setFrame(cXPos+55, cYPos -100, 20, 20);
		cloudTopLeft.setFrame(cXPos+45, cYPos-110, 20, 20);
	}
	
	public void setCloudMidAttributes() {
		cXPos += cSpeed;
		if(cXPos>ForestPanel.FOREST_W+100) {
			cXPos = -10;
		}
		cloudMidTop.setFrame(cXPos+18, cYPos , 20, 20);
		cloudMidRight.setFrame(cXPos+35, cYPos, 20, 20);
		cloudMidLeft.setFrame(cXPos+25, cYPos-10, 20, 20);
	}
	
	public void setBirdAttributes() {
		xPos += bSpeed;
		if(xPos>ForestPanel.FOREST_W+100) {
			xPos = -25;
		}
		birdBody.setFrame(xPos+50, yPos, 22, 5);
		birdWing.setFrame(xPos+55, yPos-20, 10, (int)Random.random(30,60));
	}
	
	public void render(Graphics2D g2) {
		AffineTransform at = g2.getTransform();
		setEnvironmentAttributes();
		g2.setColor(new Color(113, 80, 37));
		g2.fill(forest);
		g2.setTransform(at);
	}
	
	public void respawnFeatures(Graphics2D g2) {
		AffineTransform at = g2.getTransform();
		g2.translate(x, y);
		g2.scale(scale, scale);
		setTreeAttributes();
		setTree2Attributes();
		setTree3Attributes();
		setTree4Attributes();
		setCloudBottomAttributes();
		setCloudTopAttributes();
		setCloudMidAttributes();
		setBirdAttributes();
		g2.setColor(Color.GREEN);
		g2.fill(treeTLeaves);
		g2.fill(treeLLeaves);
		g2.fill(treeRLeaves);
		g2.fill(treeBLeaves);
		g2.setColor(Color.black);
		g2.draw(treeTLeaves);
		g2.draw(treeLLeaves);
		g2.draw(treeBLeaves);
		g2.draw(treeRLeaves);
		g2.setTransform(at);
		
		//Bird
		AffineTransform af = g2.getTransform();
		g2.setColor(Color.black);
		g2.fill(birdBody);
		g2.fill(birdWing);
		g2.setTransform(af);
		
		//Clouds
		AffineTransform c1 = g2.getTransform();
		g2.setColor(Color.white);
		g2.fill(cloudMidTop);
		g2.fill(cloudMidRight);
		g2.fill(cloudMidLeft);
		g2.setTransform(c1);
		
		AffineTransform c2 = g2.getTransform();
		g2.setColor(Color.white);
		g2.fill(cloudTopTop);
		g2.fill(cloudTopRight);
		g2.fill(cloudTopLeft);
		g2.setTransform(c2);
		
		AffineTransform c3 = g2.getTransform();
		g2.setColor(Color.white);
		g2.fill(cloudTop);
		g2.fill(cloudRight);
		g2.fill(cloudLeft);
		g2.setTransform(c3);
	}
	
}
