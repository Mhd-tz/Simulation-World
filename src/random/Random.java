package random;
/******************************************************************
 * 
 * 		Random class for getting random colors and numbers
 * 
 * ****************************************************************/
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import processing.core.PVector;

public class Random {
	public static float random(double min, double max) {
		return (float) (Math.random()*(max-min)+min);
	}
	
	public static float random(double max) {
		return (float) (Math.random()*max);
	}
	
	public static Color randomColor() {
		int r = (int) random(255);
		int g = (int) random(255);
		int b = (int) random(255);
		
		return new Color(r,g,b);
	}
	
	public static PVector randomPVector(int maxX, int maxY) {		
		return new PVector((float)random(maxX), (float)random(maxY));	
	}
	
	public static PVector randomPVector(float magnitude) {
		return PVector.random2D().mult(magnitude);
	}
	
//	public static Food randomFood(Dimension pSize) {
//		return new Food(	Util.random(50, pSize.width-50), 
//						Util.random(50, pSize.height-50),
//						Util.random(1,5));
//	}
//	
//	public static Food randomFood(int x, int y, float dist) {
//		PVector foodPos = new PVector(dist,0).rotate(random(Math.PI*2)).add(x,y);
//		return new Food(foodPos.x, foodPos.y, random(1,5));
//	}
//	
//	public static int countFood(ArrayList<SimulationObject> objList) {
//		int i = 0;
//		for (SimulationObject obj:objList) if (obj instanceof Food) i++;
//		return i;
//	}
}
