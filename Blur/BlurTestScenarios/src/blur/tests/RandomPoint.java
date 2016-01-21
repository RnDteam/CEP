package blur.tests;

import java.util.Random;

public class RandomPoint {

	public static String getRandomPoint() {
		
		Random rand = new Random();		
		int longitude = rand.nextInt(50);
		int latitude = rand.nextInt(50);
		String point = longitude + "," + latitude;
		return point;
		
	}	
}
