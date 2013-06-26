/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package nsmith;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**Data Structure for containing read/write/usable data about a level and statistics
 * or other data describing it
 *
 * Implements a Map of Long to ArrayList that permits duplicate keys
 *
 * jOption return values
 * 0 is Yes : 1 is No
 * 
 * @author nix
 */
public class LevelStat {
    private ArrayList<Long> keys;
    private ArrayList<ArrayList<Integer>> stats;    
    private int prevLength = 0;
    
    public LevelStat(){
       keys = new ArrayList<Long>();
       stats = new ArrayList<ArrayList<Integer>>(); 
              
    }
    
    public int getCurrentLength(){
        return keys.size();
    }
    
    public int getPrevLength(){
        return prevLength;
    }
    
    public String[] toFileString(){
        prevLength = keys.size();
        
        String[] out = new String[keys.size()];
        String temp;
        
        for(int i = 0; i < keys.size(); i++){
            temp = "";
            temp = temp.concat(keys.get(i).toString());
            ArrayList<Integer> x = stats.get(i);
            
            for(Integer i_i : x){
                temp = temp + " " + i_i.toString();
            }
            
            out[i] = temp;
        }
        
        return out;
    }
    
    public Long getKey(int index){
        return keys.get(index);
    }
    
    public int getIndex(Long test, boolean first){
        ArrayList<Integer> index = new ArrayList<Integer>();
        int x = 0;
        
        for(int i = 0; i < keys.size(); i++){
            if(keys.get(i) == test){
                index.add(i);
            }
        }
        
        //choices choices
        if(first){
            return index.get(0);
        }
        else if (index.size() > 1){
            Random rand = new Random();
            return index.get(rand.nextInt(index.size()));
        }
        else{
            return index.get(index.size()-1);
        }
                
    }
    
    public ArrayList<Integer> getValue(int index){
        return stats.get(index);
    }
    
    public boolean hasKey(Long test){
        if(keys.contains(test)){
            return true;
        }
        else{
            return false;
        }
    }
    
    public void addEntry(Long test, ArrayList<Integer> val){
        keys.add(test);
        stats.add(val);
                
    }
    
    public ArrayList<Integer> getPlayableANDEnjoyable(){
        ArrayList<Integer> indexWanted = new ArrayList<Integer>();
        
        for(int i = 0; i < stats.size(); i++){
            ArrayList<Integer> a_i = stats.get(i);
            
            if(a_i.get(0) == 0 && a_i.get(1) == 0){
                indexWanted.add(i);
            }
        }
        
        return indexWanted;
    }
    
    public HashMap<Integer, ArrayList<Integer>> getMapKey(Long test){
        HashMap<Integer, ArrayList<Integer>> values = null;
        ArrayList<Integer> index = new ArrayList<Integer>();
        
        for(int i = 0; i < keys.size(); i++){
            if(keys.get(i) == test){
                index.add(i);
            }
        }
        
        for(int i : index){
            values.put(i, stats.get(i));
        }
                
        return values;
    }
}
