import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * Write a description of class G4Staff here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G4Staff extends Actor
{
    //Vars for the staff
   
    private int speed = 7;
    private int missed = 0;
    private String role = "criminal";
    private boolean criminal = false;
    private int hided = 0;
    private static int TIMEOUT = 40;
    private int moveCounter = 0;
    
    
    public String getRole()
    
    {
        return role;
    }
   
    /**
     * 
     * 
     */
    public void setRole(String value) 
    {
        role = value;
        if (role.equals("G4pop1"))
        {
            setImage(new GreenfootImage("G4pop1.png"));
        }
        else if (role.equals("pop2"))
        {
            setImage(new GreenfootImage("G4pop2.png"));
        }
        else if (role.equals("pop3"))
        {
            setImage(new GreenfootImage("G4pop3.png"));
        }
        else 
        {
            setImage(new GreenfootImage("G4crimineel.png"));
            criminal = true;
        }
    } 
    
    public void act()
    {
        move(speed);
        
        G4Officer officer = (G4Officer) getOneIntersectingObject(G4Officer.class);
        if (officer != null)
        {
            if (criminal)
            {
                Greenfoot.playSound("g4whistle.mp3");
                officer.move(0);
                this.move(0);
                officer.scoredOfficer();
                ((Game4Hunter)getWorld()).addScore(1);
                getWorld().removeObject(this);
                return;
            }
            else
            {
                officer.turn(Greenfoot.getRandomNumber(30));
                officer.move(-10);
                return;
            }
        }
        
        G4Obstacle obstacle = (G4Obstacle) getOneIntersectingObject(G4Obstacle.class);
        if (obstacle != null)
        {
            if (criminal == false)
            {
                move(-10);
                turn(90);
            }
            
            else if (criminal == true)
            {
                // make sure that criminal can hide in the container and apper agin next to one of other or the same
               
                Greenfoot.playSound("g4two_explosions.mp3");
                obstacle.explosion();
                ((Game4Hunter)getWorld()).addMissed(1);
                getWorld().removeObject(this);
                return;
            }
        }
        
        
        G4Container container = (G4Container) getOneIntersectingObject(G4Container.class);
        if (container != null)
        {
            if (criminal == false)
            {
                move(-10);
                turn(Greenfoot.getRandomNumber(30));
            }
            
            else if (criminal == true)
            {
                // make sure that criminal can hide in the container and appear again next to one of other or the same
                Greenfoot.playSound("g4glovebox_open.mp3");
                ((Game4Hunter)getWorld()).addHidden(1);
                getWorld().removeObject(this);
                return;
            }
        }
        
        G4Staff staff = (G4Staff) getOneIntersectingObject(G4Staff.class);
        if (staff != null)
        {
            move(-10);
        }
        
        if (Greenfoot.getRandomNumber(100) < 10)
        {
            turn(Greenfoot.getRandomNumber(90) - 45);
            moveCounter ++;
        }
        if (getX() <= 5 || getX() >= getWorld().getWidth() - 5)
        {
            turn(180);
            moveCounter ++;
        }
        if (getY() <= 5 || getY() >= getWorld().getHeight() - 5)
        {
            turn(180);
            moveCounter ++;
        }
        
        if (moveCounter >= TIMEOUT && criminal)
        {
            missed++;
            Greenfoot.playSound("g4click_and_slide.mp3");
            ((Game4Hunter)getWorld()).addMissed(1);
            getWorld().removeObject(this);
        }
      
        else if (moveCounter >= TIMEOUT)
        {
            getWorld().removeObject(this);
        }
        
    }
    
}

