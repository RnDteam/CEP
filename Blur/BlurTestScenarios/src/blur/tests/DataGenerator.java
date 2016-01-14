package blur.tests;

import java.sql.Connection;

public class DataGenerator {
	
	public static void generatePersons(Connection connection, int number) {
		
		String s1 = GeneratePersons.getPersonInsertQuery(0);
		
		
		for (int i = 0; i < number; i++) {
			
		}
	}

}
