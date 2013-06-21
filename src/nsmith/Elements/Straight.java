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
public class Straight implements GameBlock{
    
    
    private GameBlock parentBlock; //context
    private int absPos, possibleLength, actualLength, blockDifficulty;


    ArrayList<Integer> types;
    
    /**
     *
     * @param level
     * @param origX
     * @param maxLength
     */
    public Straight(GameBlock parent, int origX, int maxLength, int difficulty){
        parentBlock = parent;
        absPos = origX;
        possibleLength = maxLength;
        blockDifficulty = difficulty;
        types = new ArrayList<Integer>();
        
         types.add(0);
        types.add(1);
        
    }
    
    @Override
    public int Add(int type) {
        if(type == 0){
            actualLength = nsLevel.LEVEL.buildStraight(absPos, possibleLength, false);
        }
        else if(type == 1){
            actualLength = nsLevel.LEVEL.buildStraight(absPos, possibleLength, true);
        }
      
        return actualLength;
    }
    //random safe/unsafe opening, can add type here to add more functionality
    public int buildOpening(){
        return nsLevel.LEVEL.buildStraight(0, nsLevel.LEVEL.getWidth(), random.nextInt(2) == 0 ? true : false);
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
        return "Straight";
    }
    
        @Override
    public void setDifficulty(int set) {
        blockDifficulty = set;
    }

    @Override
    public int getDifficulty() {
        return blockDifficulty;
    }
    
    public int getAbsPos() {
        return absPos;
    }
   
    @Override
    public ArrayList<Integer> getTypes() {
        return types;
    }
    
}
