import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class cargo here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Cargo extends Actor
{
    
    protected int weight;
    public String color;
    
    public Cargo()
    {
        GreenfootImage image = getImage();  
        image.scale(80, 35);
        setImage(image);
    }
    
    /**
     * Act - do whatever the cargo wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
    }    
    
    public int getWeight() {
        return weight;
    }
    
    public String getColor() {
        return color;
    }
}
