import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

/**
 * G3BtnAllDone is a button the user can click when done programming.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G3BtnAllDone extends Actor
{
    /**
     * When added to the world create a container on the truck
     */
    protected void addedToWorld(World world)
    {
        // Draw the color under the container sprite
        GreenfootImage txt = new GreenfootImage("All done", 22, Color.white, new Color(0, 0, 0, 0));
        GreenfootImage btn = new GreenfootImage(txt.getWidth() + 30, txt.getHeight() + 10);
        btn.setColor(new Color(210, 30, 30));
        btn.fillRect(0, 0, btn.getWidth(), btn.getHeight());
        btn.drawImage(txt, 15, 5);
        
        setImage(btn);
    }
    
    
    /**
     * Only act on mouse clicks to end programming
     */
    public void act() 
    {
        // Add your action code here.
        if (Greenfoot.mouseClicked(this))
        {
            getWorldOfType(G3Dock.class).startStage(G3GameStage.RUNNINGCODE, "So you're done programming, let's see where the trucks go!");
        }
    }
}
