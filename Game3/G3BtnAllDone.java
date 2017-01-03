import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

/**
 * Write a description of class G3BtnAllDone here.
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
     * Act - do whatever the G3BtnAllDone wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
        if (Greenfoot.mouseClicked(this))
        {
            getWorldOfType(G3World.class).startStage(GameStage.RUNNINGCODE, "So you're done programming, let's see where the trucks go!");
        }
    }
}
