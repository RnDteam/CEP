package EventsHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import DBHandler.DBReader;
import DBHandler.IDBInteraction;

import com.ibm.ia.gateway.SolutionGateway;
import com.ibm.ia.model.Event;

public abstract class EventCreation<T extends Event> implements IDBInteraction<T> {

	private final String getAllEventsQuery ="SELECT * FROM " + getTableName();
	
	public static int days = 0;
	
	@Override
	public List<T> getAllEntities(SolutionGateway gateway) {
		Connection dbConnection = DBReader.getDBConnection();
		ArrayList<T> eventsList = new ArrayList<>();
		try {
			Statement countStatement = dbConnection.createStatement();
			ResultSet countResultSet = countStatement.executeQuery("SELECT COUNT(*) FROM " + getTableName());
			countResultSet.next();
		    int rowCount = countResultSet.getInt(1);
		    countResultSet.close();
		    countStatement.close();
			
			Statement statement = dbConnection.createStatement();
			System.out.println(getAllEventsQuery);
			System.out.print( "Loading ");
			ResultSet resultSet = statement.executeQuery(getAllEventsQuery);
			int count = 0;
			
			while (resultSet.next()) {
				eventsList.add(convertDBRowToObject(resultSet, gateway));
				updateProgress( (double) count++ / (double) rowCount ); 
			}
			System.out.println();
			days++;
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		return eventsList;
	}
	
	static void updateProgress(double progressPercentage) {
	    final int width = 50; // progress bar width in chars

	    System.out.print("\r[");
	    int i = 0;
	    for (; i <= (int)(progressPercentage*width); i++) {
	      System.out.print(".");
	    }
	    for (; i < width; i++) {
	      System.out.print(" ");
	    }
	    System.out.print("]");
	  }

	
	public abstract String getTableName();

}
