import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;

public class LaunchPage implements ActionListener{
	
	JFrame frame = new JFrame();
	JButton myButton = new JButton("Click To Play");
	
	
	LaunchPage(){
		
		myButton.setBounds(300, 400, 200, 40);
		myButton.setFocusable(false);
		myButton.addActionListener(this);
		
		
		frame.add(myButton);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setLayout(null);
		frame.setVisible(true);
		
	}


	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == myButton) {
			frame.dispose();
			new FruitNinja().start();
		}
		
	}
}
