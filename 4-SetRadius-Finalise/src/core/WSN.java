package core;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Vector;

import DrawingComponent.SimulationObject;
import GraphicComponent.GraphicConstant;
import energyModel.Aggregation;
import energyModel.EnergyModelInterface;



public class WSN extends SimulationObject{
	
	//Graphic Constant
	public static final Dimension INNERSIZE = new Dimension((int)(GraphicConstant.WINDOWWIDTH*0.5), (int)(GraphicConstant.WINDOWWIDTH*0.5));
	public static final Dimension OUTERSIZE = new Dimension(INNERSIZE.width+BaseStation.GRAPHIC_RADIUS*4, INNERSIZE.height+BaseStation.GRAPHIC_RADIUS*4);
	private static Font serifFont = new Font("Serif", Font.BOLD, 20);
	
	
	
	//Used Protocol
	public ClusteringProtocol protocol;
	
	//save the position of WSN in Graphic
	public Rectangle showIdRect;
	
	public double multiple;
	
	public  Object CHListLock = new Object();
	
	
		
	
	
	public WSN(int width, int height, int radius, int maxRadius, long numberOfNode) {
		super();
		// TODO Auto-generated constructor stub
		this.width 	= width;
		this.height = height;
		this.radius	= radius;
		this.maxRadius = maxRadius;
		this.nodeNumber = numberOfNode;
		
		//Other Parameters
		this.multiple = INNERSIZE.getWidth()/this.width;
		this.alphaNodesDead = 0;
		
		
		this.random 		= new Random();
		this.CHlist			= Collections.synchronizedSortedSet(new TreeSet<Node>());
		this.nodeJustDead	= new Vector<Node>();
	}

	@Override
	public void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(Graphics g, double multiple) {
		// TODO Auto-generated method stub
		
		
		//draw Out RectAngle
		int tempX = (int)this.x;
		int tempY = ((int)this.y);
		g.setColor(Color.white);
		//g.setFont(serifFont);
		g.drawRect(tempX, tempY, (int)WSN.INNERSIZE.getWidth(), (int)WSN.INNERSIZE.getHeight());
		this.showIdRect  = new Rectangle(tempX, tempY, (int)WSN.INNERSIZE.getWidth(), (int)WSN.INNERSIZE.getHeight());
		
		
		
		//draw BaseStation
		if(this.BS != null){
			this.BS.render(g, this.multiple);
		}
		
		
		
		
		if(this.wsnNodeList == null) return;
		if(this.wsnNodeList.size() != this.nodeNumber) return;
		
		
		
		//draw Each Node
		Iterator<Node> iterator=this.wsnNodeList.iterator();
		while(iterator.hasNext()){
			(iterator.next()).render(g, this.multiple);
		}
		
		
		
		

		
		
		
		
		//Draw Cluster Line
		this.drawClusterLine(g);
		
		
		
		//Draw Protocol Name and Round Number
		if(this.roundPerformed > 0){
			String roundStr = Long.toString(this.roundPerformed);
			//FontMetrics fm = g.getFontMetrics();
			g.setColor(Color.white);
			//g.setFont(serifFont);
			
			int a	= (int)(WSN.INNERSIZE.getWidth() + 50);
			
			//draw protocol name
			g.drawString(this.protocol.toString(), a, 20);
			//draw round number
			g.drawString(roundStr, (int)BaseStation.GRAPHIC_LOCATION.getX(), 20*3);
		}
		
		Font font = new Font("Serif", Font.PLAIN, 18);
		
		//Number of Node
		String numberOfNodeStr = "Number Of Node : " + this.nodeNumber;
		g.setFont(font);
		g.setColor(Color.white);
		g.drawString(numberOfNodeStr, 3, (int)WSN.INNERSIZE.getHeight() + 20*1);
		
		//Number Of Dead Node
		String aliveNodeStr = "Number Of Alive Node : " + this.aliveNodeList.size();
		g.setFont(font);
		g.setColor(Color.white);
		g.drawString(aliveNodeStr, 3, (int)WSN.INNERSIZE.getHeight() + 20*2);
				
		//Number Of Dead Node
		String deadNodeStr = "Number Of Dead Node : " + this.deadNodeList.size();
		g.setFont(font);
		g.setColor(Color.white);
		g.drawString(deadNodeStr, 3, (int)WSN.INNERSIZE.getHeight() + 20*3);
		
		//Cluster Head
		String CHstr = "Cluster Head : " + this.getCHListString();
		g.setFont(font);
		g.setColor(Color.white);
		g.drawString(CHstr, 3, (int)WSN.INNERSIZE.getHeight() + 20*4);
	}
	private void drawClusterLine(Graphics g) {
		Iterator<Node> iterator1=this.CHlist.iterator();
		while(iterator1.hasNext()){
			Node chNode = (iterator1.next()); 
			
			//draw Line Between CH
			Point child 	= new Point((int)(chNode.x*this.multiple), (int)(chNode.y*this.multiple));
			
			if(chNode.Father != null){
				Point father	= new Point((int)(chNode.Father.x*this.multiple), (int)(chNode.Father.y*this.multiple));
				
				this.drawLineBetweenTwoPoint(child, father, g, Color.red);
			}
			
			
			//draw Line Between ClusterMember and CH
			for(int j=0;j<chNode.clusterMembers.size();j++){
				Node memberNode=chNode.clusterMembers.get(j);
				
				
				
				this.drawLineBetweenTwoNodes(chNode, memberNode, g, Color.white);
			}
		}
	
	}
	private void drawLineBetweenTwoPoint(Point a, Point b, Graphics g, Color color){
		g.setColor(color);
		g.drawLine((int)a.getX(), (int)a.getY(), (int)b.getX(), (int)b.getY());
	}
	private void drawLineBetweenTwoNodes(Node a, Node b, Graphics g, Color color){
		g.setColor(color);
		g.drawLine((int)(a.x*this.multiple), (int)(a.y*this.multiple), (int)(b.x*this.multiple), (int)(b.y*this.multiple));
	}
	
	
	
	/********************************************
	 * Network geometry: a rectangular area
	 *******************************************/
	public double width, height;
	
	/********************************************
	 * WSN node features
	 *******************************************/
	public long nodeNumber;
	public Vector<Node>  wsnNodeList;
	public Vector<Node>  nodeJustDead;			//node that are dead and not entered in the deadNodeList yet
	public Map<Integer, Node> wsnNodeList_KeyValue;
	
	
	
	public TreeSet<Node> aliveNodeList;
	public TreeSet<Node> deadNodeList;
	
	/********************************************
	 * Base station
	 *******************************************/
	public BaseStation BS;
	
	/********************************************
	 * STATS
	 *******************************************/	
	public long roundPerformed=0;
	
	/********************************************
	 * Energy life time
	 *******************************************/	
	public EnergyModelInterface energyModel;
	public long alphaNodesDead;					//The WSN dies when the number of nodes alive is less then alphaNodesDead 
										
	public Random random;  						//to randomly generate the node positions
	
	/**
	 * The list of nodes that are cluster head
	 */
	public SortedSet<Node>	CHlist;
	//public boolean 			CHElectionFinished;
	
	/**
	 * This holds the number of election phases that have been performed
	 */
	public long clusterElectionPhase = 0;
	
	
	public Aggregation aggregation;				//It specifies the aggregation factor.
	
	
	public void resetWSN(){
		Node node;
		
		for(int i=0;i<this.wsnNodeList.size();i++){
			node=this.wsnNodeList.get(i);
			node.resetNode();
		}
		
		
		Iterator<Node> iterator=this.aliveNodeList.iterator();
		Node nodeObej;
		
		while(iterator.hasNext()){
			nodeObej=iterator.next();
			nodeObej.resetNode();
		}
		
		iterator=this.deadNodeList.iterator();
		while(iterator.hasNext()){
			nodeObej=iterator.next();
			nodeObej.resetNode();
			this.aliveNodeList.add(nodeObej);
		}
		
		this.deadNodeList = new TreeSet<Node>();
		this.nodeJustDead = new Vector<Node>();
		
		this.roundPerformed = 0;
	}
	
	//to save the position of a single Node
	public Vector<Point2D.Double>  nodePosition;	
	public void initializeNodePosition(){
		
		this.nodePosition = new Vector<Point2D.Double >();
		for(int i=0;i<this.nodeNumber;i++){
			double x = random.nextDouble()*width; 
			double y = random.nextDouble()*height; 
			Point2D.Double  position = new Point2D.Double (x, y);
			nodePosition.add(i, position);
		}
	}
	/**
	 * this procedure randomly places the nodes in the WSN area, each node has the same initial energy and
	 * the same energy model.
	 * 
	 * @param c a subclass of Node that is needed in order to generate nodes for a specific protocol
	 * @param energyModel the energy model used for the energy consumption of each node
	 * @param BS the base station of the WSN
	 * @param nodeNumber the number of nodes the WSN is composed of
	 * @param alphaNodesDead The WSN dies when the number of nodes alive is less then alphaNodesDead
	 */
	
	public void generateHomogeneousWSN(Class<? extends Node> c, EnergyModelInterface energyModel, Aggregation aggregation){
		this.aliveNodeList			= new TreeSet<Node>();
		this.deadNodeList			= new TreeSet<Node>();
		this.wsnNodeList			= new Vector<Node>();
		this.wsnNodeList_KeyValue	= Collections.synchronizedMap(new HashMap<Integer, Node> ());
		this.roundPerformed=0;
		this.aggregation = aggregation;
		
		if((alphaNodesDead<0)||(alphaNodesDead>nodeNumber)){
			System.out.println("1<=alphaNodesDead<=nodeNumber");
			System.exit(1);
		}
		
		Node temp;
		for(int i = 0; i < this.nodePosition.size(); i++){
			Point2D.Double  position = this.nodePosition.get(i);
			double x = position.getX(); 
			double y = position.getY(); 
			
			try {
				temp = (Node)c.newInstance();
				temp.x 			= x;
				temp.y			= y;
				temp.id 		= i+1;
				temp.energyModel=energyModel;
				temp.wsn = this;
				
				//temp.setRadius();
				
				
				//Calculate the Distance Between BS and Node
				//temp.receiveControl(1, Constant.PACKETSIZE_CONTROL_MESSAGE);
				temp.distanceToBS = Math.sqrt(Math.pow(temp.x-this.BS.x,2)+Math.pow(temp.y-this.BS.y,2));
				if(temp.distanceToBS < BS.distanceOfMin){
					BS.distanceOfMin = temp.distanceToBS;
				}
				if(temp.distanceToBS > BS.distanceOfMax){
					BS.distanceOfMax = temp.distanceToBS;
				}
				
				
				
				this.wsnNodeList.addElement(temp);
				this.aliveNodeList.add(temp);
				
			} catch (InstantiationException e) {
				e.printStackTrace();
				System.exit(1);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				System.exit(1);
			}
			
		}
	}
	
	//Verifies whether or not the node is alive and if it is dead remove it from all
	//neighboring list
	public void freeNodeJustDeadList(){
		//if(this.nodeJustDead.size()==0)return;
		
		
		
		for(int i=0;i<nodeJustDead.size();i++){
			Node deadNode=nodeJustDead.get(i);
			
			
			this.deadNodeList.add(deadNode);
			this.aliveNodeList.remove(deadNode);
			//this.nodeJustDead.remove(deadNode);
			
			Iterator<Node> iterator=deadNode.neighborAtMaxRadiusAlive.iterator();
			
			while(iterator.hasNext()){
				Node temp=iterator.next();
				temp.neighborAtMaxRadiusAlive.remove(deadNode);
				temp.neighborAtRadiusAlive.remove(deadNode);
			} 
		}
		
		this.nodeJustDead = new Vector<Node>();
		this.CHlist = Collections.synchronizedSortedSet(new TreeSet<Node>());
	}
	
	
	/**
	 * This function defines the routing amongst CHs. The routing is based on a tree.
	 * The tree is built starting from the BS and including all reachable CH nodes (this are nodes at level 1). 
	 * All node at level 1 selects all CH at level 2 that can be achieved. When a CH can be reached (from the BS)
	 * via two different CH nodes then: (i) the one having the shortest path to the BS is selected; (ii) when path is equal
	 * then the closest in distance is selected (peharphs the energy is better). This is implemented by using a variation of 
	 * Dijkstra algorithm. 
	 * This procedure could be used to exit the system when some of the CH has not a routing path (not implemented at the moment).
	 */
	public void defineStandardCHRouting() {
		Node node, source, dest;
		Vector<Node> toBeVesited = new Vector<Node>();
		double distance;
		Iterator<Node> iterator;
		
		BS.children = new Vector<Node>();			//children vector is set to empty
		
		BS.sendBroadCastControl(1,Constant.PACKETSIZE_CONTROL_MESSAGE,true);				//all the message at max distance will receive broadcast
		iterator=BS.neighborAtMaxRadiusAlive.iterator();
		while(iterator.hasNext()){
			node=iterator.next();
			if (node.isCH()) {
				BS.children.add(node);
				node.Father = BS;
				node.level=1;
				toBeVesited.add(node);
				
				node.sendControl(BS,1,Constant.PACKETSIZE_CONTROL_MESSAGE);
			}
		}

		
		//Dijkstra is implemented in the following
		while (toBeVesited.size() != 0) {
			source = toBeVesited.get(0);
			toBeVesited.remove(0);
			
			source.sendBroadCastControl(1,Constant.PACKETSIZE_CONTROL_MESSAGE,true);			//all the message at max distance will receive broadcast
			iterator=source.neighborAtMaxRadiusAlive.iterator();
			while(iterator.hasNext()){
				dest=iterator.next();
				
				if(dest.isCH()){
					if (dest.Father == null) {									//father is also used to mark the node
						dest.Father = source;
						dest.level=source.level+1;
						source.childrenCH.add(dest);
						toBeVesited.add(dest);
						dest.sendControl(source,1,Constant.PACKETSIZE_CONTROL_MESSAGE);					//ack with a message whether or not is children
						
					} else {
						if(dest.level>(source.level+1)){
							dest.level=source.level+1;
							node=dest.Father;
							node.childrenCH.remove(dest);
							dest.Father = source;
							
						}else if(dest.level==(source.level+1)){
							distance=source.distance(dest);
							if ( dest.distance(dest.Father)> distance) {
								node=dest.Father;
								node.childrenCH.remove(dest);
								dest.Father = source;
							}
						}
					}
				}
			}
		}
		setInterTrafficLoadFactor(BS);
	}
	
	// preso da inizialise
	public void resetCHformation(){
		Node node;
		Iterator<Node> iterator = this.aliveNodeList.iterator();																				//WSN STATISTICS
		
		while(iterator.hasNext()) {
			node = iterator.next();
			node.resetRoutingInfo();			//First check the resetRoutingInfo() method of subclass of Node
		}
	}
	
	/**
	 * Set the inter-cluster load for the node v. 
	 * This is the number of CHs forwarding data to v.
	 * 
	 * @param v a CH
	 * @return the inter-traffic load
	 */
	public int setInterTrafficLoadFactor(Node v) {
		
		if (v.childrenCH.size() == 0) {
			v.visited = true;
			v.inter_traffic_load_factor = 0;
			v.total_inter_trafficLoad+=v.inter_traffic_load_factor;
			if(v.max_inter_trafficLoad<v.inter_traffic_load_factor){
				v.max_inter_trafficLoad=v.inter_traffic_load_factor;
			}
			if(v.min_inter_trafficLoad>v.inter_traffic_load_factor){
				v.min_inter_trafficLoad=v.inter_traffic_load_factor;
			}
			return 1;
		} else {
			v.total_cluster_size+=v.clusterMembers.size();
			if(v.max_cluster_size<v.clusterMembers.size()){
				v.max_cluster_size=v.clusterMembers.size();
			}
			if(v.min_cluster_size<v.clusterMembers.size()){
				v.min_cluster_size=v.clusterMembers.size();
			}
			int number_of_CH_in_subTree = 0;
			for (int i = 0; i < v.childrenCH.size(); i++) {
				if (!v.childrenCH.get(i).visited) {
					number_of_CH_in_subTree += setInterTrafficLoadFactor(v.childrenCH.get(i));
				}
			}
			v.inter_traffic_load_factor = number_of_CH_in_subTree;
			v.total_inter_trafficLoad+=v.inter_traffic_load_factor;
			if(v.max_inter_trafficLoad<v.inter_traffic_load_factor){
				v.max_inter_trafficLoad=v.inter_traffic_load_factor;
			}
			if(v.min_inter_trafficLoad>v.inter_traffic_load_factor){
				v.min_inter_trafficLoad=v.inter_traffic_load_factor;
			}
			return number_of_CH_in_subTree;
		}
	}
	
	public String toString(){
		String content="WSN HEIGHT,WSN WIDTH,WSN NODE NUMBER,NODE ALIVE NUMBER,NODE DEAD NUMBER,LIFETIME MEASURE,ROUND\n";
		
		content+=this.height+","+this.width+","+this.nodeNumber+","
						+this.aliveNodeList.size()+","+this.deadNodeList.size()+","
						+this.alphaNodesDead+","+this.roundPerformed;
		
		return content;
	}
	
	public String getCHListString(){
		String headsStr = "";
		Node node;
		Iterator<Node> iterator=this.CHlist.iterator();

		while(iterator.hasNext()){
			node = iterator.next();
			headsStr = headsStr + node.id + ",";
		}
		
		return headsStr;
	}
	public String getDeadNodeListString(){
		String headsStr = "";
		Node node;
		
		Iterator<Node> iterator=this.deadNodeList.iterator();

		while(iterator.hasNext()){
			node = iterator.next();
		
			headsStr = headsStr + node.id + " : "+ node.energyResidual + node.isCH() + "\n";
		}
		
		return headsStr;
	}
}
