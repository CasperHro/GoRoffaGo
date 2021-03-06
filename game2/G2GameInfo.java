import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.lang.Math.*;
import java.util.*; 

/**
 * Write a description of class G2GameInfo here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G2GameInfo extends Actor
{
    /**
     * Act - do whatever the G4_Gameinfo wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    private String text = "Game info\n"+
                  "Your mission is to unload the ships.\n"+
                  "Pick the right container from the ship and place it on the\n"+
                  "transport, but keep the ship in balance!!\n\n"+
                  "Use the arrow keys to navigate the container hook and\n"+
                  "press 's' to select a container or 'd' to release a container.\n\n"+
                  "Be fast or the computer wins...\n\n"+
                  "\n\n"+
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
                G2Dock world = getWorldOfType(G2Dock.class);
                
                world.removeObject(this);
                world.startGame();
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