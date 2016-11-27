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
        Hook hook = new Hook();
        addObject(hook,502,70);
        Cargo cargo = new Cargo();
        addObject(cargo,507,417);
        Harbor harbor = new Harbor();
        addObject(harbor,954,754);
        Harbor harbor2 = new Harbor();
        addObject(harbor2,854,761);
        harbor2.setLocation(847,753);
        harbor2.setLocation(848,754);
        harbor.setLocation(953,761);
        harbor.setLocation(952,754);
        Harbor harbor3 = new Harbor();
        addObject(harbor3,855,658);
        harbor3.setLocation(848,658);
        Harbor harbor4 = new Harbor();
        addObject(harbor4,962,665);
        harbor4.setLocation(953,659);
        harbor4.setLocation(952,659);
        harbor4.setLocation(952,658);
        Transport transport = new Transport();
        addObject(transport,888,567);
        transport.setLocation(888,563);
        Counter counter = new Counter();
        addObject(counter,958,650);
        Cargo cargo2 = new Cargo();
        addObject(cargo2,562,423);
        Cargo cargo3 = new Cargo();
        addObject(cargo3,482,269);
        cargo3.setLocation(406,430);
    }
}
