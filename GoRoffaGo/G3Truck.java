import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.awt.Font;
import java.util.*;

/**
 * G3Truck carries a container that must be transported to an other location.
 * The truck must be programmed with commands to move around the playfield.
 * 
 * @author C. Karreman
 * @version 1.0
 */
public class G3Truck extends G3FieldObject
{
    /**
     * Vars for the truck
     */
    private List<String> program = new ArrayList<>(); // List that holds the program
    private boolean crashed = false; // Flag if boat is crashed, animation can still be running
    private String container = ""; // The container on this truck
    private int id;
    private int executing = 0;
    private boolean active = false;
    private int blinkStep = -15;
    
    private static final int EXECUTESTEPS = 40;
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
    public G3Truck(String containerColor, int index)
    {
        container = containerColor;
        id = index;
    }
    
    /**
     * When added to the world create a container on the truck
     */
    protected void addedToWorld(World world)
    {
        gridSize = ((G3World)world).GRIDSIZE;
        
        // Draw the color under the container sprite
        GreenfootImage overlay = new GreenfootImage("G3Container.png");
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
            default:
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
     * Check for selection in the program stage. When active, blink by animating the transparency.
     */
    public void act() 
    {
        // Add your action code here.
        if (Greenfoot.mouseClicked(this))
        {
            getWorldOfType(G3World.class).selectTruckForProgramming(this);
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
            doStep();
            checkCrash();
        }
    }
    
    /**
     * Do step towards the destination, this is the actual execution of the command to 
     * move the truck towards the new location.
     */
    private void doStep()
    {
        // Decrease the animation step
        executing--;

        /*
         * This lineair movement didn't show nice when rotating. We now implemented 
         * rotation via angular positions based on the radius of the rotation.
         *    setRotation(endDirection - ((endDirection - startDirection) * executing / EXECUTESTEPS));
         *    setLocation(endX - ((endX - startX) * executing / EXECUTESTEPS),
         *                endY - ((endY - startY) * executing / EXECUTESTEPS));
         */
        if (endDirection == startDirection)
        {
            setLocation(endX - ((endX - startX) * executing / EXECUTESTEPS),
                        endY - ((endY - startY) * executing / EXECUTESTEPS));
        }
        else 
        {
            int r = startDirection + ((endDirection - startDirection) * (EXECUTESTEPS - executing) / EXECUTESTEPS);
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
    }
    
    /**
     * Check for other objects or moving out of the playfield.
     */
    private void checkCrash()
    {
        // Check out of playfield
        if (getWorldOfType(G3World.class).checkOutOfBounds(this))
        {
            setCrashed(null);
            return;
        }
        
        // First check obstacles        
        if (!crashed && getOneIntersectingObject(G3Obstacle.class) != null)
        {
            setCrashed(null);
        }
        
        // Then other trucks
        for(G3Truck o : getIntersectingObjects(G3Truck.class))
        {
            o.setCrashed(this);
            setCrashed(null);
        }
    }
    
    /**
     * Sets the crashed flag and adds a point to the worlds crash counter.
     * The explosion is also added to the crashed object.
     */
    public void setCrashed(G3Truck by)
    {
        if (!crashed)
        {
            crashed = true;
            getWorldOfType(G3World.class).addCrash(1);
            Greenfoot.playSound("metalcrash.mp3");
        }
        
        G3Explosion e = new G3Explosion();
        if (by == null)
        {
            // Self inflicted, crash in front
            double oX = getImage().getWidth() / 2.0 -10;
            double r = Math.toRadians(getRotation());
            double x = getX() + (oX * Math.cos(r)); 
            double y = getY() + (oX * Math.sin(r));
                
            getWorld().addObject(e, (int)Math.round(x), (int)Math.round(y));
        }
    }
    
    /**
     * Return the truck ID
     */
    public int getID()
    {
        return id;
    }
    
    /**
     * Return true if animation runs for the truck movements
     */
    public boolean getExecuting()
    {
        return !crashed && executing > 0;
    }
    
    /**
     * Return the number of commands in the program
     */
    public int getProgramLength()
    {
        return crashed ? 0 : program.size();
    }
    
    /**
     * Return the program list
     */
    public List<String> getProgram()
    {
        return program;
    }
    
    /**
     * Return true when selected for programming
     */
    public boolean getActive()
    {
        return active;
    }
    
    /**
     * Sets the active flag. This results in a blinking truck.
     */
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
     * Remove the programstep in the given index
     */
    public void removeCommandAt(int step)
    {
        program.remove(step);
        if (active)
        {
            // Set new step on all visual steps on screen
            for(G3ProgramStep s : getWorld().getObjects(G3ProgramStep.class))
            {
                if (s.getStep() > step)
                {
                    s.setStep(s.getStep() - 1);
                }
            }
        }
    }
    
    /**
     * External call to initiate the execution of the next program card
     */
    public void executeStep()
    {
        if (!crashed && executing == 0 && !program.isEmpty())
        {
            // first record the current position and orientation
            startDirection = getRotation();
            startX = getX();
            startY = getY();
            endDirection = startDirection;
            endX = startX;
            endY = startY;
            
            // Calculate the new coordinates to move to. 1 step = 40 pixelfs or 90 degrees
            calcStep(program.get(0));
            
            // And remove the first command
            program.remove(0);
            executing = EXECUTESTEPS;
        }
    }

    /**
     * Calculate the real destination location after the command
     */
    private void calcStep(String command)
    {
        if ("left".equals(command))
        {
            endDirection = startDirection - 90;
            endX = startX + (startDirection <= 90 ? gridSize : -gridSize);
            endY = startY + (startDirection % 270 != 0 ? gridSize : -gridSize);
        }
        else if ("right".equals(command))
        {
            endDirection = startDirection + 90;
            endX = startX + (startDirection % 270 == 0 ? gridSize : -gridSize);
            endY = startY + (startDirection <= 90 ? gridSize : -gridSize);
        }
        else if ("forward".equals(command))
        {
            if (startDirection % 180 == 0)
            {
                endX = startX + (startDirection == 0 ? gridSize : -gridSize);
            }
            else
            {
                endY = startY + (startDirection == 90 ? gridSize : -gridSize);
            }
        }
        else if ("ffwd".equals(command))
        {
            if (startDirection % 180 == 0)
            {
                endX = startX + (startDirection == 0 ? 2 * gridSize : -2 * gridSize);
            }
            else
            {
                endY = startY + (startDirection == 90 ? 2 * gridSize : -2 * gridSize);
            }
        }
    }
    
    /**
     * Get the cargo color
     */
    public String getColor()
    {
        return container;
    }
    
    /**
     * Returns the next command in the program. 
     * Not the currently executing one!!!
     */
    public String getCurrentCommand()
    {
        if (!crashed && !program.isEmpty())
        {
            return program.get(0);
        }
        return "";
    }
}
