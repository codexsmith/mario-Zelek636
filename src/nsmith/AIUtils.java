package nsmith;

import java.util.ArrayList;

public class AIUtils {
	public static int resInt;
	public static float resFloat;
	public static double resDouble;
	
	public static int std_dev (ArrayList<Integer> set){
		//uses arithmetic average
		float std_dev = 0.0f;
		float avg = 0.0f;

		for(Integer f_i : set){
			avg += f_i;
		}
		avg = std_dev / set.size();
		
		for(Integer f_i : set){
			std_dev += Math.pow((f_i - avg), 2);
		}
		
		return (int) Math.pow(std_dev/set.size(),0.5);
	}
	
	public static int getStdDevBreak(ArrayList<Integer> set, Float point){
		int quartile = 0;

		float dev = std_dev(set);
		
		float avg = arith_avg(set);
		
		if((point - avg) < avg - dev * 3){
			quartile = -4;
		}
		else if((point - avg) < avg - dev * 2){
			quartile = -3;
		}
		else if((point - avg) < avg - dev * 1){
			quartile = 2;
		}
		else if((point - avg) < avg - dev){
			quartile = -1;
		}
		else if((point - avg) < avg + dev){
			quartile = 1;
		}
		else if((point - avg) < avg + dev * 2){
			quartile = 2;
		}
		else if((point - avg) < avg + dev * 3){
			quartile = 3;
		}
		
		else{
			quartile = 4;
		}

		return quartile;
	}
	
	public static float arith_avg(ArrayList<Integer> in ){
		float out = 1.0f;

		for(float f_i : in){
			out = out * f_i;
		}
		return (float) Math.pow(out, (1/in.size()));

	}
	
	public static int math_geom_avg(ArrayList<Integer> in){
		float out = 1.0f;
		
		for(float f_i : in){
			out = out * f_i;
		}
		return (int) Math.pow(out, (1/in.size()));
		
	}
	

    
    public static int find_max(int[] input){
		int[] dummy = input;
		int max = dummy[0];
		for(int i = 0; i < dummy.length; i++){
			if(max < dummy[i]){
				max = dummy[i];
			}
		}
		return max;
	}
    
    public static int find_min(int[] input){
		int[] dummy = input;
		int min = dummy[0];
		for(int i = 0; i < dummy.length; i++){
			if(min > dummy[i]){
				min = dummy[i];
			}
		}
		return min;
	}

	public static Float find_max(ArrayList<Float> floatArr) {
		ArrayList<Float> dummy = floatArr;
		Float max = dummy.get(0);
		for(int i = 0; i < dummy.size(); i++){
			if(max < dummy.get(i)){
				max = dummy.get(i);
			}
		}
		return max;
	}
	
}
