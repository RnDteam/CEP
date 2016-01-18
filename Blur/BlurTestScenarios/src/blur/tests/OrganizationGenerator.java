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
	
	public static String generateOrganizationTable() {
		String sql = "create table organizations ( id varchar(50)," +
					"type varchar(50)" +
					")";
		return sql;
	}
	
	public static String generateOrganizationRoleTable() {
		String sql = "create table organization_roles ( id varchar(50), " +
					"name varchar(50)," +
					"organization_id varchar(50)," +
					"person_id varchar(50)," +
					"organization_id varchar(50)" +
					")";
		return sql;
	}
	
	public static String generatePersonTable() {
		String sql = "create table persons ( id varchar(50), " +
				"name varchar(50)," +
				"profession varchar(50)," +
				"location varchar(50)," +
				"state varchar(50)," +
				"organization_role_id varchar(50)," +
				")";
		return sql;
	}
}