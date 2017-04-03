/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package battleboats;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 *
 * @author Robert Zwolinski
 */
public class Assets {
    
    public static BufferedImage imgWater;
    public static BufferedImage[] imgCarrier = new BufferedImage[4],
                                  imgDestroyer = new BufferedImage[4];
    
    
    public static Clip clipPlaced;
    
    
    public static void init() {
        
        try {
            
            imgWater = ImageIO.read(Assets.class.getResource("/images/water.jpg"));
            
            
            for (int i = 0; i < 4; i++) {
                
                imgCarrier[i] = ImageIO.read(Assets.class.getResource
                        ("/images/carrier_scaled_" + String.valueOf(i) + ".png"));
                
            }
            
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Assets.class.getResource("/sound/ding.wav"));
        
            
            clipPlaced = AudioSystem.getClip();
            
            clipPlaced.open(audioInputStream);
            
            
        } catch (UnsupportedAudioFileException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (LineUnavailableException ex) {
            ex.printStackTrace();
        }
        
        
    }
    
}
