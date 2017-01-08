import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

/**
 * Write a description of class Overlay here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Overlay extends Actor
{
    protected String text = "";
    protected String textStart = "Get ready...\n\nHere we go!!!";
    
    protected int opacity = 0;
    protected boolean isShowing = true;
    protected int showStep = 25;
    protected int hideStep = 10;

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
        }
        
        if (Greenfoot.mouseClicked(this) || Greenfoot.isKeyDown("Enter"))
        {
            isShowing = false;
        }
    }    

    
    /**
     * Get the text
     */
    protected String getText()
    {
        return text;
    }
    
    /**
     * Draw an overlaying image with the infotext rendered on it
     */
    protected void drawInfobox(World world)
    {
        int width = world.getWidth();
        int height = world.getHeight();
        GreenfootImage overlay = new GreenfootImage(width, height);
        overlay.setColor(new Color(0, 0, 0, 96));
        overlay.fill();
        
        // Draw the text on the image
        GreenfootImage txt = new GreenfootImage(getText(), TextSize.size(22), Color.white, new Color(0, 0, 0, 0));
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
    
}
