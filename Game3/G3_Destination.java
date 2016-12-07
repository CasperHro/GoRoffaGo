import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

/**
 * Write a description of class G3_Destination here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G3_Destination extends G3_FieldObject
{
    /**
     * Private vars
     */
    private String color;
    
    /**
     * Contructor, requires a color
     */
    public G3_Destination(String containerColor)
    {
        color = containerColor;
    }

    protected void addedToWorld(World world)
    {
        // Draw the color under the container sprite
        GreenfootImage img = getImage();
        GreenfootImage colored = new GreenfootImage(img.getWidth(), img.getHeight());
        switch(color)
        {
            case "red":
                colored.setColor(new Color(255, 160, 160));
                break;
            case "green":
                colored.setColor(new Color(160, 255, 160));
                break;
            case "yellow":
                colored.setColor(new Color(240, 240, 160));
                break;
            case "blue":
                colored.setColor(new Color(160, 160, 255));
                break;
        }
        
        colored.fillRect(0, 0, colored.getWidth(), colored.getHeight());
        colored.drawImage(img, 0, 0);
        img.drawImage(colored, 0, 0);
    }
    
    /**
     * Act - do whatever the G3_Destination wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // This class does nothing really. It just sits where it's placed
    }    
}
