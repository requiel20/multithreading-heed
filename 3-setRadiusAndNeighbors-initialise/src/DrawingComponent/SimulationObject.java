package DrawingComponent;

import java.awt.Graphics;

public abstract class SimulationObject {
	
	public double x, y;
	public int id;
	
	public double radius;
	public double maxRadius;
	
	public abstract void tick();
	public abstract void render(Graphics g, double multiple);
	
	public SimulationObject(){
		
	}
}
