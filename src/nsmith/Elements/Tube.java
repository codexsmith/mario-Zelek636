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
public class Tube implements GameBlock {
    
    private int blockDifficulty;
    private GameBlock parentBlock; //context
    private int absPos, possibleLength, actualLength;
    ArrayList<Integer> types;
    
    /**
     *
     * @param level
     * @param origX
     * @param maxLength
     */
    public Tube(GameBlock parent, int origX, int maxLength, int difficult){
        parentBlock = parent;
        absPos = origX;
        possibleLength = maxLength;
        types = new ArrayList<Integer>();
        blockDifficulty = difficult;
        
        types.add(0);
    }
    
    public int Add(int type) {
        //perhaps use type to artificially inflate the difficulty?
        //would have to 'rewrite' nsLevel.buildTubes into this class.
        if(type == 0){
            actualLength = nsLevel.LEVEL.buildTubes(absPos, possibleLength, blockDifficulty);
        }
        
        return actualLength;
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
    public Class identifyBlock(int type) {
        return this.getClass();
    }
    
    public String toString(){
        return "Tube";
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
