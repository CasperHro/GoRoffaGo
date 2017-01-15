import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.concurrent.ThreadLocalRandom;
import java.util.*;

/**
 * Write a description of class G2Dock here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class G2Dock extends Game
{   
    private static final String TEXT = "Unload containers : game info\n"+
                  "Your mission is to unload the ships.\n"+
                  "Pick the right container from the ship and place it on the\n"+
                  "transport, but keep the ship in balance!!\n\n"+
                  "Use the arrow keys to navigate the container hook and\n"+
                  "press 's' to select a container or 'd' to release a container.\n\n"+
                  "Be fast or the computer wins...\n\n"+
                  "\n\n"+
                  "Click or press Enter to start...";
    private static final String TEXTSTART = "Get ready...\n\nHere we go!!!";
    private static final String YOUWIN = "Gongratulations, you won!!!\n\n"+
                  "Get ready for the next level. Play faster\n"+
                  "and beat the computer again.\n"+
                  "\n\n"+
                  "Click or press Enter to start...";

    int shipCentre = 260;
    int deckLevel = 473;
    int width = 4; //number of cargo
    int cargoWidth = 50; //cargo=50 x 50px
    int cargoHeight = 50;
    int space = 4; // horizontal spacing between cargo
    int maxHeight = 4;
    int maxWidth = width;
    
    // Player 1
    private G2Hook p1hook;
    private G2Deck p1deck;
    private G2Cargo[] p1grid;
    private G2EmptyCargo[] p1emptyGrid;
    private G2Transport p1transport;
    private G2ScoreCounter p1scoreCounter;
    private int p1TransportIndex = -1;

    // Player 2
    private G2Hook p2hook;
    private G2Deck p2deck;
    private G2Cargo[] p2grid;
    private G2EmptyCargo[] p2emptyGrid;
    private G2Transport p2transport;
    private G2ScoreCounter p2scoreCounter;
    private int p2TransportIndex = -1;

    G2Clock clock;
    List<String> transportOrder = new ArrayList<>();
    
    private boolean firstStep = true; // Does the first step routine
    private boolean running = false;
    private boolean gameover = false;
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
        setPaintOrder(GameInfo.class,
                  //G2_Waitbox.class,
                  GameOver.class,
                  G2Water.class,// Foreground water
                  G2Deck.class,
                  G2Hook.class,
                  G2Cargo.class,
                  G2Transport.class,
                  G2Cables.class,
                  G2BoatBack.class
                  );

        // This is a setting for the speed of the game. Tested to be playable.
        Greenfoot.setSpeed(50);

        backgroundMusic = new GreenfootSound("game_2_music.mp3");
        backgroundMusic.setVolume(70);
                  
        // Prepare all initial actors
        prepare();
    }
    
    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    public void prepare()
    {

        p1hook = new G2Hook();
        p1deck = new G2Deck();

        addObject(p1deck, shipCentre, deckLevel - 52);
        addObject(p1hook, 35, 300);

        p2hook = new G2ComputerHook();
        p2deck = new G2Deck();
        
        addObject(p2deck, getWidth() - shipCentre, deckLevel - 52);
        addObject(p2hook, getWidth() - 35, 300);

        p1scoreCounter = new G2ScoreCounter("Player score: ");
        addObject(p1scoreCounter, 270, 40);
        
        p2scoreCounter = new G2ScoreCounter("Computer score: ");
        addObject(p2scoreCounter, 530, 40);

        clock = new G2Clock();
        addObject(clock, 400, 40);

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
        backgroundMusic.playLoop();
        
        // And show game info overlay
        GameInfo ginfo = new GameInfo(TEXT, TEXTSTART);
        addObject(ginfo, getWidth() / 2, getHeight() / 2); // Centered on screen
    }

    public void startGame()
    {
        // First clear the current playfield and reset the ships
        removeObjects(getObjects(G2Transport.class));
        removeObjects(getObjects(G2Cargo.class));
        removeObjects(getObjects(G2EmptyCargo.class));
        createGrid(maxHeight, maxWidth);

        // Reset tilt
        p1deck.resetShip();
        p2deck.resetShip();

        // Hook initial location
        p1hook.reset(35, 300);
        p2hook.reset(getWidth() - 35, 300);
        
        // Now create the new cargo for this level
        level = level + 1;
        setCargo();
        setEmpty();
        
        // Call create for first transport
        p1TransportIndex = -1;
        p2TransportIndex = -1;
        set_p1Transport();
        set_p2Transport();
        
        gameover = false;
        running = true;
        p1hook.setRunning(running);
        p2hook.setRunning(running);
        clock.startClock(3 * 60 + 15 - (level * 15));
    }
    
    public void endGame()
    {
        // Stop game and show results.
        running = false;
        p1hook.setRunning(running);
        p2hook.setRunning(running);
        clock.stopClock();
    }

    public void act()
    {
        // Only start music when running the game
        if (firstStep)
        {
            firstStep();
        }
        
        if (running && !gameover)
        {
            counter++;
            if(counter == 90) { //om de 7(1~sec) ticks code uitvoeren
                delayTiltTimer++; //
                adjustShips();
                
                if (clock.timeUp() || deck1HeelOver() || player2Wins()) {
                    gameOver();
                }
                else if (player1Wins() || deck2HeelOver()) {
                    // Next level
                    endGame();
                    GameInfo ginfo = new GameInfo(YOUWIN, TEXTSTART);
                    addObject(ginfo, getWidth() / 2, getHeight() / 2); // Centered on screen
                }
                counter=0;
            }
        }
        super.act();
    }

    private boolean deck1HeelOver()
    {
        return Math.abs(p1deck.getStepCount()) > 10;
    }

    private boolean deck2HeelOver()
    {
        return Math.abs(p2deck.getStepCount()) > 10;
    }
    
    private boolean player1Wins() {
        return checkCargoLeft(p1grid)==0 && !p1hook.hasCargo();
    }
    
    private boolean player2Wins() {
        return checkCargoLeft(p2grid)==0 && !p2hook.hasCargo();
    }

    private void gameOver()
    {
        gameover = true;
        endGame();
        backgroundMusic.stop();
        addObject(new GameOver("gameover001.png"), getWidth()/2, getHeight()/2);
    }
        
    public void liftCargo(G2Cargo cargo){
        for(int i = 0; i < p1grid.length; i++) {
            if(p1grid[i]==cargo){
                p1grid[i] = null;
                break;
            }
        }

        for(int i = 0; i < p2grid.length; i++) {
            if(p2grid[i]==cargo){
                p2grid[i] = null;
                break;
            }
        }
        
        setEmpty();
    }
    
    private void adjustShips()
    {
        if(cycle == 1) { //adjust ship once every 2 cycles
            int p1tilt = getTilt(p1grid);
            int p1step = p1deck.adjustShip(p1tilt); //pas de hoek van het schip aan
            int p2tilt = getTilt(p2grid);
            int p2step = p2deck.adjustShip(p2tilt); //pas de hoek van het schip aan
            
            for(int i = 0; i < p1grid.length; i++) {//pas de hoek en plek van de cargo aan
                if(p1grid[i] != null && p1grid[i] instanceof G2Cargo) {
                    p1grid[i].adjustBooty(p1tilt, p1step);
                }
                if(p2grid[i] != null && p2grid[i] instanceof G2Cargo) {
                    p2grid[i].adjustBooty(p2tilt, p2step);
                }
            }
            
            cycle=0;
        }else{
            cycle++;
        }
    }

    public void set_p1Transport()
    {
        p1TransportIndex++;
        if (p1TransportIndex < transportOrder.size()) {
            p1transport = new G2Transport(transportOrder.get(p1TransportIndex));
            addObject(p1transport, 50, 441);
        }
    }

    public void set_p2Transport()
    {
        p2TransportIndex++;
        if (p2TransportIndex < transportOrder.size()) {
            p2transport = new G2Transport(transportOrder.get(p2TransportIndex));
            addObject(p2transport, 750, 441);
        }
    }
    
    public void removeTransport(G2Transport truck)
    {
        if (truck == p1transport)
        {
            set_p1Transport();
        }
        if (truck == p2transport)
        {
            set_p2Transport();
        }
        removeObject(truck);
        
    }
    
    public void setCargo() {
        int stackHeight = deckLevel-(cargoHeight/2);
        int height = maxHeight-1;
        int i = 0;
        int x = shipCentre-(width/2*(cargoWidth+space))+((cargoWidth+space)/2);
        
        List<String> containers = new ArrayList<>();
        
        while(i<width*height){
            if(getWeightPerX(p1grid, i%width)<=25){
                int randomCargoType = ThreadLocalRandom.current().nextInt(1, 3 + 1);
                G2Cargo p1cargo = createCargo(randomCargoType);
                G2Cargo p2cargo = createCargo(randomCargoType);
                
                if (p1cargo != null && p2cargo != null)
                {
                    containers.add(p1cargo.getColor());
                    p1grid[i] = p1cargo;
                    p2grid[i - (i % 4) + (width-1) - (i % 4)] = p2cargo;
                    addObject(p1cargo,x,stackHeight);
                    addObject(p2cargo, getWidth() - x, stackHeight);
                }
            }
            x += cargoWidth+4;// spacing 6 pixles
            i++;
            if(i%width==0){
                x = shipCentre-(width/2*(cargoWidth+space))+((cargoWidth+space)/2);
                stackHeight-=cargoHeight;
            }
        }
        
        shuffleTransportOrder(containers);
    }
    
    private G2Cargo createCargo(int type)
    {
        switch(type)
        {
            case 1:
                return new G2Red();
            case 2:
                return new G2Green();
            case 3:
                return new G2Blue();
            default:
                return null;
        }
    }
    
    public void setEmpty()
    {
        setEmpty(p1emptyGrid, p1grid);
        setEmpty(p2emptyGrid, p2grid);
    }
    
    public void setEmpty(G2EmptyCargo[] emptyGrid, G2Cargo[] grid)
    {
        for(int i = 0; i<emptyGrid.length; i++){
            if(emptyGrid[i]==null){ //  create EmptyCargo placeholders
                emptyGrid[i] = new G2EmptyCargo();
                emptyGrid[i].setId(getCargoPerX(grid, i)*width+i);
                addObject(emptyGrid[i],grid[i].getX(),grid[i].getY()+(getCargoPerX(grid, i)*-45));
            }else{  //update EmptyCargo placeholders
                int heightperX = getCargoPerX(grid, i);
                if(heightperX==0){//set to first row
                    int x = shipCentre+15-(cargoWidth*(width/2)-40)+i*cargoWidth;
                    int stackHeight = deckLevel-(cargoHeight/2);
                    emptyGrid[i].setLocation(x,stackHeight);
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
    
    private void shuffleTransportOrder(List<String> containers)
    {
        transportOrder.clear();
        while(!containers.isEmpty())
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
        p1grid = new G2Cargo[gridSize];
        p2grid = new G2Cargo[gridSize];
        p1emptyGrid = new G2EmptyCargo[x];
        p2emptyGrid = new G2EmptyCargo[x];
    }
  
    public boolean putCargoAtSpot(G2Cargo cargo, G2EmptyCargo emptySpot)
    {
        if (emptySpot != null)
        {
            if (emptySpot.getX() < getWidth() / 2)
            {
                p1grid[emptySpot.getId()] = cargo;
                setEmpty(p1emptyGrid, p1grid);
            }
            else
            {
                p2grid[emptySpot.getId()] = cargo;
                setEmpty(p2emptyGrid, p2grid);
            }
            return true;
        }
        return false;
    }
    
    public void addScore(G2Hook player)
    {
        if (player == p1hook)
        {
            p1scoreCounter.addTransportScore(1);
        }
        if (player == p2hook)
        {
            p2scoreCounter.addTransportScore(1);
        }
    }
}
