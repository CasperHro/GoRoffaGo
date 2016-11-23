import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Game1Harbour here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Game1Harbour extends World
{

    /**
     * Constructor for objects of class Game1Harbour.
     * 
     */
    int DOCKHEIGHT = 60;
    
    public Game1Harbour()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        super(800, 600, 1); 
        
        //Create a boat object
        Boat1 boat = new Boat1();
        //Add boat to the world
        addObject(boat, 10, getHeight()/2);
        
        //Create Dock
        Dock1 dock = new Dock1();
        //Add Dock to the World
        addObject(dock, getWidth()/3, 0 + DOCKHEIGHT);
        
    }
}
