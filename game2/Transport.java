import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

/**
 * Write a description of class Transport here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Transport extends Actor
{
    Cargo cargo = null;
    public int resetTransport = 0;
    public int transportCount = 0;
    int pause = 120;
    public String color;
    
    public Transport()
    {
    }

    public void setColor(String value)
    {
        color = value;
        GreenfootImage img = getImage();
        
        switch(color)
        {
            case "red":
                img.setColor(new Color(255, 0, 0));
                break;
            case "green":
                img.setColor(new Color(0, 255, 0));
                break;
            case "blue":
                img.setColor(new Color(0, 0, 255));
                break;
        }
        img.fillRect(10, 40, 40, 80);
    }
    
    /**
     * Act - do whatever the Transport wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        if (cargo == null) {
            cargo = (Cargo)getOneObjectAtOffset(0, 0, Cargo.class);
            if (cargo != null) {
                if (cargo.getColor() == getColor()) {
                    // Add score ???
                    Counter c = getWorld().getObjects(Counter.class).get(0);
                    if (c != null)
                    {
                        c.addTransportScore(1);
                    }
    
                    resetTransport = 1;
                }
            }
        }
        
        if (resetTransport == 1) {
            if(pause > 0) {
                setLocation(getX()+3, getY());
                pause--;
            }
              
            if(pause == 0) {
                if (getX() >= 720) {
                    setLocation(getX()-3, getY());
                }
                else {
                    resetTransport = 0;
                    pause = 120;
                    transportCount += 1;

                    myWorld world = (myWorld) getWorld();
                    world.setTransport(getX(), getY());
                    world.removeObject(this);
                }
                

            }

        }
        
    }
    
    public String getColor() {
        return color;
    }
}
