package energyModel;

public class MITModel implements EnergyModelInterface{

	private static double E_elect	= 50*Math.pow(10, -9); 			//J unit, 1 J = Math.pow(10, -9) nJ
	
	private static double E_fs	= 10*Math.pow(10, -12); 		//J unit, 1 J = Math.pow(10, -12) pJ
	private static double E_mp 	= 0.0013*Math.pow(10, -12); 	//J unit, 1 J = Math.pow(10, -12) pJ
	/**
	 * @param args
	 */
	/*
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	*/

	@Override
	public double send(int bits, double distance) {
		double E_tx;
		
		if(distance<75){
			E_tx = E_elect*bits + E_fs*Math.pow(distance, 2)*bits;
		}else{
			E_tx = E_elect*bits + E_mp*Math.pow(distance, 4)*bits;
		}
		
		return E_tx;
	}

	@Override
	public double receive(int bits) {
		double E_elec = E_elect*bits;
				
		return E_elec;
	}
	
	public String toString(){
		return "LEACH_MIT_MODEL";
	}

}
