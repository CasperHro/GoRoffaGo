import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;
import java.util.*;

/**
 * Write a description of class G2Dock here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G2Dock extends World
{   
    int shipCentre = 260;
    int deckLevel = 473;
    int width = 4; //number of cargo
    int cargoWidth = 50; //cargo=50 x 50px
    int cargoHeight = 50;
    int space = 4; // horizontal spacing between cargo
    int maxHeight = 4;
    int maxWidth = width;
    
    boolean aiOn = false;
    
    // Player 1
    private G2Hook p1_hook;
    private G2Deck p1_deck;
    private G2Cargo[] p1_grid;
    private G2EmptyCargo[] p1_emptyGrid;
    private G2Transport p1_transport;
    private int p1_TransportIndex = -1;
    private int p1_looted = 0;

    // Player 2
    private G2Hook p2_hook;
    private G2Deck p2_deck;
    private G2Cargo[] p2_grid;
    private G2EmptyCargo[] p2_emptyGrid;
    private G2Transport p2_transport;
    private int p2_TransportIndex = -1;
    private int p2_looted = 0;

    G2Clock clock;
    G2Counter scoreCounter;
    List<String> transportOrder = new ArrayList<String>();
    
    private boolean firstStep = true; // Does the first step routine
    private boolean running = false;
    private boolean gameOver = false;
    private int delayTiltTimer; //Timestamp van de laatste Tilt Actie
    private int counter = 0;
    private int cycle;
    
    int level = 0; // Game level
    
    /**
     * Constructor for objects of class G2Dock.
     * 
     */
    public G2Dock()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        // False makes the world boundless.
        super(800, 600, 1, false);
        
        // TODO: Set the paintorder for the overlaying classes
        setPaintOrder(G2GameInfo.class,
                  //G2_Waitbox.class,
                  G2GameOver.class,
                  G2Water.class,// Foreground water
                  G2Deck.class,
                  G2Hook.class,
                  G2Cargo.class,
                  G2Transport.class,
                  G2Cables.class,
                  G2BoatBack.class
                  );

        // Prepare all initial actors
        prepare();
    }
    
    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    public void prepare()
    {

        p1_hook = new G2Hook();
        p1_deck = new G2Deck();

        addObject(p1_deck, shipCentre, deckLevel - 52);
        addObject(p1_hook, 35, 300);

        p2_hook = new G2ComputerHook();
        p2_deck = new G2Deck();
        
        addObject(p2_deck, getWidth() - shipCentre, deckLevel - 52);
        addObject(p2_hook, getWidth() - 35, 300);
        
        scoreCounter = new G2Counter();
        addObject(scoreCounter, 750, 20);

        G2Water water = new G2Water();
        addObject(water, 400, getHeight() - (water.getImage().getHeight() / 2));
    }
    
    /**
     * Does the run initialization. To prevent 
     */
    private void firstStep()
    {
        // Reset the flag to prevent another call to this function
        firstStep = false;
        
        // Start music
        //backgroundMusic.playLoop();
        
        // And show game info overlay
        G2GameInfo g2_info = new G2GameInfo();
        addObject(g2_info, getWidth() / 2, getHeight() / 2); // Centered on screen
    }

    public void startGame()
    {
        // First clear the current playfield and reset the ships
        removeObjects(getObjects(G2Transport.class));
        removeObjects(getObjects(G2Cargo.class));
        removeObjects(getObjects(G2EmptyCargo.class));
        createGrid(maxHeight, maxWidth);

        // Reset tilt
        p1_deck.resetShip();
        p2_deck.resetShip();

        // Hook initial location
        p1_hook.setLocation(35, 300);
        p2_hook.setLocation(getWidth() - 35, 300);

        
        // Now create the new cargo for this level
        level = level + 1;
        setCargo();
        setEmpty();
        
        // Call create for first transport
        p1_TransportIndex = -1;
        p2_TransportIndex = -1;
        set_p1_Transport();
        set_p2_Transport();
        
        // Clock
        clock = new G2Clock(false, true, 0, null);
        addObject(clock, 750, 40);

        gameOver = false;
        running = true;
        p1_hook.setRunning(running);
        p2_hook.setRunning(running);
    }
    
    public void endGame()
    {
        // Stop game and show results, then prepare for next level
        running = false;
        p1_hook.setRunning(running);
        p2_hook.setRunning(running);
        clock.stopClock();
        
    }

    public void act()
    {
        // Only start music when running the game
        if (firstStep)
        {
            firstStep();
        }
        
        if (running && !gameOver)
        {
            counter++;
            if(counter == 90) { //om de 7(1~sec) ticks code uitvoeren
                delayTiltTimer++; //
                
                if(cycle == 1) { //adjust ship once every 2 cycles
                    int p1_tilt = getTilt(p1_grid);
                    int p1_step = p1_deck.adjustShip(p1_tilt); //pas de hoek van het schip aan
                    int p2_tilt = getTilt(p2_grid);
                    int p2_step = p2_deck.adjustShip(p2_tilt); //pas de hoek van het schip aan
                    
                    for(int i = 0; i < p1_grid.length; i++) {//pas de hoek en plek van de cargo aan
                        if(p1_grid[i] != null && p1_grid[i] instanceof G2Cargo) {
                            p1_grid[i].adjustBooty(p1_tilt, p1_step);
                        }
                        if(p2_grid[i] != null && p2_grid[i] instanceof G2Cargo) {
                            p2_grid[i].adjustBooty(p2_tilt, p2_step);
                        }
                    }
                    
                    cycle=0;
                }else{
                    cycle++;
                }
                
                if (p1_deck.getStepCount() > 10 || p1_deck.getStepCount() < -10 || checkCargoLeft(p2_grid)==0){
                    // player looses
                    gameOver = true;
                    endGame();
                    G2GameOver gameover = new G2GameOver();
                    addObject(gameover, getWidth()/2, getHeight()/2);
                }else if (p2_deck.getStepCount() > 10 || p2_deck.getStepCount() < -10 || checkCargoLeft(p1_grid)==0){
                    // computer looses
                    gameOver = true;
                    endGame();
                    G2GameOver gameover = new G2GameOver();
                    addObject(gameover, getWidth()/2, getHeight()/2);
                }
                counter=0;
            }  
        }else if (running && gameOver){
            endGame();
                    G2GameOver gameover = new G2GameOver();
                    addObject(gameover, getWidth()/2, getHeight()/2);
        }
    }
    
        
    public void liftCargo(G2Cargo cargo){
        int i =0;
        
        if(!aiOn){
            for(G2Cargo c : p1_grid){
                if(c==cargo){
                    p1_looted += c.getWeight();
                    p1_grid[i] = null;
                    break;
                }
                i++;
            }
        }else{
            for(G2Cargo c : p2_grid){
                if(c==cargo){
                    p2_looted += c.getWeight();
                    p2_grid[i] = null;
                    break;
                }
                i++;
            }
        }
        
        setEmpty();
    }
    
    public void set_p1_Transport()
    {
        p1_TransportIndex++;
        p1_transport = new G2Transport(transportOrder.get(p1_TransportIndex));
        addObject(p1_transport, 50, 441);
    }

    public void set_p2_Transport()
    {
        p2_TransportIndex++;
        p2_transport = new G2Transport(transportOrder.get(p2_TransportIndex));
        addObject(p2_transport, 750, 441);
    }
    
    public void removeTransport(G2Transport truck)
    {
        if (truck == p1_transport)
        {
            set_p1_Transport();
        }
        if (truck == p2_transport)
        {
            set_p2_Transport();
        }
        removeObject(truck);
        
    }
    
    public void setCargo() {
        int StackHeight = deckLevel-(cargoHeight/2);
        int height = maxHeight-1;
        int i = 0;
        int hi = 0;                                     //how high the cargo is stacked
        int x = shipCentre-(width/2*(cargoWidth+space))+((cargoWidth+space)/2);
        
        List<String> containers = new ArrayList<String>();
        
        while(i<width*height){
            if(getWeightPerX(p1_grid, i%width)<=25){
                int randomCargoType = ThreadLocalRandom.current().nextInt(1, 3 + 1);
                G2Cargo p1_cargo = null;
                G2Cargo p2_cargo = null;
                if(randomCargoType==1){
                    p1_cargo = new G2Red();
                    p2_cargo = new G2Red();
                }
                if(randomCargoType==2){
                    p1_cargo = new G2Green();
                    p2_cargo = new G2Green();
                }
                if(randomCargoType==3){
                    p1_cargo = new G2Blue();
                    p2_cargo = new G2Blue();
                }
                
                if (p1_cargo != null && p2_cargo != null)
                {
                    containers.add(p1_cargo.getColor());
                    p1_grid[i] = p1_cargo;
                    p2_grid[i - (i % 4) + (width-1) - (i % 4)] = p2_cargo;
                    addObject(p1_cargo,x,StackHeight);
                    addObject(p2_cargo, getWidth() - x, StackHeight);
                }
            }
            x += cargoWidth+4;// spacing 6 pixles
            i++;
            if(i%width==0){
                x = shipCentre-(width/2*(cargoWidth+space))+((cargoWidth+space)/2);
                StackHeight-=cargoHeight;
            }
        }
        
        shuffleTransportOrder(containers);
    }
    
    public void setEmpty()
    {
        setEmpty(p1_emptyGrid, p1_grid);
        setEmpty(p2_emptyGrid, p2_grid);
    }
    
    public void setEmpty(G2EmptyCargo[] emptyGrid, G2Cargo[] grid)
    {
        for(int i = 0; i<emptyGrid.length; i++){
            if(emptyGrid[i]==null){ //  create EmptyCargo placeholders
                emptyGrid[i] = new G2EmptyCargo();
                emptyGrid[i].setId(getCargoPerX(grid, i)*width+i);
                addObject(emptyGrid[i],grid[i].getX(),grid[i].getY()+(getCargoPerX(grid, i)*-45));
            }else{
               if(emptyGrid[i]!=null){  //update EmptyCargo placeholders
                    int heightperX = getCargoPerX(grid, i);
                    if(heightperX==0){//set to first row
                        int x = shipCentre+15-(cargoWidth*(width/2)-40)+i*cargoWidth;
                        int StackHeight = deckLevel-(cargoHeight/2);
                        emptyGrid[i].setLocation(x,StackHeight);
                        emptyGrid[i].setId(heightperX*width+i);
                    }  
                    else if(heightperX<maxHeight){//max Height not reached?
                        emptyGrid[i].setLocation(grid[i].getX(),grid[i].getY()+(heightperX*-45));
                        emptyGrid[i].setId(getCargoPerX(grid, i)*width+i);
                    } else {//at the max height
                        emptyGrid[i].setLocation(0,0);
                        emptyGrid[i].setId(0);
                    }     
                }
            }
        }
    }
    
    private void shuffleTransportOrder(List<String> containers)
    {
        transportOrder.clear();
        while(containers.size() > 0)
        {
            int rnd = Greenfoot.getRandomNumber(containers.size());
            transportOrder.add(containers.get(rnd));
            containers.remove(rnd);
        }
    }
 
    public int getTilt(G2Cargo[] grid){
        int half = width/2-width%2;
        int i=0;
        int halfLeft=0;
        int halfRight=0;
        int windSpeed;
        
       while(i<half){
            halfLeft+=getWeightPerX(grid, i)*(half-i);
            halfRight+=getWeightPerX(grid, width-i-1)*(half-i);
            i++;
       }
       windSpeed = halfLeft-halfRight;
        
       return windSpeed;
    }
    
    public int checkCargoLeft(G2Cargo[] grid){
        int count = 0;
        for(int i=0;i<grid.length-1; i++){
            if(grid[i]!=null){
                count+=1;
            }
        }

        return count;
    }
    
    public int getWeightPerX(G2Cargo[] grid, int x){
        int weight = 0;
        int i = 0;
        if (grid != null){
            while(i < maxHeight){
                int spot = x+(i*width);

                if(grid[spot]!=null){
                    weight+=grid[spot].getWeight();
                }
                i++;
            }
        }
        return weight;
    }
    
    public int getCargoPerX(G2Cargo[] grid, int x){
        int stack = 0;
        for(int i = 0;i < maxHeight; i++){
            int spot = x+(i*width);

            if(grid[spot]!=null){
                stack+=1;
            }
        }
        return stack;
    }
    
    public int getSizeGrid(int x, int y){
        int spots = x*y;
        maxHeight = y;
        maxWidth = x;
        return spots;
    }
    
    //@@@ Grid functies
    public void createGrid(int x,int y){
        int gridSize = getSizeGrid(x,y);
        p1_grid = new G2Cargo[gridSize];
        p2_grid = new G2Cargo[gridSize];
        p1_emptyGrid = new G2EmptyCargo[x];
        p2_emptyGrid = new G2EmptyCargo[x];
    }
  
    public boolean putCargoAtSpot(G2Cargo cargo, G2EmptyCargo emptySpot)
    {
        if (emptySpot != null)
        {
            if (emptySpot.getX() < getWidth() / 2)
            {
                p1_grid[emptySpot.getId()] = cargo;
                setEmpty(p1_emptyGrid, p1_grid);
            }
            else
            {
                p2_grid[emptySpot.getId()] = cargo;
                setEmpty(p2_emptyGrid, p2_grid);
            }
            return true;
        }
        return false;
    }
}
