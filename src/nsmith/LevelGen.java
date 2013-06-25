package nsmith;

import dk.itu.mario.MarioInterface.GamePlay;
import dk.itu.mario.MarioInterface.LevelInterface;
import dk.itu.mario.level.generator.CustomizedLevelGenerator;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author nix
 */
public class LevelGen extends CustomizedLevelGenerator {
    
    
    LevelInterface level;
    FileReader file = null;
    LevelStat seedLabels;
    int setLength;
    long seed;
    
    public LevelGen() {
        
        seedLabels = new LevelStat(); 
        
        try {
            this.file = new FileReader("src/nsmith/seedDataSet.txt");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(LevelGen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public LevelInterface generateLevel(GamePlay playerMetrics) {
        
//        readLabeledSet();
        
        seed = new Random().nextLong();
        
        if (nsLevel.createNsLevel(320, 15, seed, 1, 1, playerMetrics)) {
            nsLevel.LEVEL.creat();
            level = nsLevel.getLEVEL();
        } else {
            nsLevel.clearLEVEL(true);
            nsLevel.createNsLevel(320, 15, seed, 1, 1, playerMetrics);
            nsLevel.LEVEL.creat();
            level = nsLevel.getLEVEL();

        }
        
        return level;
    }
    
    private void readLabeledSet(){
            
            BufferedReader reader = new BufferedReader(file);
            String line = null;
            String[] lineIn;
            int dataNo = 0;
            ArrayList<Integer> entryRow = new ArrayList<Integer>();
            ArrayList<Integer> labels = new ArrayList<Integer>();
            try {
                
                while ((line = reader.readLine()) != null) {
                    if(nsLevel.DEBUG)System.out.println(line);
                    
                    lineIn = line.split(" ");
                    
                    if(seedLabels.hasKey(Long.decode(lineIn[0]))){
                        entryRow = seedLabels.getValue(seedLabels.getIndex(seed, true));
                    }
                    
                    for(int i = 1; i < lineIn.length; i++){   
                        labels.add(Integer.decode(lineIn[i]));
                    }
                    
                    
//                    entryRow.add(labels);
                    
                    seedLabels.addEntry(Long.decode(lineIn[0]), entryRow );
                        
                }
                
                reader.close();
            } 
            catch (IOException ex) {
                Logger.getLogger(LevelGen.class.getName()).log(Level.SEVERE, null, ex);
            }
           
    }
    
    private void writeLabel(){
        if(seedLabels.getPrevLength() < seedLabels.getCurrentLength()){
            try{

                PrintWriter out = new PrintWriter("src/nsmith/seedDataSet.txt");
                
                for(String st : seedLabels.toFileString()){
                    if(nsLevel.DEBUG){
                        System.out.println(st);
                    }
                       out.println(st);
                }
                
                out.flush();
                out.close();
            }
            catch(Exception e){

            }
        }
    }
    
    public void surveyFeedBack() {
        
        ArrayList<Integer> val = new ArrayList<Integer>();
        
        Object[] options = {"Yes","No"};
        
        val.add(JOptionPane.showOptionDialog(null,
                "Was this level playable?","Playable",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]));
        
        val.add(JOptionPane.showOptionDialog(null,
                "Was this level enjoyable?","Enjoyable",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]));
        
        val.add(AIUtils.math_geom_avg(nsLevel.getLEVEL().getDifficultyHistory()));
        val.add(AIUtils.std_dev(nsLevel.getLEVEL().getDifficultyHistory()));
        
       seedLabels.addEntry(seed, val);
       
       writeLabel();
    }
}
