package nsmith;

import dk.itu.mario.MarioInterface.GamePlay;
import dk.itu.mario.MarioInterface.LevelInterface;
import dk.itu.mario.level.generator.CustomizedLevelGenerator;
import java.util.Random;

/**
 *
 * @author nix
 */
public class LevelGen extends CustomizedLevelGenerator{
    
    @Override
    public LevelInterface generateLevel(GamePlay playerMetrics) {
        LevelInterface level;        
                long seed = new Random().nextLong();
                if(nsLevel.createNsLevel(320,15,seed,1,1,playerMetrics)){
                    nsLevel.LEVEL.create(seed, 1, 1);
                    level = nsLevel.getLEVEL();
                }
                else{
                    nsLevel.clearLEVEL(true);
                    nsLevel.createNsLevel(320,15,seed,1,1,playerMetrics);
                    nsLevel.LEVEL.create(seed, 1,1);
                    level = nsLevel.getLEVEL();
                }
		
		return level;
	}
    
}
