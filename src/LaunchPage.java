import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.ImageIcon;
import java.awt.image.*;


public class LaunchPage implements ActionListener{
	
	JFrame frame = new JFrame();
	JButton btnArcade = new JButton("Arcade Mode");
	JButton btnClassic = new JButton ("Classic Mode"); 
	String BGMpath = "media/bgm-mainmenu.wav";
	WAVplayInstance BGMplayer = new WAVplayInstance();	
	
	
	LaunchPage(){

		BGMplayer.playWAV(BGMpath, true);
		
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
