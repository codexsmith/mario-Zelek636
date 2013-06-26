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
        
        readLabeledSet();
        
        seed = new Random().nextLong();
        int difficulty = calculateDifficulty(playerMetrics);

        if (nsLevel.createNsLevel(320, 15, seed, difficulty, 0, playerMetrics)) {
            nsLevel.LEVEL.creat();
            level = nsLevel.getLEVEL();
        } else {
            nsLevel.clearLEVEL(true);
            nsLevel.createNsLevel(320, 15, seed, difficulty, 0, playerMetrics);
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
            ArrayList<Integer> labels;
            try {
                
                while ((line = reader.readLine()) != null) {
                    labels = new ArrayList<Integer>();
                    if(nsLevel.DEBUG)System.out.println(line);
                    
                    lineIn = line.split(" ");
                                        
                    for(int i = 1; i < lineIn.length; i++){   
                        labels.add(Integer.decode(lineIn[i]));
                    }
                    
                                        
                    seedLabels.addEntry(Long.decode(lineIn[0]), labels );
                        
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
                String[] lines = seedLabels.toFileString();
                
                for(String st : lines){
//                for( int i = seedLabels.getPrevLength(); i < seedLabels.getCurrentLength(); i++){
                    
                    if(nsLevel.DEBUG){
                        System.out.println(st);
//                        System.out.println(lines[i]);
                    }
                    out.println(st);
//                       out.println(lines[i]);
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
    private int calculateDifficulty(GamePlay playerMetrics) {
        int difficulty = 0;
        difficulty -= 10 * playerMetrics.timesOfDeathByArmoredTurtle;
        difficulty += 30 * playerMetrics.ArmoredTurtlesKilled;
        
        difficulty -= 10 * playerMetrics.timesOfDeathByCannonBall;
        difficulty += 20 * playerMetrics.CannonBallKilled;
        
        difficulty -= 25 * playerMetrics.timesOfDeathByChompFlower;
        difficulty += 15 * playerMetrics.ChompFlowersKilled;
        
        difficulty -= 60 * playerMetrics.timesOfDeathByGoomba;
        difficulty += 4  * playerMetrics.GoombasKilled;
        
        difficulty -= 30 * playerMetrics.timesOfDeathByGreenTurtle;
        difficulty += 10 * playerMetrics.GreenTurtlesKilled;
        
        difficulty -= 30 * playerMetrics.timesOfDeathByRedTurtle;
        difficulty += 10 * playerMetrics.RedTurtlesKilled;
        
        difficulty -= 25 * playerMetrics.timesOfDeathByJumpFlower;
        difficulty -= 40 * playerMetrics.timesOfDeathByFallingIntoGap;
        
        return difficulty < 1 ? 1 : difficulty;
    }
}
