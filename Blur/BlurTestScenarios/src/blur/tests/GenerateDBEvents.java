package blur.tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class GenerateDBEvents {

	public static void main(String[] args) {
			connectToDB();
	}
	
public static void connectToDB() {
		
		String databaseUrl = "jdbc:mysql://localhost:3306/cep_try";
		String user = "root";
		String password = "root";
		
		Connection connection = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("mysql jdbc driver is missing");
		}
		
		
		try {
			connection = DriverManager.getConnection(databaseUrl, user, password);
			if	(connection != null) {
				
				TablesGenerator.dropAllTables(connection);
				TablesGenerator.generateAllTables(connection);
<<<<<<< HEAD
=======
				
				String queryOrganization1 = OrganizationGenerator.generateOrganizationQuery("organization1");
				String queryOrganization2 = OrganizationGenerator.generateOrganizationQuery("organization2");
				String queryOrganization3 = OrganizationGenerator.generateOrganizationQuery("organization3");
				
				String queryBuilding1 = GenerateBuilding.generateBuilding("buiding1", "person1", "organization1");
				String queryBuilding2 = GenerateBuilding.generateBuilding("buiding2", "person1", "organization1");
				String queryBuilding3 = GenerateBuilding.generateBuilding("buiding3", "person1", "organization3");
				
				System.out.println(queryOrganization1);
				System.out.println(queryOrganization2);
				System.out.println(queryOrganization3);
				System.out.println(queryBuilding1);
				System.out.println(queryBuilding2);
				System.out.println(queryBuilding3);

				Statement statement = connection.createStatement();
				
				connection.setAutoCommit(false);
				statement.addBatch(queryOrganization1);
				statement.addBatch(queryOrganization2);
				statement.addBatch(queryOrganization3);
				statement.addBatch(queryBuilding1);
				statement.addBatch(queryBuilding2);
				statement.addBatch(queryBuilding3);
				
				int[] results = statement.executeBatch();
				connection.commit();
				
				//ResultSet rs = statement.executeQuery("select eventName, accountId, country, amount, merchantId, merchantType, merchantLocation, eventMethod from transactions");
/*				ResultSet rs = statement.executeQuery("select * from traffic_events");
				
				while (rs.next()) {
					System.out.println(rs.getString(1) + " " + rs.getString(2) + " " + rs.getString(3));
				}*/
>>>>>>> 2b9e66af4fcce98e137dd9f725fc95260bcd5f0f
			}
			
			System.out.println("Succeeded");
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		System.out.println("Finished getting data from DB");
		
		System.out.println("Finished");
	}

}
