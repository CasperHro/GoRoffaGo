import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

/**
 * Write a description of class G3Destination here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G3Destination extends G3FieldObject
{
    /**
     * Private vars
     */
    private String color;
    
    /**
     * Contructor, requires a color
     */
    public G3Destination(String containerColor)
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
                colored.setColor(new Color(255, 0, 0, 90));
                break;
            case "green":
                colored.setColor(new Color(0, 255, 0, 90));
                break;
            case "yellow":
                colored.setColor(new Color(255, 255, 0, 90));
                break;
            default:
                colored.setColor(new Color(0, 0, 255, 90));
                break;
        }
        
        colored.fillRect(0, 0, colored.getWidth(), colored.getHeight());
        colored.drawImage(img, 0, 0);
        img.drawImage(colored, 0, 0);
    }
    
    /**
     * Act - do whatever the G3Destination wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // This class does nothing really. It just sits where it's placed
    }
    
    public String getColor()
    {
        return color;
    }
    
    public G3Truck getParkedTruck()
    {
        for(G3Truck t : getWorld().getObjects(G3Truck.class))
        {
            // Color, location and orientation must be the same (north/south are good and east/west are equal)
            if (t.getColor() == color && 
                t.getX() == getX() && t.getY() == getY() && 
                Math.abs(t.getRotation() - 90) % 180 == Math.abs(getRotation() - 90) % 180)
            {
                return t;
            }
        }
        return null;
    }
}
