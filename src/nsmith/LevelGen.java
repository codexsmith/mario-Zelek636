package nsmith;

import dk.itu.mario.MarioInterface.GamePlay;
import dk.itu.mario.MarioInterface.LevelInterface;
import dk.itu.mario.level.CustomizedLevel;
import dk.itu.mario.level.generator.CustomizedLevelGenerator;
import java.util.Random;

/**
 *
 * @author nix
 */
public class LevelGen extends CustomizedLevelGenerator{
    
    public LevelInterface generateLevel(GamePlay playerMetrics) {
        LevelInterface level;        
        
                if(nsLevel.createNsLevel(320,15,new Random().nextLong(),1,1,playerMetrics)){
                    level = nsLevel.getLEVEL();
                }
                else{
                    nsLevel.clearLEVEL();
                    nsLevel.createNsLevel(320,15,new Random().nextLong(),1,1,playerMetrics);
                    level = nsLevel.getLEVEL();
                }
		
		return level;
	}
    
}
