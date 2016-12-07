import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.awt.Font;
import java.util.*;

/**
 * Write a description of class G3_Truck here.
 * 
 * @author C. Karreman
 * @version 1.0
 */
public class G3_Truck extends G3_FieldObject
{
    /**
     * Vars for the truck
     */
    private List<String> program = new ArrayList<String>(); // List that holds the program
    private boolean crashed = false; // Flag if boat is crashed, animation can still be running
    private int crashLoop = 0; // Crash animation stepcounter
    private String container = ""; // The container on this truck
    private int id;
    private boolean executing = false;
    private boolean active = false;
    private int blinkStep = -15;
    
    
    
    /**
     * Contructor for the truck needs a color;
     */
    public G3_Truck(String containerColor, int index)
    {
        container = containerColor;
        id = index;
    }
    
    /**
     * When added to the world create a container on the truck
     */
    protected void addedToWorld(World world)
    {
        // Draw the color under the container sprite
        GreenfootImage overlay = new GreenfootImage("Container.png");
        GreenfootImage colored = new GreenfootImage(overlay.getWidth(), overlay.getHeight());
        switch(container)
        {
            case "red":
                colored.setColor(new Color(225, 0, 0));
                break;
            case "green":
                colored.setColor(new Color(0, 225, 0));
                break;
            case "yellow":
                colored.setColor(new Color(96, 196, 0));
                break;
            case "blue":
                colored.setColor(new Color(0, 0, 225));
                break;
        }
        colored.fillRect(0, 0, colored.getWidth(), colored.getHeight());
        colored.drawImage(overlay, 0, 0);
        
        // Now draw the truck ID
        colored.setFont(colored.getFont().deriveFont(Font.BOLD, 20));
        colored.setColor(new Color(255, 255, 255));
        colored.drawString(Integer.toString(id), colored.getWidth() / 2, colored.getHeight() / 2 + 9);
        
        // And now draw it on the truck
        getImage().drawImage(colored, (getImage().getWidth() - colored.getWidth()) / 4, (getImage().getHeight() - colored.getHeight()) / 2);
    }
    
    
    /**
     * Act - do whatever the G3_Truck wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
        if (Greenfoot.mouseClicked(this))
        {
            getWorldOfType(Game3World.class).selectTruckForProgramming(this);
        }
        else if (active)
        {
            GreenfootImage img = getImage();
            int nextStep = img.getTransparency() + blinkStep;
            if (nextStep < 64)
            {
                nextStep = 128;
                blinkStep = 15;
            }
            if (nextStep > 255)
            {
                nextStep = 255;
                blinkStep = -15;
            }
            img.setTransparency(nextStep);
        }
    }
    
    public int getID()
    {
        return id;
    }
    
    public boolean getExecuting()
    {
        return executing;
    }
    
    public int getProgramLength()
    {
        return program.size();
    }
    
    public void setActive(boolean value)
    {
        active = value;
        // When active deactivate all other trucks
        if (active)
        {
            for(G3_Truck t : getWorld().getObjects(G3_Truck.class))
            {
                if (t != this)
                {
                    t.setActive(false);
                }
            }
        }
        else
        {
            getImage().setTransparency(255);
        }
    }
}
