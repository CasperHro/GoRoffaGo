import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Boat1 here.
 * 
 * @author (YuliaKr) 
 * @version (version 1.0, 23.11.16)
 */
public class Boat1 extends Actor
{
    /**
     * Act - do whatever the Boat1 wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    int SPEED = 3;
    int TURN_ANGLE = 3;
    int CRASH = 45;
    int DOCKHEIGHT = 80;
    
    public Boat1()
    {
        //set size of the boat
        GreenfootImage image = getImage();
        image.scale(image.getWidth(), image.getHeight());
        setImage(image);
    }
    public void act() 
    {
       //Sailing Boat
       move(SPEED);
       if (Greenfoot.isKeyDown("up"))
       {
        turn(-TURN_ANGLE);
        }
       
       if (Greenfoot.isKeyDown("down"))
       {
        turn(TURN_ANGLE);
       }
       
       if (Greenfoot.isKeyDown("right"))
       {
        move(SPEED*2);
       }
       
       if (Greenfoot.isKeyDown("left"))
       {
        move(-SPEED);
       }
       
      setLocation(getX(), getY());
    
        //If the boat reach the borders - Game Over
       if (getY() > getWorld().getHeight() -CRASH || getY() < CRASH || getX() > getWorld().getWidth() -CRASH)
       {
         GameOver gameOver = new GameOver();
         getWorld().addObject(gameOver, getWorld().getWidth()/2, getWorld().getHeight()/2);
         Greenfoot.stop();
  
       }
       
     
}
}
