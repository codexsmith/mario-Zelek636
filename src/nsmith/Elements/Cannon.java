/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nsmith.Elements;

import java.util.ArrayList;
import nsmith.GameBlock;
import nsmith.nsLevel;

/**GameBlock element
 *
 * @author nix
 */
public class Cannon implements GameBlock{
    
    private int blockDifficulty;
    private GameBlock parentBlock; //context
    private int absPos, possibleLength, actualLength;
    ArrayList<Integer> types; 
    
    /**
     * @param level
     * @param origX
     * @param maxLength
     */
    public Cannon(GameBlock parent, int origX, int maxLength, int difficulty){
        parentBlock = parent;
        absPos = origX;
        possibleLength = maxLength;
        blockDifficulty = difficulty;
        types = new ArrayList<Integer>();
    }
    
    @Override
    public int Add(int type) {
        int t = type;//what kind of tube do you want to build?
        //perhaps use type to artificially inflate the difficulty?
        //would have to 'rewrite' nsLevel.buildTubes into this class.
        return nsLevel.LEVEL.buildCannons(absPos, possibleLength);
    }

    @Override
    public Class identifyBlock(int type) {
        return this.getClass();
    }

    @Override
    public boolean getComposite() {
        return false;
    }

    @Override
    public GameBlock getParent() {
        return parentBlock;
    }

    @Override
    public int getLength() {
        return actualLength;
    }
    
    public String toString(){
        return "Cannon";
    }
    
        @Override
    public void setDifficulty(int set) {
        blockDifficulty = set;
    }

    @Override
    public int getDifficulty() {
        return blockDifficulty;
    }
        
    @Override
    public ArrayList<Integer> getTypes() {
        return types;
    }
}
