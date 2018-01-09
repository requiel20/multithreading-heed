package core;

import java.awt.Color;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.atomic.AtomicLong;

import DrawingComponent.NodeCircleShape;
import HEED.HEEDNODE;
import energyModel.EnergyModelInterface;

public class Node extends NodeCircleShape implements Comparable<Object>{
	
	private final Object energyResidual_lock = new Object();
	private final Object recievedMessages_lock = new Object();
	private final Object sentMessages_lock = new Object();

	public Node() {
		super();
		// TODO Auto-generated constructor stub
		
		neighborAtMaxRadius=new Vector<Node>();
		neighborAtRadius=new Vector<Node>();
		clusterMembers=new Vector<Node>();
		childrenCH=new Vector<Node>();
	}

	
	
	
	
	
	
	//Professor
	public WSN wsn;										//the WSN the node is part of
	/**
	 * Geometric features: 
	 * - (x,y) are Node's coordinates
	 * - radius is the radius of the circle defining the node coverage area default is 50
	 * - maxRadius is the radius of the maximum coverage area for the node
	 * - id is the unique address of the node
	 * ***** By Team : We moved this four parameters to the abstract object "SimulationObject", because we have to
	 * use them to generate the graphic
	 */
	//public double	x,y;
	//public double radius	= 40.0;
	//public double maxRadius		= 75.0;
	//public int 	_id;
	
	public int packet_trasmission_rate_per_round=1;
	
	/**
	 * energy variables:
	 * - energyModel an interface containing primitive for energy consumption
	 * - energyResidual the amount of energy remaining for the node
	 */
	public EnergyModelInterface energyModel;
	public double energyResidual=Constant.maxEnergy;
	public boolean nodeDead=false;
	
	/**
	 * STATISTICS VARIABLES
	 */
	
	//RECEIVED MSG
	protected long totalReceveivedMessages=0;		//total amount of messages received by the node totalReceveivedMessages=totalReceivedControlMessages + totalReceivedIntraTraffic + totalReceivedInterTraffic
	protected long totalReceivedControlMessages= 0;	//messages received during election, cluster formation and routing establishment
	protected long totalReceivedIntraTraffic=0;		//total amount of messages received due to intra cluster communication (when the ndoe is CH)
	protected long totalReceivedInterTraffic=0;		//total amount of messages received due to inter cluster communication (when the ndoe is CH)
	
	//SENT MSG
	protected long totalSentMessages=0;				//total amount of messages sent by the node totalSentMessages=totalSentControlMessages + totalSentIntraMessages + totalSentInterMessages
	protected long totalSentControlMessages=0;		//messages sent during election, cluster formation and routing establishment
	protected long totalSentIntraMessages=0;		//total amount of messages sent due to intra cluster communication (when the node is member)
	protected long totalSentInterMessages=0;		//total amount of messages sent due to inter cluster communication (when the node is CH)
	
	//INTER TRAFFIC LOAD
	public long total_inter_trafficLoad=0;				//sum of the inter_traffic load over the time (total_inter_trafficLoad/CH_frequncy is the avarage)
	public long max_inter_trafficLoad=0;				//gives ideas about the cluster size 
	public long min_inter_trafficLoad=Long.MAX_VALUE;	//gives ideas about the cluster size should decrease as less nodes
	
	//CLUSTER INFO
	public long total_cluster_size=0;			//at each round we sum to this number the number of memebers inside the cluster
	public long max_cluster_size=0;				
	public long min_cluster_size=Long.MAX_VALUE;
	
	//CH INFO
	public long total_level=0;					//total sum of the levels when the node is CH (to calculate the avarage level)
	public long roundPerformed=0;				//number of round performed by the node before its death
	public long CH_frequncy=0;					//how many time the node has been CH

	/**
	 * NEIGHBOR INFORMATION
	 * 
	 */
	//STATIC INFO
	public Vector<Node> neighborAtMaxRadius;		//these are nodes that are within the maximum transmission radius
	public Vector<Node> neighborAtRadius;			//these are nodes that are within the selected radius radius<MaxRadius (Max Radius contains everything)
	
	//DINAMIC INFO
	public  TreeSet<Node> neighborAtMaxRadiusAlive;	//all neighbor that are alive
	public  TreeSet<Node> neighborAtRadiusAlive;	//all neighbor that are alive
	
	/**
	 * CH ROUTING VARIABLE 
	 */
	public Node Father=null;					//the father of this node in the routing tree (if it is CH)					
	public Vector<Node> childrenCH;				//the children CH of this node in the routing tree (if it is CH)	
	public int level; 							//0 is the BS, 1 when distance is one from BS, 2 ...
	public boolean visited=false;				//to manage the routing formation (bread first visit during inter traffic load factor setting)
	public int inter_traffic_load_factor;		//this defines the number of CH forwarding messages to this node
	public boolean isCH=false;					//whether or not the node is currently a CH
	public Vector<Node> clusterMembers;			//the cluster members of the node (if it is CH)
	
	
	
	public boolean isCH() {
		return isCH;
	}

	public void resignCH() {
		this.isCH = false;
	}

	//increase statics only when node is relected
	protected void electNode() {
		if(this.isCH) return;
		this.isCH = true;
		this.CH_frequncy++;
		this.color = Color.red;
	}
	
	public void resetNode(){
		resetRoutingInfo();
		resetEnergy();
		resetStats();
		this.color=Color.white;
	}
	private void setToDeadNode(){
		wsn.nodeJustDead.add(this);
		this.nodeDead	= true;
		this.color		= Color.yellow;
		synchronized (this.energyResidual_lock) { 
			System.out.println(this.energyResidual);}
	}
	
	public void resetRoutingInfo(){
		Father=null;										
		childrenCH=new Vector<Node>();	
		level=0; 							
		visited=false;				
		inter_traffic_load_factor=0;		
		isCH=false;					
		clusterMembers=new Vector<Node>();	
		
		
		if(this.nodeDead){
			this.color=Color.yellow;
		}
		else{
			this.roundPerformed++;			//if the Node is alive, it will  perform next Round.
			this.color=Color.white;
		}
		
	}
	
	public void resetEnergy(){
		energyResidual=Constant.maxEnergy;
		nodeDead=false;
	}
	
	public void resetStats(){
		totalReceveivedMessages=0;		
		totalReceivedControlMessages=0;
		totalReceivedIntraTraffic=0;		
		totalReceivedInterTraffic=0;

		totalSentMessages=0;			
		totalSentControlMessages=0;		
		totalSentIntraMessages=0;		
		totalSentInterMessages=0;		

		total_inter_trafficLoad=0;
		max_inter_trafficLoad=0;
		min_inter_trafficLoad=Long.MAX_VALUE;
		
		total_cluster_size=0;
		max_cluster_size=0;				
		min_cluster_size=0;
		
		total_level=0;
		roundPerformed=0;
		CH_frequncy=0;
	}
	
	/**
	 * Constructor 
	 */
	
	
	//return true if any of the nodes dies during operations
	public boolean sendBroadCastControl(int number_of_messages,int message_bits_size,boolean isMaxDistance){
		if(this.nodeDead) return true;
		
		
		
		double			distance;
		Iterator<Node>	iterator;
		
		if(isMaxDistance){
			distance = this.maxRadius;
			iterator = this.neighborAtMaxRadiusAlive.iterator();
		}
		else{
			distance = this.radius;
			iterator = this.neighborAtRadiusAlive.iterator();
		}
		
		int messagesSendable;
		synchronized (this.energyResidual_lock) { messagesSendable = (int)(this.energyResidual/this.energyModel.send(message_bits_size, distance));}

		while(iterator.hasNext()){
			Node temp = iterator.next();
			temp.receiveControl((messagesSendable <= number_of_messages ? messagesSendable:number_of_messages), message_bits_size);
		}
		
		if(messagesSendable <= number_of_messages){
			this.totalSentMessages+=messagesSendable;
			this.totalSentControlMessages+=messagesSendable;
			synchronized (this.energyResidual_lock) {this.energyResidual-=
					messagesSendable*this.energyModel.send(message_bits_size, distance);}
			
			this.setToDeadNode();
			return true;
		}else{
			this.totalSentMessages+=number_of_messages;
			this.totalSentControlMessages+=number_of_messages;
			synchronized (this.energyResidual_lock) {this.energyResidual-=
					number_of_messages*this.energyModel.send(message_bits_size, distance);}
			return false;
		}
	}
	//will automatically update also the receiver (tornare se muore qulcuno)
	public boolean sendControl(Node receiver,int number_of_messages,int message_bits_size){
		if(this.nodeDead) return true;
		
		
		double distance=this.distance(receiver);
		
		int messagesSendable;
		synchronized (this.energyResidual_lock) { messagesSendable = (int)(this.energyResidual/this.energyModel.send(message_bits_size, distance));}
		
		if(messagesSendable<=number_of_messages){
			
			this.totalSentMessages+=messagesSendable;
			this.totalSentControlMessages+=messagesSendable;
			synchronized (this.energyResidual_lock) { this.energyResidual-=
					messagesSendable*this.energyModel.send(message_bits_size, distance);}
			receiver.receiveControl(messagesSendable, message_bits_size);
			
			this.setToDeadNode();
			
			return true;
		}else{
			this.totalSentMessages+=number_of_messages;
			this.totalSentControlMessages+=number_of_messages;
			synchronized (this.energyResidual_lock) { this.energyResidual-=
					number_of_messages*this.energyModel.send(message_bits_size, distance);}
			receiver.receiveControl(number_of_messages, message_bits_size);
			return false;
		}
	}
	public boolean receiveControl(int number_of_messages,int message_bits_size){
if(this.nodeDead) return true;
		
		
		int receivableMessage;
		
		synchronized (this.energyResidual_lock) {receivableMessage = (int)(this.energyResidual/this.energyModel.receive(message_bits_size));}
		
		if(receivableMessage<=number_of_messages){
			synchronized (this.recievedMessages_lock) {
				this.totalReceveivedMessages+=receivableMessage;
				this.totalReceivedControlMessages+=receivableMessage;
			}
			synchronized (this.energyResidual_lock) {this.energyResidual-=
					receivableMessage*this.energyModel.receive(message_bits_size);}
			
			this.setToDeadNode();
			return true;
		}else{
			synchronized (this.recievedMessages_lock) {
				this.totalReceveivedMessages+=number_of_messages;
				this.totalReceivedControlMessages+=number_of_messages;
			}
			synchronized (this.energyResidual_lock) {this.energyResidual-=
					number_of_messages*this.energyModel.receive(message_bits_size);}
			return false;
		}
	}  
	//will automatically update also the receiver
	public boolean sendData(Node receiver,int number_of_messages,int message_bits_size,boolean isIntra){
		if(this.nodeDead) return true;
		
		
		
		double distance=this.distance(receiver);
		
		int messagesSendable=(int)(this.energyResidual/this.energyModel.send(message_bits_size, distance));
		
		if(messagesSendable<=number_of_messages){
			this.totalSentMessages+=messagesSendable;
			if(isIntra){
				this.totalSentIntraMessages+=messagesSendable;
				receiver.receiveData(messagesSendable, message_bits_size,true);
			}
			else{
				this.totalSentInterMessages+=messagesSendable;
				receiver.receiveData(messagesSendable, message_bits_size,false);
			}
			this.energyResidual-=messagesSendable*this.energyModel.send(message_bits_size, distance);
			this.setToDeadNode();
			return true;
		}else{
			this.totalSentMessages+=number_of_messages;
			if(isIntra){
				this.totalSentIntraMessages+=number_of_messages;
				receiver.receiveData(number_of_messages, message_bits_size,true);
			}
			else{
				this.totalSentInterMessages+=number_of_messages;
				receiver.receiveData(number_of_messages, message_bits_size,false);
			}
			this.energyResidual-=number_of_messages*this.energyModel.send(message_bits_size, distance);
			return false;
		}
	}
	
	public boolean receiveData(int number_of_messages,int message_bits_size,boolean isIntra){
		if(this.nodeDead) return true;
		
		
		
		int receivableMessage=(int)(this.energyResidual/this.energyModel.receive(message_bits_size));
		
		if(receivableMessage<=number_of_messages){
			this.totalReceveivedMessages+=receivableMessage;
			if(isIntra){
				this.totalReceivedIntraTraffic+=receivableMessage;
			}
			else{
				this.totalReceivedInterTraffic+=receivableMessage;
			}
			this.energyResidual-=receivableMessage*this.energyModel.receive(message_bits_size);
			
			this.setToDeadNode();
			return true;
		}else{
			this.totalReceveivedMessages+=number_of_messages;
			if(isIntra){
				this.totalReceivedIntraTraffic+=number_of_messages;
			}
			else{
				this.totalReceivedInterTraffic+=number_of_messages;
			}
			this.energyResidual-=number_of_messages*this.energyModel.receive(message_bits_size);
			return false;
		}
	} 
	
	

	

	public void setRadius(){
		this.radius 	= this.wsn.radius;
		this.maxRadius	= this.wsn.maxRadius;
	}
	
	public double distance(Node dest){
		if(dest == null){
			return 0;
		}
		return Math.sqrt(Math.pow(this.x-dest.x,2)+Math.pow(this.y-dest.y,2));
	}
	
	//add control messages this is called when the network is created
	public void setNeighbor(){
		Node dest;
		double distance;
		
		neighborAtRadiusAlive		= new  TreeSet<Node>();
		neighborAtMaxRadiusAlive	= new  TreeSet<Node>();
		
		Iterator<Node> iterator=wsn.aliveNodeList.iterator();

		while(iterator.hasNext()){
			dest = (HEEDNODE) iterator.next();
		
			if(dest.id!=this.id){
				distance=this.distance(dest);
				if(distance<this.radius){						//All nodes within  radius are included in both sets
					this.totalSentControlMessages++;
					synchronized (dest) {dest.totalReceivedControlMessages++;}
					this.neighborAtMaxRadius.add(dest);
					this.neighborAtRadius.add(dest);
					this.neighborAtMaxRadiusAlive.add(dest);
					this.neighborAtRadiusAlive.add(dest);		
				}else if(distance<this.maxRadius){				//Node N s.t.  radius<distance(N) are included only in neighborAtMaxRadiusAlive
					this.totalSentControlMessages++;
					synchronized (dest) {dest.totalReceivedControlMessages++;}
					this.neighborAtMaxRadius.add(dest);
					this.neighborAtMaxRadiusAlive.add(dest);
				}
			}
		}
	}
	
	public boolean	equals(Object obj){
		Node other =(Node) obj;
		if(this.id==other.id){
			return true;
		}
		return false;
	}
	
	
	synchronized public static String getNodeFullHeadLine() {
		return "ID,X,Y,RESIDUAL ENERGY,CLUSTER RADIUS,MAX RADIUS,MESSAGE SENT PER TDMA"
				+ ",TOT REC MSG,REC CTRL MSG,REC INTRA MSG,REC INTER MSG"
				+ ",TOT SENT MSG,SENT CTRL MSG,SENT INTRA MSG,SENT INTER MSG"
				+ ",AVG INTER TRAFFIC LOAD,MAX INTER TRAFFIC LOAD,MIN INTER TRAFFIC LOAD,"
				+ ",AVG CLUSTER SIZE,MAX CLUSTER SIZE,MIN CLUSTER SIZE"
				+ ",ROUND PERFORMED,CH FREQUENCY,AVARAGE LEVEL"
				+ ",MAX NEIGHBOR LIST";
	}
	
	public String toString(){
		String comma = ",";
		double avg_inter_traffic_load=0;
		double avg_cluster_size=0;
		double avarage_level=0;
		
		if(this.CH_frequncy!=0){
			avg_inter_traffic_load=(double)this.total_inter_trafficLoad / (double) CH_frequncy; 
		}
		if(this.CH_frequncy!=0){
			avg_cluster_size=(double)this.total_cluster_size / (double) CH_frequncy; 
		}
		if(this.CH_frequncy!=0){
			avarage_level=(double)this.total_level / (double) CH_frequncy; 
		}
		String content = this.id + comma
		+ "(" + (int)this.x + "-" + (int)this.y + ")" + comma
		+ this.energyResidual + comma
		+ this.radius + comma
		+ this.maxRadius + comma
		+ this.packet_trasmission_rate_per_round + comma
		+ this.totalReceveivedMessages + comma
		+ this.totalReceivedControlMessages + comma
		+ this.totalReceivedIntraTraffic + comma
		+ this.totalReceivedInterTraffic + comma
		+ this.totalSentMessages + comma
		+ this.totalSentControlMessages + comma
		+ this.totalSentIntraMessages + comma 
		+ this.totalSentInterMessages + comma
		+ avg_inter_traffic_load + comma
		+ this.max_inter_trafficLoad + comma
		+ this.min_inter_trafficLoad + comma 
		+ avg_cluster_size + comma
		+ this.max_cluster_size + comma 
		+ this.min_cluster_size + comma 
		+ this.roundPerformed + comma
		+ this.CH_frequncy + comma 
		+ avarage_level + comma
		+ this.printNeighborList() + comma
		+ this.energyModel ;
		
		return content;
	}
	public String toHTMLlistString(){
		double avg_inter_traffic_load	= 0;
		double avg_cluster_size			= 0;
		double avarage_level			= 0;
		
		if(this.CH_frequncy!=0){
			avg_inter_traffic_load = (double)this.total_inter_trafficLoad / (double) CH_frequncy; 
		}
		if(this.CH_frequncy!=0){
			avg_cluster_size = (double)this.total_cluster_size / (double) CH_frequncy; 
		}
		if(this.CH_frequncy!=0){
			avarage_level = (double)this.total_level / (double) CH_frequncy; 
		}
		
		
		
		String content = 
		"<td>" + this.id + "</td>"
		+ "<td>" + "(" + (int)this.x + "," + (int)this.y + ")" + "</td>"
		+ "<td>" + String.format("%.8f", energyResidual) + "</td>"
		+ "<td>" + radius + "</td>"
		+ "<td>" + maxRadius + "</td>"
		+ "<td>" + packet_trasmission_rate_per_round + "</td>"
		+ "<td>" + totalReceveivedMessages + "</td>"
		+ "<td>" + totalReceivedControlMessages + "</td>"
		+ "<td>" + totalReceivedIntraTraffic + "</td>"
		+ "<td>" + totalReceivedInterTraffic + "</td>"
		+ "<td>" + totalSentMessages + "</td>"
		+ "<td>" + totalSentControlMessages + "</td>"
		+ "<td>" + totalSentIntraMessages + "</td>" 
		+ "<td>" + totalSentInterMessages + "</td>"
		+ "<td>" + avg_inter_traffic_load + "</td>"
		+ "<td>" + max_inter_trafficLoad + "</td>"
		+ "<td>" + min_inter_trafficLoad + "</td>" 
		+ "<td>" + avg_cluster_size + "</td>"
		+ "<td>" + max_cluster_size + "</td>" 
		+ "<td>" + min_cluster_size + "</td>" 
		+ "<td>" + roundPerformed + "</td>"
		+ "<td>" + CH_frequncy + "</td>" 
		+ "<td>" + avarage_level + "</td>"
		+ "<td>" + energyModel + "</td>"
		+ "<td>" + printNeighborList() + "</td>";
		
		
		
		return content;
	}
	
	public String printNeighborList(){
		String content= "";
		Node neighbor;
		int i;
		
		if(neighborAtMaxRadius.size()==0){
			content+="\"";
			return content;
		}
		
		for(i=0;i<neighborAtMaxRadius.size();i++){
			neighbor=neighborAtMaxRadius.get(i);
			content+=neighbor.id + ",";
		}
		
		return content;
	}
	public String printClusterMember(){
		String str = "";
		
		if(this.isCH){
			if(this.clusterMembers.size() > 0){
				for(int j=0;j<this.clusterMembers.size();j++){
					Node memberNode = this.clusterMembers.get(j);
					
					str = str + memberNode.id + ", ";
				}
			}
			else{
				str = "CH - " + this.id + " does not have any clusterMembers";
			}
		}
		else{
			Node fatherNode = this.Father;
			
			str = this.id + " is Not a CH, but its CH " + fatherNode.id + " : ";
			
			if(fatherNode.clusterMembers.size() > 0){
				for(int j=0;j<fatherNode.clusterMembers.size();j++){
					Node memberNode = fatherNode.clusterMembers.get(j);
					
					str = str + memberNode.id + ", ";
				}
			}
			else{
				str = "CH - " + fatherNode.id + " does not have any clusterMembers";
			}
		}
		
		
		
		return str;
	}

	@Override
	public int compareTo(Object o) {
		if(o instanceof Node){
			return (int)(this.id-((Node)o).id);
		}
		return 0;
	}
	
	
	
	public double distanceToBS;	
	
}
