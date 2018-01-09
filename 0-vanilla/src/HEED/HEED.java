package HEED;

import java.util.*;

import core.Constant;
import core.Node;
import core.ClusteringProtocol;
import core.WSN;
import unicam.wsn.simulation.TestWriter;

public class HEED extends ClusteringProtocol{

	public HEED(WSN wsn) {
		super(wsn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setRadiusAndNeighbors() {
		
		TestWriter.getTestWriter().addField("setRadiusAndNeighbors");
		
		// TODO Auto-generated method stub
		
		long millPassed = System.nanoTime();
		
		for(int i=0;i<this.wsn.wsnNodeList.size();i++){
			HEEDNODE temp = (HEEDNODE)this.wsn.wsnNodeList.get(i);
			
			temp.setRadius();
			temp.setNeighbor();
			
			this.wsn.wsnNodeList_KeyValue.put(temp.id, temp);
			
			if(Constant.debugTest) System.out.println("Adding Neighbor" + temp.id + ": " + temp.printNeighborList());
		}

		//DEALING WITH BS
		this.wsn.BS.setNeighbor();

		millPassed = System.nanoTime() - millPassed;
		
		TestWriter.getTestWriter().addValue("setRadiusAndNeighbors", millPassed);
	}
	
	
	
	/**
	 * Simulate the HEED protocol until alphaNodesDies of the nodes die. The MIT energy model (FROM LEACH PAPER)
	 * is used.
	 * 
	 * @param height the height of the AREA that contains the WSN nodes (meters)
	 * @param width the width of the AREA that contains the WSN nodes (meters)
	 * @param nodeNumber the number of nodes inside the WSN
	 * @param TDMA number of TDMA per round
	 * @param alphaNodesDies the simulation stops after alphaNodesDies are dead
	 */
	@Override
	public void ALPHANODEDIE_MIT_SIMULATION() {
		// TODO Auto-generated method stub
	}
	

	
	
	
	/**
	 * During initialization all the parameters of a nodes are reset,
	 * i.e., its probability, its CH status, its children, its father.
	 * THIS PROCEDURE CHECKS WHETHER OR NOT THE NODE IS DEAD. When alphaNodesDead 
	 * are dead the statistics are called and the system EXIT.
	 */
	public void initialse() {
		
		TestWriter.getTestWriter().addField("initialize");
		long millPassed = System.nanoTime();

		wsn.resetCHformation();
		wsn.freeNodeJustDeadList();
	
		millPassed = System.nanoTime() - millPassed;
		TestWriter.getTestWriter().addValue("initialize", millPassed);
	}

	/**
	 * This procedure is copied from the paper
	 * <<HEED: A Hybrid, Energy-Efficient, Distributed Clustering Approach for Ad-hoc Sensor Networks>>.
	 * I have also used the same variable names.
	 */

	public void repeat() {
		TestWriter.getTestWriter().addField("repeat");
		long millPassed = System.nanoTime();
		
		boolean continueWhile;				//true when the do-while has to be continued
											//to iterate over all the WSN nodes
		Random random = new Random();
		
		do{
			continueWhile = false;
			
			Iterator<Node> iterator=wsn.aliveNodeList.iterator();
			while(iterator.hasNext()){
				HEEDNODE node = (HEEDNODE) iterator.next();
				//System.out.print(node.id + ":" + node.CHprob + " -----> ");
				if(node.CHpro_previous < 1){
					continueWhile=true;									//only when all node have prob=1 the while terminates
					if(node.SCH.size() != 0){							//someone proposed as CH. Can be the node itself
						//System.out.print(node.getSCHstring() + " - ");
						//if(node.SCH.size() > 1) System.out.print("     ***** " + node.SCH.size());
						HEEDNODE my_cluster_head = node.leastCHCost();	//I select the node with least cost
						if(my_cluster_head.id == node.id){				//least cost is my self 
							if (node.CHprob == 1) {						//the node is CH
								node.sendFinalCH();						//a final CH message is sent
							} 
							else {
								node.sendTentativeCH();					//probability still not one thus a tentative is sent
							}
						}
					}
					else if (node.CHprob == 1) {						//the node elects itself since no one proposed
						node.sendFinalCH();
					}
					else if (random.nextDouble() <= node.CHprob) {		//the node tries it chances to become CH
						node.sendTentativeCH();
					}
					else{
						//System.out.print("Sleeping");
					}
					
					node.increaseCHprob();								//PROB, OF NODE is always increased
				}
				System.out.println(" ");
			}
			//System.out.println("\n\n\n\n\n**********Try Again");
			
			
			
		} while (continueWhile);
		
		if(Constant.debugTest) System.out.println("FINISHED repeat Function"+this.wsn.CHlist+"\n\n\n");
		
		millPassed = System.nanoTime() - millPassed;
		TestWriter.getTestWriter().addValue("repeat", millPassed);

	}
	
	/**
	 * This procedure is copied from the paper
	 * <<HEED: A Hybrid, Energy-Efficient, Distributed Clustering Approach for Ad-hoc Sensor Networks>>.
	 * I have also used the same variable names. When the WSN is not dense the number of CHs can drastically increase
	 * thus clustering is not effective.
	 * 
	 */
	public void finalise() {		
		
		TestWriter.getTestWriter().addField("finalise");
		long millPassed = System.nanoTime();
		try{
			Iterator<Node> iterator=wsn.aliveNodeList.iterator();

			while(iterator.hasNext()){
				HEEDNODE node = (HEEDNODE) iterator.next();
			
				if (node.isCH() == false) {												//The node is not CH
					if(node.SCH.size()!=0){												//Some of its neighbors proposed
						HEEDNODE least_cost = node.leastCHCost();						//I get the one with least cost
						node.sendControl(least_cost,1, Constant.PACKETSIZE_CONTROL_MESSAGE);			//THIS IS A JOIN MESSAGE
						least_cost.clusterMembers.add(node);	
					}else{																//The node elects itself since no node proposed
						node.sendFinalCH();												//WHEN THERE is a small number of NODES THE total number of CHs can increase
						wsn.CHlist.add(node);
					}	
				} else {
					node.sendFinalCH();
					wsn.CHlist.add(node);
				}
			}
		}
		catch(Exception e){
			System.out.println("Error in finalise()");
			System.exit(0);
		}

		if(Constant.debugTest) System.out.println("Succesfful FINISHED finalise Function and wsn.CHlist are : " + this.wsn.getCHListString() + "\n\n\n");
		
		millPassed = System.nanoTime() - millPassed;
		TestWriter.getTestWriter().addValue("finalise", millPassed);
	}
	
	public void repeat2() {											//to iterate over all the WSN nodes
		Random random = new Random();
		HEEDNODE node, my_cluster_head;		

		do{
			if(Constant.debugTest) System.out.println("\n\n\n**********Try Again");
			
			Iterator<Node> iterator=wsn.aliveNodeList.iterator();
						
			while(iterator.hasNext()){
				node = (HEEDNODE) iterator.next();
				if(Constant.debugTest) System.out.print(node.id + " -> ");
				if(node.CHpro_previous<1){
					if(node.SCH.size()!=0){								//someone proposed as CH. Can be the node itself
						if(Constant.debugTest) System.out.print(node.id + "->" + node.SCH + " ");
						my_cluster_head = node.leastCHCost();			//I select the node with least cost
						if(my_cluster_head.id==node.id){				//least cost is my self 
							if (node.CHprob == 1) {						//the node is CH
								node.sendFinalCH();						//a final CH message is sent
							} else {
								node.sendTentativeCH();					//probability still not one thus a tentative is sent
							}
						}
					}else if (node.CHprob == 1) {						//the node elects itself since no one proposed
						node.sendFinalCH();
					}else if (random.nextDouble() <= node.CHprob) {		//the node tries it chances to become CH
						node.sendTentativeCH();
					}
					else{
						if(Constant.debugTest) System.out.println(node.id + " : Sleeping *** " + node.SCH);
					}
					if(Constant.debugTest) System.out.println(" ");
					node.increaseCHprob();								//PROB, OF NODE is always increased
				}
			}
		} while (!checkIfAllNodesProbIsOne());
		
		if(Constant.debugTest) System.out.println("FINISHED repeat Function"+this.wsn.CHlist+"\n\n\n");
	}
	private boolean checkIfAllNodesProbIsOne(){					//only when all node have prob=1 the while terminates
		Iterator<Node> iterator=wsn.aliveNodeList.iterator();
		while(iterator.hasNext()){
			HEEDNODE node = (HEEDNODE) iterator.next();
			if(node.CHpro_previous < 1){
				return false;
			}
		}
		
		return true;
	}


	@Override
	public void runRound() {
		// TODO Auto-generated method stub
		
		int interTraffic;
		Node chNode,node;
		Iterator<Node> iterator = this.wsn.CHlist.iterator();

																						
		
		while(iterator.hasNext()) {
			chNode = iterator.next();
			
			if(chNode.clusterMembers.size() > 0){
				for(int j=0;j<chNode.clusterMembers.size();j++){
					node=chNode.clusterMembers.get(j);
					node.sendData(chNode,TDMA*node.packet_trasmission_rate_per_round, Constant.PACKETSIZE_DATA_MESSAGE,true);					//INTRA-CLUSTER COMMUNICATION
				}
			}
			else{
				if(Constant.debugTest) System.out.println("I do not have any clusterMembers");
			}

			interTraffic= TDMA*wsn.aggregation.numberOfinterClusterAggregatedMessages(chNode)+TDMA*wsn.aggregation.numberOfintraClusterAggregatedMessages(chNode);
			
			if(chNode.Father == null){
				String str = chNode.id + " Can not find the Father CH to communicate.";
				this.interfaceProtocolRunning.DidFailToContinueRunningProtocol(str);
			}
			else{
				chNode.sendData(chNode.Father,TDMA*chNode.inter_traffic_load_factor+TDMA, Constant.PACKETSIZE_DATA_MESSAGE,false);	//INTRA-CLUSTER COMMUNICATION
			}
		}
	}


	public String toString(){
		String className = this.getClass().getSimpleName();
		return className;
	}
}
