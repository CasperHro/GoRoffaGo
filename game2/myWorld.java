import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;

/**
 * Write a description of class myWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class myWorld extends World
{   
    int shipCentre = 405;
    int waterLevel = 680;
    int width = 4; //number of cargo
    
    /**
     * Constructor for objects of class myWorld.
     * 
     */
    public myWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        // False makes the world boundless.
        super(1000, 800, 1, false);
        prepare();

    }

    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
        sky sky = new sky();
        sky sky2 = new sky();
        Water water = new Water();
        Hook hook = new Hook();
        Harbor harbor = new Harbor();
        Harbor harbor2 = new Harbor();
        Harbor harbor3 = new Harbor();
        Harbor harbor4 = new Harbor();
        Transport transport = new Transport();
        
        Counter counter = new Counter();
        
        Deck deck = new Deck();

        addObject(sky,500,200);
        addObject(sky2,500,500);
        addObject(water,500,750);
        addObject(hook,502,70);
        addObject(harbor,952,754);
        addObject(harbor2,848,754);
        addObject(harbor3,848,658);
        addObject(harbor4,952,658);
        addObject(transport,888,563);
        addObject(counter,958,650);
        addObject(deck,shipCentre,waterLevel);

        setCargo();
        //transport.setLocation(888,563); je zet dit al goed bij addObject(name, x ,y);
        //cargo3.setLocation(4056,430);
    }
    
    public void setCargo() {
        int cargoHeight = 50;
        int i = 0;
        int x = shipCentre-(85*(width/2)-40);
        
        while(i<width){
            Cargo cargo = new Cargo();
            addObject(cargo,x,waterLevel-50);
            x = x+85;
            i++;
        }
    }
    
    public void getWeight() {
        
    }
}
