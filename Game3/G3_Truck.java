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
    private int executing = 0;
    private boolean active = false;
    private int blinkStep = -15;
    
    private static final int executeSteps = 40;
    private int startDirection;
    private int startX;
    private int startY;
    private int endDirection;
    private int endX;
    private int endY;

    
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
        
        if (executing > 0)
        {
            executing--;

            // Do step towards the destinasion
            setRotation(endDirection - ((endDirection - startDirection) * executing / executeSteps));
            setLocation(endX - ((endX - startX) * executing / executeSteps),
                        endY - ((endY - startY) * executing / executeSteps));
            
            // TODO: Check for crash or out of playfield
            
        }
    }
    
    public int getID()
    {
        return id;
    }
    
    public boolean getExecuting()
    {
        return (executing > 0);
    }
    
    public int getProgramLength()
    {
        return program.size();
    }
    
    public boolean getActive()
    {
        return active;
    }
    
    public void setActive(boolean value)
    {
        active = value;
        // When active deactivate all other trucks
        if (!active)
        {
            getImage().setTransparency(255);
        }
    }
    
    /**
     * Adds a command to the program and returns the index of the new element
     */
    public int addCommand(String value)
    {
        program.add(value);
        
        return program.size() -1;
    }
    
    public void executeStep()
    {
        if (executing == 0 && program.size() > 0)
        {
            // first record the current position and orientation
            startDirection = getRotation();
            startX = getX();
            startY = getY();
            endDirection = startDirection;
            endX = startX;
            endY = startY;
            
            // Calculate the new coordinates to move to. 1 step = 40 pixelfs or 90 degrees
            switch(program.get(0))
            {
                case "left":
                    endDirection = startDirection - 90;
                    endX = startX + (startDirection <= 90 ? 40 : -40);
                    endY = startY + (startDirection % 270 != 0 ? 40 : -40);
                    break;
                case "right":
                    endDirection = startDirection + 90;
                    endX = startX + (startDirection % 270 == 0 ? 40 : -40);
                    endY = startY + (startDirection <= 90 ? 40 : -40);
                    break;
                case "forward":
                    if (startDirection % 180 == 0)
                    {
                        endX = startX + (startDirection == 0 ? 40 : -40);
                    }
                    else
                    {
                        endY = startY + (startDirection == 90 ? 40 : -40);
                    }
                    break;
                case "ffwd":
                    if (startDirection % 180 == 0)
                    {
                        endX = startX + (startDirection == 0 ? 80 : -80);
                    }
                    else
                    {
                        endY = startY + (startDirection == 90 ? 80 : -80);
                    }
                    break;
            }
            program.remove(0);
            executing = executeSteps;
        }
    }
    
    public String getColor()
    {
        return container;
    }
}
