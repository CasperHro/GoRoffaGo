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
    private int gridSize;
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
        gridSize = ((Game3World)world).gridSize;
        
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
                colored.setColor(new Color(255, 255, 0));
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
            // Blink loop
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
        
        if (!crashed && executing > 0)
        {
            executing--;

            // Do step towards the destinasion
// This lineair movement didn't show nice when rotating. We now implemented 
// rotation via angular positions based on the radius.
//            setRotation(endDirection - ((endDirection - startDirection) * executing / executeSteps));
//            setLocation(endX - ((endX - startX) * executing / executeSteps),
//                        endY - ((endY - startY) * executing / executeSteps));
            if (endDirection == startDirection)
            {
                setLocation(endX - ((endX - startX) * executing / executeSteps),
                            endY - ((endY - startY) * executing / executeSteps));
            }
            else 
            {
                int r = startDirection + ((endDirection - startDirection) * (executeSteps - executing) / executeSteps);
                setRotation(r);

                if (startDirection % 180 == 0)
                {
                    setLocation(endX - (int)(40 * Math.cos(Math.toRadians(r))),
                                startY + (int)(40 * Math.sin(Math.toRadians(r))));
                }
                else
                {
                    setLocation(startX + (int)(40 * Math.cos(Math.toRadians(r))),
                                endY - (int)(40 * Math.sin(Math.toRadians(r))));
                }
            }
            
            // TODO: Check for crash or out of playfield
            for(G3_FieldObject o : getWorld().getObjects(G3_Obstacle.class))
            {
                if (o != this && o.getX() == getX() && o.getY() == getY())
                {
                    setCrashed();
                }
            }
            for(G3_FieldObject o : getWorld().getObjects(G3_Truck.class))
            {
                if (o != this && o.getX() == getX() && o.getY() == getY())
                {
                    setCrashed();
                }
            }
        }
    }
    
    public int getID()
    {
        return id;
    }
    
    public boolean getExecuting()
    {
        return (!crashed && executing > 0);
    }
    
    public int getProgramLength()
    {
        return (crashed ? 0 : program.size());
    }
    
    public List<String> getProgram()
    {
        return program;
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
    
    /**
     * Remove the programstep
     */
    public void removeCommandAt(int step)
    {
        program.remove(step);
        if (active)
        {
            // Set new step on all visual steps on screen
            for(G3_ProgramStep s : getWorld().getObjects(G3_ProgramStep.class))
            {
                if (s.getStep() > step)
                {
                    s.setStep(s.getStep() - 1);
                }
            }
        }
    }
    
    public void executeStep()
    {
        if (!crashed && executing == 0 && program.size() > 0)
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
                    endX = startX + (startDirection <= 90 ? gridSize : -gridSize);
                    endY = startY + (startDirection % 270 != 0 ? gridSize : -gridSize);
                    break;
                case "right":
                    endDirection = startDirection + 90;
                    endX = startX + (startDirection % 270 == 0 ? gridSize : -gridSize);
                    endY = startY + (startDirection <= 90 ? gridSize : -gridSize);
                    break;
                case "forward":
                    if (startDirection % 180 == 0)
                    {
                        endX = startX + (startDirection == 0 ? gridSize : -gridSize);
                    }
                    else
                    {
                        endY = startY + (startDirection == 90 ? gridSize : -gridSize);
                    }
                    break;
                case "ffwd":
                    if (startDirection % 180 == 0)
                    {
                        endX = startX + (startDirection == 0 ? 2 * gridSize : -2 * gridSize);
                    }
                    else
                    {
                        endY = startY + (startDirection == 90 ? 2 * gridSize : -2 * gridSize);
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
    
    /**
     * Sets the crashed flag and adds a point to the worlds crash counter.
     * The explosion is also added to the crashed object.
     */
    public void setCrashed()
    {
        if (!crashed)
        {
            crashed = true;
            getWorldOfType(Game3World.class).addCrash(1);
        }
        G3_Explosion e = new G3_Explosion();
        getWorld().addObject(e, getX(), getY());
    }
}
