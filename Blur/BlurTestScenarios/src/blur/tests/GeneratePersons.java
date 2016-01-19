package blur.tests;

import java.util.Random;

import blur.tests.EntityGenerator.RandomPoint;

public class GeneratePersons {
	
	private static final String ACTIVE = "active";
	private static final String INACTIVE = "inactive";

	public static String getPersonInsertQuery(String id, String organizationRole) {
		Random rand = new Random();
		String name = "Name" + id;
		String profession= "Profession" + rand.nextInt(100);
		String location= RandomPoint.getRandomPoint();
		String state;
		int s = rand.nextInt(100) % 2;
		if (s == 0) {
			state = ACTIVE;
		}
		else {
			state = INACTIVE;
		}	
		String query = String.format("INSERT INTO persons VALUES ('%s', '%s', '%s', '%s', '%s', '%s')", id, name, profession, location, state, organizationRole);
		return query;
	}
}
