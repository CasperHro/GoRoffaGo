import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class G3ProgramStep here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G3ProgramStep extends Actor
{
    private String cmd;
    private int step;
    
    private int oldY = 0;
    private int newY = 0;
    private int ani = 0;
    
    private static final int ANISTEPS = 20;
    
    public G3ProgramStep(String command, int programStep)
    {
        cmd = command;
        step = programStep;
    }
    
    protected void addedToWorld(World world)
    {
        // Draw the command and a remove cross on the card
        GreenfootImage img = new GreenfootImage(120, 50);
        switch(cmd)
        {
            case "left":
                img.drawImage(new GreenfootImage("G3left.png"), 0, 0);
                break;
            case "right":
                img.drawImage(new GreenfootImage("G3right.png"), 0, 0);
                break;
            case "forward":
                img.drawImage(new GreenfootImage("G3forward.png"), 0, 0);
                break;
            case "ffwd":
                img.drawImage(new GreenfootImage("G3fast.png"), 0, 0);
                break;
            default:
                img.drawImage(new GreenfootImage("G3pause.png"), 0, 0);
                break;
        }
        img.drawImage(new GreenfootImage("G3Cross.png"), 70, 0);
        setImage(img);
        getImage().setTransparency(0);
    }

    /**
     * Act - do whatever the G3ProgramStep wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if (ani > 0)
        {
            ani--;
            if (oldY == newY)
            {
                // Showing
                setLocation(getX(), newY);
                getImage().setTransparency(255 * (ANISTEPS - ani) / ANISTEPS);
            }
            else
            {
                // moving
                setLocation(getX(), oldY + ((newY - oldY) * (ANISTEPS - ani) / ANISTEPS));
                getImage().setTransparency(255);
            }
        }
        else if (Greenfoot.mouseClicked(this) &&
                 Greenfoot.getMouseInfo().getX() - getX() + (getImage().getWidth() / 2) > 70)
        {
            // Only hits on the X removes the command
            getWorldOfType(G3World.class).removeProgramStep(this);
        }
    }
    
    public int getStep()
    {
        return step;
    }
    
    public void setStep(int value)
    {
        step = value;
    }
    
    public void setYLocation(int value)
    {
        // First time we do only 1 step (no animation)
        if (oldY == 0)
        {
            oldY = value;
        }
        else
        {
            oldY = getY();
        }
        newY = value;
        ani = ANISTEPS;
    }
}
