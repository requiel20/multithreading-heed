package DrawingComponent;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;



public class NodeCircleShape extends SimulationObject{
	
	private static final int GRAPHICRADIUS = 10;
	private static Font serifFont = new Font("Serif", Font.PLAIN, 12);
	
	public Color color;
	
	
	//Save the position of a Node inside the Axis and we can show the information of Node when user click the Node in the Graphic
	public Rectangle	ShapeRect;
	public boolean 		showPowerLevel;
	public boolean      showNodeId;
	
	
	

	public NodeCircleShape() {
		super();
		// TODO Auto-generated constructor stub
		color = Color.white;
		
		this.showPowerLevel = false;
		this.showNodeId = false;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		//x +=(long)(Math.random() * 3 * (Math.random() > 0.5 ? 1 : -1));
		//y +=(long)(Math.random() * 3 * (Math.random() > 0.5 ? 1 : -1));
	}

	@Override
	public void render(Graphics g, double multiple) {
		// TODO Auto-generated method stub
		int tempX = (int)(x*multiple - GRAPHICRADIUS/2);
		int tempY = (int)(y*multiple - GRAPHICRADIUS/2);
		g.setColor(color);
		g.fillOval(tempX, tempY, GRAPHICRADIUS, GRAPHICRADIUS);
		this.ShapeRect = new Rectangle(tempX, tempY, GRAPHICRADIUS, GRAPHICRADIUS);
		
		
		
		
		//show Power Level with Circle
		if(this.showPowerLevel){
			tempX		= (int)(x*multiple - this.radius*multiple);
			tempY		= (int)(y*multiple - this.radius*multiple);
			g.setColor(Color.red);
			g.drawOval(tempX, tempY, (int)(this.radius*multiple*2), (int)(this.radius*multiple*2));
		}
		
		
		
		
		
		
		//show Node ID
		if(this.showNodeId){
			String idStr = new Integer(this.id).toString();
			
			g.setColor(Color.red);
			g.setFont(serifFont);
			g.drawString(idStr, (int)(x*multiple + GRAPHICRADIUS/2), (int)(y*multiple - GRAPHICRADIUS/2));
		}
	}
}
