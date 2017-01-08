import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.lang.Math.*;
import java.util.*;

/**
 * GameInfo shows the game info on an averlay when the game starts.
 * 
 * @author C. Karreman 
 * @version 1.0
 */
public class GameInfo extends Overlay
{
    private boolean startMessage = false;
    
    /**
     * Constructor of the info box
     */
    public GameInfo(String gameInfo, String play)
    {
        text = gameInfo;
        textStart = play;
    }

    /**
     * Check the mouse click to hide
     */
    public void act() 
    {
        super.act();
        
        if (!isShowing && opacity <= 0)
        {
            Game world = getWorldOfType(Game.class);
            world.startGame();
            world.removeObject(this);
        }
        
        if (Greenfoot.mouseClicked(this) || Greenfoot.isKeyDown("Enter"))
        {
            drawStarting(getWorld());
        }
    }    

    /**
     * Draw an overlaying image with the infotext rendered on it
     */
    private void drawStarting(World world)
    {
        if (!startMessage)
        {
            startMessage = true;
            int width = world.getWidth();
            int height = world.getHeight();
            GreenfootImage overlay = new GreenfootImage(width, height);
            overlay.setColor(new Color(0, 0, 0, 96));
            overlay.fill();
            
            // Draw the text on the image
            GreenfootImage txt = new GreenfootImage(textStart, TextSize.size(50), new Color(230, 55, 55), new Color(0, 0, 0, 0));
            int left = (width - txt.getWidth()) / 2;
            int top = (height - txt.getHeight()) / 2;
            
            // First draw a rectangle
            overlay.setColor(new Color(187, 218, 85, 233));
            overlay.fillRect(left - 40, top - 30, txt.getWidth() + 80, txt.getHeight() + 60);
            
            // The draw the text on the image
            overlay.drawImage(txt, left, top);
            
            overlay.setTransparency(opacity);
            setImage(overlay);
            
            Greenfoot.delay(30);
        }
    }
}
