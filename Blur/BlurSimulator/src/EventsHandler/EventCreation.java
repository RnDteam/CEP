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
	
	@Override
	public List<T> getAllEntities(SolutionGateway gateway) {
		Connection dbConnection = DBReader.getDBConnection();
		ArrayList<T> eventsList = new ArrayList<>();
		try {
			Statement statement = dbConnection.createStatement();
			ResultSet resultSet = statement.executeQuery(getAllEventsQuery);
			
			while (resultSet.next()) {
				eventsList.add(convertDBRowToObject(resultSet, gateway));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	
		return eventsList;
	}
	
	public abstract String getTableName();

}
