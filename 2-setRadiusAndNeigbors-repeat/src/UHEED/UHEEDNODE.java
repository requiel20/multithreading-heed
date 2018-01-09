package UHEED;



import HEED.HEEDNODE;
import core.Constant;

public class UHEEDNODE extends HEEDNODE{
	
	
	
	@Override
	public void setRadius(){
		super.setRadius();
		
		this.radius = (1 - Constant.ControlParameter*((this.wsn.BS.distanceOfMax - this.distanceToBS)/(this.wsn.BS.distanceOfMax - this.wsn.BS.distanceOfMin)))*this.maxRadius;
	}

}
