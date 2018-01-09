package core;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.Vector;

public class BaseStation extends Node{

	public static final int 	GRAPHIC_RADIUS 		= 30;
	public static final Point	GRAPHIC_LOCATION	= new Point((int)(WSN.INNERSIZE.getWidth() + 50), (int)(WSN.INNERSIZE.getHeight() - GRAPHIC_RADIUS)/2);
	
	private static Font serifFont = new Font("Serif", Font.BOLD, (int) (GRAPHIC_RADIUS*12/20));
	
	public Vector<Node> children;
	
	public double distanceOfMax;
	public double distanceOfMin;
	
	
	
	
	
	

	public BaseStation() {
		// TODO Auto-generated constructor stub
		
		this.distanceOfMax 	= 0;
		this.distanceOfMin	= Double.MAX_VALUE;
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		super.tick();
	}

	@Override
	public void render(Graphics g, double multiple) {
		// TODO Auto-generated method stub
		//super.render(g);
		
		
		//Draw BS Shape
		int tempX		= (int)(x*multiple - GRAPHIC_RADIUS/2);
		int tempY		= (int)(y*multiple - GRAPHIC_RADIUS/2);
		g.setColor(Color.blue);
		//g.fillOval(x, y, 20, 20);
		//g.drawOval(tempX, tempY, GRAPHIC_RADIUS, GRAPHIC_RADIUS);
		g.fillOval(tempX, tempY, GRAPHIC_RADIUS, GRAPHIC_RADIUS);
		this.ShapeRect = new Rectangle(tempX, tempY, GRAPHIC_RADIUS, GRAPHIC_RADIUS);
		
		
		
		String idStr = "BS";
		g.setColor(Color.blue);
		g.setFont(serifFont);
		g.drawString(idStr, tempX + GRAPHIC_RADIUS, tempY + GRAPHIC_RADIUS);
				
		
		
		
		//Draw Power Level of BS
		if(this.showPowerLevel){
			tempX		= (int)(x*multiple - this.maxRadius*multiple);
			tempY		= (int)(y*multiple - this.maxRadius*multiple);
			g.setColor(Color.blue);
			g.drawOval(tempX, tempY, (int)(this.maxRadius*multiple*2), (int)(this.maxRadius*multiple*2));
		}
	}
	
	

}
