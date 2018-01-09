package ResultGraphic;

import java.awt.Color;
import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.TreeSet;

import javax.swing.JFrame;

import GraphicComponent.GraphicConstant;
import core.Node;
import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class NodeInfoFrame  extends JFrame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	
	
	
	
	

	private Dimension frameActualContentSize;
	private JFXPanel fxpanel;
	private String nodeInfoListStr = "";


	//private Vector<Node>  clickedNodeList;


	public NodeInfoFrame() {
		// TODO Auto-generated constructor stub
		
		
		
		this.setTitle("Single Node Info");
		this.setBackground(Color.yellow);
		this.setLocation(0, 0);
		this.setPreferredSize(new Dimension(GraphicConstant.SCREENWIDTH, GraphicConstant.SCREENHEIGHT));
		this.setMaximumSize(new Dimension(GraphicConstant.SCREENWIDTH, GraphicConstant.SCREENHEIGHT));
		this.setMinimumSize(new Dimension(GraphicConstant.SCREENWIDTH, GraphicConstant.SCREENHEIGHT));
		
		this.setResizable(true);
		this.setLocationRelativeTo(null);
		//(this.getContentPane()).setLayout(null);
		this.setVisible(true);
		
		
		
		this.frameActualContentSize = this.getContentPane().getSize();
		
		
		
		int yTemp = (int)(this.getHeight() - this.frameActualContentSize.getHeight());
		fxpanel=new JFXPanel();
		this.add(fxpanel);
		fxpanel.setBounds(0, yTemp, (int)(this.frameActualContentSize.getWidth())-1, (int)(this.frameActualContentSize.getHeight())-1);
        
	}
	public void reloadNodeListTable(Node clickedNode){
		String colorStr = clickedNode.nodeDead ? (" bgcolor=Yellow") : (clickedNode.isCH ? " bgcolor=Red" : "");
		
		String singleNodeInfoStr = "<tr" + colorStr + ">" + clickedNode.toHTMLlistString() + "</tr>";
		
		this.nodeInfoListStr = this.nodeInfoListStr + singleNodeInfoStr;
						
		
		
		fxpanel.removeAll();
	    
		Platform.runLater(new Runnable() {
			@Override
			public void run(){
			    WebEngine engine;
			    WebView wv=new WebView();
			    engine=wv.getEngine();
			    fxpanel.setScene(new Scene(wv));
			    
			    String allHtmlContentStr = getHTMLTemplate().replace("[NodeInfo]", nodeInfoListStr);
			    engine.loadContent(allHtmlContentStr);
			}
		});
	}
	public void loadDeadNodeListTable(TreeSet<Node> deadNodeList){
		
		Iterator<Node> iterator=deadNodeList.iterator();
		while(iterator.hasNext()){
			Node deadNode=iterator.next();
			
			String colorStr = deadNode.nodeDead ? (" bgcolor=Yellow") : (deadNode.isCH ? " bgcolor=Red" : "");
			
			String singleNodeInfoStr = "<tr" + colorStr + ">" + deadNode.toHTMLlistString() + "</tr>";
			
			this.nodeInfoListStr = this.nodeInfoListStr + singleNodeInfoStr;
		}
					
		
		
		fxpanel.removeAll();
	    
		Platform.runLater(new Runnable() {
			@Override
			public void run(){
			    WebEngine engine;
			    WebView wv=new WebView();
			    engine=wv.getEngine();
			    fxpanel.setScene(new Scene(wv));
			    
			    String allHtmlContentStr = getHTMLTemplate().replace("[NodeInfo]", nodeInfoListStr);
			    engine.loadContent(allHtmlContentStr);
			}
		});
	}
	private String getHTMLTemplate(){
		String content = "";
	    try {
	        BufferedReader in = new BufferedReader(new FileReader("src/ResultGraphic/NodeInfoTemplate.html"));
	        String str;
	        while ((str = in.readLine()) != null) {
	            content +=str;
	        }
	        in.close();
	    } 
	    catch (IOException e) {
	    	e.printStackTrace();
	    }
	    
	    return content;
	}
}
