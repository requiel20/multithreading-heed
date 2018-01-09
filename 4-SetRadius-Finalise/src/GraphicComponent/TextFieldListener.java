package GraphicComponent;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JTextField;

public class TextFieldListener implements KeyListener{

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		JTextField textfield = (JTextField) e.getSource();
		String string = textfield.getText();
		
		if(string.length() < 1){
			textfield.setText("");
			textfield.setBackground(Color.red);
		}
		else{
			textfield.setBackground(Color.white);
		}
		
		try{
			if(Integer.parseInt(string) < 1){
				textfield.setText("");
				textfield.setBackground(Color.red);
			}
		}
		catch(Exception ex){
			textfield.setText("");
			textfield.setBackground(Color.red);
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
		JTextField textfield	= (JTextField) e.getSource();
		String string			= textfield.getText();
		char a 					= e.getKeyChar();
		
		if(Character.isDigit(a) || e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE){//input<'0' or input>'9'?
            
        }
		else{
			e.consume();//delete the typed char
			return;
		}
		
		if(textfield.getCaretPosition() == 0 && a == '0'){
			e.consume();//delete the typed char
			return;
		}
		
		
		if(string.length() > 2){
			if(e.getKeyCode() == KeyEvent.VK_DELETE || e.getKeyCode() == KeyEvent.VK_BACK_SPACE){
				
			}
			else{
				e.consume();//delete the typed char
				return;
			}
		}
	}

	

}
