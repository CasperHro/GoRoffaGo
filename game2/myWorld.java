import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Arrays;

/**
 * Write a description of class myWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class myWorld extends World
{   
    int shipCentre = 405;
    int waterLevel = 680;
    int width = 4; //number of cargo
    int cargoWidth = 80+5; //cargo=80px, padding = 5px
    
    
    int maxHeight = 4;
    int maxWidth = width;
    Cargo[] grid;
    
    
    Deck deck = new Deck();
    public int tilt;
    
    int looted;
    
    int delayTiltTimer; //Timestamp van de laatste Tilt Actie
    int stepsCount=0;
    int counter =0;
    //WeightCounter WeightCounter = new WeightCounter();
    
    
    /**
     * Constructor for objects of class myWorld.
     * 
     */
    public myWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        // False makes the world boundless.
        super(1000, 800, 1, false);
        createGrid(maxHeight,maxWidth);
        printGrid();
        prepare();   
        System.out.println("start");
        System.out.println("Weight row 0 = "+getWeightPerX(0));
        System.out.println("Weight row 1 = "+getWeightPerX(1));
        System.out.println("Weight row 2 = "+getWeightPerX(2));
        System.out.println("Weight row 3 = "+getWeightPerX(3));
    }

    /**
     * Prepare the world for the start of the program.
     * That is: create the initial objects and add them to the world.
     */
    private void prepare()
    {
        sky sky = new sky();
        sky sky2 = new sky();
        Water water = new Water();
        Hook hook = new Hook();
        Harbor harbor = new Harbor();
        Harbor harbor2 = new Harbor();
        Harbor harbor3 = new Harbor();
        Harbor harbor4 = new Harbor();
        Transport transport = new Transport();
        
        Counter counter = new Counter();
        

        addObject(sky,500,200);
        addObject(sky2,500,500);
        addObject(water,500,750);
        addObject(hook,502,70);
        addObject(harbor,952,754);
        addObject(harbor2,848,754);
        addObject(harbor3,848,658);
        addObject(harbor4,952,658);
        addObject(transport,888,563);
        addObject(counter,958,650);
        addObject(deck,shipCentre,waterLevel);

        setCargo();
        //transport.setLocation(888,563); je zet dit al goed bij addObject(name, x ,y);
        //cargo3.setLocation(4056,430);
    }
    
    
    public void act(){
            counter++;
            if(counter ==100){
                delayTiltTimer++;
                System.out.println(delayTiltTimer);
                
                tilt = getTilt();
                
                deck.adjustShip();
                if(tilt>0){
                    stepsCount++;
                }else if(tilt<0){
                    stepsCount--;
                }
                System.out.println("rotation "+deck.getRotation());
                //System.out.println("this should be 5"+tilt);
                showText("CargoWeight :"+tilt,shipCentre,waterLevel+50);
                
                counter=0;
            }
        }
    
        
        
        
        
        //@@ preparation
        
    public int liftCargo(Cargo cargo){
        //Arrays.binarySearch(grid,cargo);
        
        //System.out.println(Arrays.binarySearch(grid,cargo));
        int i =0;
        for(Cargo c :grid){
            if(c==cargo){
                break;
            }
            i++;
        }
        return i;
    }    
        
        
    public void setCargo() {
        int cargoHeight = 40;
        int StackHeight = waterLevel-50;
        int height = maxHeight-1;
        int i = 0;
        int hi = 0;                                     //how high the cargo is stacked
        int x = shipCentre-(cargoWidth*(width/2)-40);

        
        while(i<width*height){
            int randomCargoType = ThreadLocalRandom.current().nextInt(1, 3 + 1);
            if(randomCargoType==1){
                Cargo cargo = new red();
                addObject(cargo,x,StackHeight);
                grid[i]=cargo;
                //System.out.println(cargo.getColor());
                //System.out.println(cargo.getWeight());
            }
            if(randomCargoType==2){
                Cargo cargo = new blue();
                addObject(cargo,x,StackHeight);
                grid[i]=cargo;
            }
            if(randomCargoType==3){
                Cargo cargo = new green();
                addObject(cargo,x,StackHeight);
                grid[i]=cargo;
            }
            
            x = x+cargoWidth;
            i++;
            if(i%width==0){
                x = shipCentre-(cargoWidth*(width/2)-40);
                StackHeight-=cargoHeight;
            }
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
       if(halfLeft>halfRight{
           tilt = 1;
       }else if (halfLeft<halfRight){
           tilt = -1;
       }else{
           tilt=0;
       }
        
       System.out.println(windSpeed+" : tilt = "+tilt);
       System.out.println("halfLeft = "+halfLeft+": halfRight = "+halfRight);
       return windSpeed;
    }
    
    public int getWeightPerX(int x){
        int weight = 0;
        int i = 0;
        while(i < maxHeight){
            int spot = x+(i*width);
            //System.out.println("spot no "+spot);
            if(grid[spot]!=null){
                weight+=grid[spot].getWeight();
            }
            i++;
        }
        return weight;
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
        grid = new Cargo[gridSize];
    }
        
    public int getSpot(int x, int y){
        int spot = (x*maxHeight)+y;
        return grid[spot].getWeight();
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
        grid[spot] = value;
        return true;
    }     
    
    public void setGrid(int spot,Cargo value){
        grid[spot] = value;
    }
    
    public Cargo getValue(int x,int y){
        int spot = (x*maxHeight)+y;
        return grid[spot];
    }
    
    public void printGrid(){
        /*for(int i : grid){
            System.out.println(grid[i]);
            
           }*/
            System.out.println(grid[0]);
            System.out.println(grid.length);

    }
    
}
