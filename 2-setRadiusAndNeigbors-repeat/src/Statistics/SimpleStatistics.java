package Statistics;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import core.Node;
import core.WSN;

public class SimpleStatistics {
	
	private static String createStatsDirectory() {
		String directoryName;
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		Date date = new Date();
		directoryName = dateFormat.format(date);
		File dir = new File(directoryName);

		// attempt to create the directory here
		boolean successful = dir.mkdir();
		if (successful) {
			// creating the directory succeeded
			System.out.println("directory "+directoryName+" was created successfully");
		} else {
			// creating the directory failed
			System.out.println("directory "+directoryName+" was not created FAILURE");
			System.exit(1);
		}
		return directoryName;
	}

	
	public static void writeToFileWSNFeatures(WSN wsn,String protocolName){
		createStatsDirectory();
		try {

			String content,directoryName,fileName;
			Node node=null;
			
			fileName=protocolName+"_H_"+wsn.height+"_W_"+wsn.width+"_Nnumber_"+wsn.nodeNumber+
					"_Alpha_"+wsn.alphaNodesDead+"_Round_"+wsn.roundPerformed+".csv";
			
			directoryName=createStatsDirectory();
							
			File file =new File(directoryName+"/"+fileName);

			// if file doesn't exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}else{
				System.out.println("file "+file.getName()+" already exists");
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile());
			BufferedWriter bw = new BufferedWriter(fw);
			
			Iterator<Node> iterator=wsn.aliveNodeList.iterator();
			
			content=wsn.toString();
			bw.write(content+"\n");
			content=Node.getNodeFullHeadLine();
			bw.write(content+"\n");
			
			while(iterator.hasNext()){
				node=iterator.next();
				content=node.toString();
				bw.write(content+"\n");
			}
			
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void printOnScreenHEED(WSN wsn,String protocolName) {
		Iterator<Node> iterator=wsn.aliveNodeList.iterator();
		Node node;
		String content;
		
		String fileName=protocolName+"_H_"+wsn.height+"_W_"+wsn.width+"_Nnumber_"+wsn.nodeNumber+
				"_Alpha_"+wsn.alphaNodesDead+"_Round_"+wsn.roundPerformed+".csv";
		
		String directoryName=createStatsDirectory();
	
		System.out.println(directoryName+"/"+fileName);
		
		while(iterator.hasNext()){
			node=iterator.next();
			content=node.toString();
			System.out.println(content+"\n");
		}
		
	}
}
