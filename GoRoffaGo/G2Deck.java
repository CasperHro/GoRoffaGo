import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Deck here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G2Deck extends Actor
{
    private int stepcount = 0;
    G2BoatBack back = new G2BoatBack();
    
    /**
     * When added to a world also add the back of the ship
     */
    protected void addedToWorld(World world)
    {
        world.addObject(back, getX(), getY());
    }
    
    /**
     * Set the same location for tha back of the ship
     */
    public void setLocation(int x, int y)
    {
        super.setLocation(x, y);
        back.setLocation(getX(), getY());
    }
    
    /**
     * Override for setRotation to also set the rotation for the back of the 
     * ship.
     */
    public void setRotation(int rotation)
    {
        super.setRotation(rotation);
        back.setRotation(getRotation());
    }
    
    /**
     * Act - do whatever the Deck wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        // Ship is just there
    }
    
    public void resetShip()
    {
        setRotation(0);
        stepcount = 0;
    }
    
    public int adjustShip(int swaying) {
        if(swaying>20){
            turn(-1);
            stepcount++;
        } else if (swaying<-20) {
            turn(1);
            stepcount--;
        }else{
            if(stepcount>0){
                stepcount--;
                turn(1);
            } else
            if(stepcount<0){
                stepcount++;
                turn(-1);
            }
        }
        return stepcount;
    }
   
    public int getStepCount()
    {
        return stepcount;
    }
}
