import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

/**
 * Write a description of class GameOver here.
 * 
 * @author (YuliaKr) 
 * @version (version 1.0, 23.11.16)
 */
public class GameOver extends Actor
{
    private int opacity = 0;
    private int showStep = 10;
    
    public GameOver(String sprite)
    {
        setImage(new GreenfootImage(sprite));
    }
    
    /**
     * When placed on the world make a complete overlay
     */
    public void addedToWorld(World world)
    {
        drawOverlay(world, getImage());
    }

        /**
     * Draw an overlaying image with the infotext rendered on it
     */
    private void drawOverlay(World world, GreenfootImage img)
    {
        int width = world.getWidth();
        int height = world.getHeight();
        GreenfootImage overlay = new GreenfootImage(width, height);
        overlay.setColor(new Color(0, 0, 0, 160));
        overlay.fill();
        
        // Draw the image on the background
        int left = (width - img.getWidth()) / 2;
        int top = (height - img.getHeight()) / 2;
        overlay.drawImage(img, left, top);

        GreenfootImage txt = new GreenfootImage("Click or press enter to return to main menu.", 18, new Color(196, 196, 196), new Color(0, 0, 0, 0));
        left = (width - txt.getWidth()) / 2;
        top = (height - txt.getHeight()) * 5 / 6;
        overlay.drawImage(txt, left, top);
        
        overlay.setTransparency(opacity);
        setImage(overlay);
    }

    public void act() 
    {
        if (opacity < 255)
        {
            opacity = Math.min(255, opacity + showStep);
            getImage().setTransparency(opacity);
        }

        // Check user action
        if (Greenfoot.mouseClicked(this) || Greenfoot.isKeyDown("Enter") || Greenfoot.isKeyDown("Escape"))
        {
            // Return to main menu
            GoRoffaGo mainMenu = new GoRoffaGo();
            // Skip the splash screen with the PoR logo
            mainMenu.nextImage();
            Greenfoot.setWorld(mainMenu);
        }
    }    
}
