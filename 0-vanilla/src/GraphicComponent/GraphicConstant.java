package GraphicComponent;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;





public class GraphicConstant {
	
	public static int SCREENWIDTH = 0, SCREENHEIGHT = 0;
	public static int WINDOWWIDTH = 0, WINDOWHEIGHT = 0;
	
	public static final int HEIGHTofEACHCOMPONENT = 30;
	public static final int DISTANCE_COMPONENT = 15;

	public static void setGlobolParameters(){
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        Insets ins = Toolkit.getDefaultToolkit().getScreenInsets(gc);
        
        SCREENWIDTH = gc.getBounds().width - ins.left - ins.right;
        SCREENHEIGHT = gc.getBounds().height - ins.top - ins.bottom;
        
        WINDOWHEIGHT = (int)(SCREENHEIGHT*0.96);
        WINDOWWIDTH  = WINDOWHEIGHT *12/9;;
	}
	public static Dimension getTextDimension(String str, Font font){
		FontRenderContext frc = new FontRenderContext(new AffineTransform(), true, true); 
		int textwidth = (int)(font.getStringBounds(str, frc).getWidth());
		int textheight = (int)(font.getStringBounds(str, frc).getHeight());
		
		return new Dimension(textwidth, textheight);
	}
}
