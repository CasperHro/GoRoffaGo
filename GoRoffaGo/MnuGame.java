import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MnuGame here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MnuGame extends Actor
{
    private static final int NORMALTRANSPARENCY = 105;
    private static final int STEP = 8;
    
    private int game;
    private boolean showing = false;
    private boolean active = false;
    private boolean selected = false;
    private boolean mouseOver = false;
    private int transparency = 0;
    
    public MnuGame(int gameNumber)
    {
        game = gameNumber;
        setImage(new GreenfootImage(String.format("mnuGame%d.png", game)));
        getImage().setTransparency(transparency);
    }
    
    /**
     * Starts showing the 
     */
    public void show()
    {
        showing = true;
        active = mouseOver;
    }
    
    /**
     * Starts showing the 
     */
    public void activate()
    {
        active = true;
    }
    
    public void select()
    {
        selected = true;
        getImage().drawImage(new GreenfootImage("mnuSelected.png"), 0, 0);
    }
    
    public void deselect()
    {
        selected = false;
        setImage(new GreenfootImage(String.format("mnuGame%d.png", game)));
        getImage().setTransparency(transparency);
    }
    
    /**
     * Act - do whatever the MnuGame wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public void act() 
    {
        checkMouse();
        
        if (showing && transparency < NORMALTRANSPARENCY)
        {
            // This happens after the timeout, the item becomes visible
            transparency = Math.min(transparency + STEP, NORMALTRANSPARENCY);
            getImage().setTransparency(transparency);
        }
        if (checkActive())
        {
            // When active or selected the item is shown opaque
            transparency = Math.min(transparency + STEP, 255);
            getImage().setTransparency(transparency);
        }
        if (checkInActive())
        {
            // When not active return to normal transparancy
            transparency = Math.max(transparency - STEP, NORMALTRANSPARENCY);
            getImage().setTransparency(transparency);
        }
    }
    
    private boolean checkActive()
    {
        return showing && (active || selected) && transparency < 255;
    }
    
    private boolean checkInActive()
    {
        return showing && !active && !selected && transparency > NORMALTRANSPARENCY;
    }
    
    /**
     * Check if the mouse pointer hovers on this actor and when clicked start game
     */
    private void checkMouse()
    {
        if (!mouseOver && Greenfoot.mouseMoved(this))
        {
            mouseOver = true;
            active = true;
        }
        if (mouseOver && Greenfoot.mouseMoved(null) && ! Greenfoot.mouseMoved(this))
        {
            mouseOver = false;
        }        
        
        if (!mouseOver && ((GoRoffaGo)getWorld()).getActivatedItem() != this)
        {
            active = false;
        }
            

        if (Greenfoot.mouseClicked(this))  
        {  
            ((GoRoffaGo)getWorld()).startGame(game);
        }          
    }

}
