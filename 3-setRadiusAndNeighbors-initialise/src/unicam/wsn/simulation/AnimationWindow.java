package unicam.wsn.simulation;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferStrategy;
import java.util.Iterator;

import GraphicComponent.GraphicConstant;
import core.Node;
import core.ClusteringProtocol.ProtocolRunning;
import core.WSN;

public class AnimationWindow extends Canvas implements Runnable, ProtocolRunning{

	

	
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	//create a WSN
	public WSN wsn;
	
	//Used inside Runnable interface
	private Thread thread;
	public boolean running = false;
	
	//set the speed of Simulation
	public int speed;
	
	//Interface to send the AnimationWindow's state to the Window
	public AnimationWindowInterface interfaceAnimationEnd;
	
	//Used to check the state of simulation
	public boolean stopped;
	public boolean paused;
	
	public boolean draw;
	
	
	public AnimationWindow(){
		this.setBounds(1, 1, WSN.OUTERSIZE.width, WSN.OUTERSIZE.height);
		
		this.setBackground(Color.red);
		
		
		
		
		this.speed 		= 1000;
		this.stopped	= true;
		this.paused  	= false;
		this.draw		= true;

		
		
		
		
		
		
		
		
		
		
		this.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent evt) {
				// TODO Auto-generated method stub
				//SimpleStatistics.printOnScreenHEED(heed);
				int oldX = evt.getX();
		        int oldY = evt.getY();
		        System.out.println("Clicking ..... ********************************");
		        if(AnimationWindow.this.wsn == null) return;				//if WSN is null return;
		        if(AnimationWindow.this.wsn.wsnNodeList == null) return;	//if WSN is not generated return;
		        if(AnimationWindow.this.wsn.BS == null) return;				//if BS is null return;
		        
		        
		        
	        	if(AnimationWindow.this.wsn.BS.ShapeRect.contains(new Point(oldX, oldY))){
	        		if(AnimationWindow.this.wsn.BS.showPowerLevel){
	        			AnimationWindow.this.wsn.BS.showPowerLevel = false;
	        		}
	        		else{
	        			AnimationWindow.this.wsn.BS.showPowerLevel = true;
	        		}
	        	}
	        	else if(AnimationWindow.this.wsn.showIdRect.contains(new Point(oldX, oldY))){
	        		if(AnimationWindow.this.paused || AnimationWindow.this.stopped){
	        			Node clickedNode = getClickedNode(new Point(oldX, oldY));
		        		if(clickedNode != null){
		        			if(clickedNode.isCH()){
	    	        			if(clickedNode.showPowerLevel){
	    	        				clickedNode.showPowerLevel = false;
		    	        		}
		    	        		else{
		    	        			clickedNode.showPowerLevel = true;
		    	        		}
	    	        		}
		        			
		        			AnimationWindow.this.interfaceAnimationEnd.SimulationDidClickedSingleNode(clickedNode);
		        		}
	        		}
	        	}
	        	else{
	        		setAllNodesShowId();
	        		
	        	}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
            
        });
	}
	public void getReadyAndGo(){
		this.wsn.protocol.interfaceProtocolRunning = this;
		
		this.stopped	= false;
		this.paused		= false;
	}
	private Node getClickedNode(Point clickedPoint){
		Node clickedNode = null;
		
		Iterator<Node> iterator = this.wsn.wsnNodeList.iterator();
		while(iterator.hasNext()){
			Node tempNode = iterator.next();
			if(tempNode.ShapeRect.contains(clickedPoint)){
				clickedNode = tempNode;
        	}
		}
		
		return clickedNode;
	}
	public void setAllNodesShowPowerLevel(boolean show){
		Iterator<Node> iterator = this.wsn.wsnNodeList.iterator();
		while(iterator.hasNext()){
			Node node = iterator.next();
			node.showPowerLevel = show;
		}
	}
	private void setAllNodesShowId(){
		Iterator<Node> iterator = this.wsn.wsnNodeList.iterator();
		while(iterator.hasNext()){
			Node node = iterator.next();
			if(node.showNodeId){
				node.showNodeId = false;
			}
			else{
				node.showNodeId = true;
			}
		}
	}
	
	

	//Start Simulation
	public synchronized void start(){
		System.out.println("Thread Start ******************************");
		thread = new Thread(this);
		thread.start();
		running = true;
	}
	//Stop Simulation
	public synchronized void stop(){
		try{
			System.out.println("Thread Stop******************************");
			thread.join();
			running = false;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	

	//Method Implementation of Interface Runnable
	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("FPT: Starts");
		long lastTime = System.nanoTime();
		double amountOfTrick = 60.0;
		double ns = 1000000000/amountOfTrick;
		double delta = 0;
		long timer = System.currentTimeMillis();
		int frames = 0;
		while(running){
			long now = System.nanoTime();
			delta +=(now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1){
				if(this.draw) tick();
				delta--;
			}
			if(running){
				if(this.draw) render();
			}
			frames++;
			
			if(System.currentTimeMillis() - timer > this.speed){
				timer += this.speed;
				//System.out.println("FPT: " + frames);
				
				if(!this.paused && !this.stopped && this.wsn !=null){
					this.runRound();
				}
				
				frames = 0;
			}
			//System.out.println("Simulazione finita ms" + System.currentTimeMillis());
			//System.out.println("Simulazione finita nano" + System.nanoTime());
		}
		//stop();
	//System.out.println("Simulazione finita ms" + System.currentTimeMillis());
		//System.out.println("Simulazione finita nano" + System.nanoTime());

		
	}
	
	private void tick(){
		if(this.wsn == null){
			
		}
		else{
			this.wsn.tick();
		}
	}
	private void render(){
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null){
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = bs.getDrawGraphics();

		this.drawbackground(g);
		this.drawSpeedString(g);
		this.drawWSN(g);

		//Drawing
		g.dispose();
		bs.show();
	}
	//Draw Black Background
	private void drawbackground(Graphics g){
		g.setColor(Color.black);
		g.fillRect(0, 0, WSN.OUTERSIZE.width, WSN.OUTERSIZE.height);
		
	}
	//Draw Speed String of Information
	private void drawSpeedString(Graphics g){
		
		String speedStr = "Speed : " + Integer.toString(this.speed) + " ms";
		Font font = new Font("Serif", Font.PLAIN, 12);
		int textwidth = (int)(GraphicConstant.getTextDimension(speedStr, font).getWidth());
		int textheight = (int)(GraphicConstant.getTextDimension(speedStr, font).getHeight());
		g.setFont(font);
		g.setColor(Color.white);
		g.drawString(speedStr, (int)WSN.OUTERSIZE.width - textwidth -20, (int)WSN.OUTERSIZE.getHeight()-textheight);
		
	}
	private void drawWSN(Graphics g){
		
		if(this.wsn == null){
			
		}
		else{
			this.wsn.render(g, 0);			//call render
		}
	}
	public void runRound(){
		
		if(this.wsn.roundPerformed == 0){
			this.interfaceAnimationEnd.SimulationDidStarted(this.wsn);
			this.stopped = false;
		
			

		}
		
		
		if(this.stopped){
			
		}
		else{
			
			
			this.wsn.protocol.election_cluster_formation();				//cluster formation
			this.wsn.defineStandardCHRouting();							//defines the routing at CH level
			this.wsn.protocol.runRound();								//round
			this.wsn.roundPerformed++;	
			this.interfaceAnimationEnd.SimulationDidOneRound(this.wsn);
			//System.out.println("Simulazione finita ms" + System.currentTimeMillis());
			//System.out.println("Simulazione finita nano" + System.nanoTime());
		}
		 
		 
		if(this.wsn.protocol.wsn.deadNodeList.size() >= this.wsn.alphaNodesDead){
			this.stopped = true;
			this.interfaceAnimationEnd.SimulationDidEnd(this.wsn);
		}
		
	}
	
	//Method Implementation of Interface ProtocolRunning
	@Override
	public void DidFailToContinueRunningProtocol(String reseanDescriptStr) {
		// TODO Auto-generated method stub
		
		this.interfaceAnimationEnd.SimualtionDisplayAlertMessage(reseanDescriptStr);
	}

	
	
	//Create Interface to detect the state of Simulation
	public interface AnimationWindowInterface {
		public void SimualtionDisplayAlertMessage(String reseanDescriptStr);
		public void SimulationDidStarted(WSN wsn);
		public void SimulationDidOneRound(WSN wsn);
		public void SimulationDidEnd(WSN wsn);
		public void SimulationDidClickedSingleNode(Node clickedNode);
	}

	

}
