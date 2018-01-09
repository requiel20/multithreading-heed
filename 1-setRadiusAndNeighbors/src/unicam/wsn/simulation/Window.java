package unicam.wsn.simulation;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ERHEED.ERHEED;
import ERHEED.ERHEEDNODE;
import GraphicComponent.AxisPanel;
import GraphicComponent.BSparameterDialog;
import GraphicComponent.GraphicConstant;
import GraphicComponent.WSNparameterDialog;
import HEED.HEED;
import HEED.HEEDNODE;
import RUHEED.RUHEED;
import RUHEED.RUHEEDNODE;
import ResultGraphic.NodeInfoFrame;
import UHEED.UHEED;
import UHEED.UHEEDNODE;
import core.Node;
import core.ClusteringProtocol;
import core.WSN;
import energyModel.MITModel;
import energyModel.SimpleAggregation;
import unicam.wsn.simulation.AnimationWindow.AnimationWindowInterface;



public class Window extends Canvas implements AnimationWindowInterface{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	
	
	public Dimension frameActualContentSize;
	
	
	
	
	
	//Graphic Component Used 
	private JFrame frame;
	private JPanel drawingPanel;
	private JPanel toolPanel;
	private AxisPanel axisPanel;
	private JLabel topRightLabel;
	
	
	
	private NodeInfoFrame singleNodeInfoFrame;
	
	
	
	//Single Graphic Component Used to create Parameters' Object
	private JComboBox<String> 	comboBoxList;
	
	private JButton				wsnBtn;
	private WSNparameterDialog	wsnParameterFrame;
	
	private JButton 			bsBtn;
	private BSparameterDialog	bsParameterFrame;
	
	private JList<String> 		alphaNodeDieSelectBox;
	
	private JButton pauseBtn;
	private JButton stopBtn;
	private JButton startBtn;
	private JButton speedPlusBtn;
	private JButton quickBtn;
	private JButton slowBtn;
	private JButton speedMinusBtn;
	
	
	
	
	
	
	//Left tAnimation Window
	public AnimationWindow animationWindow;
	// qui ci sta la roba di cluester ecc. ecc..
	
	
	
	public Window(){
		
		frame = new JFrame("Simulator");
		frame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				TestWriter.getTestWriter().printFile();
			}
		});
		frame.setBackground(Color.red);
		frame.setPreferredSize(new Dimension(GraphicConstant.WINDOWWIDTH, GraphicConstant.WINDOWHEIGHT));
		frame.setMaximumSize(new Dimension(GraphicConstant.WINDOWWIDTH, GraphicConstant.WINDOWHEIGHT));
		frame.setMinimumSize(new Dimension(GraphicConstant.WINDOWWIDTH, GraphicConstant.WINDOWHEIGHT));
		
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		(frame.getContentPane()).setLayout(null);
		frame.setVisible(true);
		
		frameActualContentSize = frame.getContentPane().getSize();
		
		
		
		
        
		drawingPanel = new JPanel();
        drawingPanel.setBackground(Color.gray);
        drawingPanel.setBounds(50, 50, WSN.OUTERSIZE.width, WSN.OUTERSIZE.height);
        drawingPanel.setLayout(null);
        frame.add(drawingPanel);
        
        
        
        
        
        int xTemp = drawingPanel.getX() + drawingPanel.getWidth()+2;
        toolPanel = new JPanel();
        toolPanel.setBackground(Color.gray);
        toolPanel.setBounds(xTemp, drawingPanel.getY(), (int
        		)frameActualContentSize.getWidth() - xTemp - 2, WSN.OUTERSIZE.height);
        toolPanel.setLayout(null);
        frame.add(toolPanel);
        
        
        
        
        
        axisPanel = new AxisPanel();
        axisPanel.setBackground(Color.pink);
        axisPanel.setBounds(0, 0, drawingPanel.getX() + drawingPanel.getWidth(), drawingPanel.getY() + drawingPanel.getHeight());
        axisPanel.drawingPanelX = drawingPanel.getX();
        axisPanel.drawingPanelY = drawingPanel.getY();
        axisPanel.wsnLength = 0;
        frame.add(axisPanel);
        
        
        
        
        JPanel topRightPanel = new JPanel();
        topRightPanel.setBackground(Color.gray);
        topRightPanel.setBounds(toolPanel.getX(), axisPanel.getY(), toolPanel.getWidth(), drawingPanel.getY() - 2);
        topRightPanel.setLayout(null);
        frame.add(topRightPanel);
        
        
        
        
        
        int yTemp = axisPanel.getY() + axisPanel.getHeight() + 2;
        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(Color.red);
        bottomPanel.setBounds(axisPanel.getX(), yTemp, toolPanel.getX() + toolPanel.getWidth() - axisPanel.getX(), (int)this.frameActualContentSize.getHeight() - yTemp);
        bottomPanel.setLayout(null);
        frame.add(bottomPanel);
        
        
        
        
        
        
        
        
        
        
        createToolPanelUI();
        createTopRightPanelUI(topRightPanel);
        createBottomPanelUI(bottomPanel);
        createDrawingPanel();
        
        enableToolComponent(false);
	}
	private void createDrawingPanel(){
		animationWindow	= new AnimationWindow();
		animationWindow.interfaceAnimationEnd = this;
		drawingPanel.add(animationWindow);
		animationWindow.start();
		
		System.out.println(new Object(){}.getClass().getEnclosingMethod().getName());
	}
	private void createToolPanelUI(){
		
		int tempWidth = (int)(toolPanel.getWidth()*0.9);
        int tempX     = (toolPanel.getWidth() - tempWidth)/2;
        
		
        
        wsnBtn = new JButton("Generate WSN");
        toolPanel.add(wsnBtn);
        wsnBtn.setBounds(tempX, GraphicConstant.DISTANCE_COMPONENT, tempWidth, GraphicConstant.HEIGHTofEACHCOMPONENT);
        wsnBtn.addActionListener(new ActionListener(){
        	//fin qui non fa nulla ci interessa solo wsnBtun = new button che genera il bottone
        	//action listener interfaccia che implementi senza creare clase  con metodo actionPerformed
        	//che ad ogni click fa metodo 
        	//questo crea finestra di tipo 
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				wsnParameterFrame = new WSNparameterDialog(frame, "Please Insert WSN Parameters", Dialog.ModalityType.DOCUMENT_MODAL);
				wsnParameterFrame.setVisible(true);
				wsnParameterFrame.addWindowListener(new WindowAdapter(){
					@Override
					public void windowClosed(WindowEvent e) {
						if(wsnParameterFrame.wsn == null){
							
						}
						else{
							Window.this.animationWindow.wsn = wsnParameterFrame.wsn;
							Window.this.animationWindow.wsn.initializeNodePosition();
							
							Window.this.axisPanel.wsnLength = Window.this.animationWindow.wsn.width;
							Window.this.axisPanel.repaint();
							
							alphaNodeDieSelectBox.clearSelection();
						}
						wsnParameterFrame = null;
						System.out.println("windowClosed");
		            }
				});
			}
        });
        // quando finestra viene chiusa se la finestra non � stato fatto niente null 
        
        
        
        bsBtn = new JButton("Generate BS");
        toolPanel.add(bsBtn);
        bsBtn.setBounds(tempX, wsnBtn.getY() + wsnBtn.getHeight() + GraphicConstant.DISTANCE_COMPONENT, wsnBtn.getWidth(), wsnBtn.getHeight());
        bsBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				bsParameterFrame = new BSparameterDialog(frame, "Please Insert BaseStation Parameters", Dialog.ModalityType.DOCUMENT_MODAL);
				bsParameterFrame.setVisible(true);
				bsParameterFrame.addWindowListener(new WindowAdapter(){
					@Override
					public void windowClosed(WindowEvent e) {
						if(bsParameterFrame.bs == null){
							
						}
						else{
							if(checkWsnParametersIsNull()) return;
							
							
							
							Window.this.animationWindow.wsn.BS = bsParameterFrame.bs;
							Window.this.animationWindow.wsn.BS.wsn = Window.this.animationWindow.wsn;
							
							
							
							comboBoxList.setEnabled(true);
						}
						bsParameterFrame = null;
						System.out.println("windowClosed");
		            }
				});
			}
        });
        
        
        
        comboBoxList = new JComboBox<String>(ClusteringProtocol.protocolTypeStringArray);
        toolPanel.add(comboBoxList);
        comboBoxList.setSelectedIndex(0);
        comboBoxList.setBounds(tempX, bsBtn.getY() + bsBtn.getHeight() + GraphicConstant.DISTANCE_COMPONENT, bsBtn.getWidth(), bsBtn.getHeight());
        comboBoxList.setEnabled(false);
        comboBoxList.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(checkWsnParametersIsNull())	return;
				if(checkBsParametersIsNull()) return;
				
				int selectedProtocolIndex = comboBoxList.getSelectedIndex();
				
				WSN wsn = Window.this.animationWindow.wsn;
				//protocol interfaccia e HEED e ridefinisce interfaccia. 
				if(selectedProtocolIndex == 0){
					wsn.protocol	= new HEED(wsn);
					wsn.generateHomogeneousWSN(HEEDNODE.class, new MITModel(), new SimpleAggregation());
				}
				else if(selectedProtocolIndex == 1){
					wsn.protocol	= new UHEED(wsn);
					wsn.generateHomogeneousWSN(UHEEDNODE.class, new MITModel(), new SimpleAggregation());
				}
				else if(selectedProtocolIndex == 2){
					wsn.protocol	= new RUHEED(wsn);
					wsn.generateHomogeneousWSN(RUHEEDNODE.class, new MITModel(), new SimpleAggregation());
				}
				else if(selectedProtocolIndex == 3){
					wsn.protocol	= new ERHEED(wsn);
					wsn.generateHomogeneousWSN(ERHEEDNODE.class, new MITModel(), new SimpleAggregation());
				}
				wsn.protocol.setRadiusAndNeighbors();
			}
        });
        
        
        
        DefaultListModel<String> listModel = new DefaultListModel<String>();
        listModel.addElement("One Node Dies");
        listModel.addElement("Half Nodes Die");
        listModel.addElement("All Nodes Die");
        alphaNodeDieSelectBox = new JList<>(listModel);
        toolPanel.add(alphaNodeDieSelectBox);
        alphaNodeDieSelectBox.setBounds(tempX, comboBoxList.getY() + comboBoxList.getHeight() + GraphicConstant.DISTANCE_COMPONENT, bsBtn.getWidth(), listModel.size()*GraphicConstant.HEIGHTofEACHCOMPONENT);
        alphaNodeDieSelectBox.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        alphaNodeDieSelectBox.clearSelection();
        alphaNodeDieSelectBox.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				if(Window.this.animationWindow.wsn == null){
					alphaNodeDieSelectBox.clearSelection();
					if(!e.getValueIsAdjusting()) JOptionPane.showMessageDialog(frame, "Please create the WSN first");
				}
				else{
					
					int selectedValuesList = alphaNodeDieSelectBox.getSelectedIndex();
					
					WSN wsn = Window.this.animationWindow.wsn;
					
					if(selectedValuesList == 0){
						wsn.alphaNodesDead = 1;
		            }
		            else if(selectedValuesList == 1){
		            	wsn.alphaNodesDead = wsn.nodeNumber/2;
		            }
		            else if(selectedValuesList == 2){
		            	wsn.alphaNodesDead = wsn.nodeNumber;
		            }
				}
				
			}
        });
        
        
        
        pauseBtn = new JButton("Pause");
        toolPanel.add(pauseBtn);
        pauseBtn.setBounds(tempX, alphaNodeDieSelectBox.getY() + alphaNodeDieSelectBox.getHeight() + GraphicConstant.DISTANCE_COMPONENT, (int)(bsBtn.getWidth()*0.5), bsBtn.getHeight());
        pauseBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(animationWindow == null){
					showMessageDialogSimulationIsNotRunning();
				}
				else{
					if(animationWindow.running){
						animationWindow.setAllNodesShowPowerLevel(false);
						
						if(animationWindow.paused){
							animationWindow.paused = false;
							pauseBtn.setText("Pause");
						}
						else{
							animationWindow.paused = true;
							pauseBtn.setText("Run");
							Window.this.topRightLabel.setText("Simulation Paused");
						}
					}
					
				}
			}
        });
        
        
        
        stopBtn = new JButton("Stop");
        toolPanel.add(stopBtn);
        stopBtn.setBounds(tempX, pauseBtn.getY() + pauseBtn.getHeight() + GraphicConstant.DISTANCE_COMPONENT, pauseBtn.getWidth(), pauseBtn.getHeight());
        stopBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				animationWindow.setAllNodesShowPowerLevel(false);
				pauseBtn.setText("Pause");
				stopSimulation();
			}
        	
        });
        
        
        
        startBtn = new JButton("Start");
        toolPanel.add(startBtn);
        startBtn.setBounds(tempX,stopBtn.getY() + stopBtn.getHeight() + GraphicConstant.DISTANCE_COMPONENT, stopBtn.getWidth(), stopBtn.getHeight());
        startBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				startSimulation();
			}
        });
        
        
        
        speedPlusBtn = new JButton("Speed +");
        toolPanel.add(speedPlusBtn);
        speedPlusBtn.setBounds(tempX, startBtn.getY() + startBtn.getHeight() + GraphicConstant.DISTANCE_COMPONENT, stopBtn.getWidth(), stopBtn.getHeight());
        speedPlusBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(animationWindow.running){
					animationWindow.speed -= 100;
				}
			}
        	
        });
        
        
        
        quickBtn = new JButton("Quick Mode");
        toolPanel.add(quickBtn);
        quickBtn.setBounds(tempX, speedPlusBtn.getY() + speedPlusBtn.getHeight() + GraphicConstant.DISTANCE_COMPONENT, stopBtn.getWidth(), stopBtn.getHeight());
        quickBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(animationWindow.running){
					animationWindow.speed = 1;
				}
			}
        	
        });
        
        
        
        slowBtn = new JButton("Slow Mode");
        toolPanel.add(slowBtn);
        slowBtn.setBounds(tempX, quickBtn.getY() + quickBtn.getHeight() + GraphicConstant.DISTANCE_COMPONENT, stopBtn.getWidth(), stopBtn.getHeight());
        slowBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(animationWindow.running){
					animationWindow.speed = 1000;
				}
			}
        	
        });
        
        
        
        speedMinusBtn = new JButton("Speed -");
        toolPanel.add(speedMinusBtn);
        speedMinusBtn.setBounds(tempX, slowBtn.getY() + slowBtn.getHeight() + GraphicConstant.DISTANCE_COMPONENT, stopBtn.getWidth(), stopBtn.getHeight());
        speedMinusBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(animationWindow.running){
					animationWindow.speed += 100;
				}
			}
        	
        });
        
        
        
	}
	private void createTopRightPanelUI(JPanel topRightPanel){
		topRightLabel = new JLabel("Simulation", SwingConstants.CENTER);
        topRightPanel.add(topRightLabel);
        topRightLabel.setBackground(Color.gray);
        topRightLabel.setFont(new Font("Serif", Font.PLAIN, 26));
        topRightLabel.setBounds(0, 0, topRightPanel.getWidth(), topRightPanel.getHeight());
        
	}
	private void createBottomPanelUI(JPanel bottomPanel){
		
		
		
		/*
		bottomTextArea = new JTextArea();
		//textArea.setBackground(Color.white);
		bottomTextArea.setFont(new Font("Serif", Font.PLAIN, 16));
		//textArea.setBounds(0, 0, areaScrollPane.getWidth(), areaScrollPane.getHeight());
		bottomTextArea.setEditable(false);
		bottomTextArea.setLineWrap(true);
		bottomTextArea.setText("Information : \n");
		
		
		JScrollPane areaScrollPane = new JScrollPane(bottomTextArea);
		bottomPanel.add(areaScrollPane);
		areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setBounds(0, 0, bottomPanel.getWidth(), bottomPanel.getHeight());
		
		*/
		
	}
	// fatti se bottoni premuti sopra.
	public void stopSimulation(){
		this.animationWindow.stopped = true;
		
		this.animationWindow.draw = false;			//Stop draw when we processing the Data
		
		this.animationWindow.wsn.resetWSN();
		this.animationWindow.wsn.protocol.setRadiusAndNeighbors();
		this.enableParametersComponent(true);
		this.enableToolComponent(false);
		
		this.animationWindow.draw = true;			//Start draw when we finish processing the Data
    }
	public void startSimulation(){
		if(checkParametersIsNull()){
			return;
		}
		else{
			this.animationWindow.getReadyAndGo();
			
			this.enableParametersComponent(false);
			this.enableToolComponent(true);
		}
	}
	private boolean checkParametersIsNull(){
		if(checkWsnParametersIsNull()){
			return true;
		}
		if(checkBsParametersIsNull()){
			return true;
		}
		if(this.animationWindow.wsn.protocol == null){
			JOptionPane.showMessageDialog(frame, "Please select the Protocol type");
			return true;
		}
		if(!(0 < this.animationWindow.wsn.alphaNodesDead && this.animationWindow.wsn.alphaNodesDead <= this.animationWindow.wsn.nodeNumber)){
			JOptionPane.showMessageDialog(frame, "Please select the AlphaNodeType");
			return true;
		}
		
		
		return false;
	}
	private boolean checkWsnParametersIsNull(){
		if(this.animationWindow.wsn == null){
			JOptionPane.showMessageDialog(frame, "Please create the WSN first");
			return true;
		}
		return false;
	}
	private boolean checkBsParametersIsNull(){
		if(this.animationWindow.wsn.BS == null){
			JOptionPane.showMessageDialog(frame, "Please create the BS");
			return true;
		}
		return false;
	}
	private void enableParametersComponent(boolean enable){
		this.comboBoxList.setEnabled(enable);
		this.wsnBtn.setEnabled(enable);
		this.bsBtn.setEnabled(enable);
		this.alphaNodeDieSelectBox.setEnabled(enable);
		this.startBtn.setEnabled(enable);
	}
	private void enableToolComponent(boolean enable){
		this.pauseBtn.setEnabled(enable);
		this.stopBtn.setEnabled(enable);
		this.speedPlusBtn.setEnabled(enable);
		this.speedMinusBtn.setEnabled(enable);
	}
	private void showMessageDialogSimulationIsRunning(){
		JOptionPane.showMessageDialog(frame, "The simulation is Running");
	}
	private void showMessageDialogSimulationIsNotRunning(){
		JOptionPane.showMessageDialog(frame, "Please create a Simulation first");
	}
	
	
	
	//Method Implementation of Interface ProtocolRunning
	@Override
	public void SimulationDidOneRound(WSN wsn) {
		// TODO Auto-generated method stub
		this.topRightLabel.setText("Simulation is Running");
	}
	@Override
	public void SimulationDidEnd(WSN wsn) {
		// TODO Auto-generated method stub
		
		this.topRightLabel.setText("Simulation ended");
		this.enableToolComponent(false);
		this.stopBtn.setEnabled(true);
		
		singleNodeInfoFrame = new NodeInfoFrame();
		singleNodeInfoFrame.loadDeadNodeListTable(wsn.deadNodeList);
		singleNodeInfoFrame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				// TODO Auto-generated method stub
				singleNodeInfoFrame = null;
				System.out.println("windowClosed : singleNodeInfoFrame = " + singleNodeInfoFrame);
			}
		});
		
		
		
		
	}
	@Override
	public void SimulationDidStarted(WSN wsn) {
		// TODO Auto-generated method stub
		this.enableToolComponent(true);
	}
	@Override
	public void SimualtionDisplayAlertMessage(String reseanDescriptStr) {
		// TODO Auto-generated method stub
		//pauseBtn.doClick();
		this.topRightLabel.setText("reseanDescriptStr");
	}
	@Override
	public void SimulationDidClickedSingleNode(Node clickedNode) {
		// TODO Auto-generated method stub		
		if(singleNodeInfoFrame == null){
			singleNodeInfoFrame = new NodeInfoFrame();
			singleNodeInfoFrame.reloadNodeListTable(clickedNode);
			singleNodeInfoFrame.addWindowListener(new WindowAdapter(){
				@Override
				public void windowClosing(WindowEvent e) {
					// TODO Auto-generated method stub
					singleNodeInfoFrame = null;
					System.out.println("windowClosed : singleNodeInfoFrame = " + singleNodeInfoFrame);
				}
			});
		}
		else{
			singleNodeInfoFrame.reloadNodeListTable(clickedNode);
		}
	}
	
}
