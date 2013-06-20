/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nsmith;

import dk.itu.mario.MarioInterface.GamePlay;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**DataType
 *
 * @author nix
 */
public class ElementOdds {
    
    public static enum ODDS_E {
        STRAIGHT(0), HILL_STRAIGHT(1), TUBES(2), JUMP(3),CANNONS(4);
    
        private final int index;   
        
        ODDS_E(int index) {
            this.index = index;
        }

        public int index() { 
            return index; 
        }
        
    };
    public HashMap<ODDS_E, Integer> bounds; //linear scalable discrete partition of the likelyhood of each peice
    
    public HashMap<ODDS_E, Integer> odds; //RAW unscaled odds
    
    public int totalOdds;
    public static final int JumpingThreshold = 3;

    
    public ElementOdds(GamePlay playerMetric){
        bounds = new HashMap<ODDS_E, Integer>();
        odds = new HashMap<ODDS_E, Integer>();
        
        odds.put(ODDS_E.JUMP, 100);
        odds.put(ODDS_E.STRAIGHT, 100);
        odds.put(ODDS_E.HILL_STRAIGHT, 100);
        odds.put(ODDS_E.CANNONS, 100);
        odds.put(ODDS_E.TUBES, 100);
        
        sanityAndStats();
    }
    
    public void sanityAndStats(){
        
        for (Map.Entry<ODDS_E, Integer> val : odds.entrySet()){
            //failsafe (no negative odds)
            if (val.getValue() < 0) {
                val.setValue(0);
            }

           totalOdds += odds.get(val.getKey());
           bounds.put(val.getKey(), totalOdds - val.getValue());
        }
//COULD Normalize, but we don't have to, and current logic in nsLevel is set up
//  for no normalization
//        for(Map.Entry<ODDS_E, Integer> val : bounds.entrySet()){
//            val.setValue(val.getValue()/totalOdds);
//        }
    }
       
}
