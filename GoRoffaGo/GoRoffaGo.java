import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.awt.Color;

/**
 * The game GoRoffaGo!!
 * This is the main menu from where 4 different serious games can be started.
 * 
 * @author C. Karreman 
 * @version 1.0
 */
public class GoRoffaGo extends World
{
    private static final int SPLASHDELAY = 3; // Splash screen delay in seconds
    private static final int DELAY = 12; // Background switch delay in seconds
    private static final int OVERFLOW = 2600; // Image overflow timer in milliseconds
    private static final int MAXIMAGES = 9; // The number of background images

    private static final String[] payoffs = {
        "The port of Rotterdam is the largest harbour of Europe \n and is still growing", 
        "Vessels are piloted into the harbour, play the game to \n fullfill this task. Navigate different types of \n ships to the right location and dock them.", 
        "In the city centre there are still some old vessels. \n Compare them to the new ones and see the difference \n in scale between then and now...", 
        "Unloading a containership is a game of tactics. Pick the \n right containers from the ship but keep it in balance!", 
        "The port is the main reason for the development of Rotterdam", 
        "Automated trucks relocate containers on the dock. \n Program them yourself with movements and prevent \n crashing then into obstacles and eachother.", 
        "The port never sleeps. Seven days a week, \n 24 hours a day ships come and go.", 
        "To keep the port save and secure you have to keep the criminals \n away from the cargo. Catch them as fast as you can!", 
        "This is how the port grows. Even new land is build to make it \n grow into the sea."  
    };

    private MnuGame btnGame1;
    private MnuGame btnGame2;
    private MnuGame btnGame3;
    private MnuGame btnGame4;

    private Date startTime; // Used for the intro timer and the switch delays
    private boolean firstStep = true; // Does the first step routine
    private boolean splash = true; // Splash is showing
    private String lastKey = ""; // For preventing multiple actions on the same keydo
    private GreenfootSound backgroundMusic = new GreenfootSound("dream_shiner.mp3");
    private GreenfootImage lastImage = null; // Current background for image overflow
    private GreenfootImage nextImage = null; // Next image for image overflow
    private int bgIndex = 0; // Background index
    private boolean payoffShown = false;
    private int selectedGame = 0; // When keys are used this is the selected game
    
    /**
     * Constructor for objects of class MyWorld.
     */
    public GoRoffaGo()
    {    
        // Create a new world with 800x600 cells with a cell size of 1x1 pixels.
        super(800, 600, 1);
        // This is a setting for the speed of the game. Tested to be playable.
        Greenfoot.setSpeed(40);
        
        backgroundMusic.setVolume(70);
        prepare();
    }

    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
        btnGame1 = new MnuGame(1);
        addObject(btnGame1,140,215);

        btnGame2 = new MnuGame(2);
        addObject(btnGame2,660,215);

        btnGame3 = new MnuGame(3);
        addObject(btnGame3,140,490);
        
        btnGame4 = new MnuGame(4);
        addObject(btnGame4,660,490);
    }
    
    /**
     * Does the run initialization. To prevent doing things in constructor when 
     * still in greenfoot edit mode.
     */
    private void firstStep()
    {
        // Reset the flag to prevent another call to this function
        firstStep = false;
        
        // Reset counters
        startTime = new Date();
        
        // Start music
        startMusic();
    }

    /**
     * Act is the game step. In this world boats are created with a time interval. A boat
     * mut be selected to take over the controls. Use "S" to select a boat or click on one.
     */
    public void act()
    {
        // Only start music when running the game
        if (firstStep)
        {
            firstStep();
        }
        
        checkKeys();
        
        if (splash)
        {
            if ((new Date().getTime() - startTime.getTime()) / 1000 > SPLASHDELAY)
            {
                splash = false;
                nextImage();
            }
        }
        else
        {
            if ((new Date().getTime() - startTime.getTime()) / 1000 > DELAY)
            {
                nextImage();
            }
        }
        drawImage();
    }
    
    /**
     * Checks the keydown states for keys and prevents multiple executing 
     * of the same key when still down in the next loop.
     */
    private void checkKeys()
    {
        // Remember the last keystroke to prevent multiple actions
        if (lastKey == "" || !Greenfoot.isKeyDown(lastKey))
        {
            // The s-key selects the next boat for controls.
            if (Greenfoot.isKeyDown("m"))
            {
                lastKey = "m";
                stopMusic();
            }
            else if (Greenfoot.isKeyDown("p"))
            {
                lastKey = "p";
                startMusic();
            }
            else if (Greenfoot.isKeyDown("Enter"))
            {
                lastKey = "Enter";
                startSelected();
            }
            else if (Greenfoot.isKeyDown("Left"))
            {
                lastKey = "Left";
                selectLeft();
            }
            else if (Greenfoot.isKeyDown("Right"))
            {
                lastKey = "Right";
                selectRight();
            }
            else if (Greenfoot.isKeyDown("Up"))
            {
                lastKey = "Up";
                selectUp();
            }
            else if (Greenfoot.isKeyDown("Down"))
            {
                lastKey = "Down";
                selectDown();
            }
            else
            {
                lastKey = "";
            }
        }
    }

    /**
     * Stops the background music
     */
    private void stopMusic()
    {
        if (backgroundMusic.isPlaying())
        {
            backgroundMusic.stop();
        }
    }

    /**
     * Starts the background music
     */
    private void startMusic()
    {
        if (!backgroundMusic.isPlaying())
        {
            backgroundMusic.playLoop();
        }
    }

    /**
     * Start the selected game
     */
    private void startSelected()
    {
        startGame(selectedGame);
    }
    
    private void selectLeft()
    {
        unselectButton(selectedGame);
        if (selectedGame > 1)
        {
            selectButton(selectedGame - 1);
        }
        else
        {
            selectButton(4);
        }
    }
    
    private void selectRight()
    {
        unselectButton(selectedGame);
        if (selectedGame < 4)
        {
            selectButton(selectedGame + 1);
        }
        else
        {
            selectButton(1);
        }
    }
    
    private void selectUp()
    {
        if (selectedGame == 0)
        {
            selectButton(1);
        }
        else if (selectedGame == 3 || selectedGame == 4)
        {
            unselectButton(selectedGame);
            selectButton(selectedGame - 2);
        }
    }
    
    private void selectDown()
    {
        if (selectedGame == 0)
        {
            selectButton(1);
        }
        else if (selectedGame == 1 || selectedGame == 2)
        {
            unselectButton(selectedGame);
            selectButton(selectedGame + 2);
        }
    }
    
    private void selectButton(int index)
    {
        selectedGame = index;
        switch(index)
        {
            case 1:
                btnGame1.select();
                break;
            case 2:
                btnGame2.select();
                break;
            case 3:
                btnGame3.select();
                break;
            case 4:
                btnGame4.select();
                break;
            default:
                // Do nothing
                break;
        }
    }
    
    private void unselectButton(int index)
    {
        switch(index)
        {
            case 1:
                btnGame1.deselect();
                break;
            case 2:
                btnGame2.deselect();
                break;
            case 3:
                btnGame3.deselect();
                break;
            case 4:
                btnGame4.deselect();
                break;
            default:
                // Do nothing
                break;
        }
    }
    
    /**
     * Shows the next background image
     */
    public void nextImage()
    {
        bgIndex++;
        if (bgIndex > MAXIMAGES)
        {
            bgIndex = 1;
        }
        payoffShown = false;
        lastImage = getBackground();
        nextImage = new GreenfootImage(String.format("mnuBg%03d.jpg", bgIndex));
        startTime = new Date();
    }
    
    /**
     * Draws the next image over the old one
     */
    private void drawImage()
    {
        long t = new Date().getTime() - startTime.getTime();
        
        // First check the progress to show a new text on the image and 
        // activate a game button when associated with the background
        if (!payoffShown && bgIndex > 0 && t > OVERFLOW * 2/3)
        {
            payoffShown = true;
            btnGame1.show();
            btnGame2.show();
            btnGame3.show();
            btnGame4.show();

            GreenfootImage txt = new GreenfootImage(payoffs[bgIndex-1], TextSize.size(22), Color.white, new Color(0, 0, 0, 0));
            nextImage.drawImage(txt, 780 - txt.getWidth(), 10);
            MnuGame btn = getActivatedItem();
            if (btn != null)
            {
                btn.activate();
            }
        }
        if (nextImage != null && t < OVERFLOW)
        {
            GreenfootImage img = new GreenfootImage(lastImage);
            nextImage.setTransparency(Math.min(255, 255 * (int)t / OVERFLOW));
            img.drawImage(nextImage, 0, 0);
            setBackground(img);
        }
    }
    
    /**
     * Returns the item that is associated with the background image to show 
     * brighter.
     */
    public MnuGame getActivatedItem()
    {
        switch(bgIndex)
        {
            case 2:
                return btnGame1;
            case 4:
                return btnGame2;
            case 6:
                return btnGame3;
            case 8:
                return btnGame4;
            default:
                return null;
        }
    }
    
    /**
     * Start the game of choice
     */
    public void startGame(int game)
    {
        if (game > 0)
        {
            // First remove everything and stop the music
            backgroundMusic.stop();
            removeObjects(getObjects(Actor.class));
        }
        
        if (game == 1)
        {
            Greenfoot.setWorld(new G1Port());
        }
        else if (game == 2)
        {
            Greenfoot.setWorld(new G2Dock());
        }
        else if (game == 3)
        {
            Greenfoot.setWorld(new G3Dock());
        }
    }
}
