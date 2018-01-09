package unicam.wsn.simulation;

import GraphicComponent.GraphicConstant;
import HEED.HEED;
import HEED.HEEDNODE;
import core.BaseStation;
import core.WSN;
import energyModel.MITModel;
import energyModel.NOEnergyModel;
import energyModel.SimpleAggregation;

public class Simulation {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		GraphicConstant.setGlobolParameters();
		
		new Window();
		
		
		//startSimulationWithoutInterface();

	}
	
	public static void startSimulationWithoutInterface(){
		WSN wsn = new WSN(100, 100, 30, 60, 200);
		wsn.initializeNodePosition();
		wsn.alphaNodesDead = wsn.nodeNumber/2;
		
		
		BaseStation BS = new  BaseStation();											//the BS is created
		BS.x = 125;
		BS.y = 50;
		BS.maxRadius = 75;
		BS.energyModel = new NOEnergyModel();
		
		wsn.BS = BS;
		BS.wsn = wsn;
		
		
		wsn.protocol	= new HEED(wsn);
		wsn.generateHomogeneousWSN(HEEDNODE.class, new MITModel(), new SimpleAggregation());
		wsn.protocol.setRadiusAndNeighbors();
		
		
		while(true){
			wsn.roundPerformed++;
			
			wsn.protocol.election_cluster_formation();				//cluster formation
			
			System.out.println(wsn.roundPerformed);
			
			if(wsn.deadNodeList.size() >= wsn.alphaNodesDead) break;
		}
	}

}
