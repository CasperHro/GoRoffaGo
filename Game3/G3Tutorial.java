import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

/**
 * Write a description of class G3Tutorial here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G3Tutorial extends Actor
{
    private String cmd;
    private String text;
    private int opacity = 0;
    private int showStep = 25;
    
    /**
     * Contructor
     */
    public G3Tutorial(String commandImage, String message)
    {
        cmd = commandImage;
        text = message;
    }
    
    /**
     * Set game info over the complete world
     */
    public void addedToWorld(World world)
    {
        // Only here we know the world dimensions so start making a filling overlay
        drawInfobox();
    }

    /**
     * Draw an overlaying image with the infotext rendered on it
     */
    private void drawInfobox()
    {
        GreenfootImage overlay = new GreenfootImage(520, 80);
        overlay.setColor(new Color(0, 220, 40, 200));
        overlay.fill();

        // Draw the command image
        overlay.drawImage(new GreenfootImage(cmd), 20, 15);        
        
        // Draw the text on the image
        GreenfootImage txt = new GreenfootImage(text, 22, Color.white, new Color(0, 0, 0, 0));
        
        // The draw the text on the image
        overlay.drawImage(txt, 80, (overlay.getHeight() - txt.getHeight()) / 2);
        
        overlay.setTransparency(opacity);
        setImage(overlay);
    }
    
    /**
     * Act - do whatever the G3Tutorial wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if (opacity < 255)
        {
            opacity = Math.min(255, opacity + showStep);
            getImage().setTransparency(opacity);
        }
    }    
}
