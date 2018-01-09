package HEED;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;

import core.ClusteringProtocol;
import core.Constant;
import core.Node;
import core.WSN;
import unicam.wsn.simulation.TestWriter;

public class HEED extends ClusteringProtocol{

	public HEED(WSN wsn) {
		super(wsn);
		// TODO Auto-generated constructor stub
	}
	
	private class CallableRadiusAndNeighbors implements Callable<Boolean> {
		
		private Node n;
		private WSN wsn;
		
		public CallableRadiusAndNeighbors(Node n, WSN wsn) {
			this.n = n;
			this.wsn = wsn;
		}

		@Override
		public Boolean call() throws Exception {	
			n.setRadius();
			
			//problems
			n.setNeighbor();
			
			//problems
			this.wsn.wsnNodeList_KeyValue.put(n.id, n);
			
			if(Constant.debugTest) System.out.println("Adding Neighbor" + n.id + ": " + n.printNeighborList());
			return null;
		}
		
	}

	//TODO : TREADDIZZABILE
	@Override
	public void setRadiusAndNeighbors() {
		// TODO Auto-generated method stub

		TestWriter.getTestWriter().addField("setRadiusAndNeighbors");
		
		// TODO Auto-generated method stub
		
		long millPassed = System.nanoTime();
		
		ExecutorService exS = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
		ArrayList<CallableRadiusAndNeighbors> al = new ArrayList<CallableRadiusAndNeighbors>();
		
		for(int i=0;i<this.wsn.wsnNodeList.size();i++){
			al.add(new CallableRadiusAndNeighbors(this.wsn.wsnNodeList.get(i), this.wsn));
		}
		
		try {
			exS.invokeAll(al);
		} catch (InterruptedException e) {
			// FIXME Auto-generated catch block
			e.printStackTrace();
		}

		exS.shutdown();
		
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
	

	
	
	//TODO : TREADDIZZABILE
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

	private class CallableRepeat implements Callable<Boolean> {

		private HEEDNODE node;
		private Random random;
		
		public CallableRepeat(HEEDNODE node, Random random) {
			this.node = node;
			this.random = random;
		}
		
		@Override
		public Boolean call() throws Exception {
			boolean continueWhile = false;
			
			if(node.CHpro_previous < 1){
				continueWhile=true;									//only when all node have prob=1 the while terminates
				if(node.SCH.size() != 0){							//someone proposed as CH. Can be the node itself
					//System.out.print(node.getSCHstring() + " - ");
					//if(node.SCH.size() > 1) System.out.print("     ***** " + node.SCH.size());
					HEEDNODE my_cluster_head = node.leastCHCost();	//I select the node with least cost, solo scritture locali e letture
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
				
				node.increaseCHprob();								//PROB, OF NODE is always increased, solo scritture e letture locali 
			}
			System.out.println(" ");
			return continueWhile;
		}
		
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
			
			ExecutorService exS = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
			ArrayList<CallableRepeat> al = new ArrayList<CallableRepeat>();

			
			Iterator<Node> iterator=wsn.aliveNodeList.iterator();
			while(iterator.hasNext()){
				HEEDNODE node = (HEEDNODE) iterator.next();
				//System.out.print(node.id + ":" + node.CHprob + " -----> ");
				al.add(new CallableRepeat(node, random));
				
			}
			//System.out.println("\n\n\n\n\n**********Try Again");
			
			List<Future<Boolean>> fl = null;
			try {
				fl = exS.invokeAll(al);
			} catch (InterruptedException e) {
				// FIXME Auto-generated catch block
				e.printStackTrace();
			}
			
			for(Future<Boolean> cW : fl) {
				try {
					if(cW.get() == Boolean.TRUE) continueWhile = true;
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}
			}

			exS.shutdown();
			
		} while (continueWhile);
		
		if(Constant.debugTest) System.out.println("FINISHED repeat Function"+this.wsn.CHlist+"\n\n\n");
		
		millPassed = System.nanoTime() - millPassed;
		TestWriter.getTestWriter().addValue("repeat", millPassed);	}
	
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
