package core;

public abstract class ClusteringProtocol {
	
	public static final String[] protocolTypeStringArray = {"HEED", "UHEED", "RUHEED", "ER-HEED"};
	
	public int TDMA = 5;

	
	
	/**
	 * The WSN to be simulated
	 */
	public WSN wsn;
	
	
	
	//Used for Detect any problem during simulation
	public ProtocolRunning interfaceProtocolRunning;	
	

	/**
	 * @param wsn the network where the heed simulation is run
	 */
	public ClusteringProtocol(WSN wsn) {		
		this.wsn = wsn;
	}
	
	public abstract void setRadiusAndNeighbors();
	public abstract void ALPHANODEDIE_MIT_SIMULATION();
	public void election_cluster_formation(){

		this.initialse();										//reset 
		this.repeat();											//see HEED paper
		this.finalise();										//see HEED paper
		
		if(Constant.debugTest) System.out.println("Succesfful FINISHED election_cluster_formation Function and wsn.CHlist are : " + this.wsn.getCHListString() + "\n\n\n");
	}
	
	
	
	public abstract void initialse();
	public abstract void repeat();
	public abstract void finalise();
	

	
	
	/**
	 * Remove the "round(int TDMA)" from WSN, create it as a abstract method of Protocol which will be implemented inside HEED, UHEED, ERHEED and RUHEED
	 * We can create different "RunRound" for different protocol
	 */
	public abstract void runRound();
	
	
	
	/**Used for detect the problem when the Simulation is running
	 * 
	 */
	public interface ProtocolRunning {
		public void DidFailToContinueRunningProtocol(String reseanDescriptStr);
	}

}
