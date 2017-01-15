import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class G4_Officer here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G4Officer extends Actor 
{
    /**
     * Act - do whatever the G4Officer wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    
    private int speed = 4;

    public void act() 
    
    {
        
        G4Obstacle obstacle = (G4Obstacle) getOneIntersectingObject(G4Obstacle.class);
        if (obstacle != null)
        {
            this.move(-10);
            
        }
        
        G4Container container = (G4Container) getOneIntersectingObject(G4Container.class);
        if (container != null)
        {
            this.move(-10);
            
        }
        
        G4Entry entry = (G4Entry) getOneIntersectingObject(G4Entry.class);
        if (entry != null)
        {
            this.move(-10);
        }
        
        
        if (Greenfoot.isKeyDown("left"))
        {
           turn(-20);
        }
        if (Greenfoot.isKeyDown("right"))
        {
            turn(20);
        }
        if (Greenfoot.isKeyDown("down"))
        {
            move(-speed);
        }
        if (Greenfoot.isKeyDown("up"))
        {
            move(speed);
            move(speed+1);
 
        }
    }    
    public void scoredOfficer()
    {
        G4OfficerScored scored = new G4OfficerScored();
        getWorld().addObject(scored, getX(), getY());
        scored.setRotation(getRotation());

    }

}
