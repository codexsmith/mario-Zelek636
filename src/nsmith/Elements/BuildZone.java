/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nsmith.Elements;

import java.util.ArrayList;
import java.util.Random;
import nsmith.GameBlock;
import nsmith.nsLevel;

/**Used to 'build' a prefabricated or algorithmic level block
 * look at BuildEndExit as an example
 * @author nix
 */
public class BuildZone implements GameBlock{
    
    private Random random = new Random();
    
    private int possiblelength, absPos, zoneDifficulty, actualLength = 0;
    private ArrayList<GameBlock> components;
    private GameBlock par;
    ArrayList<Integer> types;

    public BuildZone(){
        possiblelength = 0;
        absPos = 0;
        par = null;
        zoneDifficulty = 0;
        actualLength = 0;
        
    }
    
    public BuildZone(GameBlock parent, int origX, int maxLength, int difficulty){
        possiblelength = maxLength;
        absPos = origX;
        components = new ArrayList<GameBlock>();   
        par = parent;
        zoneDifficulty = difficulty;
        types = new ArrayList<Integer>();
        
        types.add(0);//buildEndExit
    }
    
    /**Use this Add for hand-coded int[][] map mainpulation, see buildEndExit
     * 
     * @param type
     * @return 
     */
    @Override
    public int Add(int type) {
        if(type == 0){
            actualLength = buildEndExit();
        }
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
        return true;
    }

    @Override
    public GameBlock getParent() {
        return par;
    }

    @Override
    public int getLength() {
        return actualLength;
    }

    @Override
    public void setDifficulty(int set) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getDifficulty() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public GameBlock buildOpener(){
        GameBlock opener = new Straight(nsLevel.LEVEL,0, possiblelength, zoneDifficulty);
        opener.Add(-1);//could change this int type to an enum?
        return opener;
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
