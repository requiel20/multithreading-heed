package GraphicComponent;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import core.WSN;



public class WSNparameterDialog  extends JDialog{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	private static int WIDTH 	= (int)(GraphicConstant.WINDOWWIDTH*0.34);
	private static int HEIGHT 	= WIDTH;
	private static int HEIGHTofEACHCOMPONENT = GraphicConstant.HEIGHTofEACHCOMPONENT;
	private static int DISTANCE_COMPONENT = GraphicConstant.DISTANCE_COMPONENT;

	
	private JTextField widthTextField;
	private JTextField heightTextField;
	private JTextField radiusTextField;
	private JTextField maxRadiusTextField;
	private JTextField numberOfNodeTextField;
	
	public WSN wsn;
	
	

	
	public WSNparameterDialog(JFrame frame, String title, Dialog.ModalityType modalityType){
		// TODO Auto-generated constructor stub
		super(frame, title, modalityType);
		this.setBackground(Color.gray);
		
		
		
		
        this.setBounds((GraphicConstant.SCREENWIDTH - WIDTH)/2, (GraphicConstant.SCREENHEIGHT - HEIGHT)/2, WIDTH, HEIGHT);
        
		
		
		
		
		
		
		this.createUI();
		
	}
	
	private void createUI(){
		Container cp = this.getContentPane();
		cp.setLayout(null);
		
		
		
		
		JLabel widthLabel = new JLabel("<html>Width (m): (Between 1-999)</html>", SwingConstants.CENTER);
		cp.add(widthLabel);
		widthLabel.setBounds(0, 6, this.getWidth()/2, HEIGHTofEACHCOMPONENT);
		
		widthTextField = new JTextField();
		widthTextField.setBackground(Color.white);
		widthTextField.setText("100");
		widthTextField.setBounds(widthLabel.getX()+widthLabel.getWidth(), widthLabel.getY(), widthLabel.getWidth()/2, widthLabel.getHeight());
		cp.add(widthTextField);
		//widthTextField.setEditable(false);
	        
	        
	        
	        
		
		JLabel heightLabel = new JLabel("<html>Height (m): (Between 1-999)</html>", SwingConstants.CENTER);
		cp.add(heightLabel);
		heightLabel.setBounds(widthLabel.getX(), widthLabel.getY() + widthLabel.getHeight() + DISTANCE_COMPONENT, widthLabel.getWidth(), widthLabel.getHeight());
		
		heightTextField = new JTextField();
		heightTextField.setBackground(Color.white);
		heightTextField.setText(widthTextField.getText());
		heightTextField.setBounds(heightLabel.getX()+heightLabel.getWidth(), heightLabel.getY(), widthTextField.getWidth(), widthTextField.getHeight());
		cp.add(heightTextField);
		heightTextField.setEditable(false);
		
		
		
        
		
		final JLabel radiusLabel = new JLabel("<html>Radius (m):<br>(Between 1-" + widthTextField.getText() + ")</html>", SwingConstants.CENTER);
		cp.add(radiusLabel);
		radiusLabel.setBounds(widthLabel.getX(), heightTextField.getY() + heightTextField.getHeight() + DISTANCE_COMPONENT, widthLabel.getWidth(), widthLabel.getHeight());
		
		radiusTextField = new JTextField();
		radiusTextField.setBackground(Color.white);
		radiusTextField.setText("30");
		radiusTextField.setBounds(radiusLabel.getX() + radiusLabel.getWidth(), radiusLabel.getY(), widthTextField.getWidth(), widthTextField.getHeight());
		cp.add(radiusTextField);
		//radiusTextField.setEditable(false);
		
		
		
		
		
		final JLabel maxRadiusLabel = new JLabel("<html>Max Radius (m):<br>(Between " + radiusTextField.getText() +"-" + widthTextField.getText() + ")</html>", SwingConstants.CENTER);
		cp.add(maxRadiusLabel);
		maxRadiusLabel.setBounds(widthLabel.getX(), radiusTextField.getY() + radiusTextField.getHeight() + DISTANCE_COMPONENT, widthLabel.getWidth(), widthLabel.getHeight());
		
		maxRadiusTextField = new JTextField();
		maxRadiusTextField.setBackground(Color.white);
		maxRadiusTextField.setText("60");
		maxRadiusTextField.setBounds(maxRadiusLabel.getX()+maxRadiusLabel.getWidth(), maxRadiusLabel.getY(), widthTextField.getWidth(), widthTextField.getHeight());
		cp.add(maxRadiusTextField);
		//maxRadiusTextField.setEditable(false);
		
		
		
		
		
		JLabel numberOfNodeLabel = new JLabel("<html>Number Of Node:<br>(Between 1-999)</html>", SwingConstants.CENTER);
        numberOfNodeLabel.setBackground(Color.red);
        cp.add(numberOfNodeLabel);
        numberOfNodeLabel.setBounds(heightLabel.getX(), maxRadiusTextField.getY() + maxRadiusTextField.getHeight() + GraphicConstant.DISTANCE_COMPONENT, heightLabel.getWidth(), heightLabel.getHeight());
        
        
        numberOfNodeTextField = new JTextField();
        numberOfNodeTextField.setBackground(Color.red);
        cp.add(numberOfNodeTextField);
        numberOfNodeTextField.setBounds(numberOfNodeLabel.getX()+numberOfNodeLabel.getWidth(), numberOfNodeLabel.getY(), widthTextField.getWidth(), widthTextField.getHeight());
        
        
        
        
        
        //Add addKeyListener
        widthTextField.addKeyListener(new TextFieldListener(){
        	@Override
        	public void keyReleased(KeyEvent e) {
        		// TODO Auto-generated method stub
        		super.keyReleased(e);
        		
        		heightTextField.setText(widthTextField.getText());
        		heightTextField.setBackground(widthTextField.getBackground());
        		
        		
        		
        		
        		if(widthTextField.getText().length() < 1){
        			radiusLabel.setText("Radius (m):");
        			maxRadiusLabel.setText("Max Radius (m):");
        		}
        		else{
        			radiusLabel.setText("<html>Radius (m):<br>(Between 1-" + widthTextField.getText() + ")</html>");
        			maxRadiusLabel.setText("<html>Max Radius (m):<br>(Between 1-" + widthTextField.getText() + ")</html>");
        		}
        		radiusTextField.setText("");
        		radiusTextField.setBackground(Color.red);
    			
        		maxRadiusTextField.setText("");
        		maxRadiusTextField.setBackground(Color.red);
        	}
        });
        radiusTextField.addKeyListener(new TextFieldListener(){
        	@Override
        	public void keyReleased(KeyEvent e) {
        		// TODO Auto-generated method stub
        		super.keyReleased(e);
        		
        		
        		
        		try{
        			if(Integer.parseInt(radiusTextField.getText()) < Integer.parseInt(widthTextField.getText())){
            			
            		}
            		else{
            			radiusTextField.setText("");
            			radiusTextField.setBackground(Color.red);
            			return;
            		}
        		}
        		catch(Exception ex){
        			return;
        		}
        		
        		
        		
        		if(radiusTextField.getText().length() < 1){
        			maxRadiusLabel.setText("<html>Max Radius (m):<br>(Between 1-" + widthTextField.getText() + ")</html>");
        		}
        		else{
        			maxRadiusLabel.setText("<html>Max Radius (m):<br>(Between " + radiusTextField.getText() +"-" + widthTextField.getText() + ")</html>");
        		}
        		
        		maxRadiusTextField.setText("");
        		maxRadiusTextField.setBackground(Color.red);
        	}
        });
        maxRadiusTextField.addKeyListener(new TextFieldListener(){
        	@Override
        	public void keyReleased(KeyEvent e) {
        		// TODO Auto-generated method stub
        		super.keyReleased(e);
        		
        		
        		
        		try{
        			if(Integer.parseInt(maxRadiusTextField.getText()) <= Integer.parseInt(widthTextField.getText())){
            			
            		}
            		else{
            			maxRadiusTextField.setText("");
            			maxRadiusTextField.setBackground(Color.red);
            		}
        		}
        		catch(Exception ex){
        			return;
        		}
        	}
        });
        numberOfNodeTextField.addKeyListener(new TextFieldListener(){
        	@Override
        	public void keyReleased(KeyEvent e) {
        		// TODO Auto-generated method stub
        		super.keyReleased(e);
        		
        		
        		
        		try{
        			if(Integer.parseInt(numberOfNodeTextField.getText()) < 1000){
            			
            		}
            		else{
            			numberOfNodeTextField.setText("");
            			numberOfNodeTextField.setBackground(Color.red);
            		}
        		}
        		catch(Exception ex){
        			return;
        		}
        	}
        });
        
        
		
		
		
        int tempWidth = (int)(widthLabel.getWidth()*0.8);
        int tempX = (this.getWidth() - tempWidth*2)/3;
        int tempY = this.getHeight() - GraphicConstant.DISTANCE_COMPONENT*3 - widthLabel.getHeight();
        JButton confirmBtn = new JButton("Generate");
        cp.add(confirmBtn);
        confirmBtn.setBounds(tempX,  tempY, tempWidth, widthLabel.getHeight());
        confirmBtn.addActionListener(new ActionListener(){
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				
				int wsnWidth 		= (int) WSNparameterDialog.this.isTextfieldValid(widthTextField);
				int wsnHeight 		= (int) WSNparameterDialog.this.isTextfieldValid(heightTextField);
				int radius			= (int) WSNparameterDialog.this.isTextfieldValid(radiusTextField);
				int maxRadius		= (int) WSNparameterDialog.this.isTextfieldValid(maxRadiusTextField);
				long numberOfNode	= WSNparameterDialog.this.isTextfieldValid(numberOfNodeTextField);
				if(0 < wsnWidth &&
						0 < wsnHeight &&
						0 < radius &&
						0 < maxRadius &&
						0 < numberOfNode)
				{
					if(radius <= maxRadius && maxRadius <= wsnWidth){
						WSNparameterDialog.this.wsn = new WSN(wsnWidth, wsnHeight, radius, maxRadius, numberOfNode);
						closeDialogFrame();
					}
					else{
						showDialog();
					}
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
		WSNparameterDialog.this.dispose();
	}
	
	public long isTextfieldValid(JTextField textfield){
		String valueStr = textfield.getText();
		
		if(valueStr.length() < 1 || valueStr.length() > 3){
			showDialog();
			return 0;
		}
		long value = 0;
		try{
			value =  Long.parseLong(valueStr);
		}
		catch(Exception e){
			textfield.setText("");
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
		JOptionPane.showMessageDialog(this, "Plese insert proper parameters...");
	}

}
