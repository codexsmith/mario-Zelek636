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
    public final int JumpingThreshold = 2;
    public final int enemyScale = 2;
    public final int decorateScale = 2;

    public ElementOdds(GamePlay playerMetric) {
        bounds = new HashMap<ODDS_E, Integer>();
        odds = new HashMap<ODDS_E, Integer>();

        odds.put(ODDS_E.JUMP, new Integer(4));
        odds.put(ODDS_E.STRAIGHT, new Integer(7));
        odds.put(ODDS_E.HILL_STRAIGHT, new Integer(7));
        odds.put(ODDS_E.CANNONS, new Integer(3));
        odds.put(ODDS_E.TUBES, new Integer(4));
        odds.put(ODDS_E.CANNON_ARRAY, new Integer(2));
        odds.put(ODDS_E.CAVE, new Integer(2));
        
        for(Integer i : odds.values()){
            totalOdds+=i;
        }
        
        if(nsLevel.DEBUG){
            stringOut();
        }
        
        sanityAndStats();
    }
    
    public void stringOut(){
         System.out.println("ODDS");
        for(Map.Entry<ODDS_E, Integer> b_i : bounds.entrySet()){
             System.out.println(b_i.getKey().toString() + " " + b_i.getValue());
        }
       
    }
    
    public void scaleOdds(int maxDifficulty) {
        
        float scaleA = totalOdds/10;
        int scale = (int) ((int) maxDifficulty * scaleA);
        int next;
        
        
        for(Map.Entry<ODDS_E, Integer> i : odds.entrySet()){
            switch(i.getKey()){
                case CANNONS:next = odds.get(i.getKey()) * scale;
                    odds.put(i.getKey(),next);
                    break;
                case CANNON_ARRAY:next = odds.get(i.getKey()) * scale;
                    odds.put(i.getKey(),next);
                    break;
                case CAVE:next = odds.get(i.getKey()) * scale;
                    odds.put(i.getKey(),next);
                    break;
                case HILL_STRAIGHT:next = odds.get(i.getKey()) * scale;
                    odds.put(i.getKey(),next);
                    break;
                case JUMP: next = odds.get(i.getKey()) * scale;
                    odds.put(i.getKey(),next);
                    break;
                case STRAIGHT:next = odds.get(i.getKey()) * scale;
                    odds.put(i.getKey(),next);
                    break;
                case TUBES:next = odds.get(i.getKey()) * scale;
                    odds.put(i.getKey(),next);
                    break;
            }
        }
        if(nsLevel.DEBUG){
            System.out.println("scaled");
            stringOut();
        }
        
    }

    public void sanityAndStats() {
        int tempOdds = 0;
        
        for (Map.Entry<ODDS_E, Integer> val : odds.entrySet()) {
            //failsafe (no negative odds)
            if (val.getValue() < 0) {
                val.setValue(1);
            }

            tempOdds += val.getValue();
            bounds.put(val.getKey(), tempOdds);
        }
        
        if(nsLevel.DEBUG){
            System.out.println("sanity");
            stringOut();
        }
//COULD Normalize, but we don't have to, and current logic in nsLevel is set up
//  for no normalization
//        for(Map.Entry<ODDS_E, Integer> val : bounds.entrySet()){
//            val.setValue(val.getValue()/totalOdds);
//        }
    }
}
