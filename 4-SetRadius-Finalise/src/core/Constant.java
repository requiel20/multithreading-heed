package core;


import java.util.Random;

public class Constant {
	
	
	//public static final int WIDTH = 800, HEIGHT = WIDTH/12*9;
	
	public static int		PACKETSIZE_CONTROL_MESSAGE 	= 200;
	public static int		PACKETSIZE_DATA_MESSAGE 	= 2000;
	
	public static double 	maxEnergy	= 2.000; //Joule
	public static double 	ControlParameter	= 0.7;
	
	public static int SPEED_MAX = 24*3600*1000;
			
	public static boolean debugTest = false;
	
	
	
	
	
	
	
	public static int generatRandomPositiveNegitiveValue(int max , int min) {
	    //Random rand = new Random();
	    int ii = -min + (int) (Math.random() * ((max - (-min)) + 1));
	    return ii;
	}
	
	public static int generateRandomValueBetween(int max, int min){
		Random random = new Random();
		int randomNumber = random.nextInt(max - min) + min;
		
		return randomNumber;
	}
	public static void sleep11111(){
		try {
		    Thread.sleep(20000000);                 //1000 milliseconds is one second.
		} catch(InterruptedException ex) {
		    Thread.currentThread().interrupt();
		}
	}
}
