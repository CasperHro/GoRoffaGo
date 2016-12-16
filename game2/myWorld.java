import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;
import java.util.*;

/**
 * Write a description of class myWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class myWorld extends World
{   
    int shipCentre = 260;
    int deckLevel = 473;
    int width = 4; //number of cargo
    int cargoWidth = 50; //cargo=50 x 50px
    int cargoHeight = 50;
    int space = 4; // horizontal spacing between cargo
    
    int maxHeight = 4;
    int maxWidth = width;
    
    
    Hook p1_hook;
    Deck p1_deck;
    Cargo[] p1_grid;
    EmptyCargo[] p1_emptyGrid;
    Transport p1_transport;
    int p1_TransportIndex = -1;

    Hook p2_hook;
    Deck p2_deck;
    Cargo[] p2_grid;
    EmptyCargo[] p2_emptyGrid;
    Transport p2_transport;
    int p2_TransportIndex = -1;

    Clock clock;
    Counter scoreCounter;
    List<String> transportOrder = new ArrayList<String>();

    public int tilt;
    public int looted = 0;
    
    boolean gameOver = false;
    int delayTiltTimer; //Timestamp van de laatste Tilt Actie
    int stepsCount=0;
    int counter =0;
    int cycle;
    //WeightCounter WeightCounter = new WeightCounter();
    
    private boolean firstStep = true; // Does the first step routine
    
    
     /**
      * Game vars
      */
    private int level = 1; // Game level
    private int score = 0; // Game score
    private boolean running = false; // Flag to indicate a running game (may be paused by Greenfoot class)

    
    
    /**
     * Constructor for objects of class myWorld.
     * 
     */
    public myWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        // False makes the world boundless.
        super(800, 600, 1, false);
        
        // TODO: Set the paintorder for the overlaying classes
        setPaintOrder(//G2_GameInfo.class,
                  //G2_Waitbox.class,
                  //GameOver.class,
                  Water.class,// Foreground water
                  Deck.class,
                  Hook.class,
                  Cargo.class,
                  Transport.class,
                  Cables.class,
                  BoatBack.class
                  );

        // Prepare all initial actors
        prepare();
    }
    
    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
        p1_hook = new Hook();
        p1_deck = new Deck();

        p2_hook = new ComputerHook();
        p2_deck = new Deck();

        clock = new Clock(false, true, 0, null);
        scoreCounter = new Counter();

        addObject(p1_deck, shipCentre, deckLevel - 52);
        addObject(p1_hook, 35, 300);

        addObject(p2_deck, getWidth() - shipCentre, deckLevel - 52);
        addObject(p2_hook, getWidth() - 35, 300);

        addObject(clock, 750, 40);
        addObject(scoreCounter, 750, 20);

        Water water = new Water();
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
        //G2_GameInfo g2_info = new G2_GameInfo();
        //addObject(g2_info, getWidth() / 2, getHeight() / 2); // Centered on screen
        startGame();
    }

    public void startGame()
    {
        // First clear the current playfield and reset the ships
        removeObjects(getObjects(Transport.class));
        removeObjects(getObjects(Cargo.class));
        createGrid(maxHeight, maxWidth);

        // Now create the new cargo for this level
        setCargo();
        setEmpty();
        
        // TODO: Call create for first transport
        p1_TransportIndex = -1;
        p2_TransportIndex = -1;
        set_p1_Transport();
        set_p2_Transport();
        
        printGrid();
        System.out.println("start");
        System.out.println("Weight row 0 = "+getWeightPerX(0));
        System.out.println("Weight row 1 = "+getWeightPerX(1));
        System.out.println("Weight row 2 = "+getWeightPerX(2));
        System.out.println("Weight row 3 = "+getWeightPerX(3));
    }

    public void firstRun()
    {
        startGame();
    }
    
public void act(){
        // Only start music when running the game
        if (firstStep)
        {
            firstStep();
        }
        
        
        counter++;
        if(counter == 90 && !gameOver) { //om de 7(1~sec) ticks code uitvoeren
            delayTiltTimer++; //
            
            tilt = getTilt();
            if(cycle == 1) { //adjust ship once every 2 cycles
                p1_deck.adjustShip(); //pas de hoek van het schip aan
                
                for(int i = 0; i < p1_grid.length; i++) {//pas de hoek en plek van de cargo aan
                    if(p1_grid[i] != null) {
                        p1_grid[i].adjustBooty();
                    }
                }
                
                cycle=0;
            }else{
                cycle++;
            }
            //System.out.println("rotation "+deck.getRotation());
            showText("CargoWeight :" + tilt, shipCentre, deckLevel - 52);
            
            showText("looted         :" + looted,720,530);
            //showText("Transport score:"+looted,720,550);
            
            
            counter=0;
        }else if (gameOver){
            showText("Game over "+"looted  :" + looted, shipCentre, deckLevel - 250);
        }else if (stepsCount > 10 || stepsCount < -10){
            gameOver = true;
        }
    }
    
        
        
        
        
        //@@ preparation
        
    public void liftCargo(Cargo cargo){
        int i =0;
        for(Cargo c : p1_grid){
            if(c==cargo){
                break;
            }
            i++;
        }
        
        looted += getWeight(i);
        p1_grid[i] = null;
        
        setEmpty();
        System.out.println(i);
    }
    
    public void set_p1_Transport()
    {
        p1_TransportIndex++;
        p1_transport = new Transport(transportOrder.get(p1_TransportIndex));
        addObject(p1_transport, 50, 441);
    }

    public void set_p2_Transport()
    {
        p2_TransportIndex++;
        p2_transport = new Transport(transportOrder.get(p2_TransportIndex));
        addObject(p2_transport, 750, 441);
    }
    
    public void removeTransport(Transport truck)
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
            if(getWeightPerX(i%width)<=25){
                int randomCargoType = ThreadLocalRandom.current().nextInt(1, 3 + 1);
                Cargo p1_cargo = null;
                Cargo p2_cargo = null;
                if(randomCargoType==1){
                    p1_cargo = new red();
                    p2_cargo = new red();
                }
                if(randomCargoType==2){
                    p1_cargo = new green();
                    p2_cargo = new green();
                }
                if(randomCargoType==3){
                    p1_cargo = new blue();
                    p2_cargo = new blue();
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
    
    public void setEmpty(EmptyCargo[] emptyGrid, Cargo[] grid)
    {
        for(int i = 0; i<emptyGrid.length; i++){
            if(emptyGrid[i]==null){ //  create EmptyCargo placeholders
                emptyGrid[i] = new EmptyCargo();
                emptyGrid[i].setId(getCargoPerX(grid, i)*width+i);
                addObject(emptyGrid[i],grid[i].getX(),grid[i].getY()+(getCargoPerX(grid, i)*-45));
            }else{
                emptyGrid[i].setLocation(grid[i].getX(),grid[i].getY()+(getCargoPerX(grid, i)*-45));
                
                emptyGrid[i].setId(getCargoPerX(grid, i)*width+i);
            
               if(emptyGrid[i]!=null){  //update EmptyCargo placeholders
                    int heightperX = getCargoPerX(grid, i);
                    if(heightperX<maxHeight){
                        emptyGrid[i].setLocation(grid[i].getX(),grid[i].getY()+(heightperX*-45));
                        emptyGrid[i].setId(getCargoPerX(grid, i)*width+i);
                    } else {
                        emptyGrid[i].setLocation(0,0);
                        emptyGrid[i].setId(0);
                    }
                }else{
                    int x = shipCentre+15-(cargoWidth*(width/2)-40)+i*cargoWidth;
                    int StackHeight = deckLevel-(cargoHeight/2);
                    emptyGrid[i].setLocation(x,StackHeight);
                }            
            }
            //System.out.println("row "+i+" - "+getCargoPerX(grid, i));
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
    
    public int getTilt(){
        int half = width/2-width%2;
        int i=0;
        int halfLeft=0;
        int halfRight=0;
        int windSpeed;
        
       while(i<half){
            halfLeft+=getWeightPerX(i)*(half-i);
            halfRight+=getWeightPerX(width-i-1)*(half-i);
            i++;
       }
       windSpeed = halfLeft-halfRight;
        
       //System.out.println("Balans = "+windSpeed);
       //System.out.println("halfLeft = "+halfLeft+": halfRight = "+halfRight);
       return windSpeed;
    }
    
    public int getWeightPerX(int x){
        int weight = 0;
        int i = 0;
        while(i < maxHeight){
            int spot = x+(i*width);
            //System.out.println("spot no "+spot);
            if(p1_grid[spot]!=null){
                weight+=p1_grid[spot].getWeight();
            }
            i++;
        }
        return weight;
    }
    
    public int getCargoPerX(Cargo[] grid, int x){
        int stack = 0;
        for(int i = 0;i < maxHeight; i++){
            int spot = x+(i*width);
            //System.out.println("spot no "+spot);
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
        
    
    
    
    
    public void removeCargo(char Cargo){
        //removeObject(Cargo);
        //setGrid(i,0);
    }
    
    
    
    
    
    //@@@ Grid functies
    public void createGrid(int x,int y){
        int gridSize = getSizeGrid(x,y);
        p1_grid = new Cargo[gridSize];
        p2_grid = new Cargo[gridSize];
        p1_emptyGrid = new EmptyCargo[x];
        p2_emptyGrid = new EmptyCargo[x];
    }
        
    public int getSpot(int x, int y){
        int spot = (x*maxHeight)+y;
        return p1_grid[spot].getWeight();
    }
        
    public int getCoordX(int spot){
        int x = spot%maxWidth;
        return x;
    }
        
    public int getCoordY(int spot){
        int x = spot%maxWidth;
        int y = (spot-x)/maxWidth;
        return y;
    }
        
    public boolean setCoord(int x,int y,Cargo value){
        int spot = (x*maxHeight)+y;
        p1_grid[spot] = value;
        return true;
    }     
    
    public void setGrid(int spot,Cargo value){
        p1_grid[spot] = value;
    }
    
    public Cargo getValue(int x,int y){
        int spot = (x*maxHeight)+y;
        return p1_grid[spot];
    }
    
    public int getWeight(int spot){
        return p1_grid[spot].getWeight();
    }
    
    public void printGrid(){
        /*for(int i : grid){
            System.out.println(grid[i]);
            
           }*/
            System.out.println(p1_grid[0]);
            System.out.println(p1_grid.length);

    }
    
    public boolean putCargoAtSpot(Cargo cargo, EmptyCargo emptySpot)
    {
        // TODO: Check both players
        if (emptySpot != null)
        {
            p1_grid[emptySpot.getId()] = cargo;
            setEmpty();
            return true;
        }
        return false;
    }
}
