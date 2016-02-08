package EventsHandler;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import DBHandler.DBReader;
import DBHandler.IDBInteraction;

import com.ibm.ia.common.DataFormat;
import com.ibm.ia.common.DataParseException;
import com.ibm.ia.gateway.SolutionChangedException;
import com.ibm.ia.gateway.SolutionGateway;
import com.ibm.ia.model.Event;
import com.ibm.ia.testdriver.TestDriver;

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
				T event = convertDBRowToObject(resultSet, gateway);
				eventsList.add(event);
				//checkEvent(gateway, event);
				updateProgress( (double) count++ / (double) rowCount ); 
			}
			System.out.println();
			days++;
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		return eventsList;
	}
	
//	private void checkEvent(SolutionGateway gateway, T event) throws UnsupportedOperationException, SolutionChangedException, DataParseException {
//		String xml = gateway.getModelSerializer().serializeEvent(DataFormat.TYPED_XML, event);
//		if(xml.contains("location")) {
//			T event2 = (T) gateway.getModelSerializer().parseEvent(DataFormat.TYPED_XML, xml);
//			String xm2l = gateway.getModelSerializer().serializeEvent(DataFormat.TYPED_XML, event2);			
//		}
//	}
	
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
