import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

/**
 * G3TruckCommand is the visual presentation of the command that will be executed 
 * next when the program is running.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G3TruckCommand extends Actor
{
    private static final int STEP = 64;
    
    private int truckID;
    private String color;
    private String newCommand = "";
    private int opacity = 0;
    
    /**
     * Contructor, initialize the Actor image
     */
    public G3TruckCommand(G3Truck truck)
    {
        truckID = truck.getID();
        color = truck.getColor();
        setImage(new GreenfootImage(140, 50));
    }
    
    /**
     * Animate showing of a new command
     */
    public void act() 
    {
        if (!newCommand.isEmpty())
        {
            opacity = Math.max(0, opacity - STEP);
            getImage().setTransparency(opacity);
            
            if (opacity == 0)
            {
                drawNewCommand();
            }
        }
        else if (opacity < 255)
        {
            opacity = Math.min(255, opacity + STEP);
            getImage().setTransparency(opacity);
        }
    }
    
    public int getID()
    {
        return truckID;
    }
    
    private Color getColor()
    {
        switch(color)
        {
            case "red":
                return new Color(225, 0, 0);
            case "green":
                return new Color(0, 225, 0);
            case "yellow":
                return new Color(245, 245, 0);
            default:
                return new Color(38, 131, 246);
        }
    }

    /**
     * This is the next command to show
     */
    public void showCommand(String command)
    {
        newCommand = command.isEmpty() ? "none" : command;
    }
    
    /**
     * Show the command with a scaled down presentation of the program card.
     */
    private void drawNewCommand()
    {
        // Draw the command and a remove cross on the card
        GreenfootImage img = getImage();
        GreenfootImage txt = new GreenfootImage(String.format("Truck %d", truckID), TextSize.size(22), getColor(), new Color(0, 0, 0, 0));
        GreenfootImage cmd;
        int left = 5;
        int top = (50 - txt.getHeight()) / 2;
        img.clear();
        img.drawImage(txt, left, top);
        
        // Draw the command
        switch(newCommand)
        {
            case "left":
                cmd = new GreenfootImage("G3left.png");
                break;
            case "right":
                cmd = new GreenfootImage("G3right.png");
                break;
            case "forward":
                cmd = new GreenfootImage("G3forward.png");
                break;
            case "ffwd":
                cmd = new GreenfootImage("G3fast.png");
                break;
            case "park":
                cmd = new GreenfootImage("G3pause.png");
                break;
            default:
                cmd = null;
                break;
        }
        if (cmd != null)
        {
            cmd.scale(30, 30);
            img.drawImage(cmd, 95, 10);
        }
        newCommand = "";
    }
}
