package nsmith;


import java.util.HashMap;
import java.util.Map;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**DataType 
 *
 * @author nix
 */
public class Feature {
    public static enum PLAYER {KILLER,TREASURE,SPEED};
    
    public HashMap<PLAYER,Integer> vector;
    
    public Feature(int K, int T, int S){
        vector = new HashMap<PLAYER, Integer>();
        
        vector.put(PLAYER.KILLER, K);
        vector.put(PLAYER.TREASURE, T);
        vector.put(PLAYER.SPEED, S);
        
        normalize();
        
    }
    
    public void normalize(){
        int sum = 0;
        
        for(Integer i : vector.values()){
            sum  += i;
        }
        if(sum <= 1){
            return;
        }
        else{
            for(Map.Entry<PLAYER, Integer> et : vector.entrySet()){
                et.setValue(et.getValue()/sum);
            }
        }
    }
            
}
