package DBHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DBReader {
	
	private static final String MY_SQL_DB_URL = "jdbc:mysql://52.49.109.120:3306/cep_try";
	private static final String USER_NAME = "root";
	private static final String PASSWORD = "cep_blur";
	
	private static Connection connection = null;

	public static void closeConnection() {
		try {
			if(connection != null) {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static Connection getDBConnection() {
			if(connection == null) {
				try {
					connection = DriverManager.getConnection(MY_SQL_DB_URL, USER_NAME, PASSWORD);
				} catch (Exception e) {
					e.printStackTrace();
				} 
			}
		
			return connection;
		}
}
