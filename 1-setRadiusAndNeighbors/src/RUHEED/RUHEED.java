package RUHEED;



import ERHEED.ERHEED;
import core.Constant;
import core.Node;
import core.WSN;
import energyModel.MITModel;

public class RUHEED extends ERHEED{


	public RUHEED(WSN wsn) {
		super(wsn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void setRadiusAndNeighbors() {
		// TODO Auto-generated method stub
		for(int i=0;i<this.wsn.wsnNodeList.size();i++){
			RUHEEDNODE temp = (RUHEEDNODE)this.wsn.wsnNodeList.get(i);
			
			temp.setRadius();
			temp.setNeighbor();
			
			this.wsn.wsnNodeList_KeyValue.put(temp.id, temp);
			
			if(Constant.debugTest) System.out.println("Adding Neighbor" + temp.id + ": " + temp.printNeighborList());
		}

		//DEALING WITH BS
		this.wsn.BS.setNeighbor();
	}
	
	
	
	
	@Override
	public void ALPHANODEDIE_MIT_SIMULATION(){
	}
	
	
	@Override
	public void initialse() {
		// TODO Auto-generated method stub
		super.initialse();
	}

	@Override
	public void repeat() {
		super.repeat();

	}

	@Override
	public void finalise() {
		// TODO Auto-generated method stub
		super.finalise();
	}


}
