package blur.tests;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class TablesGenerator {

	public static void generateAllTables(Connection connection) {
		String personStatement = generatePersonTable();
		String buildingStatement = generateBuildingTable();
		String organizationStatement = generateOrganizationTable();
		String organizationRoleStatement = generateOrganizationRoleTable();
		String vehicleStatement = generateVehicleTable();
		String vehicleDetailsStatement = generateVehicleDetailsTable();
		
		try {
			Statement statement = connection.createStatement();
			connection.setAutoCommit(false);
			statement.addBatch(personStatement);
			statement.addBatch(buildingStatement);
			statement.addBatch(organizationStatement);
			statement.addBatch(organizationRoleStatement);
			statement.addBatch(vehicleStatement);
			statement.addBatch(vehicleDetailsStatement);
			
			int[] results = statement.executeBatch();
			connection.commit();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private static String generateVehicleDetailsTable() {
		String createVehicleDetailsStatement = "CREATE TABLE vehicle_details (id varchar(255),"
				+ "maker varchar(255), "
				+ "model varchar(255),"
				+ "type varchar(255),"
				+ "year varchar(255),"
				+ "maximum_speed varchar(255))";
		
		return createVehicleDetailsStatement;
	}

	private static String generateVehicleTable() {
		String createVehicleStatement = "CREATE TABLE vehicles (id varchar(255),"
				+ "location varchar(255), "
				+ "status varchar(255),"
				+ "details_id varchar(255),"
				+ "last_seen varchar(255),"
				+ "owner_id varchar(255),"
				+ "organization_id varchar(255))";
		
		return createVehicleStatement;
	}

	private static String generateBuildingTable() {
		String createBuildingStatement = "CREATE TABLE buildings (id varchar(255), "
				+ "location varchar(255),"
				+ "type varchar(255),"
				+ "usage_type varchar(255),"
				+ "owner_id varchar(255))";
		
		return createBuildingStatement;
	}
	
	public static String generateOrganizationTable() {
		String sql = "create table organizations ( id varchar(255)," +
					"type varchar(255)" +
					")";
		return sql;
	}
	
	public static String generateOrganizationRoleTable() {
		String sql = "create table organization_roles ( id varchar(255), " +
					"name varchar(255)," +
					"organization_id varchar(255)," +
					"person_id varchar(255)," +
					"organization_role_id varchar(255)" +
					")";
		return sql;
	}
	
	public static String generatePersonTable() {
		String sql = "create table persons ( id varchar(255), " +
				"name varchar(255)," +
				"profession varchar(255)," +
				"location varchar(255)," +
				"state varchar(255)," +
				"organization_role_id varchar(255))";
		return sql;
	}
	
	public static void dropAallTables(Connection connection) {
		String personDrop = dropTable("persons");
		String buildingDrop = dropTable("buildings");
		String organizationDrop = dropTable("organizations");
		String organizationRoleDrop = dropTable("organization_roles");
		String vehicleDrop = dropTable("vehicles");
		String vehicleDetailsDrop = dropTable("vehicle_details");
		
		try {
			Statement statement = connection.createStatement();
			connection.setAutoCommit(false);
			statement.addBatch(personDrop);
			statement.addBatch(buildingDrop);
			statement.addBatch(organizationDrop);
			statement.addBatch(organizationRoleDrop);
			statement.addBatch(vehicleDrop);
			statement.addBatch(vehicleDetailsDrop);
			
			int[] results = statement.executeBatch();
			connection.commit();
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String dropTable(String tableName) {
		return "DROP TABLE " + tableName;
	}
	
}
