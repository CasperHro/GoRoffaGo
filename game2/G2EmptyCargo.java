import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class G2EmptyCargo here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G2EmptyCargo extends Actor
{
    
    public int gridID;
    
    public G2EmptyCargo()
    {
        GreenfootImage image = getImage();  
        image.scale(80, 40);
        setImage(image);
    }
    
    /**
     * Act - do whatever the EmptyCargo wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Add your action code here.
    }    
    
    public int getId() {
        return gridID;
    }
    
    public int setId(int id) {
        gridID = id;
        return gridID;
    }
}
