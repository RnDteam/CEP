package DBHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import blur.model.TrafficCameraReport;


public class DBReader {
	
	private static final String MY_SQL_DB_URL = "jdbc:mysql://localhost:3306/cep_try";
	private static final String USER_NAME = "root";
	private static final String PASSWORD = "root";

	public List<TrafficCameraReport> getAllTrafficCameraReports() {
		
		return null;
	}
	
public static void connectToDB() {
		
		Connection connection = null;	
		
		try {
			connection = DriverManager.getConnection(MY_SQL_DB_URL, USER_NAME, PASSWORD);
			if	(connection != null) {
				
				TablesGenerator.dropAllTables(connection);
				TablesGenerator.generateAllTables(connection);
			}
			
			System.out.println("Succeeded");
		} catch (Exception e) {
			
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Finished getting data from DB");
		
		System.out.println("Finished");
	}
}
