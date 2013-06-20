/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nsmith;
import java.util.ArrayList;
import java.util.Random;

/**Implements the Composite pattern for a level. Composites can either be a an entire level,
 * a buildZone, or a single build item. I.E. the class Level, Level.
 *
 * @author nix
 */
public interface GameBlock {
    
    public static Random random = new Random();
    
    public int Add(int type);
    
    public ArrayList<Integer> getTypes();
    
    public Class identifyBlock(int type);
    
    public String toString();
    
    public boolean getComposite();
            
    public GameBlock getParent();
    
    public int getLength();
    
    public void setDifficulty(int set);
    public int getDifficulty();
 
    
}
