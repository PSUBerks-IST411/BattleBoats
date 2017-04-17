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
                                  imgBattleship = new BufferedImage[4],
                                  imgDestroyer = new BufferedImage[4],
                                  imgSubmarine = new BufferedImage[4],
                                  imgPatrolBoat = new BufferedImage[4];
    
    
    public static Clip clipPlaced;
    public static Clip clipWrong;
    public static Clip clipPickup;
    public static Clip clipSwap;
    public static Clip clipDeselect;
    
    
    public static void init() {
        
        try {
            
            imgWater = ImageIO.read(Assets.class.getResource("/images/water.jpg"));
            
            
            for (int i = 0; i < 4; i++) {
                
                imgCarrier[i] = ImageIO.read(Assets.class.getResource
                        ("/images/carrier_scaled_" + String.valueOf(i) + ".png"));
                
                imgBattleship[i] = ImageIO.read(Assets.class.getResource
                        ("/images/battleship_scaled_" + String.valueOf(i) + ".png"));
                
                imgDestroyer[i] = ImageIO.read(Assets.class.getResource
                        ("/images/destroyer_scaled_" + String.valueOf(i) + ".png"));
                
                imgSubmarine[i] = ImageIO.read(Assets.class.getResource
                        ("/images/submarine_scaled_" + String.valueOf(i) + ".png"));
                
                imgPatrolBoat[i] = ImageIO.read(Assets.class.getResource
                        ("/images/patrolboat_scaled_" + String.valueOf(i) + ".png"));
                
            }
            
            
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        
        
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(Assets.class.getResource("/sound/placed.wav"));
            clipPlaced = AudioSystem.getClip();
            clipPlaced.open(audioInputStream);
            
            audioInputStream = AudioSystem.getAudioInputStream(Assets.class.getResource("/sound/wrong.wav"));
            clipWrong = AudioSystem.getClip();
            clipWrong.open(audioInputStream);
            
            audioInputStream = AudioSystem.getAudioInputStream(Assets.class.getResource("/sound/pickup.wav"));
            clipPickup = AudioSystem.getClip();
            clipPickup.open(audioInputStream);
            
            audioInputStream = AudioSystem.getAudioInputStream(Assets.class.getResource("/sound/swap.wav"));
            clipSwap = AudioSystem.getClip();
            clipSwap.open(audioInputStream);
            
            audioInputStream = AudioSystem.getAudioInputStream(Assets.class.getResource("/sound/deselect.wav"));
            clipDeselect = AudioSystem.getClip();
            clipDeselect.open(audioInputStream);
            
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
            ex.printStackTrace();
        }
        
        
    }
    
}
