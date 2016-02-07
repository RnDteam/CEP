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

public class CellularReportEvent extends EventCreation<CellularReport> {

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

	private String getBuildingIdFromLocation(String logntitude,
			String latitude, SolutionGateway gateway) {
		Connection dbConnection = DBReader.getDBConnection();
		String query = "SELECT id FROM DB_Structure WHERE Lang = "
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
			return buildingList.get(0);	
		}
	}

	@Override
	public String getTableName() {
		return "RP_Cellular";
	}
}
