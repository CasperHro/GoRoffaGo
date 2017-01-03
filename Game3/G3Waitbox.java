import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.lang.Math.*;
import java.util.*;

/**
 * Write a description of class G3GameInfo here.
 * 
 * @author C. Karreman 
 * @version 1.0
 */
public class G3Waitbox extends Actor
{
    private String text;
    private GameStage nextStage;
    private String gameOn = "\n\nPress Enter or click to continue...";
    private int opacity = 0;
    private boolean isShowing = true;
    private int showStep = 25;
    private int hideStep = 20;
    
    /**
     * Contructor
     */
    public G3Waitbox(GameStage forStage, String message)
    {
        text = message;
        nextStage = forStage;
    }
    
    /**
     * Set game info over the complete world
     */
    public void addedToWorld(World world)
    {
        // Only here we know the world dimensions so start making a filling overlay
        drawInfobox(world);
    }
    
    /**
     * Check the mouse click to hide
     */
    public void act() 
    {
        if (isShowing && opacity < 255)
        {
            opacity = Math.min(255, opacity + showStep);
            getImage().setTransparency(opacity);
        }
        else if (!isShowing && opacity > 0)
        {
            opacity = Math.max(0, opacity - hideStep);
            getImage().setTransparency(opacity);
            
             if (opacity <= 0)
            {
                G3World world = getWorldOfType(G3World.class);
                world.setStage(nextStage);
                world.removeObject(this);
            }
        }
        
        if (Greenfoot.mouseClicked(this) || Greenfoot.isKeyDown("Enter"))
        {
            isShowing = false;
        }
    }    

    /**
     * Draw an overlaying image with the infotext rendered on it
     */
    private void drawInfobox(World world)
    {
        int width = world.getWidth();
        int height = world.getHeight();
        GreenfootImage overlay = new GreenfootImage(width, height);
        overlay.setColor(new Color(0, 0, 0, 96));
        overlay.fill();
        
        // Draw the text on the image
        GreenfootImage txt = new GreenfootImage(getText(), 22, Color.white, new Color(0, 0, 0, 0));
        int left = (width - txt.getWidth()) / 2;
        int top = (height - txt.getHeight()) / 2;
        
        // First draw a rectangle
        overlay.setColor(new Color(0, 0, 108, 233));
        overlay.fillRect(left - 20, top - 20, txt.getWidth() + 40, txt.getHeight() + 40);
        
        // The draw the text on the image
        overlay.drawImage(txt, left, top);
        
        overlay.setTransparency(opacity);
        setImage(overlay);
    }
    
    public String getText()
    {
        return text + gameOn;
    }
}
