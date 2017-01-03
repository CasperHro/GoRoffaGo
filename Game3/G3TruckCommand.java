import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

/**
 * Write a description of class G3TruckCommand here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G3TruckCommand extends Actor
{
    private int truckID;
    
    
    public G3TruckCommand(int index)
    {
        truckID = index;
        setImage(new GreenfootImage(140, 50));

    }
    
    /**
     * Act - do whatever the G3TruckCommand wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
    }
    
    public int getID()
    {
        return truckID;
    }
    
    public void showCommand(String command)
    {
        // Draw the command and a remove cross on the card
        GreenfootImage img = getImage();
        GreenfootImage txt = new GreenfootImage(String.format("Truck %d", truckID), 22, Color.white, new Color(0, 0, 0, 0));
        GreenfootImage cmd;
        int left = 5;
        int top = (50 - txt.getHeight()) / 2;
        img.clear();
        img.drawImage(txt, left, top);
        
        // Draw the command
        switch(command)
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
    }
}
