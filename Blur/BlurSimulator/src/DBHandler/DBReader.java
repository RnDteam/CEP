package DBHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import blur.model.TrafficCameraReport;
import EntitiesInitialization.PersonExternalInit;
import EntitiesInitialization.PersonInternalInit;
import Events.TrafficCameraReportEvent;


public class DBReader {
	
	private static final String MY_SQL_DB_URL = "jdbc:mysql://localhost:3306/cep_try";
	private static final String USER_NAME = "root";
	private static final String PASSWORD = "root";
	
	private static Connection connection = null;

	public static void closeConnection(Connection dbConnection) {
		try {
			if(dbConnection != null) {
				dbConnection.close();
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
