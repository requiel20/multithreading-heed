package GraphicComponent;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;


import javax.swing.JPanel;

import core.WSN;


public class AxisPanel extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public static Font AXISFONT = new Font("Serif", Font.PLAIN, 12);
	
	
	private Color axisColor = Color.black;
	public int drawingPanelX;
	public int drawingPanelY;
	public double wsnLength;
	

	
	
	
	 
	
	
	@Override
	public void paint(Graphics g) {
		int axisBlackRectLength	= (int)(this.drawingPanelY*0.3);
		int axisBold 			= 3;
		
		
		Graphics2D g2d = (Graphics2D) g;
		
		
		
		g2d.setColor(Color.pink);
		g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
		
		
		
		
		
		
		
		
		g2d.setColor(axisColor);
		g2d.fillRect(this.drawingPanelX, this.drawingPanelY - axisBold, this.getWidth() - this.drawingPanelX, axisBold);
		
		g2d.setColor(axisColor);
		g2d.fillRect(this.drawingPanelX - axisBold, this.drawingPanelY, axisBold, this.getHeight() - this.drawingPanelY);
		
		
		
		
		
		
		
		
		
		String str = "O";
		Font font = new Font("Serif", Font.PLAIN, AXISFONT.getSize() + 12);
		Dimension strDimension = GraphicConstant.getTextDimension(str, font);
		g.setFont(font);
		g2d.setColor(axisColor);
		g2d.drawString(str, this.drawingPanelX - (int)strDimension.getWidth() - axisBlackRectLength, this.drawingPanelY - axisBlackRectLength);
		
		str = "m";
		strDimension = GraphicConstant.getTextDimension(str, font);
		g.setFont(font);
		g2d.setColor(axisColor);
		g2d.drawString(str, this.getWidth() - (int)strDimension.getWidth()*2, this.drawingPanelY - axisBlackRectLength);
		g2d.drawString(str, (this.drawingPanelX - (int)strDimension.getWidth())/2, this.getHeight() - (int)strDimension.getWidth()*2);
		
		
		
		
		
		if(this.wsnLength < 5) return;
		
		int numberOfBlock	= 10;
		int eachBlock 		= WSN.INNERSIZE.width/numberOfBlock;
		
		font = AxisPanel.AXISFONT;
		
		
		double wsnLengthBlockValue = this.wsnLength/numberOfBlock;
		
		for(int i = 0; i <= numberOfBlock + 1; i++){
			int tempX = this.drawingPanelX + i*eachBlock;
			int tempY = this.drawingPanelY - axisBlackRectLength - axisBold;
			g2d.setColor(axisColor);
			g2d.fillRect(tempX, tempY, axisBold, axisBlackRectLength);
			if(i > 0){
				double wsnLengthBlockCurrentValue = i*wsnLengthBlockValue;
				str = Double.toString(wsnLengthBlockCurrentValue);
				strDimension = GraphicConstant.getTextDimension(str, font);
				g.setFont(font);
				g2d.setColor(axisColor);
				g2d.drawString(str, tempX + (axisBold - (int)strDimension.getWidth())/2, tempY - 3);
			}
		}
		for(int i = 0; i <= numberOfBlock + 1; i++){
			int tempX = this.drawingPanelY - axisBlackRectLength - axisBold;
			int tempY = this.drawingPanelY + i*eachBlock;
			g2d.setColor(axisColor);
			g2d.fillRect(tempX, tempY, axisBlackRectLength, axisBold);
			if(i > 0){
				double wsnLengthBlockCurrentValue = i*wsnLengthBlockValue;
				str = Double.toString(wsnLengthBlockCurrentValue);
				strDimension = GraphicConstant.getTextDimension(str, font);
				g.setFont(font);
				g2d.setColor(axisColor);
				g2d.drawString(str, tempX - (int)strDimension.getWidth() - 3, tempY + (int)(strDimension.getHeight()/3));
			}
		}
		
		
		
		
		//g2d.fillRect(50, 0, 30, 30);
		//g2d.drawRect(50, 50, 30, 30);

		//g2d.draw(new Ellipse2D.Double(0, 100, 30, 30));
	}

}
