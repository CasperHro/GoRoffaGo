import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Game here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Game extends World
{
    protected GreenfootSound backgroundMusic = null; // Background music for the game
    protected String lastKey = ""; // For preventing multiple actions on the same keydo

    public Game()
    {
        // Create a new world with 800x600 cells with a cell size of 1x1 pixels.
        super(800, 600, 1);
    }
    
    /**
     * This method starts the game after the infomessage is clicked. Implement this
     * in the game worlds.
     */
    public void startGame()
    {
        // Implement in the games
    }
    
    /**
     * This act already checks the keys for the enabling or disabling the background 
     * music.
     */
    public void act()
    {
        checkKeys();
    }
    
    /**
     * Checks the keydown states for keys and prevents multiple executing 
     * of the same key when stilldown in the next loop.
     */
    protected void checkKeys()
    {
        // Remember the last keystroke to prevent multiple actions
        if (lastKey == "" || !Greenfoot.isKeyDown(lastKey))
        {
            if (Greenfoot.isKeyDown("m"))
            {
                lastKey = "m";
                if (backgroundMusic != null && backgroundMusic.isPlaying())
                {
                    backgroundMusic.stop();
                }
            }
            else if (Greenfoot.isKeyDown("p"))
            {
                lastKey = "p";
                if (backgroundMusic != null && !backgroundMusic.isPlaying())
                {
                    backgroundMusic.playLoop();
                }
            }
            else
            {
                lastKey = "";
            }
        }
    }
}
