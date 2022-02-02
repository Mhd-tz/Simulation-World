package main;
import javax.swing.JFrame;

public class ForestApp extends JFrame {
	
	public ForestApp(String title) {
		super(title);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.add(new ForestPanel());
		this.pack();
		this.setVisible(true);
		}
	
	public static void main(String[] args) {
		ForestApp app = new ForestApp("Mahdi_Taziki_Forest Simulation");
	}

}
