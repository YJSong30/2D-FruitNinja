import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import acm.graphics.GCanvas;
import acm.graphics.GLabel;
import acm.graphics.GObject;
import acm.graphics.GRect;
import acm.program.Program;
	
	
	public class MainMenuScreen extends GCanvas implements MouseListener {
		
		private ArcadeMode fruitNinja;
		private GLabel clicktoplayText;
		private GRect playButton;
		
		
		public MainMenuScreen(ArcadeMode fruitNinja) {
			this.fruitNinja = fruitNinja;
		
			
		}
		
		
		public void menuScreen() {
			clicktoplayText = new GLabel("Click To Play", fruitNinja.getWidth() / 2 - 100, fruitNinja.getHeight() / 2 + 30);
			fruitNinja.add(clicktoplayText);
			
			playButton = new GRect(fruitNinja.getWidth() / 2 - 150, fruitNinja.getHeight() / 2, 200, 50);
			fruitNinja.add(playButton);
			playButton.addMouseListener(this);

		}
		
		private void clickPlay() {
			
			fruitNinja.remove(clicktoplayText);
			fruitNinja.remove(playButton);
			fruitNinja.startGame();
		}	
		
		@Override
		public void mousePressed(MouseEvent e) {
			
		}
		
		@Override 
		public void mouseClicked(MouseEvent e) {
			System.out.println("click");
			
			if (playButton.contains(e.getX(), e.getY())) {
				clickPlay();
			}
		
//		    GObject clickedObject = fruitNinja.getElementAt(e.getX(), e.getY());
//		    if (clickedObject == playButton) {
//		    	clickPlay();
		    
		}
		
	
		@Override 
		public void mouseReleased(MouseEvent e) {
			
		}
	
	
		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
	
	
		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	
	}
