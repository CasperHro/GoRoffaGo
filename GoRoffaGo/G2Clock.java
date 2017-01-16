import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.awt.Color;
 
public class G2Clock extends Actor
{
    private boolean running = false;
    private Date startTime; // Initial time
    private int timeout = 0; // timeout in seconds
     
    /**
     * Create a new Clock with your own text or other values.
     * 
     * @param timeout
     *      timeout in seconds.
     */
    public G2Clock() {
        GreenfootImage img = new GreenfootImage(100, 40);
        img.setColor(Color.white);
        img.fill();
        setImage(img);
    }
   
    /**
     * The act method of the class Clock.
     * Let the clock count the time. If the act mehtod is not executed the clock will count on but not change it's image. To pause the clock use the method stopClock().
     * The Clock is irrespective of the acting speed of the scenario so that it doesn't mater how fast the other act methods are executed.
     */
    public void act() {
        drawTime();
    }
 
    /**
     * The drawing method of the clock.
     * This method draws the current value of your clock onto the clock object.
     */
    private void drawTime() {
        int time;
        if (timeout > 0) {
            time = timeout - getTimeSpend();
        } else {
            time = getTimeSpend();
        }

        String timeAsString = String.format("%1$d:%2$02d", (time - (time % 60)) / 60, time % 60);
        GreenfootImage timg = new GreenfootImage(timeAsString, TextSize.size(14), Color.black, new Color(0, 0, 0, 0));
        getImage().fill();
        getImage().drawImage(timg, (getImage().getWidth() - timg.getWidth()) / 2, (getImage().getHeight() - timg.getHeight()) / 2);
    }
    
    private int getTimeSpend()
    {
        if (running) {
            return Math.toIntExact((new Date().getTime() - startTime.getTime()) / 1000);
        } else {
            return 0;
        }
    }
    
    /**
     * Check whether the time is up.
     * 
     * @return
     *      Returns true if the time is up. If the clock is no countdown clock the method will return false.
     */
    public boolean timeUp() {
        return timeout != 0 && running && getTimeSpend() > timeout;
    }
     
    public void startClock() {  
        startClock(0);
    }
    
    /**
     * Start the clock.
     */
    public void startClock(int timeout) {  
        this.timeout = timeout;
        startTime = new Date(); 
        running = true;
    }
    /**
     * Stop the clock.
     */
    public void stopClock() {  
        running = false;
    }
}