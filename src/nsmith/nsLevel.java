/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nsmith;

import dk.itu.mario.MarioInterface.Constraints;
import dk.itu.mario.MarioInterface.GamePlay;
import dk.itu.mario.MarioInterface.LevelInterface;
import dk.itu.mario.engine.sprites.Enemy;
import dk.itu.mario.engine.sprites.SpriteTemplate;
import dk.itu.mario.level.Level;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import nsmith.ElementOdds.ODDS_E;
import nsmith.Elements.BuildZone;
import nsmith.Elements.Cannon;
import nsmith.Elements.Hill;
import nsmith.Elements.Jump;
import nsmith.Elements.Straight;
import nsmith.Elements.Tube;

/**Starting with copy of CustomizedLevel. nsLevel is called from nsmith.LevelGen
 *
 * @author Nicholas Smith
 */
public class nsLevel extends Level implements GameBlock{ 
    
    Random random;
    
    public static boolean DEBUG = true;
    
    ElementOdds oddsObj;
    
    protected int difficulty;
    protected int type;
    protected int gaps;
    protected int turtles;
    protected int coins;
    
    protected GamePlay playerN;
    
    private ArrayList<GameBlock> map_blocks;
    
    public static nsLevel LEVEL = null;//singleton
    
    public static boolean createNsLevel(int width, int height, long seed, int difficulty,int type, GamePlay playerMetrics){
        if(LEVEL == null){
            LEVEL = new nsLevel( width,  height,  seed,  difficulty, type,  playerMetrics);
            return true;
        }
        else{
            return false;
        }
    }
    
    /**Just to be sure you MUST pass TRUE as the parameter
     * 
     * @param yesOrNo 
     */
    public static void clearLEVEL(boolean yesOrNo){
        if(yesOrNo){
            LEVEL = null;
        }
    } 
   
    public static nsLevel getLEVEL(){
        return LEVEL;
    }
    
    public ArrayList<GameBlock> getBlockMap(){
        return map_blocks;
    }
    
    public void addBlock(GameBlock bl){
        map_blocks.add(bl);
    }
    
    /**searches backward and removes a 'count' number of GameBlock types
     * equivalent to bl
     * 
     * @param bl 
     */
    public void removeBlock(GameBlock bl, int count){
        Collections.reverse(map_blocks);//so we remove from the back
        int i = 0;
        for(int j = 0; j < map_blocks.size();j++){
            if(bl.getClass() == map_blocks.get(j).getClass()){
                map_blocks.remove(j);
                i++;
                if(i <= count){
                    break;
                }
            }
        }
        Collections.reverse(map_blocks);//so everything is in correct order
    }
    
    public ArrayList<GameBlock> getSlice(int x0, int x1){
        ArrayList<GameBlock> slice = new ArrayList<GameBlock>();
        for(int i = 0; i < x1 - x0; i++){
            slice.add(map_blocks.get(x0+i));
        }
        return slice;
    }
    
    private nsLevel(int width, int height, long seed, int difficulty,int type, GamePlay playerMetrics) {
        super(width, height);
        
        if(DEBUG){
            System.out.println("nsLevel");
              }
        
        map_blocks = new ArrayList<GameBlock>();
        
        this.playerN = playerMetrics;
        oddsObj = new ElementOdds(playerN);
    }

    public void creat(long seed, int difficulty, int type) {
        //
        this.type = type;
        this.difficulty = difficulty;
        oddsObj.odds.put(ElementOdds.ODDS_E.STRAIGHT, 30);
        oddsObj.odds.put(ElementOdds.ODDS_E.HILL_STRAIGHT, 20);
        oddsObj.odds.put(ElementOdds.ODDS_E.TUBES, 2+2*difficulty);
        int jumpDifficulty = 1;
        // adapt the game so that it has a number of gaps proportional to the
        //number of jumps the player made in the test level. The more the jumps,
        //the more the gaps.
        if (playerN.jumpsNumber > oddsObj.JumpingThreshold)
            jumpDifficulty = 2;
        oddsObj.odds.put(ElementOdds.ODDS_E.JUMP, jumpDifficulty);;
        oddsObj.odds.put(ElementOdds.ODDS_E.CANNONS, 10 + 5 * difficulty);

        if (type != LevelInterface.TYPE_OVERGROUND) {
            oddsObj.odds.put(ElementOdds.ODDS_E.HILL_STRAIGHT, 0);
        }

        
        random = new Random(seed);
        int length = 0;
        
        //Creating and adding blocks/ elements
        //build map_blocks
        //can adjust difficulty of all blocks
        
        BuildZone prefab = new BuildZone(this,length,getWidth(), difficulty);
        
        map_blocks.add(prefab.buildOpener());
        
        length += map_blocks.get(0).getLength();//random SAFE start
        
        //create all middle sections      
        while(length < getWidth() - 64){
            //choose next block and it's difficulty
            int nextDiff = decideBlockDifficulty();
            GameBlock next = decideBlock(length,getWidth()-length, nextDiff);
            if(next == null){
                continue;
            }
            int param = decideType(next);
            
            //add to block map, check for playability in here
            Add(next);
            //update length
            length+= next.Add(param);
        }
        
        prefab.buildEndExit(length);
        
        //creates the ceiling in castles, underground
        if (type == LevelInterface.TYPE_CASTLE || type == LevelInterface.TYPE_UNDERGROUND) {
            int ceiling = 0;
            int run = 0;
            for (int x = 0; x < width; x++) {
                if (run-- <= 0 && x > 4) {
                    ceiling = random.nextInt(4);
                    run = random.nextInt(4) + 4;
                }
                for (int y = 0; y < height; y++) {
                    if ((x > 4 && y <= ceiling) || x < 1) {
                        setBlock(x, y, GROUND);
                    }
                }
            }
        }

        fixWalls();

    }

    public int decideType(GameBlock bl){
        //return random type
        //do something with difficulty... have higher type ## correspond to more difficult
        //build specs
        int ty = 0;
        
        if(bl != null && bl.getTypes() != null){

            ty = bl.getTypes().get(random.nextInt(bl.getTypes().size()));
        }
        
        return ty;
    }
    
    public GameBlock decideBlock(int origin, int length, int difficult){
        GameBlock next = new BuildZone();
                
        int pick = random.nextInt(oddsObj.totalOdds);
        
        if(pick < oddsObj.bounds.get(ODDS_E.STRAIGHT)){
                next = new Straight(this, origin, length, difficult);
        }
        else if(pick < oddsObj.bounds.get(ODDS_E.HILL_STRAIGHT)){
            next = new Hill(this, origin, length, difficult);
        }
        else if(pick < oddsObj.bounds.get(ODDS_E.TUBES)){
                next = new Tube(this, origin, length, difficult);

        }
        else if(pick < oddsObj.bounds.get(ODDS_E.JUMP)){
                next = new Jump(this, origin, length, difficult);

        }
        else if(pick < oddsObj.bounds.get(ODDS_E.CANNONS)){
            next = new Cannon(this, origin, length, difficult);

        }
        
        return next;
    }
    
    public int decideBlockDifficulty(){
        int diff = difficulty;
        //adjust for player preferences
        return diff;
    }
    public int getDifficulty(){
        return difficulty;
    }
    public void setDifficulty(int set){
        difficulty = set;
    }
    
    public byte getGround(){
        return Level.GROUND;
    }
    
    public void setXExit(int x){
        xExit = x;
    }
    
    public void setYExit(int y){
        yExit = y;
    }
    
    public int getXExit(){
        return xExit;
    }
    
    public int getYExit(){
        return yExit;
    }
    
    public int buildJump(int xo, int maxLength) {
        gaps++;
        //jl: jump length
        //js: the number of blocks that are available at either side for free
        int js = random.nextInt(4) + 2;
        int jl = random.nextInt(2) + 2;
        int length = js * 2 + jl;

        boolean hasStairs = random.nextInt(3) == 0;

        int floor = height - 1 - random.nextInt(4);
        //run for the from the start x position, for the whole length
        for (int x = xo; x < xo + length; x++) {
            if (x < xo + js || x > xo + length - js - 1) {
                //run for all y's since we need to paint blocks upward
                for (int y = 0; y < height; y++) { //paint ground up until the floor
                    if (y >= floor) {
                        setBlock(x, y, GROUND);
                    }
                    //if it is above ground, start making stairs of rocks
                    else if (hasStairs) { //LEFT SIDE
                        if (x < xo + js) { //we need to max it out and level because it wont
                            //paint ground correctly unless two bricks are side by side
                            if (y >= floor - (x - xo) + 1) {
                                setBlock(x, y, ROCK);
                            }
                        } else { //RIGHT SIDE
                            if (y >= floor - ((xo + length) - x) + 2) {
                                setBlock(x, y, ROCK);
                            }
                        }
                    }
                }
            }
        }

        return length;
    }

    public int buildCannons(int xo, int maxLength) {
        int length = random.nextInt(10) + 2;
        if (length > maxLength) length = maxLength;

        int floor = height - 1 - random.nextInt(4);
        int xCannon = xo + 1 + random.nextInt(4);
        for (int x = xo; x < xo + length; x++) {
            if (x > xCannon) {
                xCannon += 2 + random.nextInt(4);
            }
            if (xCannon == xo + length - 1) xCannon += 10;
            int cannonHeight = floor - random.nextInt(4) - 1;

            for (int y = 0; y < height; y++) {
                if (y >= floor) {
                    setBlock(x, y, (byte) (1 + 9 * 16));
                } else {
                    if (x == xCannon && y >= cannonHeight) {
                        if (y == cannonHeight) {
                            setBlock(x, y, (byte) (14 + 0 * 16));
                        } else if (y == cannonHeight + 1) {
                            setBlock(x, y, (byte) (14 + 1 * 16));
                        } else {
                            setBlock(x, y, (byte) (14 + 2 * 16));
                        }
                    }
                }
            }
        }

        return length;
    }

    public int buildHillStraight(int xo, int maxLength) {
        int length = random.nextInt(10) + 10;
        if (length > maxLength) length = maxLength;

        int floor = height - 1 - random.nextInt(4);
        for (int x = xo; x < xo + length; x++) {
            for (int y = 0; y < height; y++) {
                if (y >= floor) {
                    setBlock(x, y,Level.GROUND);
                }
            }
        }

        addEnemyLine(xo + 1, xo + length - 1, floor - 1);

        int h = floor;

        boolean keepGoing = true;

        boolean[] occupied = new boolean[length];
        while (keepGoing) {
            h = h - 2 - random.nextInt(3);

            if (h <= 0) {
                keepGoing = false;
            } else {
                int l = random.nextInt(5) + 3;
                int xxo = random.nextInt(length - l - 2) + xo + 1;

                if (occupied[xxo - xo] || occupied[xxo - xo + l] ||
                    occupied[xxo - xo - 1] || occupied[xxo - xo + l + 1]) {
                    keepGoing = false;
                } else {
                    occupied[xxo - xo] = true;
                    occupied[xxo - xo + l] = true;
                    addEnemyLine(xxo, xxo + l, h - 1);
                    if (random.nextInt(4) == 0) {
                        decorate(xxo - 1, xxo + l + 1, h);
                        keepGoing = false;
                    }
                    for (int x = xxo; x < xxo + l; x++) {
                        for (int y = h; y < floor; y++) {
                            int xx = 5;
                            if (x == xxo) xx = 4;
                            if (x == xxo + l - 1) xx = 6;
                            int yy = 9;
                            if (y == h) yy = 8;

                            if (getBlock(x, y) == 0) {
                                setBlock(x, y, (byte) (xx + yy * 16));
                            } else {
                                if (getBlock(x, y) == Level.HILL_TOP_LEFT)
                                    setBlock(x, y, Level.HILL_TOP_LEFT_IN);
                                if (getBlock(x, y) == Level.HILL_TOP_RIGHT)
                                    setBlock(x, y, Level.HILL_TOP_RIGHT_IN);
                            }
                        }
                    }
                }
            }
        }

        return length;
    }

    public void addEnemyLine(int x0, int x1, int y) {
        for (int x = x0; x < x1; x++) {
            if (random.nextInt(50) < 25) {
                int type = random.nextInt(4);

                    type = random.nextInt(3);
                if (turtles < Constraints.turtels) {
                    if (type == Enemy.ENEMY_GREEN_KOOPA || type == Enemy.ENEMY_RED_KOOPA) {
                        turtles++;
                        setSpriteTemplate(x, y,
                                          new SpriteTemplate(type,
                                random.nextInt(35) < difficulty));
                    } else {
                        setSpriteTemplate(x, y,
                                          new SpriteTemplate(type,
                                random.nextInt(35) < difficulty));
                    }
                }
                else{
                	setSpriteTemplate(x, y,
                            new SpriteTemplate(Enemy.ENEMY_GOOMBA,
                  random.nextInt(35) < difficulty));
                }
            }
        }
    }

    public int buildTubes(int xo, int maxLength) {
        int length = random.nextInt(10) + 5;
        if (length > maxLength) length = maxLength;

        int floor = height - 1 - random.nextInt(4);
        int xTube = xo + 1 + random.nextInt(4);
        int tubeHeight = floor - random.nextInt(2) - 2;
        for (int x = xo; x < xo + length; x++) {
            if (x > xTube + 1) {
                xTube += 3 + random.nextInt(4);
                tubeHeight = floor - random.nextInt(2) - 2;
            }
            if (xTube >= xo + length - 2) xTube += 10;

            if (x == xTube && random.nextInt(11) < difficulty + 1) {
                setSpriteTemplate(x, tubeHeight,
                                  new SpriteTemplate(Enemy.ENEMY_FLOWER, false));
            }

            for (int y = 0; y < height; y++) {
                if (y >= floor) {
                    setBlock(x, y, (byte) (1 + 9 * 16));

                } else {
                    if ((x == xTube || x == xTube + 1) && y >= tubeHeight) {
                        int xPic = 10 + x - xTube;

                        if (y == tubeHeight) {
                            //tube top
                            setBlock(x, y, (byte) (xPic + 0 * 16));
                        } else {
                            //tube side
                            setBlock(x, y, (byte) (xPic + 1 * 16));
                        }
                    }
                }
            }
        }

        return length;
    }

    public int buildStraight(int xo, int maxLength, boolean safe) {
        int length = random.nextInt(10) + 2;

        if (safe)
            length = 10 + random.nextInt(5);

        if (length > maxLength)
            length = maxLength;

        int floor = height - 1 - random.nextInt(4);

        //runs from the specified x position to the length of the segment
        for (int x = xo; x < xo + length; x++) {
            for (int y = 0; y < height; y++) {
                if (y >= floor) {
                    setBlock(x, y, Level.GROUND);
                }
            }
        }

        if (!safe) {
            if (length > 5) {
                decorate(xo, xo + length, floor);
            }
        }

        return length;
    }
    //look into changing this function
    public void decorate(int xStart, int xLength, int floor) {
        //if its at the very top, just return
        if (floor < 1)
            return;
        boolean rocks = true;

        //add an enemy line above the box
        addEnemyLine(xStart + 1, xLength - 1, floor - 1);

        int s = random.nextInt(4);
        int e = random.nextInt(4);

        if (floor - 2 > 0) {
            if ((xLength - 1 - e) - (xStart + 1 + s) > 1) {
                for (int x = xStart + 1 + s; x < xLength - 1 - e; x++) {
                    setBlock(x, floor - 2, (byte) (2 + 2 * 16));
                }
            }
        }

        s = random.nextInt(4);
        e = random.nextInt(4);

        if (floor - 4 > 0) {
            if ((xLength - 1 - e) - (xStart + 1 + s) > 2) {
                for (int x = xStart + 1 + s; x < xLength - 1 - e; x++) {
                    if (rocks) {
                        if (x != xStart + 1 && x != xLength - 2 &&
                            random.nextInt(2) == 0) {
                            if (random.nextInt(2) == 0) {
                                setBlock(x, floor - 4, BLOCK_POWERUP);
                            } else {
                                if(coins < Constraints.coinBlocks){
                                    coins++;
                                    setBlock(x, floor - 4, BLOCK_COIN);
                                }
                                else{
                                    setBlock(x, floor - 4, BLOCK_EMPTY);
                                }
                            }
                        } else if (random.nextInt(4) == 0) {
                            if (random.nextInt(4) == 0) {
                                setBlock(x, floor - 4, (byte) (2 + 1 * 16));
                            } else {
                                setBlock(x, floor - 4, (byte) (1 + 1 * 16));
                            }
                        } else {
                            setBlock(x, floor - 4, BLOCK_EMPTY);
                        }
                    }
                }
            }
        }
    }

    public void fixWalls() {
        boolean[][] blockMap = new boolean[width + 1][height + 1];

        for (int x = 0; x < width + 1; x++) {
            for (int y = 0; y < height + 1; y++) {
                int blocks = 0;
                for (int xx = x - 1; xx < x + 1; xx++) {
                    for (int yy = y - 1; yy < y + 1; yy++) {
                        if (getBlockCapped(xx, yy) == GROUND) {
                            blocks++;
                        }
                    }
                }
                blockMap[x][y] = blocks == 4;
            }
        }
        blockify(this, blockMap, width + 1, height + 1);
    }

    public void blockify(Level level, boolean[][] blocks, int width,
                          int height) {
        int to = 0;
        if (type == LevelInterface.TYPE_CASTLE) {
            to = 4 * 2;
        } else if (type == LevelInterface.TYPE_UNDERGROUND) {
            to = 4 * 3;
        }

        boolean[][] b = new boolean[2][2];

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int xx = x; xx <= x + 1; xx++) {
                    for (int yy = y; yy <= y + 1; yy++) {
                        int _xx = xx;
                        int _yy = yy;
                        if (_xx < 0) _xx = 0;
                        if (_yy < 0) _yy = 0;
                        if (_xx > width - 1) _xx = width - 1;
                        if (_yy > height - 1) _yy = height - 1;
                        b[xx - x][yy - y] = blocks[_xx][_yy];
                    }
                }

                if (b[0][0] == b[1][0] && b[0][1] == b[1][1]) {
                    if (b[0][0] == b[0][1]) {
                        if (b[0][0]) {
                            level.setBlock(x, y, (byte) (1 + 9 * 16 + to));
                        } else {
                            // KEEP OLD BLOCK!
                        }
                    } else {
                        if (b[0][0]) {
                            //down grass top?
                            level.setBlock(x, y, (byte) (1 + 10 * 16 + to));
                        } else {
                            //up grass top
                            level.setBlock(x, y, (byte) (1 + 8 * 16 + to));
                        }
                    }
                } else if (b[0][0] == b[0][1] && b[1][0] == b[1][1]) {
                    if (b[0][0]) {
                        //right grass top
                        level.setBlock(x, y, (byte) (2 + 9 * 16 + to));
                    } else {
                        //left grass top
                        level.setBlock(x, y, (byte) (0 + 9 * 16 + to));
                    }
                } else if (b[0][0] == b[1][1] && b[0][1] == b[1][0]) {
                    level.setBlock(x, y, (byte) (1 + 9 * 16 + to));
                } else if (b[0][0] == b[1][0]) {
                    if (b[0][0]) {
                        if (b[0][1]) {
                            level.setBlock(x, y, (byte) (3 + 10 * 16 + to));
                        } else {
                            level.setBlock(x, y, (byte) (3 + 11 * 16 + to));
                        }
                    } else {
                        if (b[0][1]) {
                            //right up grass top
                            level.setBlock(x, y, (byte) (2 + 8 * 16 + to));
                        } else {
                            //left up grass top
                            level.setBlock(x, y, (byte) (0 + 8 * 16 + to));
                        }
                    }
                } else if (b[0][1] == b[1][1]) {
                    if (b[0][1]) {
                        if (b[0][0]) {
                            //left pocket grass
                            level.setBlock(x, y, (byte) (3 + 9 * 16 + to));
                        } else {
                            //right pocket grass
                            level.setBlock(x, y, (byte) (3 + 8 * 16 + to));
                        }
                    } else {
                        if (b[0][0]) {
                            level.setBlock(x, y, (byte) (2 + 10 * 16 + to));
                        } else {
                            level.setBlock(x, y, (byte) (0 + 10 * 16 + to));
                        }
                    }
                } else {
                    level.setBlock(x, y, (byte) (0 + 1 * 16 + to));
                }
            }
        }
    }
    
     public int getLength(){
        int total = 0;
        for(GameBlock bl : map_blocks){
            total += bl.getLength();
       }
        return total;
    }
    
    public int getComponentsCount(){
        return map_blocks.size();
    }
    
    /**Adds to the end of the block map
     * 
     * @param bl 
     */
    public void Add(GameBlock bl){
        if(DEBUG){System.out.println("Added Block "+ bl.toString());}
        map_blocks.add(map_blocks.size()-1,bl); 
    }
    
    @Override
    public Class identifyBlock(int type) {
        return this.getClass();
    }

    @Override
    public boolean getComposite() {
        return true;
    }

    @Override
    public GameBlock getParent() {
        return this;
    }

    @Override
    public int Add(int type) {
        return 0;
    }

    @Override
    public ArrayList<Integer> getTypes() {
        return null;
    }




}
