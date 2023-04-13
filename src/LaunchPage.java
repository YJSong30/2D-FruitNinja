import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import acm.graphics.GImage;
import java.awt.Graphics;
import javax.swing.ImageIcon;


public class LaunchPage implements ActionListener{
	
	JFrame frame = new JFrame();
	JButton btnArcade = new JButton("Arcade Mode");
	JButton btnClassic = new JButton ("Classic Mode"); 
	String BGMpath = "media/bgm-mainmenu.wav";
	WAVplayInstance BGMplayer = new WAVplayInstance();	
	
	
	LaunchPage() {

		BGMplayer.playWAV(BGMpath, true);
		
		ImagePanel bgPanel = new ImagePanel("media/dojo.jpg");
		bgPanel.setLayout(null);
		bgPanel.setSize(frame.getSize());
		frame.setContentPane(bgPanel);
		
		JLabel textLabel;
		textLabel = new JLabel("Welcome to Fruit Ninja!");
		textLabel.setBounds(300, 100, 100, 100);
		textLabel.setFont(new Font("Verdana", Font.PLAIN, 18));
		frame.add(textLabel);
		
		btnArcade.setBounds(450, 400, 200, 40);
		btnArcade.setFocusable(false);
		btnArcade.addActionListener(this);
		
		btnClassic.setBounds(150, 400, 200, 40);
		btnClassic.setFocusable(false);
		btnClassic.addActionListener(this);
		
		
		frame.add(btnArcade);
		frame.add(btnClassic);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(800, 600);
		frame.setLayout(null);
		frame.setVisible(true);
		
		JLabel imageLabel = menuImage("media/fruitninjalogo.png"); //doesn't show in the beginning but shows when window is expanded. Fix later
		
		frame.add(imageLabel);
		imageLabel.setBounds(300, 200, 100, 100);
		
		
	
		
	}
	
	class ImagePanel extends JPanel {
		
	    private Image image;
	    public ImagePanel(Image image) {
	        this.image = image;
	        
	    }
	    
	    public ImagePanel(String string) {
			// TODO Auto-generated constructor stub
	    	try {
				image = ImageIO.read(new File(string));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
		}

		@Override
	    protected void paintComponent(Graphics g) {
	        super.paintComponent(g);
	        g.drawImage(image, 0, 0, this);
	    }
	}
	
	private JLabel menuImage(String imagePath){
		
		ImageIcon imageIcon = new ImageIcon(imagePath);
	    JLabel imageLabel = new JLabel(imageIcon);
	    return imageLabel;
	 
	}
	
	class WAVplayInstance {
		Clip tempIn;
		
        void playWAV(String WAVpath, boolean loopState){
                 try {
                         File pathString = new File(WAVpath);
                          if(pathString.exists()){ 
                                  AudioInputStream WAVstream = AudioSystem.getAudioInputStream(pathString);
                                  tempIn = AudioSystem.getClip();
                                  tempIn.open(WAVstream);
                                  if (loopState==false) {
                                	  
                                  }
                                  else {
                                	  tempIn.loop(Clip.LOOP_CONTINUOUSLY);
                                  }
                                  tempIn.start();

                           }
                          else{
                                   System.out.println("WAV path nonexistent!");
                                }
                }
                catch (Exception e){
                           System.out.println(e);
                     }
           }
        
        void stopWAV() {
        	if (tempIn != null) {
        		tempIn.stop();
        	}
        }
}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		if (e.getSource() == btnArcade) {
			BGMplayer.stopWAV();
			frame.dispose();
			new ArcadeMode().start();
			
		}
		else if (e.getSource() == btnClassic) {
			BGMplayer.stopWAV();
			frame.dispose();
			new ClassicMode().start();
		}
		
	}
}
