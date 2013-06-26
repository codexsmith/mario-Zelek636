/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nsmith;

import dk.itu.mario.MarioInterface.GamePlay;
import java.util.HashMap;
import java.util.Map;

/**
 * DataType
 *
 * @author nix
 */
public class ElementOdds {

    public static enum ODDS_E {

        STRAIGHT(0), HILL_STRAIGHT(1), TUBES(2), JUMP(3), CANNONS(4), CANNON_ARRAY(5), CAVE(6);
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
    public final int JumpingThreshold = 3;
    public final int enemyScale = 3;
    public final int decorateScale = 3;

    public ElementOdds(GamePlay playerMetric) {
        bounds = new HashMap<ODDS_E, Integer>();
        odds = new HashMap<ODDS_E, Integer>();

        odds.put(ODDS_E.JUMP, 3);
        odds.put(ODDS_E.STRAIGHT, 8);
        odds.put(ODDS_E.HILL_STRAIGHT, 8);
        odds.put(ODDS_E.CANNONS, 3);
        odds.put(ODDS_E.TUBES, 3);
        odds.put(ODDS_E.CANNON_ARRAY, 1);
        odds.put(ODDS_E.CAVE, 2);
     
        totalOdds = 0;

        sanityAndStats();
    }

    public void scaleOdds(int maxDifficulty) {
        
        int scale = maxDifficulty/10;
        
        for(Map.Entry<ODDS_E, Integer> i : odds.entrySet()){
            switch(i.getKey()){
                case CANNONS:odds.put(i.getKey(),odds.get(i.getKey()) * scale);
                    break;
                case CANNON_ARRAY:odds.put(i.getKey(),odds.get(i.getKey()) * scale);
                    break;
                case CAVE:odds.put(i.getKey(),odds.get(i.getKey()) * scale);
                    break;
                case HILL_STRAIGHT:odds.put(i.getKey(),odds.get(i.getKey()) * scale);
                    break;
                case JUMP: odds.put(i.getKey(),odds.get(i.getKey()) * scale);
                    break;
                case STRAIGHT:odds.put(i.getKey(),odds.get(i.getKey()) * scale);
                    break;
                case TUBES:odds.put(i.getKey(),odds.get(i.getKey()) * scale);
                    break;
            }
        }
    }

    public void sanityAndStats() {

        for (Map.Entry<ODDS_E, Integer> val : odds.entrySet()) {
            //failsafe (no negative odds)
            if (val.getValue() < 0) {
                val.setValue(0);
            }

            totalOdds += val.getValue();
            bounds.put(val.getKey(), totalOdds);
        }
//COULD Normalize, but we don't have to, and current logic in nsLevel is set up
//  for no normalization
//        for(Map.Entry<ODDS_E, Integer> val : bounds.entrySet()){
//            val.setValue(val.getValue()/totalOdds);
//        }
    }
}
