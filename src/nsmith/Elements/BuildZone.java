/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nsmith.Elements;

import dk.itu.mario.level.Level;
import java.util.ArrayList;
import java.util.Random;
import nsmith.GameBlock;
import nsmith.nsLevel;

/**
 *
 * @author nix
 */
public class BuildZone implements GameBlock{
    
    private Random random = new Random();
    
    private int length, absPos, zoneDifficulty;
    private ArrayList<GameBlock> components;
    private GameBlock par;
    ArrayList<Integer> types;

    
    public BuildZone(GameBlock parent, int origX, int maxLength, int difficulty){
        length = maxLength;
        absPos = origX;
        components = new ArrayList<GameBlock>();   
        par = parent;
        zoneDifficulty = difficulty;
        types = new ArrayList<Integer>();
    }
    
    /**Use this Add for hand-coded int[][] map mainpulation, see buildEndExit
     * 
     * @param type
     * @return 
     */
    @Override
    public int Add(int type) {
        int length  = 0;
        if(type == 0){
            length = buildEndExit();
        }
        return length;
    }
    
    public GameBlock AddBlock(int type){
        if(type == 0){
            return buildOpening();
        }
        return null;
    }
    
    @Override
    public ArrayList<Integer> getTypes() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Class identifyBlock(int type) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean getComposite() {
        return true;
    }

    @Override
    public GameBlock getParent() {
        return par;
    }

    @Override
    public int getLength() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setDifficulty(int set) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getDifficulty() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public GameBlock buildOpening(){
        return new Straight(this,0, nsLevel.LEVEL.getWidth(), nsLevel.LEVEL.getDifficulty());
    }
    
    public int buildEndExit(){
        //set the end piece
        int length = 0;
        int floor = nsLevel.LEVEL.getHeight() - 1 - random.nextInt(4);

        //creat the exit
        nsLevel.LEVEL.setXExit(length + 8);
        nsLevel.LEVEL.setYExit(floor);

        for (int x = length; x < nsLevel.LEVEL.getWidth(); x++) {
            for (int y = 0; y < nsLevel.LEVEL.getHeight(); y++) {
                if (y >= floor) {
                    nsLevel.LEVEL.setBlock(x, y, nsLevel.LEVEL.getGround());
                }
            }
            length++;
        }
        return length;
    }
}
