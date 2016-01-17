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
		
		String databaseUrl = "jdbc:mysql://localhost:3306/mysql";
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
