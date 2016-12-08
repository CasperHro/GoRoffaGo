import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Transport here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Transport extends Actor
{
    Actor Cargo = null;
    public int resetTransport = 0;
    public int transportCount = 0;
    int pause = 40;
    protected String color;
    
    public Transport()
    {
        GreenfootImage image = getImage();  
        image.scale(120, 60);
        setImage(image);
    }
    
    /**
     * Act - do whatever the Transport wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        Cargo = getOneObjectAtOffset(0, 0, Cargo.class);
        if (Cargo != null) {
            resetTransport = 1;
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
                    pause = 40;
                    transportCount += 1;
                    World world;
                    world = getWorld();
                }
                

            }

        }
        
    }
    
    public String getColor() {
        return color;
    }
}
