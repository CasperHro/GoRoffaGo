import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.lang.Math.*;
import java.util.*;

/**
 * Write a description of class G1_GameInfo here.
 * 
 * @author C. Karreman 
 * @version 1.0
 */
public class G1_GameInfo extends Actor
{
    private String text = "Game info\n"+
                  "Your mission is to direct the ships to the right harbour.\n"+
                  "When a ship is docked a point is scored!\n\n"+
                  "Use the arrows to navigate:\n"+
                  "up/down - speed up or slow down\n"+
                  "left/right - rotate left/right\n\n"+
                  "Use 'S' to select a boat.\n\n"+
                  "When you crash 5 boats, you're game over!\n\n\n"+
                  "Click or press Enter to start...";
    private String textStart = "Get ready...\n\nHere we go!!!";
    
    private int opacity = 0;
    private boolean isShowing = true;
    private int showStep = 25;
    private int hideStep = 10;
    

    
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
                Game1Harbour world = getWorldOfType(Game1Harbour.class);
                world.startGame();
                world.removeObject(this);
            }
        }
        
        if (Greenfoot.mouseClicked(this) || Greenfoot.isKeyDown("Enter"))
        {
            isShowing = false;
            drawStarting(getWorld());
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
        GreenfootImage txt = new GreenfootImage(text, 22, Color.white, new Color(0, 0, 0, 0));
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
    
    /**
     * Draw an overlaying image with the infotext rendered on it
     */
    private void drawStarting(World world)
    {
        int width = world.getWidth();
        int height = world.getHeight();
        GreenfootImage overlay = new GreenfootImage(width, height);
        overlay.setColor(new Color(0, 0, 0, 96));
        overlay.fill();
        
        // Draw the text on the image
        GreenfootImage txt = new GreenfootImage(textStart, 50, new Color(230, 55, 55), new Color(0, 0, 0, 0));
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
