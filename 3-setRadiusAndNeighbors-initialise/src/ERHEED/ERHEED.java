package ERHEED;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


import HEED.HEED;
import core.Node;
import core.WSN;
import energyModel.MITModel;

public class ERHEED extends HEED{
	
	//public TreeSet<ERHEEDNODE>	previousCHlist;
	
	public HashMap<ERHEEDNODE, HashSet<Integer>> previous_CH_ClusterMember_KeyValue;

	public ERHEED(WSN wsn) {
		super(wsn);
		// TODO Auto-generated constructor stub
	}

	
	public void ALPHANODEDIE_MIT_SIMULATION(){
	}
	
	
	@Override
	public void initialse() {
		// TODO Auto-generated method stub
		if(this.wsn.roundPerformed < 1){
			
		}
		else{
			this.previous_CH_ClusterMember_KeyValue	= new HashMap<ERHEEDNODE, HashSet<Integer>> (); //Used to Memorize the Previous CH and CH's Member
						
			Iterator<Node> iterator1=this.wsn.CHlist.iterator();
			
			while(iterator1.hasNext()){
				ERHEEDNODE previousChNode = (ERHEEDNODE) (iterator1.next());
				
				
				HashSet<Integer> clusterMembers_id = new HashSet<Integer>();
				
				if(previousChNode.clusterMembers.size()> 0){
					double energyResidual = 0;
					
					//Find out MAX Power Neighbor
					for(int j=0; j<previousChNode.clusterMembers.size(); j++){
						ERHEEDNODE memberNode = (ERHEEDNODE) previousChNode.clusterMembers.get(j);
						
						if(memberNode.nodeDead){
							//Only Select alive Node
						}
						else{
							if(energyResidual < memberNode.energyResidual){
								energyResidual = memberNode.energyResidual;
								previousChNode.maxPowerNeighborNode = memberNode;
							}
						}
						
					}
					//Save Cluster Members
					for(int j=0;j<previousChNode.clusterMembers.size();j++){
						ERHEEDNODE memberNode = (ERHEEDNODE) previousChNode.clusterMembers.get(j);
						
						if(memberNode == previousChNode.maxPowerNeighborNode){
							if(previousChNode.nodeDead){
								
							}
							else{
								clusterMembers_id.add(previousChNode.id);
							}
						}
						else{
							if(memberNode.nodeDead){
								
							}
							else{
								clusterMembers_id.add(memberNode.id);
							}
						}
					}
				}
				else{
					previousChNode.maxPowerNeighborNode = previousChNode;		//If the Node is alone, it will become CH for next Round
					//System.out.println("Myself become CH next Round : " + previousChNode.id);
				}
				
				
				this.previous_CH_ClusterMember_KeyValue.put(previousChNode, clusterMembers_id);		//Save Each previous CH and CH's members
			}
		}
		
		
		super.initialse();
	}

	@Override
	public void repeat() {
		if(this.wsn.roundPerformed < 1){
			super.repeat();
		}
		else{
			
			Set<Entry<ERHEEDNODE, HashSet<Integer>>> set = this.previous_CH_ClusterMember_KeyValue.entrySet();
			
			Iterator<Entry<ERHEEDNODE, HashSet<Integer>>> iterator = set.iterator();
			while(iterator.hasNext()) {
				Map.Entry<ERHEEDNODE, HashSet<Integer>> me = (Map.Entry<ERHEEDNODE, HashSet<Integer>>)iterator.next();
		        
				//Get Key and Value
		        ERHEEDNODE previousChNode	= me.getKey();
		        HashSet<Integer> clusterMembers_id_Array = me.getValue();
		        
		        //Set the previousChNode's maxPowerNeighborNode as a NextCH
				ERHEEDNODE nextChNode 		= previousChNode.maxPowerNeighborNode;
				
				previousChNode.maxPowerNeighborNode = null;		//remove maxPowerNeighborNode
		        
				
				if(nextChNode != null){
					nextChNode.sendFinalCH();
					this.wsn.CHlist.add(nextChNode);
					
					
					
					//Start add its Cluster Member
					nextChNode.clusterMembers.add(previousChNode);
					Iterator<Integer> iterator2 = clusterMembers_id_Array.iterator();
					while(iterator2.hasNext()){
						int key = iterator2.next();
						Node tempNode = this.wsn.wsnNodeList_KeyValue.get(key);
						if(tempNode.id == nextChNode.id){
							//If the Cluster Member is itself, it will not be added
						}
						else{
							nextChNode.clusterMembers.add(tempNode);
						}
					}
				}
		      }
		}
	}

	@Override
	public void finalise() {
		// TODO Auto-generated method stub
		if(this.wsn.roundPerformed < 1){
			super.finalise();
		}
		else{
			
		}
		
	}
	
}
