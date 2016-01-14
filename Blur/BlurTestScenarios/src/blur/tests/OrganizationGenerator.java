package blur.tests;

import java.util.Random;

public class OrganizationGenerator {

	public static String generateOrganizationQuery(Integer id) {
		
		String organizationType = null;
		Random random = new Random();

		if (random.nextBoolean()) {
			organizationType = "CRIMINAL";
		} else {
			organizationType = "COMMERCIAL";
		}
		
		String sqlQuery = String.format("insert into organizations values ('{0}', '{1}'", id, organizationType);
		
		return sqlQuery;
	}
}