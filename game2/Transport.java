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
    int pause = 140;
    
    /**
     * Act - do whatever the Transport wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        Cargo = getOneObjectAtOffset(0, 0, Cargo.class);
        if (Cargo != null || Greenfoot.isKeyDown("r")) {
            resetTransport = 1;
        }
        
        if (resetTransport == 1) {
            if(pause > 0) {
                setLocation(getX()+3, getY());
                pause--;
            }
              
            if(pause == 0) {
                if (getX() >= 888) {
                    setLocation(getX()-3, getY());
                }
                else {
                    resetTransport = 0;
                    pause = 140;
                    transportCount += 1;
                    World world;
                    world = getWorld();
                }
                

            }

        }
        
    }
}
