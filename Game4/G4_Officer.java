import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * Write a description of class G4_Officer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G4_Officer extends Actor
{
    /**
     * Act - do whatever the G4_Officer wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    private int speed = 3;

   
    
    public void act() 
    
    {
        G4_Obstacle obstacle = (G4_Obstacle) getOneIntersectingObject(G4_Obstacle.class);
        if (obstacle != null)
        {
            this.turn(30);
            this.move(10);
        }
        
        
        G4_Entry entry = (G4_Entry) getOneIntersectingObject(G4_Entry.class);
        if (entry != null)
        {
            this.turn(30);
            this.move(5);
        }
        
        if (Greenfoot.isKeyDown("up"))
        {
            turn(-20);
        }
        if (Greenfoot.isKeyDown("down"))
        {
          turn(20);
        }
        if (Greenfoot.isKeyDown("left"))
        {
           move(-speed);
        }
        if (Greenfoot.isKeyDown("right"))
        {
            move(speed);
            move(speed+1);
        }
    }    
    
}
