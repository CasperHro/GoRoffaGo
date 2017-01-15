import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class G2cargo here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G2Cargo extends Actor
{
    
    protected int weight;
    public String color;
    
    public G2Cargo()
    {
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
        G2Dock world = getWorldOfType(G2Dock.class);
        int dist = world.shipCentre-this.getX();
        int direction;
        int direction2= -1;
        if(dist>0){//links of rechts
            direction = 1;
        }else {
            direction = -1;
        }
        
        if(tiltside){//binen of buiten
            direction2 = 1;
        }
        
        if(dist > -60 && dist < 50){
            this.setLocation(this.getX()+(-2*direction2),this.getY()+(1*direction*direction2));
        }else{
            this.setLocation(this.getX()+(-2*direction2),this.getY()+(3*direction*direction2));
        }
   }
    
    public void adjustBooty(int swaying, int stepscount) {
        if(swaying>20){
            turn(-1);
            adjustHeight(true);
        } else 
        if (swaying<-20) {
            turn(1);
            adjustHeight(false);
        }else{
            if(stepscount>0){
                turn(1);
                this.adjustHeight(false);
            } else
            if(stepscount<0){
                turn(-1);
                adjustHeight(true);
            }
        }
    }
}
