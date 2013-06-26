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
public class Hill implements GameBlock{

    private int blockDifficulty;
    private GameBlock parentBlock; //context
    private int absPos, possibleLength, actualLength;
    ArrayList<Integer> types; 
    
    public Hill(GameBlock parent, int origX, int maxLength, int difficulty){
        parentBlock = parent;
        absPos = origX;
        possibleLength = maxLength;
        blockDifficulty = difficulty;
        types = new ArrayList<Integer>();
        
        types.add(0);
        
    }
    
    @Override
    public int Add(int type) {
        
        if(type == 0){
            actualLength = nsLevel.LEVEL.buildHillStraight(absPos, possibleLength, blockDifficulty);
        }
        
        return actualLength;
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
    
    public String toString(){
        return "Hill";
    }

    @Override
    public int getLength() {
        return actualLength;
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
