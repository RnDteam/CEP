package blur.tests;

import java.util.Random;

public class OrganizationGenerator {

	public static String generateOrganizationQuery(String id) {
		
		String type = null;
		Random random = new Random();

		if (random.nextBoolean()) {
			type = "CRIMINAL";
		} else {
			type = "COMMERCIAL";
		}
		
		String sqlQuery = String.format("insert into organizations (id, type) values ('%s', '%s')", id, type);
		
		return sqlQuery;
	}
}