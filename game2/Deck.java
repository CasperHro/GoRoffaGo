import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Deck here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Deck extends Actor
{
    int centerX;
    int tilt;
    
    BoatBack back = new BoatBack();
    
    public Deck()
    {
    }
    
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
        //turn(1);
        //int swaying = ((myWorld)getworld()).tilt;
        //turn(1);
    }    
    
    public int adjustShip() {
       // int swaying = ((myWorld)getworld()).getTilt();
        //int swaying = getWorld(myWorld.class).getTilt();
        myWorld world = getWorldOfType(myWorld.class);
        int swaying = world.tilt;
        //System.out.println(" sway "+swaying);
        if(swaying>20){
            turn(-1);
            world.stepsCount++;
        } else 
        if (swaying<-20) {
            turn(1);
            world.stepsCount--;
        }else{
            if(world.stepsCount>0){
                world.stepsCount--;
                turn(1);
            } else
            if(world.stepsCount<0){
                world.stepsCount++;
                turn(-1);
            }
        }
        
        return 0;
    }
}
