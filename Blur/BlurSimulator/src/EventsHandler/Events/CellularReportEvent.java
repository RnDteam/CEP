package EventsHandler.Events;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import DBHandler.ConverterUtility;
import DBHandler.DBReader;
import EventsHandler.EventCreation;
import blur.model.Building;
import blur.model.CellularReport;
import blur.model.Person;

import com.ibm.ia.gateway.SolutionGateway;
import com.spaceprogram.kittycache.KittyCache;

public class CellularReportEvent extends EventCreation<CellularReport> {
	
	private static final int TTL_SECONDS = 60;
	private static final KittyCache<String,String> buildingIdCache = new KittyCache<String,String>(4096);

	@Override
	public CellularReport convertDBRowToObject(ResultSet resultSet,
			SolutionGateway gateway) {
		CellularReport cellularReportEvent = null;
		try {
			cellularReportEvent = gateway.getEventFactory().createEvent(
					CellularReport.class);

			// String cellularReportId = resultSet.getString(1);
			String reportTime = resultSet.getString(2);
			String personId = resultSet.getString(3);
			String logntitude = resultSet.getString(4);
			String latitude = resultSet.getString(5);

			String buildingId = getBuildingIdFromLocation(logntitude, latitude,
					gateway);
			cellularReportEvent.setBuilding(gateway.createRelationship(
					Building.class, buildingId));
			cellularReportEvent.setPerson(gateway.createRelationship(
					Person.class, personId));
			cellularReportEvent.setTimestamp(ConverterUtility.absDate
					.plusMinutes(Integer.parseInt(reportTime)));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return cellularReportEvent;
	}
	
	private static String getCacheKey(String logntitude,
			String latitude) {
		return  logntitude + "/" + latitude;
	}

	private String getBuildingIdFromLocation(String logntitude,
			String latitude, SolutionGateway gateway) {
		
		String cacheKey = getCacheKey(logntitude,latitude);
		String result = buildingIdCache.get(cacheKey);
		
		if(result==null) {
			Connection dbConnection = DBReader.getDBConnection();
			String query = "SELECT id FROM db_structure WHERE Lang = "
					+ logntitude + " AND Lat = " + latitude;

			ArrayList<String> buildingList = new ArrayList<>();
			try {
				Statement statement = dbConnection.createStatement();

				ResultSet resultSet = statement.executeQuery(query);

				while (resultSet.next()) {
					buildingList.add(resultSet.getString(1));
				}
				
				resultSet.close();
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			
			if(buildingList.size() !=  1) {
				System.err.println( "Problem getting building from lat/lon using query " + query );
				return null;
			}
			else {
				result = buildingList.get(0);
				buildingIdCache.put(cacheKey, result, TTL_SECONDS);
			}
		}
		
		return result;
	}

	@Override
	public String getTableName() {
		return "rp_cellular";
	}
}
