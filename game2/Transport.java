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
    int pause = 60;
    public String color;
    int scale = 0;
    int counter = 2;
    
    public Transport(String value)
    {
        color = value;
    }

    protected void addedToWorld(World world)
    {
        switch(color)
        {
            case "red":
                setImage("g2_truckRed.png");
                break;
            case "green":
                setImage("g2_truckYellow.png");
                break;
            case "blue":
                setImage("g2_truckBlue.png");
                break;
        }
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
                    // release from hook
                    
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
            myWorld world = (myWorld) getWorld();
            world.removeObject(cargo);
            GreenfootImage image = getImage();
            if(pause > 0) {
                if (image.getWidth() > 9) {
                    if (scale == 1) {
                        image.scale(image.getWidth() - 1, image.getHeight() - 1);
                        setLocation(getX(), getY()+1);
                        setImage(image);
                    }
                }

                
                pause--;
            }
              
            if(pause == 0) {
                if (image.getWidth() < 55) {
                    if (scale == 1) {
                        setLocation(getX(), getY()-1);
                        image.scale(image.getWidth() + 1, image.getHeight() + 1);
                        setImage(image);
                    }
                }
                else {
                    resetTransport = 0;
                    pause = 60;
                    transportCount += 1;

                    world.removeTransport(this);
                }
                

            }
            
            if (counter > 0) {
                scale = 0;
                counter--;
            } else {
                scale = 1;
                counter = 2;
            }

        }
        
    }
    
    public String getColor() {
        return color;
    }
    
    public int getResetTransport() {
        return resetTransport;
    }
}
