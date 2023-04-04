import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JFrame;


public class LaunchPage implements ActionListener{
	
	JFrame frame = new JFrame();
	JButton myButton = new JButton("CLICK TO PLAY");
	String BGMpath = "media/Maplestory Theme Music - Intro.wav";
	WAVplayInstance BGMplayer = new WAVplayInstance();
	
	LaunchPage(){

		BGMplayer.playWAV(BGMpath, true);
		
		myButton.setBounds(300, 400, 200, 40);
		myButton.setFocusable(false);
		myButton.addActionListener(this);
		
		
		frame.add(myButton);
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
		if (e.getSource() == myButton) {
			BGMplayer.stopWAV();
			frame.dispose();
			new FruitNinja().start();
			
		}
		
	}
}
