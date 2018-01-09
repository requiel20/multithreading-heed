package HEED;

import java.util.Collections;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.Semaphore;

import core.Constant;
import core.Node;

public class HEEDNODE extends Node {
	
	public double Cprob=0.05;
	public double CHprob=Cprob;
	public double pmin=0.0005;
	public double CHpro_previous=Cprob;
	
	public SortedSet<HEEDNODE> SCH;				//within max radius the node that are tentative

	public HEEDNODE(){
		super();
		
		SCH = Collections.synchronizedSortedSet(new TreeSet<HEEDNODE>());
	}
	
	@Override
	public void setRadius(){
		super.setRadius();
	}
	
	
	public void resetRoutingInfo(){
		super.resetRoutingInfo();
		
		CHprob=Cprob*(energyResidual/Constant.maxEnergy);
		if(pmin>CHprob) CHprob=pmin;
		SCH= Collections.synchronizedSortedSet(new TreeSet<HEEDNODE>());
		this.CHpro_previous=CHprob;
	}
	
	/**
	 * A node selects its cluster based on the distance. More precisely, 
	 * the node select the closest CH (this can be itself).
	 *  
	 * @return the node selected as cluster head. NULL when there is no 
	 * node in the set.
	 */
	//solo scritture locali e letture
	public HEEDNODE leastCHCost(){
		HEEDNODE least_cost=null;
		
		double distanceMin = Double.MAX_VALUE;
		
		synchronized (SCH) {
		
			if(this.SCH.size()!=0){
				Iterator<HEEDNODE> iterator = SCH.iterator();
				while (iterator.hasNext()) {
					HEEDNODE temp = iterator.next();
					double distance = temp.distance(this);
					if(distance < distanceMin){
						distanceMin=distance;
						least_cost=temp;
					}
				}
			}
		}
		return least_cost;
	}
	
	/**
	 * The node sent a tentative message (it proposes itself as cluster head).
	 * The sender send a control message. Al the neighbor receive the control message.
	 * Add itself in its set of tentative CH node,i.e., SCH. 
	 * 
	 */
	public void sendTentativeCH(){
		//System.out.print("I want to be a CH.");
		boolean dead = this.sendBroadCastControl(1, Constant.PACKETSIZE_CONTROL_MESSAGE, false);
		if(dead) return;		//After sendBroadCastControl, maybe it will die
		
		this.SCH.add(this);
		this.addToNeighborSCH();		//The Node inform all its Neighbors that it wants to be a CH
	}
	
	/**
	 * The node is now cluster head.
	 * The node send a control message. Al the neighbor receive the control message 
	 */
	public void sendFinalCH(){
		//System.out.print("I am Selected to be a CH.");
		boolean dead = this.sendBroadCastControl(1, Constant.PACKETSIZE_CONTROL_MESSAGE, false);		//After sendBroadCastControl, maybe it will die
		//if(dead) return;
		
		this.SCH.add(this);
		this.addToNeighborSCH();
		
		this.electNode();		//It becomes a CH
	}
	public void addToNeighborSCH(){
		
		Iterator<Node> iterator = this.neighborAtRadiusAlive.iterator();		//We use small Radius
		
		while(iterator.hasNext()){
			HEEDNODE neighborNode = (HEEDNODE) iterator.next();
			neighborNode.SCH.add(this);
		}
	}
	
	/**
	 * The node is now cluster head.
	 * The node send a control message. Al the neighbor receive the control message 
	 */

	
	public void sendRouting(){
		this.sendBroadCastControl(1, Constant.PACKETSIZE_CONTROL_MESSAGE,true);
	}
	
	/**
	 * Double the node's probability of becoming CH
	 */
	public void increaseCHprob(){		
		CHpro_previous=CHprob;
		CHprob=CHprob*2.0;
		if(CHprob>1) CHprob=1.0;
	}

	/*public String toString(){
		String temp= "{node="+id+";x="+x+";y="+y+"CHprob"+CHprob+";isCH="+isCH+";\n";
		HEEDNODE node;
		for(int i=0;i<neighborList.size();i++){
			node=(HEEDNODE)neighborList.get(i);
			temp+="[neighbor id="+neighborList.get(i).id+" x="+neighborList.get(i).x+" y="+neighborList.get(i).y+
					"CHprob"+node.CHprob+";isCH="+node.isCH+";\n";;
		}
		temp+="}\n";
		return temp;
	}*/
	public String getString(){
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
		+ this.energyModel + comma
		+ this.CHpro_previous + comma
		+ this.CHprob + comma
		+ "Comma" + "\n";
		return content;
	}
	public String getSCHstring(){
		String str = "";
		
		synchronized (SCH) {
		
			Iterator<HEEDNODE> iterator = this.SCH.iterator();
			
			while(iterator.hasNext()){
				HEEDNODE node = iterator.next();
				str = str + node.id + ", ";
			}
		}
		
		return str;
	}

	
}
