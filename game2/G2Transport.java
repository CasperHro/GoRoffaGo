import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

/**
 * Write a description of class G2Transport here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G2Transport extends Actor
{
    G2Cargo cargo = null;
    public int resetTransport = 0;
    public int transportCount = 0;
    int pause = 60;
    protected String color;
    int scale = 0;
    int counter = 2;
    
    public G2Transport(String value)
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
    public boolean setCargo(G2Cargo container)
    {
        if (cargo == null) {
            if (container.getColor() == getColor()) {
                cargo = container;

                resetTransport = 1;
                return true;
            }
        }

        return false;
    }
    
    public void act() 
    {
        if (resetTransport == 1) {
            G2Dock world = (G2Dock) getWorld();
            world.removeObject(cargo);
            cargo = null;
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
            } else {
                world.removeTransport(this);
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
