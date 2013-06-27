package nsmith;

import dk.itu.mario.MarioInterface.GamePlay;
import dk.itu.mario.MarioInterface.LevelInterface;
import dk.itu.mario.level.generator.CustomizedLevelGenerator;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**Contains the level stats file IO, the questionnaire, seeds, and interacts with nsLevel
 * calculates the player's difficulty
 *Call stack bzLevelTest -> this -> nsLevel
 * @author Nicholas Smith
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
//        seed = -4986433319767209288L; well balanced level
//        -2266529554013408243
//        -4364097651690792009
//        -3732855959174562952
//        -4201392615275519362
        
        //THIS IS OUR DIFFICULTY
        int difficulty = calculateDifficulty(playerMetrics);
        
         if(nsLevel.DEBUG){
                System.out.println("Calculated Difficulty "+difficulty);
                System.out.println();
        }
        
        if(difficulty < 1){
            difficulty = 1;
        }
        
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
    
//    public LevelInterface generateTestLevel(GamePlay playerMetrics){
//        
//        readLabeledSet();
//                
//        seed = pickFromTrainingSet();
//        
//        int difficulty = calculateDifficulty(playerMetrics);
//
//        if (nsLevel.createNsLevel(320, 15, seed, difficulty, 0, playerMetrics)) {
//            nsLevel.LEVEL.creat();
//            level = nsLevel.getLEVEL();
//        } else {
//            nsLevel.clearLEVEL(true);
//            nsLevel.createNsLevel(320, 15, seed, difficulty, 0, playerMetrics);
//            nsLevel.LEVEL.creat();
//            level = nsLevel.getLEVEL();
//
//        }
//        
//        return level;
//    }
    
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
                    
//                    if(nsLevel.DEBUG){
//                        System.out.println(st);
////                        System.out.println(lines[i]);
//                    }
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
        float difficulty = 1;
        difficulty += 2 * playerMetrics.timesOfDeathByArmoredTurtle+1 / (playerMetrics.totalEnemies + 1)* playerMetrics.ArmoredTurtlesKilled;
        
        difficulty -= 1 * playerMetrics.timesOfDeathByCannonBall/ (playerMetrics.totalEnemies + 1)* playerMetrics.CannonBallKilled+1;
  
        
        difficulty += 2 * playerMetrics.timesOfDeathByChompFlower / (playerMetrics.totalEnemies + 1)* playerMetrics.ChompFlowersKilled+1;
    
        
        difficulty -= 1 * playerMetrics.timesOfDeathByGoomba/ (playerMetrics.totalEnemies +1)* playerMetrics.GoombasKilled+1;
       
        
        difficulty += 1 * playerMetrics.timesOfDeathByGreenTurtle/ (playerMetrics.totalEnemies +1) * playerMetrics.GreenTurtlesKilled+1;
       
        
        difficulty -= 1 * playerMetrics.timesOfDeathByRedTurtle/( playerMetrics.totalEnemies + 1)* playerMetrics.RedTurtlesKilled+1;
        
        
        difficulty -= 1 * playerMetrics.timesOfDeathByJumpFlower * playerMetrics.timesOfDeathByFallingIntoGap+1;
       
        
        int difficulty_i = (int) difficulty;
        
        return difficulty_i < 1 ? 1 : difficulty_i;
    }

    public boolean resetFeedBack() {
        Object[] options = {"Yes","No"};
        
        int val = JOptionPane.showOptionDialog(null,
                "Do you want a new level to play?","Playable",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
        if (val == 1){
            return false;
        }
        else{
            return true;
        }
    }

    private long pickFromTrainingSet() {
        long pick = 0L;
        ArrayList<Integer> indexes = seedLabels.getPlayableANDEnjoyable();
        Random rand = new Random();
        
        pick = seedLabels.getKey(indexes.get(rand.nextInt(indexes.size())));
        
        return pick;
    }
}
