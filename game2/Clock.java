import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
 
public class Clock extends Actor
{
    private int startingTime;
     
    private int seconds = 180;
    private int clockType = 0;
     
    private long lastCurrentSecond;
    private long timeSaved = 0;
     
    private boolean timeUp = false;
    private boolean count = false;
    private boolean displayTime;
    private boolean countDown;
     
    private String text;
     
    private Actor alertedActor;
     
    private World alertedWorld;
     
    /**
     * Create a new Clock with your own text or other values.
     * 
     * @param countDown
     *      Set the kind of the clock. If true the clock will cound down from the given starting time. Otherwise it'll count up from 0.
     * 
     * @param displayTime
     *      Set the clock's visibility. If true the clock is visible.
     * 
     * @param startingTime
     *      The starting time for countdown clocks. If you restart the clock it'll start from this value.
     * 
     * @param text
     *      The text above the clock. If text is null the clock will be centered and there is no text.
     */
    public Clock(boolean countDown, boolean displayTime, int startingTime, String text) {
        this.countDown = countDown;
        this.displayTime = displayTime;
        this.startingTime = startingTime;
        this.text = text;
        this.alertedActor = null;
        this.alertedWorld = null;
        seconds = startingTime;
        getImage().clear();
        if (!countDown) {
            seconds = 0;
        }
        if (displayTime) {
            getImage().scale(100, 40);
        }
        this.startClock();
    }
   
    /**
     * The act method of the class Clock.
     * Let the clock count the time. If the act mehtod is not executed the clock will count on but not change it's image. To pause the clock use the method stopClock().
     * The Clock is irrespective of the acting speed of the scenario so that it doesn't mater how fast the other act methods are executed.
     */
    public void act() {
        if (countDown) {
            if (count && !timeUp) {
                if (System.currentTimeMillis() - lastCurrentSecond >= 1000) {
                    lastCurrentSecond += 1000;
                    seconds--;
                    if (displayTime) {
                        drawTime();
                    }
                }
                if (seconds == 0) {
                    //alertActor();
                    //alertWorld();
                    timeUp = true;
                }
            }
        }
        else {
            if (count) {
                if (System.currentTimeMillis() - lastCurrentSecond >= 1000) {
                    lastCurrentSecond += 1000;
                    seconds++;
                    if (displayTime) {
                        drawTime();
                    }
                }
            }
        }
         
        
         
        
         
    }
 
    /**
     * The drawing method of the clock.
     * This method draws the current value of your clock onto the clock object.
     */
    private void drawTime() {
        //int min = (int) (seconds / 60);
        int sec = seconds;
        String remainingTime;
        if (sec < 10) {
            remainingTime = "0" + sec;
        }
        else {
            remainingTime = " " + sec;
        }
        
        // Seconds to minutes and seconds.
        remainingTime = getDurationString(sec);
        
        getImage().setColor(Color.white);
        getImage().fill();
        GreenfootImage text = new GreenfootImage((this.text == null ? "" : this.text), 30, Color.black, new Color(0, 0, 0, 0));
        GreenfootImage time = new GreenfootImage(remainingTime, 10, Color.black, new Color(0, 0, 0, 0));
        if (text.getWidth() > getImage().getWidth()) {
            getImage().clear();
            getImage().scale(text.getWidth() + 10, 70);
            getImage().setColor(Color.gray);
            getImage().fill();
        }
        getImage().drawImage(text, (getImage().getWidth()/2)-(text.getWidth()/2), 5);
        getImage().drawImage(time, (getImage().getWidth()/2)-(time.getWidth()/2), (this.text == null ? (getImage().getHeight()/2)-(time.getHeight()/2) : 30));
    }
    
    private String getDurationString(int seconds) {

        int hours = seconds / 3600;
        int minutes = (seconds % 3600) / 60;
        seconds = seconds % 60;
    
        return twoDigitString(minutes) + " : " + twoDigitString(seconds);
    }
    
    private String twoDigitString(int number) {
        if (number == 0) {
            return "00";
        }
    
        if (number / 10 == 0) {
            return "0" + number;
        }
    
        return String.valueOf(number);
    }
     
    /**
     * Check whether the time is up.
     * 
     * @return
     *      Returns true if the time is up. If the clock is no countdown clock the method will return false.
     */
    public boolean timeUp() {
        return timeUp;
    }
     
    /**
     * Start the clock.
     */
    public void startClock() {  
        lastCurrentSecond = System.currentTimeMillis() - timeSaved;  
        count = true;  
    }
    /**
     * Pause the clock.
     */
    public void stopClock() {  
        timeSaved = System.currentTimeMillis() - lastCurrentSecond;  
        count = false; 
    }
    /**
     * Reset the clock.
     */
    public void resetClock() {
        seconds = startingTime;
        timeUp = false;
    }
     
     
    long initialTime;  
    public void TimeChecker() {  
        initialTime = System.currentTimeMillis();  
    }  
       
    }