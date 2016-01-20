package DBHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import Events.TrafficCameraReportEvent;


public class DBReader {
	
	private static final String MY_SQL_DB_URL = "jdbc:mysql://localhost:3306/cep_try";
	private static final String USER_NAME = "root";
	private static final String PASSWORD = "root";
	
	private static TrafficCameraReportEvent trafficCameraReportEvent = new TrafficCameraReportEvent();

	public static void closeConnection(Connection dbConnection) {
		try {
			if(dbConnection != null) {
				dbConnection.close();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static Connection getDBConnection() {
			Connection connection = null;	
			
			try {
				connection = DriverManager.getConnection(MY_SQL_DB_URL, USER_NAME, PASSWORD);
			} catch (Exception e) {
				e.printStackTrace();
			} 
		
			return connection;
		}
	
	public static TrafficCameraReportEvent getTrafficCameraReportEvent() {
		return trafficCameraReportEvent;
	}
}
