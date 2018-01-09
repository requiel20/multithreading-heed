package drawing;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.*;
import javax.swing.border.*;
import core.Node;
import core.WSN;


public class HEED_WSN extends JFrame {

	private static final long serialVersionUID = 1L;
	private int tool = 1;
    int currentX, currentY, oldX, oldY;
    
    static Vector<Color> colorList=new Vector<Color>();
    static public WSN wsn;

    public HEED_WSN() {
        initComponents();
    }

    private void initComponents() {
    	
    	// we want a custom Panel2, not a generic JPanel!
        jPanel2 = new Panel2();
        jPanel2.setBackground(new java.awt.Color(255, 255, 255));
        jPanel2.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        jPanel2.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent evt) {
                jPanel2MousePressed(evt);
            }
            public void mouseReleased(MouseEvent evt) {
                jPanel2MouseReleased(evt);
            }
        });
        jPanel2.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent evt) {
                jPanel2MouseDragged(evt);
            }
        });

        // add the component to the frame to see it!
        this.setContentPane(jPanel2);
        // be nice to testers..
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
    }// </editor-fold>

    private void jPanel2MouseDragged(MouseEvent evt) {
        if (tool == 1) {
            currentX = evt.getX();
            currentY = evt.getY();
            oldX = currentX;
            oldY = currentY;
            System.out.println(currentX + " " + currentY);
            System.out.println("PEN!!!!");
        }
    }

    private void jPanel2MousePressed(MouseEvent evt) {
    	//SimpleStatistics.printOnScreenHEED(heed);
    	//oldX = evt.getX();
        //oldY = evt.getY();
        //.out.println(oldX + " " + oldY);
    }

    //mouse released//
    private void jPanel2MouseReleased(MouseEvent evt) {
        if (tool == 2) {
            currentX = evt.getX();
            currentY = evt.getY();
            System.out.println("line!!!! from" + oldX + "to" + currentX);
        }
    }

    //set ui visible//
    public static void drawWSN(WSN wsn) {
    	colorList.add(Color.BLACK);
    	colorList.add(Color.BLUE);
    	colorList.add(Color.GREEN);
    	colorList.add(Color.ORANGE);
    	colorList.add(Color.GRAY);
    	colorList.add(Color.MAGENTA);
    	colorList.add(Color.LIGHT_GRAY);
    	colorList.add(Color.PINK);
    	colorList.add(Color.RED);
    	colorList.add(Color.CYAN);
     	colorList.add(Color.DARK_GRAY);
     	colorList.add(Color.YELLOW);
     	
    	HEED_WSN.wsn=wsn;
    	
    	EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HEED_WSN().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify
    private JPanel jPanel2;
    // End of variables declaration

    // This class name is very confusing, since it is also used as the
    // name of an attribute!
    //class jPanel2 extends JPanel {
    class Panel2 extends JPanel {

        /**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		int x_origin_stats=(int)wsn.width+20,y_origin_stats=20;
		
		
		
		Panel2() {
            // set a preferred size for the custom panel.
            setPreferredSize(new Dimension(420,420));
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            Node node;
    		Iterator<Node> iterator = wsn.CHlist.iterator();
    		int i=0;
    		Color c;
    		
    		while(iterator.hasNext()) {
    			node = iterator.next();
    			c=colorList.get(i%colorList.size());
    			i++;
    			g2.setColor(c);
    			g2.fillRect((int)node.x,(int)node.y, 8,8);
    			for(int j=0;j<node.clusterMembers.size();j++){
    				g2.drawOval((int)node.clusterMembers.get(j).x,(int)node.clusterMembers.get(j).y,6,6);
    				//g2.drawString(Integer.toString(node.clusterMembers.get(j).id), (int)node.clusterMembers.get(j).x, (int)node.clusterMembers.get(j).y);
    			}
    			g2.drawString(Integer.toString(node.inter_traffic_load_factor), (int)node.x, (int)node.y);
    			g2.draw((Shape) new Line2D.Double(node.x,node.y, node.Father.x, node.Father.y));	
    					
    			g2.drawString(node.toString(), x_origin_stats, y_origin_stats+i*12);
    		}

        }
        
        void printStats( Graphics2D g2,int x0,int y0){
        	 
        }
    }
}

