package nsmith.Elements;

import java.util.ArrayList;
import nsmith.GameBlock;
import nsmith.nsLevel;

public class PillarJump implements GameBlock {
    
    private GameBlock parentBlock;
    private int absPos, possibleLength, actualLength, blockDifficulty;
    
    ArrayList<Integer> types;
    
    public PillarJump(GameBlock parent, int origX, int maxLength, int difficulty){
        parentBlock = parent;
        absPos = origX;
        possibleLength = maxLength;
        blockDifficulty = difficulty;
        types = new ArrayList<Integer>();
        
        types.add(0);
    }
    
    public String toString() {
        return "Pillar Jump";
    }

    @Override
    public int Add(int type) {
        if (type == 0)
            actualLength = nsLevel.LEVEL.buildPillarJump(absPos, possibleLength);
        return actualLength;
    }

    @Override
    public ArrayList<Integer> getTypes() {
        return types;
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

    @Override
    public void setDifficulty(int set) {
        blockDifficulty = set;
    }

    @Override
    public int getDifficulty() {
        return blockDifficulty;
    }
    
}
