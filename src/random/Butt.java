package random;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

public class Butt {
	
	private int x,y;
	private int size;
	private Rectangle2D.Double shape;
	private Dimension dim;
	
	public Butt(int x, int y, int w, int h, int size) {
		this.x = x;
		this.y = y;
		this.dim = new Dimension(w,h);
		this.size = size;
		setShapeAttributes();
		
	}
	
	private void setShapeAttributes() {
		shape = new Rectangle2D.Double(dim.width, dim.height, dim.width*1.1, dim.height*1.1);
	}
	
	public void draw(Graphics2D g) {
		AffineTransform at = g.getTransform();
		g.translate(x, y);

		String st1 = "UPGRADE?";

		Font f = new Font("Courier", Font.PLAIN, 18);
		FontMetrics metrics = g.getFontMetrics(f);

		float textWidth = metrics.stringWidth(st1);
		float textHeight = metrics.getHeight();
		float margin = 12, spacing = 6;

		g.setColor(Color.LIGHT_GRAY);
		g.fillRect((int) (-textWidth / 2 - margin),
				(int) (-dim.height * size * .75f - textHeight * 5f - spacing * 4f - margin * 2f),
				(int) (textWidth + margin * 2f), (int) (textHeight * 5f + spacing * 4f + margin * 2f));

		g.setColor(Color.blue.darker());
		g.drawString(st1, -textWidth / 2, -dim.height * size * .75f - margin - (textHeight + spacing) * 2f);

		g.setTransform(at);
	}
}
