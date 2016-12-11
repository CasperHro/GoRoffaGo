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
        image.scale(80, 40);
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
    
    public void adjustHeight(boolean tiltside) {
        myWorld world = getWorldOfType(myWorld.class);
        int dist = world.shipCentre-this.getX();
        int direction;
        int direction2= -1;
        if(dist>0){
            direction = 1;
        }else {
            direction = -1;
        }
        
        if(tiltside){
            direction2 = 1;
        }
        
        if(dist > -60 && dist < 50){
            this.setLocation(this.getX(),this.getY()+(2*direction*direction2));
        }else{
            this.setLocation(this.getX(),this.getY()+(4*direction*direction2));
        }
   }
    
    public void adjustBooty() {
        myWorld world = getWorldOfType(myWorld.class);
        int swaying = world.tilt;
        
        
        if(swaying>20){
            turn(-1);
            this.adjustHeight(true);
        } else 
        if (swaying<-20) {
            turn(1);
            this.adjustHeight(false);
        }else{
            if(world.stepsCount>0){
                turn(1);
                this.adjustHeight(false);
            } else
            if(world.stepsCount<0){
                turn(-1);
                this.adjustHeight(true);
            }
        }
        
    }
}
