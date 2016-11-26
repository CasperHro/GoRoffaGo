import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Transport here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Transport extends Actor
{
    int reset = 0;
    Actor Cargo = null;
    
    /**
     * Act - do whatever the Transport wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        Cargo = getOneObjectAtOffset(10, 0, Cargo.class);
        if (Cargo != null) {
            resetTransport();
        }
        
    }   
    
    public void resetTransport() {
        move(8);
    }
}
