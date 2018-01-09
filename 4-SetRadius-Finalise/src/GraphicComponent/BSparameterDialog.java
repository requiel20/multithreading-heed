package GraphicComponent;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import core.BaseStation;
import core.WSN;
import energyModel.NOEnergyModel;




public class BSparameterDialog extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static int WIDTH 	= (int)(GraphicConstant.WINDOWWIDTH*0.34);
	private static int HEIGHT 	= WIDTH;
	private static int HEIGHTofEACHCOMPONENT = GraphicConstant.HEIGHTofEACHCOMPONENT;
	private static int DISTANCE_COMPONENT = GraphicConstant.DISTANCE_COMPONENT;
	
	
	private JTextField xTextField;
	private JTextField yTextField;
	private JTextField maxRadiusTextField;
	
	
	
	public BaseStation bs;
	
	
	public BSparameterDialog(JFrame frame, String title, Dialog.ModalityType modalityType){
		super(frame, title, modalityType);
		this.setBackground(Color.gray);
		
		
		
		
        this.setBounds((GraphicConstant.SCREENWIDTH - WIDTH)/2, (GraphicConstant.SCREENHEIGHT - HEIGHT)/2, WIDTH, HEIGHT);
        
		
		
		
		
		
		
		this.createUI();
		
		
		
	}
	
	private void createUI(){
		Container cp = this.getContentPane();
		cp.setLayout(null);
		
		
		
		
		JLabel xLabel = new JLabel("X (m): ", SwingConstants.CENTER);
		cp.add(xLabel);
		xLabel.setBounds(0, 6, this.getWidth()/2, HEIGHTofEACHCOMPONENT);
		
		xTextField = new JTextField();
		xTextField.setBackground(Color.white);
		xTextField.setText(Integer.toString(WSN.INNERSIZE.width + 25));
		xTextField.setText("125");
		xTextField.setBounds(xLabel.getX()+xLabel.getWidth(), xLabel.getY(), xLabel.getWidth()/2, xLabel.getHeight());
		cp.add(xTextField);
		//xTextField.setEditable(false);
	        
	        
	        
	        
	        
	        
        
		JLabel yLabel = new JLabel("Y (m): ", SwingConstants.CENTER);
		cp.add(yLabel);
		yLabel.setBounds(xLabel.getX(), xLabel.getY() + xLabel.getHeight() + DISTANCE_COMPONENT, xLabel.getWidth(), xLabel.getHeight());
		
		yTextField = new JTextField();
		yTextField.setBackground(Color.white);
		yTextField.setText(Integer.toString(WSN.INNERSIZE.height/2));
		yTextField.setText("50");
		yTextField.setBounds(yLabel.getX()+yLabel.getWidth(), yLabel.getY(), xTextField.getWidth(), yLabel.getHeight());
		cp.add(yTextField);
		//yTextField.setEditable(false);
		
		
		
        
		JLabel radiusLabel = new JLabel("Radius (m): ", SwingConstants.CENTER);
		cp.add(radiusLabel);
		radiusLabel.setBounds(xLabel.getX(), yLabel.getY() + yLabel.getHeight() + DISTANCE_COMPONENT, xLabel.getWidth(), xLabel.getHeight());
		
		maxRadiusTextField = new JTextField();
		maxRadiusTextField.setBackground(Color.white);
		maxRadiusTextField.setText("65");
		maxRadiusTextField.setBounds(radiusLabel.getX()+radiusLabel.getWidth(), radiusLabel.getY(), xTextField.getWidth(), radiusLabel.getHeight());
		cp.add(maxRadiusTextField);
		
		
		
		
		
		maxRadiusTextField.addKeyListener(new TextFieldListener());
		xTextField.addKeyListener(new TextFieldListener());
		yTextField.addKeyListener(new TextFieldListener());
		
		
		
		
		
		int tempWidth = (int)(xLabel.getWidth()*0.8);
        int tempX = (this.getWidth() - tempWidth*2)/3;
        int tempY = this.getHeight() - GraphicConstant.DISTANCE_COMPONENT*3 - xTextField.getHeight();
        JButton confirmBtn = new JButton("Generate");
        cp.add(confirmBtn);
        confirmBtn.setBounds(tempX,  tempY, tempWidth, xTextField.getHeight());
        confirmBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				int x 		= BSparameterDialog.this.isNumberOfNodeValid(xTextField);
				int y 		= BSparameterDialog.this.isNumberOfNodeValid(yTextField);
				int maxRadius	= BSparameterDialog.this.isNumberOfNodeValid(maxRadiusTextField);
				if(x>0 && y > 0 && maxRadius>0){
					
					BaseStation BS = new  BaseStation();											//the BS is created
					BS.x = x;
					BS.y = y;
					BS.maxRadius = maxRadius;
					BS.energyModel = new NOEnergyModel();															//The BS does not consume energy
					
					BSparameterDialog.this.bs = BS;
					
					closeDialogFrame();
				}
				else{
					JOptionPane.showMessageDialog(BSparameterDialog.this, "Plese insert proper parametes");
				}
			}
        });
        
        
        
        JButton cancelBtn = new JButton("Cancel");
        cp.add(cancelBtn);
        cancelBtn.setBounds(confirmBtn.getX() + confirmBtn.getWidth() + tempX, confirmBtn.getY(), confirmBtn.getWidth(), confirmBtn.getHeight());
        cancelBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				closeDialogFrame();
			}
        	
        });
	}
	public void closeDialogFrame(){
		setModal(true);
		getOwner().setEnabled(true);
		BSparameterDialog.this.dispose();
	}
	public int isNumberOfNodeValid(JTextField textField){
		String valueStr = textField.getText();
		
		if(valueStr.length() < 1 || valueStr.length() > 3){
			showDialog();
			return 0;
		}
		int value = 0;
		try{
			value =  Integer.parseInt(valueStr);
		}
		catch(Exception e){
			textField.setText("");
			showDialog();
			return 0;
		}
		
		if(value < 1 || value >999){
			showDialog();
			return 0;
		}
		
		return value;
	}
	public void showDialog(){
		JOptionPane.showMessageDialog(this, "Plese insert the Number of Node between 1-999");
	}

}
