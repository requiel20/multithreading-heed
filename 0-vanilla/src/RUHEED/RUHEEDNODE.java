package RUHEED;

import ERHEED.ERHEEDNODE;
import core.Constant;

public class RUHEEDNODE extends ERHEEDNODE{
	
	
	
	@Override
	public void setRadius(){
		super.setRadius();
		
		this.radius = (1 - Constant.ControlParameter*((this.wsn.BS.distanceOfMax - this.distanceToBS)/(this.wsn.BS.distanceOfMax - this.wsn.BS.distanceOfMin)))*this.maxRadius;
	}
}
